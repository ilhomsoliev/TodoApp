package com.ilhomsoliev.todo.feature.divkit_screen

import android.content.Context
import android.content.ContextWrapper
import android.view.ContextThemeWrapper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.picasso.PicassoDivImageLoader
import org.json.JSONObject

@Composable
fun InfoScreen(onBack: () -> Unit) {

    fun createDivConfiguration(context: Context): DivConfiguration {
        return DivConfiguration.Builder(PicassoDivImageLoader(context))
            .actionHandler(SampleDivActionHandler(onBack))
            .build()
    }

    fun getLifecycleOwner(context: Context): LifecycleOwner {
        var context = context
        while (context !is LifecycleOwner) {
            context = (context as ContextWrapper).baseContext;
        }
        return context as LifecycleOwner
    }
    AndroidView(modifier = Modifier.fillMaxSize(), factory = {
        val jsonString = it.assets.open("about_screen.json").bufferedReader().use { it.readText() }
        val updatedJsonString = replaceColorsInJson(it, jsonString)
        val divJson = JSONObject(updatedJsonString)
        val templatesJson = divJson.optJSONObject("templates")
        val cardJson = divJson.getJSONObject("card")

        val divContext = Div2Context(
            baseContext = it as ContextThemeWrapper,
            configuration = createDivConfiguration(it),
            lifecycleOwner = getLifecycleOwner(it)
        )
        val divView = Div2ViewFactory(divContext, templatesJson).createView(cardJson)
        divView
    })
}