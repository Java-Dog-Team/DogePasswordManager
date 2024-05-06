package com.example.dogepasswordmanager

import android.annotation.SuppressLint
import android.app.Activity
import androidx.biometric.BiometricPrompt
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.coroutineScope

class MainActivity : FragmentActivity() {


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            loginPage(context = this@MainActivity)
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStart() {
        super.onStart()


        //抓取使用者登入狀態
        val currentUser = auth.currentUser


    }


}

private lateinit var auth: FirebaseAuth

//登入頁面
@RequiresApi(Build.VERSION_CODES.P)
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

    //使否要開啟生物驗證
    var biometricFlag = remember {
        mutableStateOf(true)
    }


    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {


        //登入頁面開頭
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.login_title),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

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
                        label = { Text(text = stringResource(id = R.string.login_email)) },
                        singleLine = true,
                        onValueChange = {
                            userInputUsername.value = it
                        },
                        isError = usernameError.value,
                        supportingText = {
                            if (usernameError.value)
                                Text(
                                    text = stringResource(id = R.string.login_email_error),
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
                        label = { Text(text = stringResource(id = R.string.login_password)) },
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
                                    text = stringResource(id = R.string.login_password_error),
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
                        Text(
                            text = stringResource(id = R.string.login_button_text),
                            fontSize = 20.sp
                        )
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
                            .weight(1.5f)
                            .padding(start = 5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.register_text),
                                fontSize = 25.sp
                            )
                            Text(
                                text = stringResource(id = R.string.register_button_text),
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

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
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
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.forget_password_button_text),
                                fontSize = 25.sp,
                                color = Color.Gray,
                                modifier = Modifier.clickable() {
                                    //點擊忘記密碼按鈕後的操作

                                    //啟動忘記密碼頁面
                                    var intent = Intent()
                                    intent.setClass(context, UpdatePasswordPage::class.java)
                                    context.startActivity(intent)
                                })

                        }
                    }
                }

            }
        }


    }
    if (biometricFlag.value) {
        biometricHandler(context = context as FragmentActivity)
        biometricFlag.value = false
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
                    Toast.makeText(
                        activity,
                        activity.getString(R.string.email_verification_error),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            } else {

                //登入帳密有誤或者帳號不存在
                Firebase.firestore.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { result ->
                        if (result.size() == 0) {
                            //使用者帳號不存在
                            Toast.makeText(
                                activity,
                                activity.getString(R.string.username_not_exist_error),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            //使用者帳號存在，但是帳號密碼錯誤
                            Toast.makeText(
                                activity,
                                activity.getString(R.string.username_password_input_error),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                    }


            }
        }


}

@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun biometricHandler(context: FragmentActivity) {


    //當前使用者
    val currentUser = auth.currentUser
    val db = Firebase.firestore




    if (currentUser != null) {
        //檢查生物驗證對象是否刪除帳號
        db.collection("users")
            .get().addOnSuccessListener { result ->
                for (document in result) {

                    if (document.data.get("email").toString() == currentUser.email.toString()) {

                        //該帳號存在於資料庫才啟動生物辨識
                        if (isBiometricAvailable(context)) {


                            getBiometricPrompt(context) {
                                var intent = Intent()
                                intent.setClass(context, MainPage::class.java)
                                context.startActivity(intent)

                                context.finish()
                            }.authenticate(getPromptInfo(context))

                        }
                        break
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }


}

//檢查電子郵件格式
private fun checkEmail(email: String = ""): Boolean {
    return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


// 檢查該裝置是否支援生物辨識
fun isBiometricAvailable(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
        BiometricManager.BIOMETRIC_SUCCESS -> true
        else -> {
            Log.e("TAG", "Biometric authentication not available")
            false
        }
    }
}


@RequiresApi(Build.VERSION_CODES.P)
private fun getBiometricPrompt(
    context: FragmentActivity,
    onAuthSucceed: (BiometricPrompt.AuthenticationResult) -> Unit
): BiometricPrompt {
    val biometricPrompt =
        BiometricPrompt(
            context,
            ContextCompat.getMainExecutor(context),
            object : BiometricPrompt.AuthenticationCallback() {
                // Handle successful authentication
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    Log.e("TAG", "Authentication Succeeded: ${result.cryptoObject}")
                    // 驗證成功後的動作
                    onAuthSucceed(result)
                }

                // Handle authentication errors
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Log.e("TAG", "onAuthenticationError")
                }

                // Handle authentication failures
                override fun onAuthenticationFailed() {
                    Log.e("TAG", "onAuthenticationFailed")
                }
            }
        )
    return biometricPrompt
}


//指紋辨識顯示畫面
private fun getPromptInfo(context: FragmentActivity): BiometricPrompt.PromptInfo {
    val currentUser = auth.currentUser

    return BiometricPrompt.PromptInfo.Builder()
        .setTitle(context.getString(R.string.biometric_login_title))
        .setDescription(
            context.getString(R.string.biometric_login_content_start) + currentUser!!.email + context.getString(
                R.string.biometric_login_content_end
            )
        )
        .setConfirmationRequired(false)
        .setNegativeButtonText(
            context.getString(R.string.biometric_login_cancel_button_text)
        )
        .build()
}

