package com.toyibnurseha.dicodingsubmission.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    var id: Int = 0,
    var username: String? = null,
    var name: String? = null,
    var bio: String? = null,
    var avatar: String? = null
) : Parcelable