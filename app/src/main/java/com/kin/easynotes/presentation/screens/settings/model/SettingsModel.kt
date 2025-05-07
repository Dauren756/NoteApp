package com.kin.easynotes.presentation.screens.settings.model

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kin.easynotes.BuildConfig
import com.kin.easynotes.R
import com.kin.easynotes.domain.model.Settings
import com.kin.easynotes.domain.usecase.NoteUseCase
import com.kin.easynotes.domain.usecase.SettingsUseCase
import com.kin.easynotes.presentation.components.GalleryObserver
import com.kin.easynotes.presentation.navigation.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    val galleryObserver: GalleryObserver,
    private val settingsUseCase: SettingsUseCase,
    val noteUseCase: NoteUseCase,
) : ViewModel() {
    var defaultRoute: String? = null

    fun loadDefaultRoute() {
        if (_settings.value.fingerprint == false && _settings.value.passcode == null && _settings.value.pattern == null) {
            defaultRoute == NavRoutes.Home.route
        } else {
            defaultRoute = _settings.value.defaultRouteType

        }
    }

    fun updateDefaultRoute(route: String) {
        _settings.value = _settings.value.copy(defaultRouteType = route)
        update(settings.value.copy(defaultRouteType = route))
    }

    val databaseUpdate = mutableStateOf(false)
    var password : String? = null

    private val _settings = mutableStateOf(Settings())
    var settings: State<Settings> = _settings

    private suspend fun loadSettings() {
        val loadedSettings = runBlocking(Dispatchers.IO) {
            settingsUseCase.loadSettingsFromRepository()
        }
        _settings.value = loadedSettings
        if (_settings.value.fingerprint == false && _settings.value.passcode == null && _settings.value.pattern == null) {

            defaultRoute = NavRoutes.Home.route
        } else {
            defaultRoute = loadedSettings.defaultRouteType
        }
    }

    fun update(newSettings: Settings) {
        _settings.value = newSettings.copy()
        viewModelScope.launch {
            settingsUseCase.saveSettingsToRepository(newSettings)
        }
    }



    // Taken from: https://stackoverflow.com/questions/74114067/get-list-of-locales-from-locale-config-in-android-13
    private fun getLocaleListFromXml(context: Context): LocaleListCompat {
        val tagsList = mutableListOf<CharSequence>()
        try {
            val xpp: XmlPullParser = context.resources.getXml(R.xml.locales_config)
            while (xpp.eventType != XmlPullParser.END_DOCUMENT) {
                if (xpp.eventType == XmlPullParser.START_TAG) {
                    if (xpp.name == "locale") {
                        tagsList.add(xpp.getAttributeValue(0))
                    }
                }
                xpp.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return LocaleListCompat.forLanguageTags(tagsList.joinToString(","))
    }

    fun getSupportedLanguages(context: Context): Map<String, String> {
        val allowedLanguages = setOf("en", "ru", "kk")
        val localeList = getLocaleListFromXml(context)
        val map = mutableMapOf<String, String>()

        for (i in 0 until localeList.size()) {
            val locale = localeList[i]
            val tag = locale?.toLanguageTag() ?: continue
            if (tag in allowedLanguages) {
                map[locale.getDisplayName(locale)] = tag
            }
        }

        // Добавим системный язык первым, если хочешь
        map[context.getString(R.string.system_language)] = ""

        return map
    }


    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    val version: String = BuildConfig.VERSION_NAME
    val build: String = BuildConfig.BUILD_TYPE

    init {
        runBlocking {
            loadSettings()
        }
    }
}
