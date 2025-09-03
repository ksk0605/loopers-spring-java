package com.loopers.support.error

data class CoreException(
    val errorType: ErrorType,
    val customMessage: String? = null
) : RuntimeException(customMessage ?: errorType.message)
