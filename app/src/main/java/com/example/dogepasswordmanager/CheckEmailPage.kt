package com.example.dogepasswordmanager

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class CheckEmailPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            checkEmailPage(this@CheckEmailPage)
        }
    }
}

private lateinit var auth: FirebaseAuth

//提醒查收email頁面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun checkEmailPage(context: Context) {
    val activity = LocalContext.current as? Activity
    Scaffold(
        // 叉叉按鈕
        // 按下後直接顯示登入頁面
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(235, 195, 18,0),
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        activity?.finish()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Localized description",
                            tint= Color(235, 195, 18),
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                        )
                    }
                },
                modifier = Modifier.padding(0.dp)
            )
        },
    ) { innerPadding ->
        Column {
            //標題
            Row (
                modifier = Modifier.padding(innerPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = stringResource(id = R.string.checkEmailTitle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    color = Color(237, 197, 49),
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center
                )
            }
            Column (
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = stringResource(id = R.string.checkEmailArtical),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, ),
                    color = Color.Gray,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(id = R.string.checkEmailAddress),
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color.Gray,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center
                )
                Image(painter = painterResource(R.drawable.recive_email),
                    contentDescription = stringResource(id = R.string.login_title),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.End)
                )
            }
        }
    }
}