package com.kin.easynotes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.kin.easynotes.presentation.screens.settings.MainSettings
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

import com.kin.easynotes.presentation.screens.settings.settings.ColorStylesScreen
import com.kin.easynotes.presentation.screens.settings.settings.LanguageScreen
import com.kin.easynotes.presentation.screens.settings.settings.MarkdownScreen


enum class ActionType {
    PASSCODE,
    FINGERPRINT,
    PATTERN
}

sealed class NavRoutes(val route: String) {
    data object Home : NavRoutes("home")
    data object Edit : NavRoutes("edit/{id}/{encrypted}") {
        fun createRoute(id: Int, encrypted : Boolean) = "edit/$id/$encrypted"
    }
    data object Terms : NavRoutes("terms")
    data object Settings : NavRoutes("settings")
    data object ColorStyles : NavRoutes("settings/color_styles")
    data object Language : NavRoutes("settings/language")
    data object Markdown : NavRoutes("settings/markdown")
    data object Support : NavRoutes("settings/support")
    data object LockScreen : NavRoutes("settings/lock/{type}") {
        fun createRoute(action: ActionType?) = "settings/lock/$action"
    }
}

val settingScreens = mapOf<String, @Composable (settingsViewModel: SettingsViewModel, navController : NavController) -> Unit>(
    NavRoutes.Settings.route to { settings, navController -> MainSettings(settings, navController) },
    NavRoutes.ColorStyles.route to { settings, navController -> ColorStylesScreen(navController,settings) },
    NavRoutes.Language.route to { settings, navController -> LanguageScreen(navController,settings) },
    NavRoutes.Markdown.route to { settings, navController ->  MarkdownScreen(navController,settings) },

)
