package com.skele.pomodoro.data

import android.content.Context

sealed interface ExceptionMessage {
    data class RuntimeError(
        val messageId: Int,
    ) : ExceptionMessage

    data class NetworkError(
        val message: String,
    ) : ExceptionMessage

    fun getErrorMessage(context: Context): String =
        when (this) {
            is RuntimeError -> context.getString(messageId)
            is NetworkError -> message
        }
}
