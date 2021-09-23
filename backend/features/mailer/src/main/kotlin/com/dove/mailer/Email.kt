package com.dove.mailer

class Email(
    val subject: String,
    val message: String,
    val receivers: List<String>
) {
    constructor(subject: String, message: String, receiver: String) : this(subject, message, listOf(receiver))
}