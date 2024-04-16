package com.example.dogepasswordmanager

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            mainPage(this@MainPage)
        }
    }
}

//選項按鈕對應的物件
var appData: AppData? = null

@Composable
fun mainPage(context: Context) {
    var dialogShowingFlag = remember { mutableStateOf(false) }
    //當前頁面名稱(密碼庫、密碼產生器、設定)
    var currentPageName = remember {
        mutableStateOf("密碼庫")
    }

    var topLeftButtonVisibleFlag = remember {
        mutableStateOf(true)
    }

    var lists = ArrayList<AppData>()
    lists.add(AppData(R.drawable.google_icon, "Google", "test@gmail.com", "testPassword"))
    lists.add(AppData(R.drawable.google_icon, "Google2", "test@gmail.com", "testPassword"))
    lists.add(AppData(R.drawable.google_icon, "Google3", "test@gmail.com", "testPassword"))
    lists.add(AppData(R.drawable.google_icon, "Google4", "test@gmail.com", "testPassword"))
    lists.add(AppData(R.drawable.google_icon, "Google5", "test@gmail.com", "testPassword"))
    lists.add(AppData(R.drawable.google_icon, "Google6", "test@gmail.com", "testPassword"))
    lists.add(AppData(R.drawable.google_icon, "Google7", "test@gmail.com", "testPassword"))
    lists.add(AppData(R.drawable.google_icon, "Google8", "test@gmail.com", "testPassword"))
    lists.add(AppData(R.drawable.google_icon, "Google9", "test@gmail.com", "testPassword"))
    lists.add(AppData(R.drawable.google_icon, "Google10", "test@gmail.com", "testPassword"))
    lists.add(AppData(R.drawable.google_icon, "Google11", "test@gmail.com", "testPassword"))
    lists.add(AppData(R.drawable.google_icon, "Google12", "test@gmail.com", "testPassword"))


    Column(modifier = Modifier.fillMaxSize()) {
        //Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue)
                .weight(0.7f)
        ) {

            //頁面名稱
            Column {
                Text(text = currentPageName.value, fontSize = 30.sp, color = Color.White)
            }
            //功能圖示
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {

                topLeftFunctionButton(isVisible = topLeftButtonVisibleFlag.value)

            }


        }


        //Body(存放密碼紀錄區塊)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(8f)
                .background(Color.White)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                items(lists) { item ->

                    AppDataBlock(
                        AppImg = item.AppImg,
                        AppName = item.AppName,
                        AppUsername = item.AppUsername,
                        AppPassword = item.AppPassword,
                        dialogShowingFlag
                    )


                }
            }
        }


        //功能按鈕
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .weight(1f)
                .border(1.dp, Color.LightGray)
        ) {
            //密碼庫按鈕
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .border(1.dp, Color.LightGray)
                    .clickable() {
                        //按下按鈕後處理
                        currentPageName.value = "密碼庫"
                        //顯示左上角搜尋、過濾按鈕
                        topLeftButtonVisibleFlag.value = true;
                    }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.padlock),
                        contentDescription = "Padlock Icon",
                        modifier = Modifier.size(50.dp, 50.dp)
                    )
                    Text(text = "密碼庫")
                }

            }
            //密碼產生器按鈕
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .border(1.dp, Color.LightGray)
                    .clickable() {
                        //當前頁面名稱
                        currentPageName.value = "密碼產生器"
                        //隱藏左上角搜尋、過濾按鈕
                        topLeftButtonVisibleFlag.value = false;
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sync),
                    contentDescription = "Generate Password Icon",
                    modifier = Modifier.size(50.dp, 50.dp)
                )
                Text(text = "密碼產生器")
            }
            //設定按鈕
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .border(1.dp, Color.LightGray)
                    .clickable() {
                        //按下按鈕後處理
                        currentPageName.value = "設定"
                        //隱藏左上角搜尋、過濾按鈕
                        topLeftButtonVisibleFlag.value = false;
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = "Setting Icon",
                    modifier = Modifier.size(50.dp, 50.dp)
                )
                Text(text = "設定")
            }
        }


    }

    //顯示對話窗
    dialogWindow(context, dialogShowingFlag)
}

//應用程式紀錄區塊
@Composable
fun AppDataBlock(
    AppImg: Int,
    AppName: String,
    AppUsername: String,
    AppPassword: String,
    dialogShowingState: MutableState<Boolean>
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 7.dp)
            .clip(RoundedCornerShape(50.dp))
            .height(70.dp)

    ) {
        //App圖示
        Column(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //App Icon圖片
            Image(
                painter = painterResource(id = AppImg),
                contentDescription = AppName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Black, CircleShape)
            )
        }
        //App資訊顯示區塊
        Column(
            modifier = Modifier
                .weight(6f)

                .fillMaxSize()
        ) {
            //App名稱
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top
            ) {
                Text(text = AppName, fontSize = 26.sp)
            }

            //該使用者之App帳號
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = AppUsername, fontSize = 20.sp)
            }
        }

        //選項按鈕
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center, modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {

            //選項按鈕Icon
            Image(
                painter = painterResource(id = R.drawable.option),
                contentDescription = "Option Icon",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        //跳出對話框
                        dialogShowingState.value = true
                        appData = AppData(AppImg, AppName, AppUsername, AppPassword)
                    }
            )
        }
    }


}

//按下選項按鈕後出現的對話窗
@Composable
fun dialogWindow(context: Context, dialogShowingState: MutableState<Boolean>) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    if (dialogShowingState.value) {
        //對話窗內容
        Dialog(onDismissRequest = { dialogShowingState.value = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(375.dp)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RectangleShape
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 5.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //應用程式名稱Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)

                    ) {
                        Text(
                            text = appData!!.AppName,
                            fontSize = 25.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 7.dp)
                        )

                    }
                    //檢視帳號資訊
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable() {

                        }) {


                        Text(
                            text = "檢視帳號資訊",
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 7.dp)
                        )

                    }
                    //編輯帳號資訊
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable() {

                        }) {

                        Text(
                            text = "編輯帳號資訊",
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 7.dp)
                        )

                    }
                    //複製帳號
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable() {
                            //複製帳號到剪貼簿
                            clipboardManager.setText(AnnotatedString(appData!!.AppUsername))
                            //顯示複製成功訊息
                            Toast
                                .makeText(context, "複製成功", Toast.LENGTH_SHORT)
                                .show()
                        }) {

                        Text(
                            text = "複製帳號",
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 7.dp)
                        )


                    }
                    //複製密碼
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)

                        .clickable() {
                            //複製密碼到剪貼簿
                            clipboardManager.setText(AnnotatedString(appData!!.AppPassword))
                            //顯示複製成功訊息
                            Toast
                                .makeText(context, "複製成功", Toast.LENGTH_SHORT)
                                .show()
                        }) {

                        Text(
                            text = "複製密碼",
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 7.dp)
                        )


                    }
                }
            }

        }
    }

}

//左上方功能按鈕(搜尋、過濾)
@Composable
fun topLeftFunctionButton(isVisible: Boolean) {
    if (isVisible) {
        Row {
            Column(modifier = Modifier
                .padding(start = 5.dp, end = 10.dp)
                .clickable() {
                    //搜尋按鈕按下後的操作
                }) {

                //搜尋圖示
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search Icon",
                    modifier = Modifier
                        .size(35.dp, 35.dp)


                )
            }

            Column(modifier = Modifier
                .padding(start = 5.dp, end = 10.dp)
                .clickable() {
                    //搜尋按鈕按下後的操作
                }) {
                //過濾圖示
                Image(
                    painter = painterResource(id = R.drawable.filter),
                    contentDescription = "Filter Icon",
                    modifier = Modifier
                        .size(35.dp, 35.dp)

                )
            }

        }
    }

}