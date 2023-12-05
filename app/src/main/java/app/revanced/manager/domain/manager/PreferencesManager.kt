package app.revanced.manager.domain.manager

import android.content.Context
import app.revanced.manager.domain.manager.base.BasePreferencesManager
import app.revanced.manager.ui.theme.Theme

class PreferencesManager(
    context: Context
) : BasePreferencesManager(context, "settings") {
    val dynamicColor = booleanPreference("dynamic_color", true)
    val theme = enumPreference("theme", Theme.SYSTEM)

    val api = stringPreference("api_url", "https://api.revanced.app")

    val multithreadingDexFileWriter = booleanPreference("multithreading_dex_file_writer", true)
    val allowExperimental = booleanPreference("allow_experimental", false)

    val keystoreCommonName = stringPreference("keystore_cn", KeystoreManager.DEFAULT)
    val keystorePass = stringPreference("keystore_pass", KeystoreManager.DEFAULT)

    val preferSplits = booleanPreference("prefer_splits", false)

    val firstLaunch = booleanPreference("first_launch", true)
    val managerAutoUpdates = booleanPreference("manager_auto_updates", false)

    val disableSelectionWarning = booleanPreference("disable_selection_warning", false)
    val enableSelectionWarningCountdown = booleanPreference("enable_selection_warning_countdown", true)
}
