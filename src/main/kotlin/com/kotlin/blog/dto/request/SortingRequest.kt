package com.kotlin.blog.dto.request

data class SortingRequest(
    var sortBy: SortBy = SortBy.ID,
    var orderBy: OrderBy = OrderBy.DESC,
)

enum class SortBy(val fieldName: String) {
    ID("id"),
    TITLE("title"),
    CREATED_AT("createdAt")
}

enum class OrderBy {
    ASC,
    DESC,
}
