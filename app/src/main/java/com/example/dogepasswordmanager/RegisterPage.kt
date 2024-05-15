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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.google.android.gms.common.internal.StringResourceValueReader
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
@OptIn(ExperimentalMaterial3Api::class)
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


    Scaffold(
        // 返回按鈕
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
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description",
                            tint=Color(235, 195, 18),
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
        //標題
        Row (
            modifier = Modifier.padding(innerPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = stringResource(id = R.string.register_page),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp),
                color = Color(237, 197, 49),
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
        }

        //頁面元件
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            //電子郵件欄位
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedTextField(value = userInputEmail.value, onValueChange = {
                    userInputEmail.value = it },
                    label = { Text(text = stringResource(id = R.string.register_email_field)) },
                    singleLine = true,
                    isError = emailError.value,
                    supportingText = {
                        //錯誤訊息
                        if (emailError.value) {
                            Text(
                                text = stringResource(id = R.string.register_email_field_format_error),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        else{
                            Text(text = " ")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color(0, 0, 0),
                        unfocusedBorderColor = Color(235, 195, 18),
                        unfocusedLabelColor = Color(235, 195, 18),
                        focusedTextColor = Color(0, 0, 0),
                        focusedBorderColor = Color(235, 195, 18),
                        focusedLabelColor = Color(235, 195, 18)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.width(275.dp)
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
                OutlinedTextField(
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
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        else{
                            Text(
                                text = stringResource(id = R.string.register_password_field_format_error),
                                color = Color.Gray,
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
                                    },
                                tint=Color(235, 195, 18)
                            )

                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.invisible),
                                contentDescription = "Invisible Icon",
                                modifier = Modifier
                                    .size(25.dp)
                                    .clickable() {
                                        passwordVisible.value = !passwordVisible.value
                                    },
                                tint=Color(235, 195, 18)
                            )
                        }
                    },
                    onValueChange = {
                        userInputPassword.value = it
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color(0, 0, 0),
                        unfocusedBorderColor = Color(235, 195, 18),
                        unfocusedLabelColor = Color(235, 195, 18),
                        focusedTextColor = Color(0, 0, 0),
                        focusedBorderColor = Color(235, 195, 18),
                        focusedLabelColor = Color(235, 195, 18)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.width(275.dp)
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
                OutlinedTextField(
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
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        else{
                            Text(
                                text = stringResource(id = R.string.register_confirmed_password_field_format_error),
                                color = Color(0,0,0,1)
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
                                    },
                                tint=Color(235, 195, 18)
                            )

                        } else {

                            Icon(
                                painter = painterResource(id = R.drawable.invisible),
                                contentDescription = "Invisible Icon",
                                modifier = Modifier
                                    .size(25.dp)
                                    .clickable() {
                                        confirmedPasswordVisible.value =
                                            !confirmedPasswordVisible.value
                                    },
                                tint=Color(235, 195, 18)
                            )
                        }
                    },
                    onValueChange = {
                        userInputConfirmedPassword.value = it
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color(0, 0, 0),
                        unfocusedBorderColor = Color(235, 195, 18),
                        unfocusedLabelColor = Color(235, 195, 18),
                        focusedTextColor = Color(0, 0, 0),
                        focusedBorderColor = Color(235, 195, 18),
                        focusedLabelColor = Color(235, 195, 18)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.width(275.dp)
                )
            }

            //註冊按鈕
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
                            .width(275.dp),
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
                                            userInputPassword.value,
                                            context
                                        )
                                    }
                                }

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(Color(235, 195, 18)),
                        ) {
                            Text(text = stringResource(id = R.string.register_button),
                                fontSize = 20.sp)
                        }
                    }
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

private fun sendAuthenticationMail(activity: Activity, email: String, password: String,context: Context) {
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

                //跳轉至提醒收郵件頁面
                activity.finish()
                var intent = Intent()
                intent.setClass(
                    context,
                    CheckEmailPage::class.java
                )
                context.startActivity(intent)

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