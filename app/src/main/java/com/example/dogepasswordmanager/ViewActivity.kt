package com.example.dogepasswordmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ViewActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        intentObj = intent
        super.onCreate(savedInstanceState)
        setContent {
            ViewPage(context = this@ViewActivity)

        }
    }
}

private lateinit var intentObj: Intent

@Composable
fun ViewPage(context: Context) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //應用程式名稱
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
            ) {

                Column(modifier = Modifier.fillMaxWidth()) {

                    Text(text = "名稱", color = Color.Gray, fontSize = 15.sp)

                    intentObj.getStringExtra(MainPage.APP_NAME)
                        ?.let { Text(text = it, fontSize = 25.sp) }
                }


            }
            Divider(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                color = Color.LightGray
            )
            //使用者名稱(帳號)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
            ) {

                Column(modifier = Modifier.fillMaxWidth()) {

                    Text(text = "使用者名稱", color = Color.Gray, fontSize = 15.sp)

                    intentObj.getStringExtra(MainPage.APP_USERNAME)
                        ?.let { Text(text = it, fontSize = 25.sp) }
                }

            }

            Divider(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                color = Color.LightGray
            )
            //密碼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
            ) {

                Column(modifier = Modifier.fillMaxWidth()) {

                    Text(text = "密碼", color = Color.Gray, fontSize = 15.sp)

                    intentObj.getStringExtra(MainPage.APP_PASSWORD)
                        ?.let { Text(text = it, fontSize = 25.sp) }
                }


            }
            Divider(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                color = Color.LightGray
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            //刪除、修改按鈕
            Row(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                //編輯按鈕
                Icon(
                    painter = rememberVectorPainter(Icons.Filled.Edit),
                    contentDescription = "Edit Icon",
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                )
                //刪除按鈕
                Icon(
                    painter = rememberVectorPainter(Icons.Filled.Delete),
                    contentDescription = "Edit Icon",
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                )
            }
        }
    }
}