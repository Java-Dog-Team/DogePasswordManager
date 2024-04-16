package com.example.dogepasswordmanager

import android.media.Image
import android.os.Bundle
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            mainPage()
        }
    }
}

@Composable
fun mainPage() {
    //當前頁面名稱(密碼庫、密碼產生器、設定)
    var currentPageName = remember {
        mutableStateOf("密碼庫")
    }

    var topLeftButtonVisibleFlag = remember {
        mutableStateOf(true)
    }

    var lists = ArrayList<String>()
    lists.add("test1")
    lists.add("test2")
    lists.add("test3")
    lists.add("test3")
    lists.add("test3")
    lists.add("test3")
    lists.add("test3")
    lists.add("test3")
    lists.add("test3")
    lists.add("test3")
    lists.add("test3")
    lists.add("test3")
    lists.add("test3")
    lists.add("test3")
    lists.add("test3")
    lists.add("test3")

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
                    Text(text = item, fontSize = 50.sp)
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