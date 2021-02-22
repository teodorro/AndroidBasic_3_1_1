package ru.netology.androidbasic_3_1_1.dto

data class Attachment(
    val url: String,
    val description: String,
    val type: AttachmentType
)

enum class AttachmentType {
    IMAGE
}
