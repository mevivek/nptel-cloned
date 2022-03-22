package com.nptel.courses.online.entities

import androidx.annotation.Keep

@Keep
data class User(val id: String,
                var firstName: String? = null,
                var lastName: String? = null,
                var email: String? = null,
                var phone: String? = null,
                var provider: String? = null,
                var openId: String? = null)