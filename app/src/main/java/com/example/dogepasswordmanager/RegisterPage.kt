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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dogepasswordmanager.ui.theme.BrickRed
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//註冊頁面
class RegisterPage : ComponentActivity() {

    companion object {
        //帳號已存在
        var ALREADY_EXIST = 0

        //輸入格式錯誤(空白、不為正確格式)
        var FORMAT_ERROR = 1


        //正確
        var OK = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            registerPage(context = this@RegisterPage)
        }
    }


}

private lateinit var auth: FirebaseAuth

//註冊頁面
@Composable
fun registerPage(context: Context) {

    val activity = LocalContext.current as? Activity


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
                    }, label = {
                        Text(text = stringResource(id = R.string.register_email_field))
                    }, singleLine = true,
                        isError = emailError.value,
                        supportingText = {
                            //錯誤訊息
                            if (emailError.value) {
                                Text(
                                    text = stringResource(id = R.string.register_email_field_format_error),
                                    modifier = Modifier.fillMaxWidth(),
                                    color = BrickRed,
                                    fontSize = 15.sp
                                )

                            }
                        })

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
                        label = { Text(text = stringResource(id = R.string.register_password_field)) },
                        isError = passwordError.value,
                        supportingText = {
                            //錯誤訊息
                            if (passwordErrorType.value == RegisterPage.FORMAT_ERROR) {
                                Text(
                                    text = stringResource(id = R.string.register_password_field_format_error),

                                    color = BrickRed,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Left
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
                        label = { Text(text = stringResource(id = R.string.register_confirmed_password_field)) },
                        isError = confirmedPasswordError.value,
                        supportingText = {
                            //錯誤訊息
                            if (confirmedPasswordError.value) {
                                Text(
                                    text = stringResource(id = R.string.register_confirmed_password_field_format_error),
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


                            //檢查密碼
                            passwordErrorType.value = checkPassword(userInputPassword.value)

                            if (passwordErrorType.value != RegisterPage.OK) {
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
                            if (!passwordError.value && !confirmedPasswordError.value && !emailError.value) {
                                //寄送驗證信到該使用者信箱中
                                if (activity != null) {
                                    sendAuthenticationMail(
                                        activity = activity,
                                        userInputEmail.value,
                                        userInputPassword.value
                                    )
                                }
                            }

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        shape = RoundedCornerShape(30)
                    ) {
                        Text(text = stringResource(id = R.string.register_button), fontSize = 30.sp)
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
                    Text(
                        text = stringResource(id = R.string.register_already_has_account_text),
                        fontSize = 25.sp
                    )
                    Text(
                        text = stringResource(id = R.string.register_already_has_account_button_text),
                        fontSize = 22.sp,
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


//檢查電子郵件格式
private fun checkEmail(email: String = ""): Boolean {
    return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


//檢查密碼
fun checkPassword(password: String): Int {

    //空字串
    if (password.equals(""))
        return RegisterPage.FORMAT_ERROR

    val regex = Regex("[a-zA-Z0-9\\W_]{6,}")

    if (regex.matches(password)) {
        return RegisterPage.OK
    }
    //格式錯誤
    return RegisterPage.FORMAT_ERROR

}

private fun sendAuthenticationMail(activity: Activity, email: String, password: String) {


    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {


                //當前使用者資訊
                val user = auth.currentUser

                //寄送驗證信件
                user!!.sendEmailVerification()

                //將使用者帳號加入資料庫中
                val data = hashMapOf(
                    "email" to user.email.toString()
                )

                Firebase.firestore.collection("users")
                    .add(data)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }

                //跳轉至登入頁面
                activity.finish()

            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    activity,
                    activity.getString(R.string.register_user_already_exist),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
}