package com.example.dogepasswordmanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.dogepasswordmanager.ui.theme.BrickRed
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

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
//firebase 資料庫
private val db = Firebase.firestore
@Composable
fun ViewPage(context: Context) {
    var openDialog by remember {
        mutableStateOf(false)
    }
    val activity = LocalContext.current as Activity
    //剪貼簿管理員物件
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        if (openDialog) {
            AlertDialog(
                icon = {
                    Icon(Icons.Filled.Warning, contentDescription = "Example Icon")
                },
                title = {
                    Text(text = "刪除項目", fontSize = 30.sp)
                },
                text = {
                    Text(text = "您確定要刪除此項目嗎?", fontSize = 20.sp)
                },
                onDismissRequest = {
                    openDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            //按下確認刪除後的操作
                            openDialog = false

                            //刪除檔案
                        }
                    ) {
                        Text("確定")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            //按下取消刪除後的操作
                            openDialog = false
                        }
                    ) {
                        Text("取消")
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp, start = 5.dp)
            ) {
                Text(text = "項目資訊", color = Color.Blue, fontSize = 20.sp)
            }
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
                    .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                //資訊顯示
                Column(modifier = Modifier.weight(1f)) {

                    Text(text = "使用者名稱", color = Color.Gray, fontSize = 15.sp)

                    intentObj.getStringExtra(MainPage.APP_USERNAME)
                        ?.let { Text(text = it, fontSize = 25.sp) }
                }
                //複製按鈕
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End
                ) {

                    //複製按鈕icon
                    Icon(
                        painter = painterResource(id = R.drawable.copy),
                        contentDescription = "Copy Icon",
                        modifier = Modifier
                            .clickable() {
                                //複製帳號到剪貼簿
                                intentObj
                                    .getStringExtra(
                                        MainPage.APP_USERNAME
                                    )
                                    ?.let {
                                        clipboardManager.setText(
                                            AnnotatedString(
                                                it
                                            )
                                        )

                                    }

                                Toast
                                    .makeText(context, "複製成功", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            .size(35.dp)
                            .padding(bottom = 5.dp),
                        tint = Color.Blue
                    )

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
                    .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Text(text = "密碼", color = Color.Gray, fontSize = 15.sp)

                    intentObj.getStringExtra(MainPage.APP_PASSWORD)
                        ?.let { Text(text = it, fontSize = 25.sp) }
                }

                //複製按鈕
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End
                ) {
                    //複製按鈕icon
                    Icon(
                        painter = painterResource(id = R.drawable.copy),
                        contentDescription = "Copy Icon",
                        modifier = Modifier
                            .clickable() {
                                //複製帳號到剪貼簿
                                intentObj
                                    .getStringExtra(
                                        MainPage.APP_PASSWORD
                                    )
                                    ?.let {
                                        clipboardManager.setText(
                                            AnnotatedString(
                                                it
                                            )
                                        )

                                    }

                                Toast
                                    .makeText(context, "複製成功", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            .size(35.dp)
                            .padding(bottom = 5.dp),
                        tint = Color.Blue
                    )
                }

            }
            Divider(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                color = Color.LightGray
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                //刪除、修改按鈕
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp, end = 10.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End

                ) {

                    //編輯按鈕
                    FloatingActionButton(
                        onClick = { /*TODO*/ },
                        shape = CircleShape,
                        containerColor = Color.Blue,
                        contentColor = Color.White,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Icon")
                    }

                    //刪除按鈕
                    FloatingActionButton(
                        onClick = {
                            //按下刪除按鈕後的操作
                            openDialog = true
                        },
                        shape = CircleShape,
                        containerColor = BrickRed,
                        contentColor = Color.White,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Edit Icon")
                    }
                }
            }


        }

    }
}


