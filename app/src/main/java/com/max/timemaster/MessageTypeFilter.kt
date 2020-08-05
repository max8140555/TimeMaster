package com.max.timemaster

enum class MessageTypeFilter(val value: String) {
    CONFLICT("conflict"),
    TIME_ERROR("timeError"),
    NOT_TITLE("notTitle"),
    INCOMPLETE_TEXT("incompleteText"),
    ACTIVE("active"),
    ARCHIVE("archive")
}