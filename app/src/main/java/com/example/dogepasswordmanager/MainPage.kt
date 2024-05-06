package com.example.dogepasswordmanager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.compose.rememberAsyncImagePainter
import com.example.dogepasswordmanager.ui.theme.BackGroundColor
import com.example.dogepasswordmanager.ui.theme.BrickRed
import com.example.dogepasswordmanager.ui.theme.ItemColor
import com.example.dogepasswordmanager.ui.theme.digitsColor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.storage
import java.security.SecureRandom


class MainPage : ComponentActivity() {

    companion object {
        val PASSWORD_ROOM = 0
        val PASSWORD_GEN = 1
        val SETTING = 2
        val APP_NAME = "appName"
        val APP_USERNAME = "appUsername"
        val APP_PASSWORD = "appPassword"
        val DATA_ID = "dataId"
        var APP_IMG_ID = "appImgId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)



        auth = Firebase.auth

        userMail = auth.currentUser?.email.toString()



        setContent {

            mainPage(this@MainPage)
        }
    }

    override fun onStart() {
        super.onStart()
        //重新初始化
        storage = Firebase.storage
        root = storage.reference
        imageRef = root.child("images")

        //載入使用者紀錄，載入完畢後就關閉載入動畫
        loadUserData()

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

//選項按鈕對應的物件
private var appData: AppData? = null

private lateinit var auth: FirebaseAuth
private lateinit var userMail: String

//使用者紀錄資訊
var lists = mutableStateListOf<AppData?>()

//firebase 儲存空間物件
private var storage = com.google.firebase.Firebase.storage

//儲存空間根目錄參考
private var root = storage.reference

//根目錄->images資料夾
private var imageRef = root.child("images")


@SuppressLint("UnrememberedMutableState")
@Composable
fun mainPage(context: Context) {

    //顯示畫面內容 default:密碼庫頁面
    var showingPage = remember { mutableStateOf(MainPage.PASSWORD_ROOM) }

    var dialogShowingFlag = remember { mutableStateOf(false) }

    var topLeftButtonVisibleFlag = remember {
        mutableStateOf(true)
    }

    var showingTitle = remember { mutableStateOf(MainPage.PASSWORD_ROOM) }




    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        //Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(BackGroundColor)
                .weight(0.7f)

        ) {

            //頁面名稱
            Column {
                //當前為密碼庫頁面
                if (showingTitle.value == MainPage.PASSWORD_ROOM)
                    Text(
                        text = stringResource(id = R.string.button_option1),
                        fontSize = 30.sp,
                        color = Color.White,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                else if (showingTitle.value == MainPage.PASSWORD_GEN)
                    Text(
                        text = stringResource(id = R.string.button_option2),
                        fontSize = 30.sp,
                        color = Color.White,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                else if (showingTitle.value == MainPage.SETTING)
                    Text(
                        text = stringResource(id = R.string.button_option3),
                        fontSize = 30.sp,
                        color = Color.White,
                        modifier = Modifier.padding(start = 15.dp)
                    )
            }
            //功能圖示
            Column(
                modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End
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


            if (showingPage.value == MainPage.PASSWORD_ROOM) {


                //密碼庫頁面
                passwordRoom(
                    context,
                    dialogShowingFlag = dialogShowingFlag
                )


            } else if (showingPage.value == MainPage.PASSWORD_GEN) {
                //密碼產生器頁面
                passwordGeneratorPage(context)
            } else if (showingPage.value == MainPage.SETTING) {
                //設定畫面
                settingPage(context)
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
                        showingTitle.value = MainPage.PASSWORD_ROOM
                        //顯示左上角搜尋、過濾按鈕
                        topLeftButtonVisibleFlag.value = true;
                        //切換顯示密碼庫頁面
                        showingPage.value = MainPage.PASSWORD_ROOM
                    }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.padlock),
                        contentDescription = "Padlock Icon",
                        modifier = Modifier.size(40.dp, 40.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                    Text(
                        text = stringResource(id = R.string.button_option1),
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 5.dp)
                    )
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
                        showingTitle.value = MainPage.PASSWORD_GEN
                        //隱藏左上角搜尋、過濾按鈕
                        topLeftButtonVisibleFlag.value = false;
                        //切換顯示密碼產生器頁面
                        showingPage.value = MainPage.PASSWORD_GEN
                    }) {
                Image(
                    painter = painterResource(id = R.drawable.sync),
                    contentDescription = "Generate Password Icon",
                    modifier = Modifier.size(40.dp, 40.dp),
                    colorFilter = ColorFilter.tint(Color.Gray)
                )
                Text(
                    text = stringResource(id = R.string.button_option2),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 5.dp)
                )
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
                        showingTitle.value = MainPage.SETTING
                        //隱藏左上角搜尋、過濾按鈕
                        topLeftButtonVisibleFlag.value = false;
                        //切換顯示設定頁面
                        showingPage.value = MainPage.SETTING
                    }) {
                Image(
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = "Setting Icon",
                    modifier = Modifier.size(40.dp, 40.dp),
                    colorFilter = ColorFilter.tint(Color.Gray)
                )
                Text(
                    text = stringResource(id = R.string.button_option3),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
        }


    }


    //顯示對話窗
    dialogWindow(context, dialogShowingFlag)
}

//密碼庫頁面
@Composable
fun passwordRoom(
    context: Context,
    dialogShowingFlag: MutableState<Boolean>
) {

    Box {

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            items(lists) { item ->

                AppDataBlock(
                    item!!,
                    dialogShowingFlag,
                    context
                )


            }
        }

        //新增記錄按鈕
        FloatingActionButton(containerColor = ItemColor,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            onClick = {

                //按下新增按鈕後的操作
                var intent = Intent()
                intent.setClass(context, AddRecordActivity::class.java)
                intent.putExtra("email", userMail)
                context.startActivity(intent)
            }) {

            Icon(Icons.Filled.Add, "Floating Action Button")

        }

    }

}

//設定頁面
@Composable
fun settingPage(context: Context){
    val albumlist = ArrayList<Int>()

    val language= (R.string.language)
    val theme=(R.string.theme)
    val account=(R.string.account)
    val version=(R.string.version)
    val document=(R.string.teach)
    val deleteAccount=(R.string.deleteAccount)

    albumlist.add(language)
    albumlist.add(theme)
    albumlist.add(account)
    albumlist.add(version)
    albumlist.add(document)
    albumlist.add(deleteAccount)

    LazyColumn (){
        items(albumlist){
            item->
            Surface (color = Color(255,255,255),
                modifier = Modifier
                    .clickable{
                        itemClick(item,context)
                    }
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .height(85.dp)
                    ){
                Row(modifier = Modifier
                    .fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxSize()
                    ){
                        var col=Color.Black
                        if(item==R.string.deleteAccount){
                            col=Color.Red
                        }
                        else{
                            col=Color.Black
                        }
                        Text(text = stringResource(item),
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(20.dp),
                            fontSize = 30.sp,
                            color=col)

                    }
                }
            }
            Divider(
                color = Color.LightGray, thickness = 1.dp
            )
        }
    }
}
fun itemClick(clickItem:Int,context: Context){
    var activity:Activity = context as Activity
    //    點選刪除帳號
    if(clickItem == R.string.deleteAccount){
        //    取得目前登入的用戶
        val user = Firebase.auth.currentUser!!

//      讀取存放資料的 database
        val db=Firebase.firestore
//      刪除使用者的所有資料
        db.collection(user.email.toString())
            .get()
            .addOnSuccessListener {
                result->
                for(document in result){
                    db.collection(user.email.toString()).document(document.id)
                        .delete()
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                        .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                }
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

//      刪除 user 內的使用者的 email
        db.collection("users")
            .whereEqualTo("email", userMail)
            .get()
            .addOnSuccessListener {
                docs->
                for(doc in docs){
                    db.collection("users").document(doc.id)
                        .delete()
                }
            }
            .addOnFailureListener { }


//      刪除使用者帳號
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }

//      切換到 login activity
        var intent=Intent()
        intent.setClass(context,MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
        activity.finish()
    }
}

//密碼產生器頁面
@Composable
fun passwordGeneratorPage(context: Context) {

    //剪貼簿管理員物件
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    //產生的密碼
    var gen_password = remember { mutableStateOf(generateSecurePassword()) }

    //滑條選定的值
    var sliderPosition = remember {
        mutableFloatStateOf(8f)
    }

    //密碼產生含大寫字母
    var upperCaseLetter = remember {
        mutableStateOf(true)
    }
    //密碼產生含小寫字母
    var lowerCaseLetter = remember {
        mutableStateOf(true)
    }

    //密碼產生含數字
    var digits = remember {
        mutableStateOf(true)
    }

    //密碼產生含特殊符號
    var specialChar = remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .width(IntrinsicSize.Max)
            .verticalScroll(rememberScrollState())
    ) {
        //顯示產生的密碼
        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, Color.LightGray, RectangleShape)
                .padding(top = 15.dp, bottom = 10.dp)

        ) {

            //顯示產生的密碼
            showGeneratePassword(gen_password = gen_password.value)
        }

        //密碼欄位右下方複製、產生按鈕
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.End
        ) {

            Column(modifier = Modifier.padding(end = 15.dp)) {
                //複製按鈕
                Image(
                    painter = painterResource(id = R.drawable.copy),
                    contentDescription = "Copy Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable() {
                            //按下複製按鈕後的操作

                            //複製帳號到剪貼簿
                            clipboardManager.setText(AnnotatedString(gen_password.value))
                            Toast
                                .makeText(
                                    context,
                                    context.getString(R.string.copy_success),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        },
                    colorFilter = ColorFilter.tint(ItemColor)
                )
            }

            Column(modifier = Modifier.padding(end = 15.dp)) {
                //重新產生按鈕
                Image(
                    painter = painterResource(id = R.drawable.sync),
                    contentDescription = "Generate Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable() {

                            //按下重新產生按鈕後的操作

                            //生成新的密碼
                            gen_password.value = generateSecurePassword(
                                sliderPosition.value.toInt(),
                                upperCaseLetter.value,
                                lowerCaseLetter.value,
                                digits.value,
                                specialChar.value
                            )

                        },
                    colorFilter = ColorFilter.tint(ItemColor)

                )
            }
        }


        //產生密碼長度選項
        Row(modifier = Modifier.fillMaxWidth()) {

            //長度選項
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 50.dp, top = 30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = stringResource(id = R.string.gen_length),
                        fontSize = 30.sp,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    //顯示滑條當前數值
                    Text(
                        text = sliderPosition.value.toInt().toString(),
                        fontSize = 20.sp,
                        color = Color.Gray
                    )
                    //滑條
                    Slider(
                        value = sliderPosition.value, onValueChange = {
                            //使用者調整欲生成的密碼長度過程，產生新的密碼並顯示在頁面上
                            sliderPosition.value = it
                            //生成新的密碼
                            gen_password.value = generateSecurePassword(
                                sliderPosition.value.toInt(),
                                upperCaseLetter.value,
                                lowerCaseLetter.value,
                                digits.value,
                                specialChar.value
                            )
                        }, valueRange = 8f..128f, colors = SliderDefaults.colors(
                            thumbColor = Color.LightGray, activeTrackColor = ItemColor
                        )
                    )
                }
            }

        }


        //產生密碼符號選項
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
        ) {

            //密碼符號選項
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                //大寫字母
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //A-Z文字
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "A-Z", fontSize = 25.sp)
                    }

                    //啟用大寫字母
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Switch(checked = upperCaseLetter.value, colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = ItemColor,
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color.DarkGray,
                        ), onCheckedChange = {

                            upperCaseLetter.value = it

                            //避免所有選項都關閉(至少強制打開小寫字母)
                            if (!upperCaseLetter.value && !lowerCaseLetter.value && !digits.value && !specialChar.value) {
                                lowerCaseLetter.value = true
                            }

                            //生成新的密碼
                            gen_password.value = generateSecurePassword(
                                sliderPosition.value.toInt(),
                                upperCaseLetter.value,
                                lowerCaseLetter.value,
                                digits.value,
                                specialChar.value
                            )
                        }, thumbContent = {
                            //選擇時，在thumb上出現打勾圖案
                            if (upperCaseLetter.value) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            } else {
                                null
                            }

                        })
                    }

                }

                //小寫字母
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //a-z文字
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "a-z", fontSize = 25.sp)
                    }

                    //啟用小寫字母
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Switch(checked = lowerCaseLetter.value, colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = ItemColor,
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color.DarkGray,
                        ), onCheckedChange = {

                            lowerCaseLetter.value = it

                            //避免所有選項都關閉(至少強制打開小寫字母)
                            if (!upperCaseLetter.value && !lowerCaseLetter.value && !digits.value && !specialChar.value) {
                                lowerCaseLetter.value = true
                            }

                            //生成新的密碼
                            gen_password.value = generateSecurePassword(
                                sliderPosition.value.toInt(),
                                upperCaseLetter.value,
                                lowerCaseLetter.value,
                                digits.value,
                                specialChar.value
                            )
                        }, thumbContent = {
                            //選擇時，在thumb上出現打勾圖案
                            if (lowerCaseLetter.value) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            } else {
                                null
                            }

                        })
                    }

                }

                //數字符號
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    //0-9文字
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "0-9", fontSize = 25.sp)

                    }

                    //啟用數字
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Switch(checked = digits.value, colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = ItemColor,
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color.DarkGray,
                        ), onCheckedChange = {

                            digits.value = it

                            //避免所有選項都關閉(至少強制打開小寫字母)
                            if (!upperCaseLetter.value && !lowerCaseLetter.value && !digits.value && !specialChar.value) {
                                lowerCaseLetter.value = true
                            }

                            //生成新的密碼
                            gen_password.value = generateSecurePassword(
                                sliderPosition.value.toInt(),
                                upperCaseLetter.value,
                                lowerCaseLetter.value,
                                digits.value,
                                specialChar.value
                            )
                        }, thumbContent = {
                            //選擇時，在thumb上出現打勾圖案
                            if (digits.value) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            } else {
                                null
                            }

                        })
                    }

                }

                //特殊符號
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {


                    //特殊符號文字
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "!@#\$%^&*", fontSize = 25.sp)

                    }

                    //啟用特殊符號
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Switch(checked = specialChar.value, colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = ItemColor,
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color.DarkGray,
                        ), onCheckedChange = {

                            specialChar.value = it

                            //避免所有選項都關閉(至少強制打開小寫字母)
                            if (!upperCaseLetter.value && !lowerCaseLetter.value && !digits.value && !specialChar.value) {
                                lowerCaseLetter.value = true
                            }

                            //生成新的密碼
                            gen_password.value = generateSecurePassword(
                                sliderPosition.value.toInt(),
                                upperCaseLetter.value,
                                lowerCaseLetter.value,
                                digits.value,
                                specialChar.value
                            )
                        }, thumbContent = {
                            //選擇時，在thumb上出現打勾圖案
                            if (specialChar.value) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            } else {
                                null
                            }

                        })
                    }

                }
            }
        }

    }

}

//應用程式紀錄區塊
@Composable
fun AppDataBlock(
    userAppData: AppData,
    dialogShowingState: MutableState<Boolean>,
    context: Context
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 7.dp)
            .clip(RoundedCornerShape(50.dp))
            .height(70.dp)
            .clickable() {
                appData = userAppData
                //按下項目條後進入檢視畫面
                //跳出編輯視窗
                var newIntent = Intent()
                newIntent.setClass(context, ViewActivity::class.java)
                newIntent.putExtra(
                    MainPage.DATA_ID,
                    appData!!.DataId
                )
                newIntent.putExtra(
                    MainPage.APP_IMG_ID,
                    appData!!.AppImgId
                )
                newIntent.putExtra(
                    MainPage.APP_NAME,
                    appData!!.AppName
                )
                newIntent.putExtra(
                    MainPage.APP_PASSWORD,
                    appData!!.AppPassword
                )
                newIntent.putExtra(
                    MainPage.APP_USERNAME,
                    appData!!.AppUsername
                )


                context.startActivity(newIntent)
                //關閉此頁面
            }

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
            //此應用程式圖示為使用者自訂
            if (userAppData.AppImgId != "") {

                var img = remember {
                    mutableStateOf<Uri?>(null)
                }

                imageRef.child(userMail + "/" + userAppData.AppImgId + ".jpg").downloadUrl.addOnCompleteListener { result ->
                    img.value = result.result
                }

                //App Icon
                Image(
                    painter = rememberAsyncImagePainter(model = img.value),
                    contentDescription = "App Icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape)
                )
            } else {
                //App Icon預設圖片
                Image(
                    painter = painterResource(id = R.drawable.default_icon),
                    contentDescription = "App Icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape)
                )
            }


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
                    .fillMaxSize(), verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = userAppData.AppName,
                    fontSize = 26.sp,
                    fontFamily = FontFamily.Serif,
                    overflow = TextOverflow.Ellipsis
                )
            }

            //使用者App帳號
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = userAppData.AppUsername,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color.DarkGray,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        //選項按鈕
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = 20.dp)
        ) {

            //選項按鈕Icon
            Image(painter = painterResource(id = R.drawable.option),
                contentDescription = "Option Icon",
                colorFilter = ColorFilter.tint(Color.LightGray),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .clickable {
                        //跳出對話框
                        dialogShowingState.value = true

                        appData = userAppData

                    })
        }
    }


}

//按下選項按鈕後出現的對話窗
@Composable
fun dialogWindow(context: Context, dialogShowingState: MutableState<Boolean>) {

    //剪貼簿管理員
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
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .weight(0.5f)
                    ) {
                        Divider(
                            color = Color.LightGray, thickness = 1.dp
                        )


                    }

                    //檢視帳號資訊
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clickable() {
                                //按下檢視帳號資訊按鈕後的動作

                                //顯示檢視帳號資訊頁面
                                var intent = Intent()
                                intent.setClass(context, AddRecordActivity::class.java)
                                intent.putExtra(MainPage.DATA_ID, appData!!.DataId)
                                intent.putExtra(MainPage.APP_IMG_ID, appData!!.AppImgId)
                                intent.putExtra(MainPage.APP_NAME, appData!!.AppName)
                                intent.putExtra(MainPage.APP_PASSWORD, appData!!.AppPassword)
                                intent.putExtra(MainPage.APP_USERNAME, appData!!.AppUsername)
                                context.startActivity(intent)

                                dialogShowingState.value = false
                            }) {


                        Text(
                            text = stringResource(id = R.string.edit_item),
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 7.dp)
                        )

                    }

                    //複製帳號
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clickable() {
                                //複製帳號到剪貼簿
                                clipboardManager.setText(AnnotatedString(appData!!.AppUsername))
                                //顯示複製成功訊息
                                Toast
                                    .makeText(
                                        context,
                                        context.getString(R.string.copy_success),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                //關閉對話框
                                dialogShowingState.value = false
                            }) {

                        Text(
                            text = stringResource(id = R.string.copy_item_username),
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 7.dp)
                        )


                    }
                    //複製密碼
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                            .clickable() {
                                //複製密碼到剪貼簿
                                clipboardManager.setText(AnnotatedString(appData!!.AppPassword))
                                //顯示複製成功訊息
                                Toast
                                    .makeText(
                                        context,
                                        context.getString(R.string.copy_success),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                //關閉對話框
                                dialogShowingState.value = false
                            }) {

                        Text(
                            text = stringResource(id = R.string.copy_item_password),
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 7.dp)
                        )


                    }
                }
            }

        }
    }

}


//產生器產生的密碼，最規格化並顯示
@Composable
fun showGeneratePassword(gen_password: String) {
    //字母正規表達式
    var letterRegex = """[a-z A-Z]""".toRegex()
    //數字正規表達式
    var digitsRegex = """[0-9]""".toRegex()

    Text(
        buildAnnotatedString {
            for (i in 0..gen_password.length - 1) {
                //字元滿足數字格式
                if (digitsRegex.matches(gen_password[i].toString())) {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 25.sp,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.Serif,
                            color = digitsColor
                        )
                    ) {
                        append(gen_password[i])
                    }
                } else if (letterRegex.matches(gen_password[i].toString())) {
                    //滿足字母格式
                    withStyle(
                        style = SpanStyle(
                            fontSize = 25.sp,
                            letterSpacing = 2.sp,
                            fontFamily = FontFamily.Serif,
                            color = Color.Black
                        )
                    ) {
                        append(gen_password[i])
                    }
                } else {
                    //滿足特殊符號格式
                    withStyle(
                        style = SpanStyle(
                            fontSize = 25.sp,
                            letterSpacing = 2.sp,
                            fontFamily = FontFamily.Serif,
                            color = BrickRed
                        )
                    ) {
                        append(gen_password[i])
                    }
                }
            }
        },
        textAlign = TextAlign.Justify,
        maxLines = 15,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
    )
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
                    modifier = Modifier.size(35.dp, 35.dp),
                    colorFilter = ColorFilter.tint(Color.White)


                )
            }
        }
    }
}


//生成密碼
fun generateSecurePassword(
    length: Int = 8,
    upperCase: Boolean = true,
    lowerCase: Boolean = true,
    digits: Boolean = true,
    specialChar: Boolean = true
): String {

    var charset = arrayListOf<Char>()


    var upperLetterLst = ('A'..'Z').toList()
    var lowerLetterLst = ('a'..'z').toList()
    var digitLst = ('0'..'9').toList()

    //!@#$%^&*
    var specialCharLst = arrayListOf<Char>(
        '!', '@', '#', '$', '%', '^', '&', '*'
    )

    //用來避免沒有產生到某些使用者要求的符號
    var hasUpperCase = true
    var hasLowerCase = true
    var hasDigit = true
    var hasSpecialChar = true


    //依照使用者需求生成密碼
    if (upperCase) {
        for (ch in 'A'..'Z') {
            charset.add(ch)
        }

        hasUpperCase = false
    }

    if (lowerCase) {
        for (ch in 'a'..'z') {
            charset.add(ch)
        }
        hasLowerCase = false
    }

    if (digits) {
        for (ch in '0'..'9') {
            charset.add(ch)
        }
        hasDigit = false
    }

    if (specialChar) {
        charset.add('!')
        charset.add('@')
        charset.add('#')
        charset.add('$')
        charset.add('%')
        charset.add('^')
        charset.add('&')
        charset.add('*')
        hasSpecialChar = false
    }


    val secureRandom = SecureRandom()
    var result = ""
    for (i in 1..length) {
        if (!hasUpperCase) {
            result = result.plus(upperLetterLst[secureRandom.nextInt(upperLetterLst.size)])
            hasUpperCase = true
        } else if (!hasLowerCase) {
            result = result.plus(lowerLetterLst[secureRandom.nextInt(lowerLetterLst.size)])
            hasLowerCase = true
        } else if (!hasDigit) {
            result = result.plus(digitLst[secureRandom.nextInt(digitLst.size)])
            hasDigit = true
        } else if (!hasSpecialChar) {
            result = result.plus(specialCharLst[secureRandom.nextInt(specialCharLst.size)])
            hasSpecialChar = true
        } else {
            result = result.plus(charset[secureRandom.nextInt(charset.size)])
        }

    }

    Log.d("MSG", result)

    return result
}


fun loadUserData() {

    lists.clear()

    val db = Firebase.firestore
    var docRef = db.collection(userMail)

    docRef.get()
        .addOnSuccessListener { document ->
            for (data in document) {
                val detailData = data.getData()
                lists!!.add(
                    AppData(
                        DataId = detailData.get("dataId").toString(),
                        userEmail = detailData.get("userEmail").toString(),
                        AppImgId = detailData.get("appImgId").toString(),
                        AppName = detailData.get("appName").toString(),
                        AppUsername = detailData.get("appUsername").toString(),
                        AppPassword = detailData.get("appPassword").toString()
                    )
                )
            }
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents: ", exception)
        }


}