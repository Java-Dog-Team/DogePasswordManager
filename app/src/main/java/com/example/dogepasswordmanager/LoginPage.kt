package com.example.dogepasswordmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            loginPage(context = this@MainActivity)
        }
    }
}


//登入頁面
@Composable
fun loginPage(context: Context) {

    //使用者輸入帳號
    var userInputUsername = remember {
        mutableStateOf("")
    }


    //使用者輸入密碼
    var userInputPassword = remember {
        mutableStateOf("")

    }

    //密碼是否可視
    var passwordVisible = remember {
        mutableStateOf(false)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {


        //此App名稱
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "狗狗密碼管理器", fontSize = 40.sp, fontWeight = FontWeight.Bold)
        }


        //帳密輸入欄位、登入按鈕以及註冊 忘記密碼按鈕
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(6f)
                .padding(bottom = 100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //帳號輸入
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    //顯示帳號文字
                    Text(text = "帳號", fontSize = 30.sp)
                    //帳號輸入框
                    TextField(
                        value = userInputUsername.value,
                        placeholder = { Text(text = "請輸入帳號") },
                        singleLine = true,
                        onValueChange = {
                            userInputUsername.value = it
                        })
                }

                //密碼輸入
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    //顯示密碼文字
                    Text(text = "密碼", fontSize = 30.sp)

                    //密碼輸入欄位
                    TextField(
                        value = userInputPassword.value,
                        singleLine = true,
                        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        placeholder = { Text(text = "請輸入密碼") },
                        trailingIcon = {
                            //可以看到密碼
                            if (passwordVisible.value) {
                                //設定對應Icon
                                Icon(
                                    painter = painterResource(id = R.drawable.visible),
                                    contentDescription = "Visible Icon",
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable() {
                                            passwordVisible.value = !passwordVisible.value
                                        })

                            } else {

                                Icon(
                                    painter = painterResource(id = R.drawable.invisible),
                                    contentDescription = "Invisible Icon",
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable() {
                                            passwordVisible.value = !passwordVisible.value
                                        })
                            }


                        },
                        onValueChange = {
                            userInputPassword.value = it
                        },
                    )
                }

                //登入按鈕
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                ) {
                    //登入按鈕元件
                    Button(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                        onClick = {
                            //按下登入按鈕後的操作
                        }) {
                        //登入按鈕文字
                        Text(text = "登入", fontSize = 20.sp)
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "沒有帳號嗎? ", fontSize = 25.sp)
                            Text(
                                text = "註冊",
                                fontSize = 20.sp,
                                color = Color.Gray,
                                modifier = Modifier.clickable() {
                                    //點擊註冊按鈕後的操作


                                    //挑轉至註冊頁面
                                    var intent = Intent()
                                    intent.setClass(
                                        context,
                                        RegisterPage::class.java
                                    )
                                    context.startActivity(intent)
                                })

                        }

                    }

                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "忘記密碼?",
                                fontSize = 25.sp,
                                color = Color.Gray,
                                modifier = Modifier.clickable() {
                                    //點擊忘記密碼按鈕後的操作
                                })

                        }
                    }
                }


            }
        }


    }

}

