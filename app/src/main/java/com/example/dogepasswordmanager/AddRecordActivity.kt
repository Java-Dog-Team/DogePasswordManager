package com.example.dogepasswordmanager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddRecordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val intent = intent


        setContent {
            AddRecordPage(context = this@AddRecordActivity, intent)
        }


    }
}


@Composable
fun AddRecordPage(context: Context, intent: Intent) {
    //使用者選擇的圖片
    var selectedImg = remember {
        mutableStateOf<List<Uri?>>(emptyList())
    }
    Log.d("MSG", selectedImg.value.toString())

    //圖片選擇物件


}

