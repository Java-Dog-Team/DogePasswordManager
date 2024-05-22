package com.example.dogepasswordmanager

import android.app.Activity
import android.app.ActivityOptions
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.dogepasswordmanager.ui.theme.BrickRed
import com.example.dogepasswordmanager.ui.theme.ItemColor
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class ViewActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        intentObj = intent
        auth = Firebase.auth


        userEmail = auth.currentUser?.email.toString()
        super.onCreate(savedInstanceState)
        setContent {
            ViewPage(context = this@ViewActivity)

        }
    }
}

private lateinit var intentObj: Intent

//firebase 資料庫
private val db = Firebase.firestore
private lateinit var auth: FirebaseAuth

//使用者信箱
private lateinit var userEmail: String

fun deleteRecord(activity: Activity) {
//    刪除記錄
    intentObj.getStringExtra(MainPage.DATA_ID)?.let {
        db.collection(userEmail).document(it)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
                //關閉檢視頁面
                activity.finish()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                    Icon(Icons.Filled.Warning,
                        contentDescription = "Example Icon",
                        tint = BrickRed,)
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.view_alert_dialog_title),
                        fontSize = 30.sp
                    )
                },
                text = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.view_alert_dialog_content),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
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
                            deleteRecord(activity)
                        }
                    ) {
                        Text(stringResource(id = R.string.view_alert_dialog_confirm_button),
                            color = BrickRed)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            //按下取消刪除後的操作
                            openDialog = false
                        }
                    ) {
                        Text(stringResource(id = R.string.view_alert_dialog_cancel_button),
                            color = Color.Gray)
                    }
                },
                containerColor = Color(253, 248, 225)
            )
        }

        Scaffold(
            // 返回按鈕
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ItemColor,
                        titleContentColor = ItemColor,
                    ),
                    title = {
                        Text(
                            text = stringResource(id = R.string.view_title),
                            color = Color.White,
                            fontSize = 30.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            activity?.finish()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description",
                                tint = Color.White,
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //應用程式名稱
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp, top = 30.dp, bottom = 10.dp)
                ) {

                    Column(modifier = Modifier.fillMaxWidth()) {

                        Text(
                            text = stringResource(R.string.view_app_name),
                            color = Color.Gray,
                            fontSize = 15.sp
                        )

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

                        Text(
                            text = stringResource(id = R.string.view_app_username),
                            color = Color.Gray,
                            fontSize = 15.sp
                        )

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
                                        .makeText(
                                            context,
                                            context.getString(R.string.copy_success),
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                                .size(35.dp)
                                .padding(bottom = 5.dp),
                            tint = ItemColor
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

                        Text(
                            text = stringResource(id = R.string.view_app_password),
                            color = Color.Gray,
                            fontSize = 15.sp
                        )

                        intentObj.getStringExtra(MainPage.APP_PASSWORD)
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
                                        .makeText(
                                            context,
                                            context.getString(R.string.copy_success),
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                                .size(35.dp)
                                .padding(bottom = 5.dp),
                            tint = ItemColor
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
                            onClick = {
                                //按下編輯按鈕後的操作

                                //跳出編輯視窗
                                var newIntent = Intent()
                                newIntent.setClass(context, AddRecordActivity::class.java)
                                newIntent.putExtra(
                                    MainPage.DATA_ID,
                                    intentObj.getStringExtra(MainPage.DATA_ID)
                                )
                                newIntent.putExtra(
                                    MainPage.APP_IMG_ID,
                                    intentObj.getStringExtra(MainPage.APP_IMG_ID)
                                )
                                newIntent.putExtra(
                                    MainPage.APP_NAME,
                                    intentObj.getStringExtra(MainPage.APP_NAME)
                                )
                                newIntent.putExtra(
                                    MainPage.APP_PASSWORD,
                                    intentObj.getStringExtra(MainPage.APP_PASSWORD)
                                )
                                newIntent.putExtra(
                                    MainPage.APP_USERNAME,
                                    intentObj.getStringExtra(MainPage.APP_USERNAME)
                                )

                                context.startActivity(
                                    newIntent, ActivityOptions.makeCustomAnimation(
                                        context as Activity,
                                        androidx.appcompat.R.anim.abc_slide_in_bottom,
                                        androidx.appcompat.R.anim.abc_popup_exit
                                    ).toBundle()
                                )
                                //關閉此頁面
                                activity.finish()
                            },
                            shape = CircleShape,
                            containerColor = ItemColor,
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
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Edit Icon"
                            )
                        }
                    }
                }
            }
        }
    }
}


