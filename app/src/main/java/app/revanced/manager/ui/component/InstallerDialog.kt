package app.revanced.manager.ui.component

import android.content.pm.PackageInstaller
import android.os.Build
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

private typealias InstallerDialogButtonHandler = (() -> Unit)?
private typealias InstallerDialogButton = @Composable (handler: InstallerDialogButtonHandler) -> Unit

@Composable
fun InstallerDialog(
    statusFlag: Int,
    confirmButtonHandler: InstallerDialogButtonHandler = null,
    dismissButtonHandler: InstallerDialogButtonHandler = null,
) {
    InstallerDialog(
        DialogKind.fromValue(statusFlag) ?: DialogKind.FAILURE,
        confirmButtonHandler,
        dismissButtonHandler,
    )
}

@Composable
fun InstallerDialog(
    dialogKind: DialogKind = DialogKind.SUCCESS,
    confirmButtonHandler: InstallerDialogButtonHandler = null,
    dismissButtonHandler: InstallerDialogButtonHandler = null,
) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            dialogKind.confirmButton(confirmButtonHandler)
        },
        dismissButton = {
            dialogKind.dismissButton(dismissButtonHandler)
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

@FromValue("flag")
enum class DialogKind(
    internal val flag: Int,
    internal val title: Int,
    @StringRes internal val contentStringResId: Int,
    internal val icon: ImageVector = Icons.Outlined.ErrorOutline,
    @StringRes confirmButtonStringResId: Int = R.string.save_apk,
    @StringRes dismissButtonStringResId: Int = R.string.cancel
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
        confirmButtonStringResId = R.string.uninstall,
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
    ),
    FAILURE_STORAGE(
        flag = PackageInstaller.STATUS_FAILURE_STORAGE,
        title = R.string.installer_result_storage_issue,
        contentStringResId = R.string.installer_result_storage_issue_description,
        confirmButtonStringResId = R.string.ok,
    ),

    @RequiresApi(34)
    FAILURE_TIMEOUT(
        flag = PackageInstaller.STATUS_FAILURE_TIMEOUT,
        title = R.string.installer_result_timeout,
        contentStringResId = R.string.installer_result_timeout_description,
        dismissButtonStringResId = R.string.cancel
    ),
    SUCCESS(
        flag = PackageInstaller.STATUS_SUCCESS,
        title = R.string.installer_result_success,
        contentStringResId = R.string.installer_result_success_description,
        icon = Icons.Outlined.Check,
        confirmButtonStringResId = R.string.ok,
    );

    internal val confirmButton = installerDialogButton(confirmButtonStringResId)
    internal val dismissButton = installerDialogButton(dismissButtonStringResId)

    private fun installerDialogButton(@StringRes id: Int): InstallerDialogButton = { handler ->
        TextButton(onClick = handler ?: { }) { Text(stringResource(id)) }
    }

    // This is needed in order to have static extensions for @FromValue
    companion object
}