package com.example.dogepasswordmanager

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dogepasswordmanager.ui.theme.BrickRed

//註冊頁面
class RegisterPage : ComponentActivity() {

    companion object {
        //帳號已存在
        var ALREADY_EXIST = 0

        //輸入格式錯誤(空白、不為正確格式)
        var FORMAT_ERROR = 1

        //帳號太短
        var USERNAME_TOO_SHORT = 2


        //正確
        var OK = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            registerPage(context = this@RegisterPage)
        }
    }
}


//註冊頁面
@Composable
fun registerPage(context: Context) {

    val activity = LocalContext.current as? Activity

    //使用者輸入帳號
    var userInputUsername = remember {
        mutableStateOf("")
    }

    //使用者輸入帳號是否有誤
    var usernameError = remember {
        mutableStateOf(false)
    }

    //帳號錯誤類型
    var usernameErroType = remember {
        mutableStateOf(RegisterPage.OK)
    }


    //使用者輸入密碼
    var userInputPassword = remember {
        mutableStateOf("")
    }

    //使用者輸入密碼是否有誤
    var passwordError = remember {
        mutableStateOf(false)
    }

    //密碼錯誤類型
    var passwordErrorType = remember {
        mutableStateOf(RegisterPage.OK)
    }

    //使用者輸入確認密碼
    var userInputConfirmedPassword = remember {
        mutableStateOf("")
    }

    //使用者輸入確認密碼是否有誤
    var confirmedPasswordError = remember {
        mutableStateOf(false)
    }

    //使用者輸入電子郵件
    var userInputEmail = remember {
        mutableStateOf("")
    }

    //使用者輸入電子郵件卻是否有誤
    var emailError = remember {
        mutableStateOf(false)
    }

    //是否可視密碼欄位
    var passwordVisible = remember {
        mutableStateOf(false)
    }
    // 是否可視確認密碼欄位
    var confirmedPasswordVisible = remember {
        mutableStateOf(false)
    }


    //頁面元件
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            //輸入框
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //帳號欄位
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    TextField(
                        value = userInputUsername.value,
                        onValueChange = {
                            userInputUsername.value = it
                        },
                        placeholder = {
                            Text(text = "帳號")
                        },
                        singleLine = true,
                        isError = usernameError.value,
                        supportingText = {
                            //沒有輸入帳號或帳號格式錯誤
                            if (usernameErroType.value == RegisterPage.FORMAT_ERROR) {
                                Text(
                                    text = "請確認帳號輸入",
                                    modifier = Modifier.fillMaxWidth(),
                                    color = BrickRed,
                                    fontSize = 15.sp
                                )

                            } else if (usernameErroType.value == RegisterPage.ALREADY_EXIST) {
                                //帳號已存在
                                Text(
                                    text = "帳號已存在",
                                    modifier = Modifier.fillMaxWidth(),
                                    color = BrickRed,
                                    fontSize = 15.sp
                                )
                            } else if (usernameErroType.value == RegisterPage.USERNAME_TOO_SHORT) {
                                //帳號太短
                                Text(
                                    text = "帳號至少要3個字元",
                                    modifier = Modifier.fillMaxWidth(),
                                    color = BrickRed,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    )
                }

                //密碼欄位
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp)
                        .width(IntrinsicSize.Max),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {


                    //密碼輸入欄位
                    TextField(
                        value = userInputPassword.value,
                        singleLine = true,
                        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        placeholder = { Text(text = "密碼") },
                        isError = passwordError.value,
                        supportingText = {
                            //錯誤訊息
                            if (passwordErrorType.value == RegisterPage.FORMAT_ERROR) {
                                Text(
                                    text = "密碼(>6)需包含大小寫英文、數字、特殊符號",
                                    modifier = Modifier
                                        .fillMaxWidth(),

                                    color = BrickRed,
                                    fontSize = 15.sp,
                                )
                            }

                        },
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

                //確認密碼欄位
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {


                    //確認密碼輸入欄位
                    TextField(
                        value = userInputConfirmedPassword.value,
                        singleLine = true,
                        visualTransformation = if (confirmedPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        placeholder = { Text(text = "確認密碼") },
                        isError = confirmedPasswordError.value,
                        supportingText = {
                            //錯誤訊息
                            if (confirmedPasswordError.value) {
                                Text(
                                    text = "請確認密碼輸入是否相同",
                                    modifier = Modifier.fillMaxWidth(),
                                    color = BrickRed,
                                    fontSize = 15.sp
                                )

                            }
                        },
                        trailingIcon = {
                            //可以看到密碼
                            if (confirmedPasswordVisible.value) {
                                //設定對應Icon
                                Icon(
                                    painter = painterResource(id = R.drawable.visible),
                                    contentDescription = "Visible Icon",
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable() {
                                            confirmedPasswordVisible.value =
                                                !confirmedPasswordVisible.value
                                        })

                            } else {

                                Icon(
                                    painter = painterResource(id = R.drawable.invisible),
                                    contentDescription = "Invisible Icon",
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable() {
                                            confirmedPasswordVisible.value =
                                                !confirmedPasswordVisible.value
                                        })
                            }


                        },
                        onValueChange = {
                            userInputConfirmedPassword.value = it
                        },
                    )
                }

                //電子郵件欄位
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    TextField(value = userInputEmail.value, onValueChange = {
                        userInputEmail.value = it
                    }, placeholder = {
                        Text(text = "電子郵件")
                    }, singleLine = true,
                        isError = emailError.value,
                        supportingText = {
                            //錯誤訊息
                            if (emailError.value) {
                                Text(
                                    text = "請確認是否為電子郵件格式",
                                    modifier = Modifier.fillMaxWidth(),
                                    color = BrickRed,
                                    fontSize = 15.sp
                                )

                            }
                        })

                }

            }
        }


        //註冊按鈕、返回登入按鈕
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //註冊按鈕
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            //按下註冊按鈕後的操作

                            //對所有輸入欄位進行檢查 以及 帳號存在性檢查

                            //檢查帳號
                            usernameErroType.value = checkUsername(userInputUsername.value)


                            if (usernameErroType.value != RegisterPage.OK) {
                                usernameError.value = true
                            } else {
                                usernameError.value = false
                            }


                            //檢查密碼
                            passwordErrorType.value = checkPassword(userInputPassword.value)

                            if (usernameErroType.value != RegisterPage.OK) {
                                passwordError.value = true
                            } else {
                                passwordError.value = false
                            }


                            //檢查確認密碼欄位(未輸入或者與輸入密碼不同)
                            if (userInputConfirmedPassword.value == userInputPassword.value && userInputConfirmedPassword.value.length != 0) {
                                confirmedPasswordError.value = false
                            } else {
                                confirmedPasswordError.value = true
                            }

                            //檢查電子郵件
                            emailError.value = checkEmail(userInputEmail.value)

                            //全部欄位都沒發生錯誤
                            if (!usernameError.value && !passwordError.value && !confirmedPasswordError.value && !emailError.value) {
                                //前往驗證碼驗證頁面
                            }

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        shape = RoundedCornerShape(30)
                    ) {
                        Text(text = "註冊", fontSize = 30.sp)
                    }
                }

                //返回登入
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "已經有帳號了?", fontSize = 30.sp)
                    Text(
                        text = "返回登入",
                        fontSize = 25.sp,
                        modifier = Modifier.clickable() {
                            //按下返回登入按鈕後的操作

                            //直接結束當前activity
                            activity?.finish()
                        },
                        color = Color.Gray
                    )
                }
            }
        }
    }

}


//檢查帳號
fun checkUsername(username: String = ""): Int {

    //要補一個帳號已經存在的檢查

    //空字串
    if (username.equals("")) {
        return RegisterPage.FORMAT_ERROR
    }

    //帳號太短
    if (username.length < 3) {
        return RegisterPage.USERNAME_TOO_SHORT
    }

    var regex = "([a-zA-Z0-9]){3,18}".toRegex()
    var result = regex.matches(username)

    //輸入正確
    if (result) {
        return RegisterPage.OK
    }
    //輸入格式有誤
    return RegisterPage.FORMAT_ERROR
}

//檢查電子郵件格式
fun checkEmail(email: String = ""): Boolean {
    return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


//檢查密碼
fun checkPassword(password: String): Int {
    //空字串
    if (password.equals(""))
        return RegisterPage.FORMAT_ERROR


    var regex =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~\$^+=<>]).{6,}\$".toRegex()

    var result = regex.matches(password)
    Log.d("MSG", result.toString())
    //正確
    if (result)
        return RegisterPage.OK

    //格式錯誤
    return RegisterPage.FORMAT_ERROR
}
