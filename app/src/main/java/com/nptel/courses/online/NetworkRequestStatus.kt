package com.nptel.courses.online

data class NetworkRequestStatus(val status: Status, val error: String? = null)

enum class Status {
    Loading, Loaded, Error
}