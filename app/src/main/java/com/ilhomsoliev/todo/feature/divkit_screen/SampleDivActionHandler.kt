package com.ilhomsoliev.todo.feature.divkit_screen

import android.net.Uri
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction

class SampleDivActionHandler(private val onBack: () -> Unit) : DivActionHandler() {
    override fun handleAction(
        action: DivAction, view: DivViewFacade, resolver: ExpressionResolver
    ): Boolean {
        val url =
            action.url?.evaluate(resolver) ?: return super.handleAction(action, view, resolver)

        return if (url.scheme == SCHEME_SAMPLE && handleSampleAction(url)) {
            true
        } else {
            super.handleAction(action, view, resolver)
        }
    }

    private fun handleSampleAction(action: Uri): Boolean {
        return when (action.host) {
            "go_back" -> {
                onBack()
                true
            }

            else -> false
        }
    }

    companion object {
        const val SCHEME_SAMPLE = "sample-action"
    }
}