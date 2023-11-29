package com.kotlin.blog.post.dto.request

data class SortingRequest(
    var sortBy: SortBy = SortBy.ID,
    var orderBy: OrderBy = OrderBy.DESC,
)

enum class SortBy(val fieldName: String) {
    ID("id"),
    TITLE("title"),
}

enum class OrderBy {
    ASC,
    DESC,
}
