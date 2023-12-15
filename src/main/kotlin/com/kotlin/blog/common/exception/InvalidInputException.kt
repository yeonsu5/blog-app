package com.kotlin.blog.common.exception

import java.lang.RuntimeException

class InvalidInputException(
    val fieldName: String = "",
    message: String = "유효하지 않은 입력",
) : RuntimeException(message)
