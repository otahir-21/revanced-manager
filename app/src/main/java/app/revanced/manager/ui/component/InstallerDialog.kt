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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.revanced.manager.R
import com.github.materiiapps.enumutil.FromValue

abstract class InstallerDialogModel {
    abstract var installerStatus: Int
    abstract var showInstallerDialog: Boolean

    abstract fun reinstall()
}

private typealias InstallerDialogButtonHandler = ((model: InstallerDialogModel) -> Unit)
private typealias InstallerDialogButton = @Composable (model: InstallerDialogModel) -> Unit


@Composable
fun InstallerDialog(
    model: InstallerDialogModel,
) {
    val dialogKind = DialogKind.fromValue(model.installerStatus) ?: DialogKind.FAILURE

    AlertDialog(
        onDismissRequest = {
            model.showInstallerDialog = false
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
    handler: InstallerDialogButtonHandler = { },
): InstallerDialogButton = { model ->
    TextButton(onClick = {
        model.showInstallerDialog = false
        handler(model)
    }) { Text(stringResource(buttonStringResId)) }
}

// TODO: Change to internal once https://github.com/MateriiApps/enumutil-kt/issues/4 is fixed
@FromValue("flag")
enum class DialogKind(
    val flag: Int,
    val title: Int,
    @StringRes val contentStringResId: Int,
    val icon: ImageVector = Icons.Outlined.ErrorOutline,
    val confirmButton: InstallerDialogButton = installerDialogButton(R.string.ok),
    val dismissButton: InstallerDialogButton? = null,
) {
    FAILURE(
        flag = PackageInstaller.STATUS_FAILURE,
        title = R.string.installer_result_failed,
        contentStringResId = R.string.installer_result_failed_description,
    ),
    FAILURE_ABORTED(
        flag = PackageInstaller.STATUS_FAILURE_ABORTED,
        title = R.string.installer_result_cancelled,
        contentStringResId = R.string.installer_result_aborted_description,
    ),
    FAILURE_BLOCKED(
        flag = PackageInstaller.STATUS_FAILURE_BLOCKED,
        title = R.string.installer_result_blocked,
        contentStringResId = R.string.installer_result_blocked_description,
    ),
    FAILURE_CONFLICT(
        flag = PackageInstaller.STATUS_FAILURE_CONFLICT,
        title = R.string.installer_result_conflict,
        contentStringResId = R.string.installer_result_conflict_description,
        confirmButton = installerDialogButton(R.string.installer_reinstall) { vm ->
            vm.reinstall()
        },
        dismissButton = installerDialogButton(R.string.cancel),
    ),
    FAILURE_INCOMPATIBLE(
        flag = PackageInstaller.STATUS_FAILURE_INCOMPATIBLE,
        title = R.string.installer_result_incompatible,
        contentStringResId = R.string.installer_result_incompatible_description,
    ),
    FAILURE_INVALID(
        flag = PackageInstaller.STATUS_FAILURE_INVALID,
        title = R.string.installer_result_invalid,
        contentStringResId = R.string.installer_result_invalid_description,
        confirmButton = installerDialogButton(R.string.installer_reinstall) { vm ->
            vm.reinstall()
        },
        dismissButton = installerDialogButton(R.string.cancel),
    ),
    FAILURE_STORAGE(
        flag = PackageInstaller.STATUS_FAILURE_STORAGE,
        title = R.string.installer_result_storage_issue,
        contentStringResId = R.string.installer_result_storage_issue_description,
    ),

    @RequiresApi(34)
    FAILURE_TIMEOUT(
        flag = PackageInstaller.STATUS_FAILURE_TIMEOUT,
        title = R.string.installer_result_timeout,
        contentStringResId = R.string.installer_result_timeout_description,
    ),
    SUCCESS(
        flag = PackageInstaller.STATUS_SUCCESS,
        title = R.string.installer_result_success,
        contentStringResId = R.string.installer_result_success_description,
        icon = Icons.Outlined.Check,
    );

    // This is needed in order to have static extensions for @FromValue
    companion object
}