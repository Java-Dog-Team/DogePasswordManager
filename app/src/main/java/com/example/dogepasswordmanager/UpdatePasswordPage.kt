package com.example.dogepasswordmanager

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UpdatePasswordPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UpdatePasswordPage(this@UpdatePasswordPage)
        }
    }
}


//更新密碼頁面
@Composable
fun UpdatePasswordPage(context: Context) {
    var activity = LocalContext.current as Activity
    //使用者輸入信箱
    var userInputEmail by remember {
        mutableStateOf("")
    }

    //使用者是否輸入錯誤
    var userInputError by remember {
        mutableStateOf(false)
    }

    Row(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //  使用者輸入電子郵件頁面
            TextField(value = userInputEmail, onValueChange = {
                userInputEmail = it
            },
                label = {
                    Text(text = "電子郵件")
                },
                isError = userInputError,
                supportingText = {
                    if (userInputError) {
                        Text(text = "請確認輸入", color = MaterialTheme.colorScheme.error)
                    } else {
                        userInputError = false
                    }
                })

            // 發送重設密碼郵件按鈕
            Button(onClick = {
                //按下按鈕後的操作

                //檢查輸入
                if (userInputEmail.isEmpty() || userInputEmail.isBlank() || checkEmail(
                        userInputEmail
                    )
                ) {
                    userInputError = true

                } else {
                    //寄送密碼重設郵件
                    Firebase.auth.sendPasswordResetEmail(userInputEmail)
                        .addOnCompleteListener { task ->
                            //顯示寄送成功Toast
                            if (task.isSuccessful) {

                                Toast.makeText(
                                    context,
                                    "已寄送密碼重設信件至您的信箱",
                                    Toast.LENGTH_SHORT
                                ).show()
                                userInputError = false
                                activity.finish()
                            } else {
                                userInputError = true

                            }
                        }
                }


            }, modifier = Modifier.padding(top = 15.dp)) {
                Text(text = "發送密碼重設郵件")
            }
        }

    }
}

//檢查電子郵件格式
private fun checkEmail(email: String = ""): Boolean {
    return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}