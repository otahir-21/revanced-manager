package app.revanced.manager.ui.component

import android.content.pm.PackageInstaller
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Update
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

private typealias InstallerDialogButtonHandler = (() -> Unit)?
private typealias InstallerDialogButton = @Composable (handler: InstallerDialogButtonHandler) -> Unit

@Composable
fun InstallerDialog(
    statusFlag: PackageInstallerStatusFlag = PackageInstallerStatusFlag.STATUS_SUCCESS,
    confirmButtonHandler: InstallerDialogButtonHandler = null,
    dismissButtonHandler: InstallerDialogButtonHandler = null,
) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            statusFlag.confirmButton(confirmButtonHandler)
        },
        dismissButton = {
            statusFlag.dismissButton(dismissButtonHandler)
        },
        icon = {
            Icon(statusFlag.icon, null)
        },
        title = {
            Text(
                text = stringResource(statusFlag.title),
                style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center),
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = statusFlag.content
            )
        }
    )
}

// Use once https://github.com/MateriiApps/enumutil-kt/issues/1 is closed:
// @FromValue
enum class PackageInstallerStatusFlag(
    internal val flag: Int,
    internal val title: Int,
    internal val content: @Composable ColumnScope.() -> Unit,
    internal val icon: ImageVector,
    @StringRes confirmButtonStringResId: Int,
    @StringRes dismissButtonStringResId: Int,
) {
    STATUS_FAILURE(
        flag = PackageInstaller.STATUS_FAILURE,
        title = R.string.auto_updates_dialog_title,
        content = { Text("STATUS_FAILURE") },
        icon = Icons.Outlined.Update,
        confirmButtonStringResId = R.string.ok,
        dismissButtonStringResId = R.string.cancel
    ),
    STATUS_FAILURE_ABORTED(
        flag = PackageInstaller.STATUS_FAILURE_ABORTED,
        title = R.string.auto_updates_dialog_title,
        content = { Text("STATUS_FAILURE_ABORTED") },
        icon = Icons.Outlined.Update,
        confirmButtonStringResId = R.string.ok,
        dismissButtonStringResId = R.string.cancel
    ),
    STATUS_FAILURE_BLOCKED(
        flag = PackageInstaller.STATUS_FAILURE_BLOCKED,
        title = R.string.auto_updates_dialog_title,
        content = { Text("STATUS_FAILURE_BLOCKED") },
        icon = Icons.Outlined.Update,
        confirmButtonStringResId = R.string.ok,
        dismissButtonStringResId = R.string.cancel
    ),
    STATUS_FAILURE_CONFLICT(
        flag = PackageInstaller.STATUS_FAILURE_CONFLICT,
        title = R.string.auto_updates_dialog_title,
        content = { Text("STATUS_FAILURE_CONFLICT") },
        icon = Icons.Outlined.Update,
        confirmButtonStringResId = R.string.ok,
        dismissButtonStringResId = R.string.cancel
    ),
    STATUS_FAILURE_INCOMPATIBLE(
        flag = PackageInstaller.STATUS_FAILURE_INCOMPATIBLE,
        title = R.string.auto_updates_dialog_title,
        content = { Text("STATUS_FAILURE_INCOMPATIBLE") },
        icon = Icons.Outlined.Update,
        confirmButtonStringResId = R.string.ok,
        dismissButtonStringResId = R.string.cancel
    ),
    STATUS_FAILURE_INVALID(
        flag = PackageInstaller.STATUS_FAILURE_INVALID,
        title = R.string.auto_updates_dialog_title,
        content = { Text("STATUS_FAILURE_INVALID") },
        icon = Icons.Outlined.Update,
        confirmButtonStringResId = R.string.ok,
        dismissButtonStringResId = R.string.cancel
    ),
    STATUS_FAILURE_STORAGE(
        flag = PackageInstaller.STATUS_FAILURE_STORAGE,
        title = R.string.auto_updates_dialog_title,
        content = { Text("STATUS_FAILURE_STORAGE") },
        icon = Icons.Outlined.Update,
        confirmButtonStringResId = R.string.ok,
        dismissButtonStringResId = R.string.cancel
    ),

    @RequiresApi(34)
    STATUS_FAILURE_TIMEOUT(
        flag = PackageInstaller.STATUS_FAILURE_TIMEOUT,
        title = R.string.auto_updates_dialog_title,
        content = { Text("STATUS_FAILURE_TIMEOUT") },
        icon = Icons.Outlined.Update,
        confirmButtonStringResId = R.string.ok,
        dismissButtonStringResId = R.string.cancel
    ),
    STATUS_PENDING_USER_ACTION(
        flag = PackageInstaller.STATUS_PENDING_USER_ACTION,
        title = R.string.auto_updates_dialog_title,
        content = { Text("STATUS_PENDING_USER_ACTION") },
        icon = Icons.Outlined.Update,
        confirmButtonStringResId = R.string.ok,
        dismissButtonStringResId = R.string.cancel
    ),
    STATUS_SUCCESS(
        flag = PackageInstaller.STATUS_SUCCESS,
        title = R.string.auto_updates_dialog_title,
        content = { Text("STATUS_SUCCESS") },
        icon = Icons.Outlined.Update,
        confirmButtonStringResId = R.string.ok,
        dismissButtonStringResId = R.string.cancel
    );

    internal val confirmButton = installerDialogButton(confirmButtonStringResId)
    internal val dismissButton = installerDialogButton(dismissButtonStringResId)

    private fun installerDialogButton(@StringRes id: Int): InstallerDialogButton = { handler ->
        TextButton(onClick = handler ?: { }) {
            Text(stringResource(id))
        }
    }

    // region Workaround until https://github.com/MateriiApps/enumutil-kt/issues/1 is closed

    companion object {
        fun fromValue(flag: Int) = when (flag) {
            STATUS_FAILURE.flag -> STATUS_FAILURE
            STATUS_FAILURE_ABORTED.flag -> STATUS_FAILURE_ABORTED
            STATUS_FAILURE_BLOCKED.flag -> STATUS_FAILURE_BLOCKED
            STATUS_FAILURE_CONFLICT.flag -> STATUS_FAILURE_CONFLICT
            STATUS_FAILURE_INCOMPATIBLE.flag -> STATUS_FAILURE_INCOMPATIBLE
            STATUS_FAILURE_INVALID.flag -> STATUS_FAILURE_INVALID
            STATUS_FAILURE_STORAGE.flag -> STATUS_FAILURE_STORAGE
            STATUS_PENDING_USER_ACTION.flag -> STATUS_PENDING_USER_ACTION
            STATUS_SUCCESS.flag -> STATUS_SUCCESS
            else -> if (Build.VERSION.SDK_INT >= 34 && flag == STATUS_FAILURE_TIMEOUT.flag)
                STATUS_FAILURE_TIMEOUT
            else throw IllegalArgumentException("Unknown flag: $flag")
        }
    }

    // endregion
}