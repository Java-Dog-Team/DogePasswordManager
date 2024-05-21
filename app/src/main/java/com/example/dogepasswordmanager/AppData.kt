package com.example.dogepasswordmanager

import android.net.Uri
import android.webkit.WebStorage.Origin


//紀錄Id,使用者信箱,應用程式圖片id(空字串代表使用預設圖片)，應用程式名稱,應用程式使用者名稱,應用程式密碼
data class AppData(
    val DataId: String,
    val userEmail: String,
    var AppImgId: String = "",
    var AppName: String,
    var AppUsername: String,
    var AppPassword: String,
    var AppImgUri: Uri? = null,
    var Theme: String = "Origin"
)
