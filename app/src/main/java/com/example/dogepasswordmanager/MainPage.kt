package com.example.dogepasswordmanager

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.dogepasswordmanager.ui.theme.BrickRed
import com.example.dogepasswordmanager.ui.theme.ChooseItemColor
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
        val SEARCH_RESULT = 3
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

    }

    override fun onStart() {
        super.onStart()
        //重新初始化
        storage = Firebase.storage
        root = storage.reference
        imageRef = root.child("images")


        setContent {
            var loaderFlag = remember {
                mutableStateOf(true)
            }
            //載入使用者紀錄，載入完畢後就關閉載入動畫
            loadUserData(loaderFlag)
            if (loaderFlag.value) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = ItemColor
                    )
                }

            } else
                mainPage(this@MainPage, false)
        }
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
var customImgCnt = mutableStateOf(0)


//使用者輸入搜尋文字
private var userSearchInput = mutableStateOf("")


//firebase 儲存空間物件
private var storage = com.google.firebase.Firebase.storage

//儲存空間根目錄參考
private var root = storage.reference

//根目錄->images資料夾
private var imageRef = root.child("images")


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun mainPage(context: Context, refresh:Boolean) {

    //顯示畫面內容 default:密碼庫頁面
    var showingPage = remember { mutableStateOf(MainPage.PASSWORD_ROOM) }

    var dialogShowingFlag = remember { mutableStateOf(false) }

    var topLeftButtonVisibleFlag = remember {
        mutableStateOf(true)
    }

    var showingTitle = remember { mutableStateOf(MainPage.PASSWORD_ROOM) }


    //是否要顯示載入動畫(在圖片尚未載入完成時顯示)
    var showLoader = remember {
        mutableStateOf(false)
    }
    val radiogroup:RadioGroup

    //使用者客製化圖片的紀錄數量大於0 需要花時間載入圖片
    if (customImgCnt.value > 0)
        showLoader.value = true

    val focusManager = LocalFocusManager.current
    Scaffold(
        floatingActionButton = {
            if (showingPage.value == MainPage.PASSWORD_ROOM) {
                //新增記錄按鈕
                FloatingActionButton(containerColor = ItemColor,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        //清除所有focus狀態
                        focusManager.clearFocus()
                        //清楚使用者輸入
                        userSearchInput.value = ""
                        //按下新增按鈕後的操作
                        var intent = Intent()
                        intent.setClass(context, AddRecordActivity::class.java)
                        intent.putExtra("email", userMail)
                        context.startActivity(
                            intent,
                            ActivityOptions.makeCustomAnimation(
                                context as Activity,
                                androidx.appcompat.R.anim.abc_slide_in_bottom,
                                androidx.appcompat.R.anim.abc_popup_exit
                            ).toBundle()
                        )


                    }) {

                    Icon(Icons.Filled.Add, "Floating Action Button")

                }
            }

        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomAppBar(
                containerColor = ItemColor,
                actions = {
                    //密碼庫按鈕
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
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
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                            Text(
                                text = stringResource(id = R.string.button_option1),
                                color = Color.White,
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
                            .clickable() {
                                //當前頁面名稱
                                showingTitle.value = MainPage.PASSWORD_GEN
                                //隱藏左上角搜尋、過濾按鈕
                                topLeftButtonVisibleFlag.value = false;
                                //切換顯示密碼產生器頁面
                                showingPage.value = MainPage.PASSWORD_GEN
                                //切換顯示密碼產生器頁面
                                showingPage.value = MainPage.PASSWORD_GEN
                            }) {
                        Image(
                            painter = painterResource(id = R.drawable.sync),
                            contentDescription = "Generate Password Icon",
                            modifier = Modifier.size(40.dp, 40.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text(
                            text = stringResource(id = R.string.button_option2),
                            color = Color.White,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }

                    //設定按鈕
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
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
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text(
                            text = stringResource(id = R.string.button_option3),
                            color = Color.White,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }


                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            //Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ItemColor)
                    .padding(bottom = 15.dp, top = 15.dp)
            ) {

                //頁面名稱
                Column(modifier = Modifier.padding(end = 20.dp)) {
                    //當前為密碼庫頁面
                    if (showingTitle.value == MainPage.PASSWORD_ROOM)
                        Text(
                            text = stringResource(id = R.string.button_option1),
                            fontSize = 30.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(start = 15.dp)
                        )
                    else if (showingTitle.value == MainPage.PASSWORD_GEN)
                        Text(
                            text = stringResource(id = R.string.button_option2),
                            fontSize = 30.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(start = 15.dp)
                        )
                    else if (showingTitle.value == MainPage.SETTING)
                        Text(
                            text = stringResource(id = R.string.button_option3),
                            fontSize = 30.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(start = 15.dp)
                        )
                }
                //功能圖示
                Column(
                    modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End
                ) {

                    topLeftFunctionButton(
                        isVisible = topLeftButtonVisibleFlag.value,
                        showingPage = showingPage,
                        context = context,
                        dialogShowingState = dialogShowingFlag,
                        showLoader = showLoader,
                    )

                }


            }


            //Body(存放密碼紀錄區塊)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(7f)
                    .background(Color.White)
            ) {


                if (showingPage.value == MainPage.PASSWORD_ROOM) {


                    //密碼庫頁面
                    passwordRoom(
                        context,
                        dialogShowingFlag = dialogShowingFlag,
                        showLoader,
                        showingPage
                    )


                } else if (showingPage.value == MainPage.PASSWORD_GEN) {
                    //密碼產生器頁面
                    passwordGeneratorPage(context)

                } else if (showingPage.value == MainPage.SETTING) {

                    //設定畫面
                    settingPage(context)

                } else if (showingPage.value == MainPage.SEARCH_RESULT) {

                    showSearchResult(userSearchInput.value, dialogShowingFlag, context, showLoader)

                }


            }





        }
    }

    //顯示對話窗
    dialogWindow(context, dialogShowingFlag, showLoader)
    if(refresh){


        //按下按鈕後處理
        showingTitle.value = MainPage.SETTING
        //隱藏左上角搜尋、過濾按鈕
        topLeftButtonVisibleFlag.value = false;
        //切換顯示設定頁面
        showingPage.value = MainPage.SETTING
        //設定畫面
        settingPage(context)
    }
}

//密碼庫頁面
@Composable
fun passwordRoom(
    context: Context,
    dialogShowingFlag: MutableState<Boolean>,
    showLoader: MutableState<Boolean>,
    showingPage: MutableState<Int>
) {

    //每一個紀錄項目的載入狀態
    var flagList = remember {
        mutableStateListOf<MutableState<Boolean>>()
    }
    flagList.clear()

    Box {


        //lazyColumn的modifier 會配合載入動畫做消失和出現
        var lazyColModifier = Modifier
            .fillMaxSize()
            .alpha(1f)

        if (showLoader.value) {
            lazyColModifier = Modifier
                .fillMaxSize()
                .alpha(0f)
        } else {
            lazyColModifier = Modifier
                .fillMaxSize()
                .alpha(1f)
        }
        val focusManager = LocalFocusManager.current

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = lazyColModifier
            ) {

                items(lists) { item ->
                    //每項紀錄的圖片是否載入完畢(預設為預設圖片，不需要載入動畫)
                    var flag = remember { mutableStateOf(true) }
                    if (item!!.AppImgId != "") {
                        //使用者使用客製化圖片，需要載入動畫
                        flag.value = false
                        flagList.add(flag)
                    }

                    AppDataBlock(
                        item!!,
                        dialogShowingFlag,
                        context,
                        flag,
                        showLoader,
                        flagList
                    )


                }
            }
            if (showLoader.value) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = ItemColor
                    )
                }

            }

        }
    }
}

//設定頁面
@Composable
fun settingPage(context: Context) {
    var showDialog by remember { mutableStateOf(false) }
    var acc by remember { mutableStateOf(false) }
    var delete_account by remember { mutableStateOf(false) }
    var show_delete_account by remember { mutableStateOf(false) }
    var show_logout_account by remember { mutableStateOf(false) }
    val albumlist = ArrayList<Int>()

    val language = (R.string.language)
    val theme = (R.string.theme)
    val account = (R.string.account)
    val logout = (R.string.logout)
    val deleteAccount = (R.string.deleteAccount)
    val biometricAuthentication = R.string.biometricAuthentication
    albumlist.add(theme)
    albumlist.add(account)
    albumlist.add(biometricAuthentication)
    albumlist.add(logout)
    albumlist.add(deleteAccount)

    //生物辨識開關狀態
    var checked by remember { mutableStateOf(false) }
    //使用者偏好設定:生物辨識開關狀態
    var bioPref by remember {
        mutableStateOf(
            context.getSharedPreferences(
                auth.currentUser!!.email.toString() + "_" + MainActivity.BIOMETRIC_AVAILABLE,
                MODE_PRIVATE
            ).getBoolean(MainActivity.BIOMETRIC_AVAILABLE_KEY, false)
        )
    }

    //根據使用者偏好設定設定開關初始值
    if (bioPref == true)
        checked = true

    val biometricPref: SharedPreferences = context.getSharedPreferences(
        auth.currentUser!!.email.toString() + "_" + MainActivity.BIOMETRIC_AVAILABLE,
        MODE_PRIVATE
    )
    val editor: SharedPreferences.Editor = biometricPref.edit()

    var chooseItem=0
    LazyColumn() {
        items(albumlist) { item ->
            Surface(color = Color(255, 255, 255),
                modifier = Modifier
                    .clickable {
                        if (item == theme)
                            showDialog = true
                        else if (item == account)
                            acc = true
                        else if(item == deleteAccount)
                            show_delete_account = true
                        else if(item == logout)
                            show_logout_account = true
                    }
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .height(85.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    //若為生物辨識
                    if (item == R.string.biometricAuthentication) {

                        //生物辨識啟動開關
                        Row(modifier = Modifier.fillMaxWidth()) {
                            var col = Color.Black
                            Text(
                                text = stringResource(item),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(23.dp),
                                fontSize = 25.sp,
                                color = col
                            )
                            Switch(
                                checked = checked,
                                onCheckedChange = {
                                    checked = it
                                    bioPref = it
                                    //修改使用者偏好設定
                                    editor.putBoolean(MainActivity.BIOMETRIC_AVAILABLE_KEY, checked)
                                    editor.commit()
                                },
                                modifier = Modifier.padding(23.dp),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = ItemColor,
                                    uncheckedThumbColor = Color.LightGray,
                                    uncheckedTrackColor = Color.DarkGray,
                                )
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            var col = Color.Black
                            // 若為刪除帳號或登出帳號
                            if (item == R.string.deleteAccount) {
                                col = Color.Red
                            } else {
                                col = Color.Black
                            }
                            Text(
                                text = stringResource(item),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(20.dp),
                                fontSize = 30.sp,
                                color = col
                            )

                        }
                    }

                }
            }
            Divider(
                color = Color.LightGray, thickness = 1.dp
            )
        }
    }
    CustomDialog(
        showDialog = showDialog,
        onDismissRequest = {
            showDialog = false

            val activity = context as Activity
            activity.recreate()},
        context = context
    ) {

    }
    val activity = context as Activity
    accountInformation(
        acc = acc,
        activity = activity,
        onDismissRequest = {acc = false },
        context = context){

    }
    deleteAccountCheck(
        show_delete_account = show_delete_account,
        delete_account = delete_account,
        onDismissRequest = {show_delete_account = false},
        callDeleteAccount = {
            show_delete_account = false
            deleteAccount(context)},
        context = context
    ){}
    logoutAccountCheck(
        show_logout_account = show_logout_account,
        onDismissRequest = {show_logout_account = false},
        calllogoutAccount = {
            show_logout_account = false
            val intent = Intent()
            intent.setClass(
                context,
                MainActivity::class.java
            )
            context.startActivity(intent)
            activity.finish()
        },
        context = context
    ){}

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    context: Context,
    content: @Composable () -> Unit
) {
    //儲存偏好設定(主題部分)
    val pref: SharedPreferences = context.getSharedPreferences(
        "THEME",
        MODE_PRIVATE
    )
    val editor: SharedPreferences.Editor = pref.edit()
    val theme:List<String> = listOf(stringResource(id = R.string.choosetheme1),
        stringResource(id = R.string.choosetheme2))
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(pref.getString("theme","Original") ) }

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
                                    containerColor = Color(235, 195, 18, 0),
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
                                            tint = ItemColor,
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
                                text = stringResource(R.string.theme),
                                color = ItemColor,
                                fontSize = 30.sp,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = stringResource(R.string.choosetheme_artical),
                                color = Color.Gray,
                                fontSize = 17.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 20.dp
                                    ),
                                textAlign = TextAlign.Center
                            )
                            theme.forEach { text ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .selectable(
                                            selected = (text == selectedOption),
                                            onClick = {
                                                onOptionSelected(text)
                                                editor.putString("theme", text)
                                                editor.commit()
                                                for (i in 0..1) {
                                                    if (pref.getString(
                                                            "theme",
                                                            "Original"
                                                        ) == theme[i]
                                                    ) {
                                                        ItemColor = ChooseItemColor[i]
                                                        break
                                                    }
                                                }
                                            }
                                        )
                                        .padding(horizontal = 16.dp)
                                ) {
                                    RadioButton(selected = (text == selectedOption),
                                        onClick = {
                                            onOptionSelected(text)
                                            editor.putString("theme", text)
                                            editor.commit()
                                            val s: String =
                                                pref.getString("theme", "Original").toString()
                                            Log.d("Theme be choose:", s)
                                            for (i in 0..1) {
                                                if (s == theme[i]) {
                                                    ItemColor = ChooseItemColor[i]
                                                    break
                                                }
                                            }

                                        },
                                        colors = RadioButtonDefaults.colors(
                                            ItemColor
                                        )
                                    )
                                    Text(
                                        text = text,
                                        modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        mainPage(context = context, refresh = true)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun accountInformation(
    acc:Boolean,
    activity: Activity,
    onDismissRequest: () -> Unit,
    context: Context,
    content: @Composable () -> Unit) {
    //剪貼簿管理員物件
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var showDialog by remember { mutableStateOf(false) }
    var accc by remember { mutableStateOf(true) }
    if(accc && acc){
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
                        .height(350.dp)
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
                                    containerColor = Color(235, 195, 18, 0),
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
                                            tint = ItemColor,
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
                                text = stringResource(R.string.account),
                                color = ItemColor,
                                fontSize = 30.sp,
                                textAlign = TextAlign.Center,
                            )

                            //使用者名稱(帳號)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 5.dp, end = 5.dp, top = 50.dp, bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                //資訊顯示
                                Column() {

                                    Text(
                                        text = stringResource(id = R.string.view_app_username),
                                        color = Color.Gray,
                                        fontSize = 15.sp
                                    )

                                    Text(text = auth.currentUser?.email.toString(), fontSize = 20.sp)
                                }
                                //複製按鈕
                                Column(
                                    modifier = Modifier
                                        .weight(1f),
                                    verticalArrangement = Arrangement.Bottom,
                                    horizontalAlignment = Alignment.End
                                ) {

                                    //複製按鈕icon
                                    Icon(
                                        painter = painterResource(id = R.drawable.copy),
                                        contentDescription = "Copy Icon",
                                        modifier = Modifier
                                            .clickable() {
                                                //複製帳號到剪貼簿

                                                clipboardManager.setText(
                                                    AnnotatedString(
                                                        auth.currentUser?.email.toString()
                                                    )
                                                )

                                                Toast
                                                    .makeText(
                                                        context,
                                                        context.getString(R.string.copy_success),
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            }
                                            .size(30.dp)
                                            .padding(bottom = 5.dp),
                                        tint = ItemColor
                                    )

                                }
                            }
                            //重設密碼
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 5.dp, end = 5.dp, top = 50.dp, bottom = 10.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.UpdatePassword),
                                        color = ItemColor,
                                        fontSize = 20.sp,
                                        modifier = Modifier.clickable {
                                            val userInputEmail = auth.currentUser?.email.toString()
                                            var userInputError = false
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
                                                        //寄送成功
                                                        if (task.isSuccessful) {
                                                            showDialog = true
                                                        } else {
                                                            Log.d("HIIIIIIII", "msg")
                                                            userInputError = true
                                                        }
                                                    }
                                            }

                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

        resetPassword(
            showDialog = showDialog,
            onDismissRequest = {
                showDialog = false
                accc = false
            },
            title_num = R.string.forget_email_success,
            artical_num = R.string.forget_email_success_artical
        ) {

        }
}

//檢查電子郵件格式
private fun checkEmail(email: String = ""): Boolean {
    return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun resetPassword(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    title_num:Int,
    artical_num:Int,
    content: @Composable () -> Unit,
) {

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
                        .height(350.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            MaterialTheme.colorScheme.surface,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Scaffold(
                        // 叉叉按鈕
                        // 按下後直接顯示原本頁面
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
                                            tint= ItemColor,
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
                                text = stringResource(title_num),
                                color = ItemColor,
                                fontSize = 30.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 70.dp)
                            )
                            Text(
                                text = stringResource(artical_num),
                                color = Color.Gray,
                                fontSize = 17.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 20.dp,
                                        bottom = 100.dp
                                    ),
                                textAlign = TextAlign.Center
                            )


                        }
                    }
                }

            }
        }
    }
}
@Composable
fun logoutAccountCheck(
    show_logout_account:Boolean,
    onDismissRequest: () -> Unit,
    calllogoutAccount: () -> Unit,
    context: Context,
    content: @Composable () -> Unit
){
    //點選登出帳號按鈕
    if (show_logout_account) {
        AlertDialog(
            title = {
                Text(
                    text = stringResource(id = R.string.logoutTitle),
                    fontSize = 25.sp
                )
            },
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = calllogoutAccount
                ) {
                    Text(stringResource(id = R.string.logoutNeutralButton),
                        color = BrickRed)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(stringResource(id = R.string.logoutPositiveButton),
                        color = Color.Gray)
                }
            },
            containerColor = Color(255, 255, 255)
        )
    }
}
@Composable
fun deleteAccountCheck(
    show_delete_account:Boolean,
    delete_account:Boolean,
    onDismissRequest: () -> Unit,
    callDeleteAccount: () -> Unit,
    context: Context,
    content: @Composable () -> Unit
){
    //點選刪除帳號按鈕
    if (show_delete_account) {
        AlertDialog(
            title = {
                Text(
                    text = stringResource(id = R.string.deleteTitle),
                    fontSize = 25.sp
                )
            },
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = callDeleteAccount
                ) {
                    Text(stringResource(id = R.string.deleteNeutralButton),
                        color = BrickRed)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(stringResource(id = R.string.deletePositiveButton),
                        color = Color.Gray)
                }
            },
            containerColor = Color(255, 255, 255)
        )
    }
}
fun deleteAccount(context: Context) {
    var activity: Activity = context as Activity
    //    取得目前登入的用戶
    val user = Firebase.auth.currentUser!!

//      讀取存放資料的 database
    val db = Firebase.firestore



    //      刪除使用者的所有資料
    db.collection(user.email.toString())
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                //刪除使用者紀錄
                db.collection(user.email.toString()).document(document.id)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(
                            TAG,
                            "DocumentSnapshot successfully deleted!"
                        )
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
            }

        }
        .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    //刪除使用者帳號後再刪除資料庫帳號紀錄
    //      刪除 user 內的使用者的 email
    db.collection("users")
        .whereEqualTo("email", userMail)
        .get()
        .addOnSuccessListener { docs ->

            for (doc in docs) {

                db.collection("users").document(doc.id)
                    .delete()

            }

            //刪除後再刪除整個帳號
            //      刪除使用者帳號
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User account deleted.")
                        //登出帳號
                        FirebaseAuth.getInstance().signOut()
                        //      切換到 login activity
                        var intent = Intent()
                        intent.setClass(context, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        context.startActivity(
                            intent, ActivityOptions.makeCustomAnimation(
                                context as Activity,
                                androidx.appcompat.R.anim.abc_slide_in_bottom,
                                androidx.appcompat.R.anim.abc_popup_exit
                            ).toBundle()
                        )
                        activity.finish()
                    }
                }
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
                    colorFilter = ColorFilter.tint(ItemColor),
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
    context: Context,
    loaderFlag: MutableState<Boolean>,
    showLoader: MutableState<Boolean>,
    flagList: MutableList<MutableState<Boolean>>
) {

    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp, bottom = 3.dp)
            .clip(RoundedCornerShape(50.dp))
            .height(90.dp)
            .clickable() {
                //清除使用者輸入
                userSearchInput.value = ""
                focusManager.clearFocus()

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

                flagList.clear()

                context.startActivity(
                    newIntent,
                    ActivityOptions
                        .makeCustomAnimation(
                            context as Activity,
                            androidx.appcompat.R.anim.abc_slide_in_bottom,
                            androidx.appcompat.R.anim.abc_popup_exit
                        )
                        .toBundle()
                )
            }

    ) {
        //App圖示
        Column(
            modifier = Modifier
                .weight(2f)
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

                AsyncImage(
                    model = img.value,
                    contentDescription = "App Icon",

                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape),
                    contentScale = ContentScale.Crop,
                    onSuccess = {
                        Log.d("MSG", "SUCESSS")
                        //圖片以載入完畢，不須顯示載入動畫
                        loaderFlag.value = true
                        //若所有項目圖片都載入完畢
                        val result = flagList.all {
                            it.value
                        }
                        Log.d("MSG", "result: " + result.toString())
                        Log.d("MSG", "Length: " + flagList.size.toString())
                        if (result == true) {
                            //關閉loader動畫
                            showLoader.value = false
                        }
                    },

                    )

            } else {
                //App Icon預設圖片
                Image(
                    painter = painterResource(id = R.drawable.doge),
                    contentDescription = "App Icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape)
                )
            }


        }
        //App資訊顯示區塊
        Column(
            modifier = Modifier
                .weight(6f)
                .padding(top = 10.dp, start = 3.dp)
                .fillMaxSize()

        ) {
            //App名稱
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 3.dp)
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
                    .padding(end = 3.dp)
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

    Divider(
        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
    )

}

//按下選項按鈕後出現的對話窗
@Composable
fun dialogWindow(
    context: Context,
    dialogShowingState: MutableState<Boolean>,
    showLoader: MutableState<Boolean>
) {

    //剪貼簿管理員
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val focusManager = LocalFocusManager.current

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

                                showLoader.value = false
                                focusManager.clearFocus()
                                //清除使用者搜尋輸入
                                userSearchInput.value = ""
                                //顯示檢視帳號資訊頁面
                                var intent = Intent()
                                intent.setClass(context, AddRecordActivity::class.java)
                                intent.putExtra(MainPage.DATA_ID, appData!!.DataId)
                                intent.putExtra(MainPage.APP_IMG_ID, appData!!.AppImgId)
                                intent.putExtra(MainPage.APP_NAME, appData!!.AppName)
                                intent.putExtra(MainPage.APP_PASSWORD, appData!!.AppPassword)
                                intent.putExtra(MainPage.APP_USERNAME, appData!!.AppUsername)
                                context.startActivity(
                                    intent,
                                    ActivityOptions
                                        .makeCustomAnimation(
                                            context as Activity,
                                            androidx.appcompat.R.anim.abc_slide_in_bottom,
                                            androidx.appcompat.R.anim.abc_popup_exit
                                        )
                                        .toBundle()
                                )

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
fun topLeftFunctionButton(
    isVisible: Boolean,
    showingPage: MutableState<Int>,
    dialogShowingState: MutableState<Boolean>,
    context: Context,
    showLoader: MutableState<Boolean>
) {
    if (isVisible) {


        Row {
            Column(modifier = Modifier
                .padding(start = 5.dp, end = 10.dp)
                .clickable() {
                    //搜尋按鈕按下後的操作
                }) {


                //搜尋欄
                SearchBar(
                    hint = stringResource(id = R.string.searchbar_hint),
                    showingPage = showingPage
                ) {
                    //若使用者有輸入內容
                    if (it.isNotEmpty()) {
                        //顯示對應的搜尋結果
                        showingPage.value = MainPage.SEARCH_RESULT

                        userSearchInput.value = it
                    } else {

                        //使用者尚未輸入內容
                        showingPage.value = MainPage.PASSWORD_ROOM


                    }
                }


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


    return result
}


fun loadUserData(loaderFlag: MutableState<Boolean>) {

    lists.clear()
    customImgCnt.value = 0
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
                if (detailData.get("appImgId").toString() != "")
                    customImgCnt.value += 1
            }
            loaderFlag.value = false
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents: ", exception)
        }


}


@Composable
fun SearchBar(
    hint: String,
    modifier: Modifier = Modifier,
    isEnabled: (Boolean) = true,
    showingPage: MutableState<Int>,
    height: Dp = 40.dp,
    elevation: Dp = 20.dp,
    cornerShape: RoundedCornerShape = RoundedCornerShape(20.dp),
    backgroundColor: Color = Color.White,
    onSearchClicked: () -> Unit = {},
    onTextChange: (String) -> Unit = {},
) {
    //使用者輸入文字
    var text by remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .height(height)
            .padding(start = 5.dp, end = 5.dp)
            .fillMaxWidth()
            .shadow(elevation = elevation, shape = cornerShape)
            .background(color = backgroundColor, shape = cornerShape)
            .clickable { onSearchClicked() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            modifier = modifier
                .weight(5f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .onFocusChanged {

                    text = TextFieldValue("")
                }
                .focusRequester(focusRequester),
            value = text,

            onValueChange = {
                text = it
                onTextChange(it.text)
            },
            enabled = isEnabled,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            decorationBox = {
                //搜尋欄內hint顯示
                    innerTextField ->
                if (text.text.isEmpty()) {
                    Text(
                        text = hint,
                        color = Color.Gray.copy(alpha = 0.5f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )

                }
                innerTextField()
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
            singleLine = true
        )
        Box(
            modifier = modifier
                .weight(1f)
                .size(50.dp)
                .background(color = Color.Transparent, shape = CircleShape)
                .clickable {
                    //按下搜尋欄位按鈕
                    //若使用者有輸入，則clear掉輸入內容
                    if (text.text.isNotEmpty()) {
                        text = TextFieldValue(text = "")
                        onTextChange("")
                    }
                },
        ) {
            if (text.text.isNotEmpty()) {
                Icon(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear Icon",
                    tint = Color.Black,
                )
            } else {
                Icon(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Black,
                )
            }
        }
    }
}


//顯示使用者搜尋結果顯示
@Composable
fun showSearchResult(
    searchInput: String,
    dialogShowingState: MutableState<Boolean>,
    context: Context,
    showLoader: MutableState<Boolean>
) {

    //每一個紀錄項目的載入狀態
    var searchflagList = remember {
        mutableStateListOf<MutableState<Boolean>>()
    }
    searchflagList.clear()

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(lists) { item ->


            //顯示對應結果
            if (item!!.AppName.lowercase()
                    .contains(searchInput.lowercase())
            ) {
                //每項紀錄的圖片是否載入完畢(預設為預設圖片，不需要載入動畫)
                var flag = remember { mutableStateOf(true) }
                if (item!!.AppImgId != "") {
                    //使用者使用客製化圖片，需要載入動畫
                    flag.value = false
                    searchflagList.add(flag)

                }

                //顯示對應結果
                searchResultItem(
                    item,
                    dialogShowingState,
                    context,
                    flag,
                    showLoader,
                    searchflagList
                )
            }
        }
    }

}


//使用者搜尋結果項目物件
@Composable
fun searchResultItem(
    userAppData: AppData,
    dialogShowingFlag: MutableState<Boolean>,
    context: Context,
    loaderFlag: MutableState<Boolean>,
    showLoader: MutableState<Boolean>,
    searchFlagList: MutableList<MutableState<Boolean>>
) {

    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp, bottom = 3.dp)
            .clip(RoundedCornerShape(50.dp))
            .height(90.dp)
            .clickable() {

                userSearchInput.value = ""
                focusManager.clearFocus()
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

                searchFlagList.clear()

                context.startActivity(
                    newIntent,
                    ActivityOptions
                        .makeCustomAnimation(
                            context as Activity,
                            androidx.appcompat.R.anim.abc_slide_in_bottom,
                            androidx.appcompat.R.anim.abc_popup_exit
                        )
                        .toBundle()
                )
                //關閉此頁面
            }

    ) {
        //App圖示
        Column(
            modifier = Modifier
                .weight(2f)
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

                AsyncImage(
                    model = img.value,
                    contentDescription = "App Icon",

                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape),
                    contentScale = ContentScale.Crop,
                    onSuccess = {
                        Log.d("MSG", "SUCESSS")
                        //圖片以載入完畢，不須顯示載入動畫
                        loaderFlag.value = true
                        //若所有項目圖片都載入完畢
                        val result = searchFlagList.all {
                            it.value
                        }
                        Log.d("MSG", "result: " + result.toString())
                        Log.d("MSG", "Length: " + searchFlagList.size.toString())
                        if (result == true) {
                            //關閉loader動畫
                            showLoader.value = false
                        }
                    },

                    )

            } else {
                //App Icon預設圖片
                Image(
                    painter = painterResource(id = R.drawable.doge),
                    contentDescription = "App Icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape)
                )
            }


        }
        //App資訊顯示區塊
        Column(
            modifier = Modifier
                .weight(6f)
                .padding(top = 10.dp, start = 3.dp)
                .fillMaxSize()
        ) {
            //App名稱
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 3.dp)
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
                    .padding(end = 3.dp)
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
                        dialogShowingFlag.value = true

                        appData = userAppData

                    })
        }
    }
    Divider(
        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
    )

}