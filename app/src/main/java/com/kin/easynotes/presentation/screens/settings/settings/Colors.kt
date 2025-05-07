package com.kin.easynotes.presentation.screens.settings.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.Battery1Bar
import androidx.compose.material.icons.rounded.Colorize
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.DynamicFeed
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.HdrAuto
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.ViewAgenda
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.presentation.screens.settings.SettingsScaffold
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.ActionType
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox

fun shapeManager(isBoth: Boolean = false,isLast: Boolean = false,isFirst: Boolean = false,radius: Int): RoundedCornerShape {
    val smallerRadius: Dp = (radius/5).dp
    val defaultRadius: Dp = radius.dp

    return when {
        isBoth -> RoundedCornerShape(defaultRadius)
        isLast -> RoundedCornerShape(smallerRadius,smallerRadius,defaultRadius,defaultRadius)
        isFirst -> RoundedCornerShape(defaultRadius,defaultRadius,smallerRadius,smallerRadius)
        else -> RoundedCornerShape(smallerRadius)
    }
}

@Composable
fun ColorStylesScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.color_styles),
        onBackNavClicked = { navController.navigateUp() }
    ) {
        LazyColumn {
            item {
                SettingsBox(
                    settingsViewModel = settingsViewModel,
                    title = stringResource(id = R.string.minimalistic_mode),
                    description = stringResource(id = R.string.minimalistic_mode_description),
                    icon = Icons.Rounded.DynamicFeed,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isBoth = true),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.minimalisticMode,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(minimalisticMode = it))}
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    settingsViewModel = settingsViewModel,
                    title = stringResource(id = R.string.system_theme),
                    description = stringResource(id = R.string.system_theme_description),
                    icon = Icons.Rounded.HdrAuto,
                    actionType = ActionType.SWITCH,
                    radius = shapeManager(isFirst = true ,isBoth = (!isSystemInDarkTheme() && settingsViewModel.settings.value.automaticTheme), radius = settingsViewModel.settings.value.cornerRadius),
                    variable = settingsViewModel.settings.value.automaticTheme,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(automaticTheme = it))}
                )
            }
            item {
                SettingsBox(
                    settingsViewModel = settingsViewModel,
                    title = stringResource(id = R.string.dark_theme),
                    description = stringResource(id = R.string.dark_theme_description),
                    isEnabled = !settingsViewModel.settings.value.automaticTheme,
                    icon = Icons.Rounded.Palette,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.darkTheme,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(automaticTheme = false, darkTheme = it))}
                )
            }
            item {
                SettingsBox(
                    settingsViewModel = settingsViewModel,
                    title = stringResource(id = R.string.dynamic_colors),
                    description = stringResource(id = R.string.dynamic_colors_description),
                    icon = Icons.Rounded.Colorize,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = !(settingsViewModel.settings.value.darkTheme)),
                    isEnabled = !settingsViewModel.settings.value.automaticTheme,
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.dynamicTheme,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(automaticTheme = false, dynamicTheme = it))}
                )
            }
            item {
                val value = settingsViewModel.settings.value.amoledTheme
                SettingsBox(
                    settingsViewModel = settingsViewModel,
                    title = stringResource(id = R.string.amoled_colors),
                    description = stringResource(id = R.string.amoled_colors_description),
                    icon = Icons.Rounded.DarkMode,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = !value),
                    actionType = ActionType.SWITCH,
                    isEnabled = settingsViewModel.settings.value.darkTheme,
                    variable = value,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(amoledTheme = it))}
                )
            }
            item {
                SettingsBox(
                    settingsViewModel = settingsViewModel,
                    title = stringResource(id = R.string.extreme_amoled_mode),
                    icon = Icons.Rounded.Battery1Bar,
                    description = stringResource(id = R.string.extreme_amoled_mode_description),
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = true),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.extremeAmoledMode,
                    isEnabled = settingsViewModel.settings.value.amoledTheme && settingsViewModel.settings.value.darkTheme,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(extremeAmoledMode = it))}
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    settingsViewModel = settingsViewModel,
                    title = if (settingsViewModel.settings.value.viewMode) stringResource(id = R.string.grid_view) else stringResource(id = R.string.column_view),
                    icon = if (settingsViewModel.settings.value.viewMode) Icons.Rounded.GridView else Icons.Rounded.ViewAgenda,
                    description = stringResource(id = R.string.view_style_description),
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.viewMode,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(viewMode = it))}
                )
            }
            item {
                SettingsBox(
                    settingsViewModel = settingsViewModel,
                    title = if (settingsViewModel.settings.value.sortDescending) stringResource(id = R.string.sort_descending) else stringResource(id = R.string.sort_ascending),
                    description = stringResource(id = R.string.sort_description),
                    icon = Icons.AutoMirrored.Rounded.Sort,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = true),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.sortDescending,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(sortDescending = it)) }
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}
