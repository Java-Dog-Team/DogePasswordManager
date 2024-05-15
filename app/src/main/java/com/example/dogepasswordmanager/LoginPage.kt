package com.example.dogepasswordmanager

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.KeyguardManager
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.icu.text.ListFormatter.Width
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Secure
import android.provider.Settings.SettingNotFoundException
import android.text.Html
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.checkerframework.common.subtyping.qual.Bottom


class MainActivity : FragmentActivity() {

    companion object {
        //生物辨識偏好設定檔案名稱
        val BIOMETRIC_AVAILABLE = "biometric_available"


        //生物辨識內容存取key
        val BIOMETRIC_AVAILABLE_KEY = "0"


    }

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
@OptIn(ExperimentalMaterial3Api::class)
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

    var showDialog by remember { mutableStateOf(false) }




    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 35.dp)) {
        //登入頁面開頭

        Image(painter = painterResource(R.drawable.doogemanager),
            contentDescription = stringResource(id = R.string.login_title),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )

        //帳密輸入欄位、登入按鈕以及註冊 忘記密碼按鈕
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
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
                    OutlinedTextField(
                        value = userInputUsername.value,
                        onValueChange =  {
                            userInputUsername.value = it
                        },
                        label = { Text(text = stringResource(id = R.string.login_email)) },
                        singleLine = true,
                        isError = usernameError.value,
                        supportingText = {
                            if (usernameError.value) {
                                Text(
                                    text = stringResource(id = R.string.login_email_error),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            else{
                                Text(
                                    text = stringResource(id = R.string.login_email_error),
                                    color = Color(0,0,0,1)
                                )
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

                //密碼輸入 && 忘記密碼
                Column (modifier = Modifier.width(275.dp)){
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceEvenly
//                    ) {
                        //密碼輸入欄位
                        OutlinedTextField(
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
                            isError = passwordError.value,
                            supportingText = {
                                if (passwordError.value) {
                                    Text(
                                        text = stringResource(id = R.string.login_password_error),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                                else{
                                    Text(
                                        text = stringResource(id = R.string.login_password_error),
                                        color = Color(0,0,0,1)
                                    )
                                }
                            } ,

                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedTextColor = Color(0, 0, 0),
                                unfocusedBorderColor = Color(235, 195, 18),
                                unfocusedLabelColor = Color(235, 195, 18),
                                focusedTextColor = Color(0, 0, 0),
                                focusedBorderColor = Color(235, 195, 18),
                                focusedLabelColor = Color(235, 195, 18)
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .width(275.dp)
                                .padding(bottom = 0.dp)
                        )

                    //忘記密碼按鈕
                    Text(
                        text = stringResource(R.string.forget_password_button_text),
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .clickable() {
                                //點擊忘記密碼按鈕後的操作

                                //啟動忘記密碼頁面
                                //  設定彈跳視窗
                                showDialog = true

                            }
                            .align(alignment = Alignment.End)
                    )
                }
                CustomDialog(
                    showDialog = showDialog,
                    onDismissRequest = { showDialog = false },
                    title_num = R.string.forget_title,
                    artical_num = R.string.forget_artical
                ){

                }


                //登入按鈕
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    //登入按鈕元件
                    Button(modifier = Modifier
                        .width(275.dp)
                        .padding(top = 20.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(Color(235, 195, 18)),
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


                //註冊
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 30.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.register_text),
                        fontSize = 25.sp,
                        modifier = Modifier.align(alignment = Alignment.Bottom)
                    )
                    Text(
                        text = stringResource(id = R.string.register_button_text),
                        fontSize = 25.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .clickable() {
                                //點擊註冊按鈕後的操作

                                //挑轉至註冊頁面
                                var intent = Intent()
                                intent.setClass(
                                    context,
                                    RegisterPage::class.java
                                )
                                context.startActivity(intent)

                            }
                            .align(alignment = Alignment.Bottom)
                    )

                }
            }
        }


    }
    //快速登入處理
    FastLoginHandler(context)


}

//快速登入(生物辨識、PIN、圖形碼)
@Composable
@RequiresApi(Build.VERSION_CODES.P)
private fun FastLoginHandler(context: Context) {
    //是否啟動生物辨識畫面
    var biometricFlag by remember {
        mutableStateOf(true)
    }


    val currentUser = auth.currentUser
    if (currentUser != null) {

        //獲取生物辨識偏好設定內容(預設為不開啟)
        val bioPref: Boolean? = context.getSharedPreferences(
            currentUser.email.toString() +"_"+ MainActivity.BIOMETRIC_AVAILABLE,
            MODE_PRIVATE
        ).getBoolean(MainActivity.BIOMETRIC_AVAILABLE_KEY, false)


        //快速登入優先度: 生物辨識->圖形碼->PIN碼
        //使用者剛打開app且手機支援生物辨識且使用者有開啟生物辨識選項(在設定頁面需先判斷使用者是否能使用生物辨識...等)
        if (biometricFlag && isBiometricAvailable(context) && bioPref == true) {
            biometricHandler(context = context as FragmentActivity)
            biometricFlag = false
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
                    activity.startActivity(intent, ActivityOptions.makeCustomAnimation(activity,
                        androidx.appcompat.R.anim.abc_slide_in_bottom, androidx.appcompat.R.anim.abc_popup_exit).toBundle())
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


    //檢查生物驗證對象是否刪除帳號
    db.collection("users")
        .get().addOnSuccessListener { result ->
            for (document in result) {

                if (document.data.get("email").toString() == currentUser!!.email.toString()) {

                    //該帳號存在於資料庫才啟動生物辨識


                    getBiometricPrompt(context) {
                        //驗證成功後的動作
                        var intent = Intent()
                        intent.setClass(context, MainPage::class.java)
                        context.startActivity(intent,ActivityOptions.makeCustomAnimation(context as Activity,
                            androidx.appcompat.R.anim.abc_slide_in_bottom, androidx.appcompat.R.anim.abc_popup_exit).toBundle())

                        context.finish()
                    }.authenticate(getPromptInfo(context))


                    break
                }

            }
        }
        .addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents.", exception)
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    title_num:Int,
    artical_num:Int,
    content: @Composable () -> Unit,
) {
    //使用者輸入信箱
    var userInputEmail by remember {
        mutableStateOf("")
    }
    userInputEmail = ""
    //使用者是否輸入錯誤
    var userInputError by remember {
        mutableStateOf(false)
    }
    userInputError = false

    //dialog 的 title
    var title by remember {
        mutableStateOf(title_num)
    }
    title = title_num
    //dialog 的內文部分
    var artical by remember {
        mutableStateOf(artical_num)
    }
    artical = artical_num
    var title_top_padding by remember {
        mutableStateOf(0)
    }
    title_top_padding = 0
    var artical_buttom_padding by remember {
        mutableStateOf(20)
    }
    artical_buttom_padding = 20
    var read by remember {
        mutableStateOf(false)
    }
    read = false
    var aph by remember {
        mutableStateOf(1f)
    }
    aph = 1f

    if (showDialog) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = true,
                dismissOnBackPress = true
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .pointerInput(Unit) { detectTapGestures { } }
                        .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                        .width(330.dp)
                        .height(370.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            MaterialTheme.colorScheme.surface,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Scaffold(
                        // 叉叉按鈕
                        // 按下後直接顯示登入頁面
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
                                    IconButton(onClick = onDismissRequest) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Localized description",
                                            tint= Color(235, 195, 18),
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
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxWidth()
                        ) {
                            // 添加標題
                            Text(
                                text = stringResource(title),
                                color = Color(237, 197, 49),
                                fontSize = 30.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = title_top_padding.dp)
                            )
                            Text(
                                text = stringResource(artical),
                                color = Color.Gray,
                                fontSize = 17.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 20.dp,
                                        bottom = artical_buttom_padding.dp
                                    ),
                                textAlign = TextAlign.Center
                            )
                            //  使用者輸入電子郵件
                            OutlinedTextField(value = userInputEmail,
                                onValueChange = {
                                    userInputEmail = it
                                },
                                label = {
                                    Text(text = stringResource(R.string.forget_email_field))
                                },
                                isError = userInputError,
                                supportingText = {
                                    if (userInputError) {
                                        Text(text = stringResource(R.string.forget_email_field_error), color = MaterialTheme.colorScheme.error)
                                    } else {
                                        userInputError = false
                                        Text(text = "")
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
                                singleLine = true,
                                modifier = Modifier
                                    .width(250.dp)
                                    .alpha(aph),
                                readOnly = read
                            )

                            // 發送重設密碼郵件按鈕
                            Button(
                                onClick = {
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
                                                    title = R.string.forget_email_success
                                                    artical = R.string.forget_email_success_artical
                                                    title_top_padding = 70
                                                    artical_buttom_padding = 150
                                                    read = true
                                                    aph=0f
                                                } else {
                                                    Log.d("HIIIIIIII","msg")
                                                    userInputError = true
                                                }
                                            }
                                    }
                                },
                                modifier = Modifier
                                    .padding(top = 15.dp)
                                    .alpha(aph),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(Color(235, 195, 18)),
                            ) {
                                Text(text = stringResource(id=R.string.forget_send_reset_password_email_button))
                            }
                        }
                    }
                }

            }
        }
    }
}
