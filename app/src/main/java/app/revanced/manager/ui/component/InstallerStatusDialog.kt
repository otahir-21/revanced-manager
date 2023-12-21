package app.revanced.manager.ui.component

import android.content.pm.PackageInstaller
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.revanced.manager.R
import app.revanced.manager.ui.viewmodel.PackageInstallerResult
import com.github.materiiapps.enumutil.FromValue

interface InstallerStatusDialogModel {
    var packageInstallerResult: PackageInstallerResult
    var showInstallerStatusDialog: Boolean

    fun reinstall()
    fun install()
}

private typealias InstallerStatusDialogButtonHandler = ((model: InstallerStatusDialogModel) -> Unit)
private typealias InstallerStatusDialogButton = @Composable (model: InstallerStatusDialogModel) -> Unit


@Composable
fun InstallerStatusDialog(model: InstallerStatusDialogModel, ) {
    val dialogKind = remember {
        DialogKind.fromValue(model.packageInstallerResult.status) ?: DialogKind.FAILURE
    }

    AlertDialog(
        onDismissRequest = {
            model.showInstallerStatusDialog = false
        },
        confirmButton = {
            dialogKind.confirmButton(model)
        },
        dismissButton = {
            dialogKind.dismissButton?.invoke(model)
        },
        icon = {
            Icon(dialogKind.icon, null)
        },
        title = {
            Text(
                text = stringResource(dialogKind.title),
                style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center),
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    Text(stringResource(dialogKind.contentStringResId))
                }
            )
        }
    )
}

private fun installerDialogButton(
    @StringRes buttonStringResId: Int,
    handler: InstallerStatusDialogButtonHandler = { },
): InstallerStatusDialogButton = { model ->
    TextButton(onClick = {
        model.showInstallerStatusDialog = false
        handler(model)
    }) { Text(stringResource(buttonStringResId)) }
}

@FromValue("flag")
enum class DialogKind(
    val flag: Int,
    val title: Int,
    @StringRes internal val contentStringResId: Int,
    val icon: ImageVector = Icons.Outlined.ErrorOutline,
    val confirmButton: InstallerStatusDialogButton = installerDialogButton(R.string.ok),
    internal val dismissButton: InstallerStatusDialogButton? = null,
) {
    FAILURE(
        flag = PackageInstaller.STATUS_FAILURE,
        title = R.string.installation_failed,
        contentStringResId = R.string.installation_failed_description,
    ),
    FAILURE_ABORTED(
        flag = PackageInstaller.STATUS_FAILURE_ABORTED,
        title = R.string.installation_cancelled,
        contentStringResId = R.string.installation_aborted_description,
    ),
    FAILURE_BLOCKED(
        flag = PackageInstaller.STATUS_FAILURE_BLOCKED,
        title = R.string.installation_blocked,
        contentStringResId = R.string.installation_blocked_description,
    ),
    FAILURE_CONFLICT(
        flag = PackageInstaller.STATUS_FAILURE_CONFLICT,
        title = R.string.installation_conflict,
        contentStringResId = R.string.installation_conflict_description,
        confirmButton = installerDialogButton(R.string.reinstall) { vm ->
            vm.reinstall()
        },
        dismissButton = installerDialogButton(R.string.cancel),
    ),
    FAILURE_INCOMPATIBLE(
        flag = PackageInstaller.STATUS_FAILURE_INCOMPATIBLE,
        title = R.string.installation_incompatible,
        contentStringResId = R.string.installation_incompatible_description,
    ),
    FAILURE_INVALID(
        flag = PackageInstaller.STATUS_FAILURE_INVALID,
        title = R.string.installation_invalid,
        contentStringResId = R.string.installation_invalid_description,
        confirmButton = installerDialogButton(R.string.reinstall) { vm ->
            vm.reinstall()
        },
        dismissButton = installerDialogButton(R.string.cancel),
    ),
    FAILURE_STORAGE(
        flag = PackageInstaller.STATUS_FAILURE_STORAGE,
        title = R.string.installation_storage_issue,
        contentStringResId = R.string.installation_storage_issue_description,
    ),

    @RequiresApi(34)
    FAILURE_TIMEOUT(
        flag = PackageInstaller.STATUS_FAILURE_TIMEOUT,
        title = R.string.installation_timeout,
        contentStringResId = R.string.installation_timeout_description,
        confirmButton = installerDialogButton(R.string.install_app) { vm ->
            vm.install()
        },
    ),
    SUCCESS(
        flag = PackageInstaller.STATUS_SUCCESS,
        title = R.string.installation_success,
        contentStringResId = R.string.installation_success_description,
        icon = Icons.Outlined.Check,
    );

    // This is needed in order to have static extensions for @FromValue
    companion object
}