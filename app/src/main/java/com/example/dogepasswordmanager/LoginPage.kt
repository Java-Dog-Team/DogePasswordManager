package com.example.dogepasswordmanager

import android.app.Activity
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            loginPage(context = this@MainActivity)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
    }

}

private lateinit var auth: FirebaseAuth

//登入頁面
@Composable
fun loginPage(context: Context) {

    val activity = LocalContext.current as Activity

    //使用者帳號是否輸入有誤
    var usernameError = remember {
        mutableStateOf(false)
    }

    //使用者密碼是否輸入有誤
    var passwordError = remember {
        mutableStateOf(false)
    }


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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    //帳號輸入框
                    TextField(
                        value = userInputUsername.value,
                        label = { Text(text = "電子郵件") },
                        singleLine = true,
                        onValueChange = {
                            userInputUsername.value = it
                        },
                        isError = usernameError.value,
                        supportingText = {
                            if (usernameError.value)
                                Text(
                                    text = "請檢查電子郵件輸入",
                                    color = MaterialTheme.colorScheme.error
                                )
                        }
                    )
                }

                //密碼輸入
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {


                    //密碼輸入欄位
                    TextField(
                        value = userInputPassword.value,
                        singleLine = true,
                        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        label = { Text(text = "密碼") },
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
                        isError = passwordError.value,
                        supportingText = {
                            if (passwordError.value)
                                Text(
                                    text = "請檢查密碼輸入",
                                    color = MaterialTheme.colorScheme.error
                                )
                        }
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


                            //檢查帳密是否有誤 有誤:顯示錯誤文字
                            if (userInputPassword.value == "") {
                                passwordError.value = true
                            } else {
                                passwordError.value = false
                            }
                            if (userInputUsername.value == "") {
                                usernameError.value = true
                            } else {
                                usernameError.value = false
                            }

                            //檢查輸入無誤: 檢查帳號密碼是否正確以及電子郵件是否驗證
                            if (userInputUsername.value != "" && userInputPassword.value != "") {
                                passwordError.value = false
                                usernameError.value = false
                                LogIn(activity, userInputUsername.value, userInputPassword.value)
                            }


                        }) {
                        //登入按鈕文字
                        Text(text = "登入", fontSize = 20.sp)
                    }
                }


                //註冊、修改密碼
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

private fun LogIn(activity: Activity, email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            //登入成功後 檢查電子郵件是否驗證
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithEmail:success " + email)
                val user = auth.currentUser
                //使用者已驗證過電子郵件
                if (user!!.isEmailVerified) {
                    //導向主頁面

                    var intent = Intent()
                    intent.setClass(
                        activity,
                        MainPage::class.java
                    )
                    //開啟主頁面
                    activity.startActivity(intent)
                    //關閉登入頁面
                    activity.finish()
                } else {

                    //信箱尚未驗證
                    Toast.makeText(activity, "信箱尚未驗證", Toast.LENGTH_SHORT)
                        .show()
                }

            } else {

                //登入帳密有誤
                Toast.makeText(activity, "輸入帳密有誤", Toast.LENGTH_SHORT)
                    .show()
            }
        }


}