package com.example.dogepasswordmanager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.compose.AsyncImage
import com.example.dogepasswordmanager.ui.theme.BackGroundColor
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage

class AddRecordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        auth = Firebase.auth


        userEmail = auth.currentUser?.email.toString()
        db = Firebase.firestore
        storage = Firebase.storage
        intentObj = intent
        root = storage.reference
        imageRef = root.child("images")
        setContent {
            AddRecordPage(context = this@AddRecordActivity)
        }


    }
}

//firebase 資料庫
private lateinit var db: FirebaseFirestore

//firebase 儲存空間物件
private lateinit var storage: FirebaseStorage

//儲存空間根目錄參考
private lateinit var root: StorageReference

//根目錄->images資料夾
private lateinit var imageRef: StorageReference
private lateinit var auth: FirebaseAuth
private lateinit var intentObj: Intent


//使用者信箱
private lateinit var userEmail: String

@Composable
fun AddRecordPage(context: Context) {

    val activity: Activity = LocalContext.current as Activity


    //使用者選擇的圖片
    var selectedImg: Uri? by remember { mutableStateOf(null) }

    var selectedImg2: Uri? by remember { mutableStateOf(null) }


    //使用者之前自訂的圖片
    if (!intentObj.getStringExtra(MainPage.APP_IMG_ID).isNullOrEmpty()) {
        imageRef.child(userEmail + "/" + intentObj.getStringExtra(MainPage.APP_IMG_ID) + ".jpg").downloadUrl
            .addOnCompleteListener { result ->
                selectedImg = result.result
            }
    }

    var title: String? = null
    if (!intentObj.getStringExtra(MainPage.DATA_ID).isNullOrEmpty()) {
        title = stringResource(id = R.string.addPage_edit_title)
    } else {
        title = stringResource(id = R.string.addPage_add_title)
    }


    //使用者輸入帳號
    var userInputUsername: MutableState<String>? = null
    if (!intentObj.getStringExtra(MainPage.APP_USERNAME).isNullOrEmpty()) {

        userInputUsername = remember {
            mutableStateOf(intentObj.getStringExtra(MainPage.APP_USERNAME)!!)
        }
    } else {
        userInputUsername = remember {
            mutableStateOf("")
        }
    }

//使用者輸入密碼
    var userInputPassword: MutableState<String>? = null
    if (!intentObj.getStringExtra(MainPage.APP_PASSWORD).isNullOrEmpty()) {
        userInputPassword = remember {
            mutableStateOf(intentObj.getStringExtra(MainPage.APP_PASSWORD)!!)
        }
    } else {
        userInputPassword = remember {
            mutableStateOf("")
        }
    }

//使用者輸入應用程式名稱
    var userInputAppName: MutableState<String>? = null
    if (!intentObj.getStringExtra(MainPage.APP_PASSWORD).isNullOrEmpty()) {
        userInputAppName = remember {
            mutableStateOf(intentObj.getStringExtra(MainPage.APP_NAME)!!)
        }
    } else {
        userInputAppName = remember {
            mutableStateOf("")
        }

    }

//使用者帳號輸入錯誤
    var usernameInputError by remember {
        mutableStateOf(false)
    }

//使用者密碼輸入錯誤
    var passwordInputError by remember {
        mutableStateOf(false)
    }

//使用者帳號輸入錯誤
    var appNameInputError by remember {
        mutableStateOf(false)
    }

//圖片選擇器
    val photoPicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                if (uri != null) {
                    selectedImg2 = uri
                }


            })

    var updateFlag by remember {
        mutableStateOf(true)
    }


    Row(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //標題
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackGroundColor)
                    .padding(top = 10.dp, bottom = 10.dp)
            ) {
                //標題文字
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }

                //儲存按鈕
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable() {

                            //按下儲存按鈕後的操作

                            //檢查使用者是否有輸入每一欄位
                            if (userInputAppName.value == "") {
                                usernameInputError = true
                            } else
                                usernameInputError = false

                            if (userInputPassword.value == "") {
                                passwordInputError = true
                            } else
                                passwordInputError = false

                            if (userInputAppName.value == "") {
                                appNameInputError = true
                            } else
                                appNameInputError = false

                            //輸入均正確
                            if (!usernameInputError && !passwordInputError && !appNameInputError) {
                                var imgId = System
                                    .currentTimeMillis()
                                    .toString()
                                //此紀錄的id

                                var dataId: String? = null


                                //為編輯項目
                                if (!intentObj
                                        .getStringExtra(MainPage.DATA_ID)
                                        .isNullOrEmpty()
                                ) {
                                    dataId = intentObj.getStringExtra(MainPage.DATA_ID)
                                } else {
                                    dataId = System
                                        .currentTimeMillis()
                                        .toString()
                                }

                                //使用者新選的圖片
                                if (selectedImg2 != null) {
                                    //存照片到該使用者的雲端儲存庫
                                    uploadImg(
                                        context,
                                        selectedImg2!!, imgId, AppData(
                                            DataId = dataId!!,
                                            userEmail = userEmail!!,
                                            AppName = userInputAppName.value!!,
                                            AppUsername = userInputUsername.value!!,
                                            AppPassword = userInputPassword.value!!,
                                            AppImgId = imgId
                                        )
                                    )
                                    //有更新圖片的話 就讓上傳圖片的listener去更新資料庫，避免先存資料庫結果找不到圖片的問題
                                    updateFlag = false
                                } else if (selectedImg != null)
                                    imgId = intentObj
                                        .getStringExtra(MainPage.APP_IMG_ID)
                                        .toString()
                                else
                                    imgId = ""


                                //將此項紀錄覆蓋或新增到資料庫

                                if (updateFlag)
                                    updateDbRecord(
                                        context,
                                        AppData(
                                            DataId = dataId!!,
                                            userEmail = userEmail!!,
                                            AppName = userInputAppName.value!!,
                                            AppUsername = userInputUsername.value!!,
                                            AppPassword = userInputPassword.value!!,
                                            AppImgId = imgId
                                        )

                                    )


                            }


                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = stringResource(id = R.string.addPage_add_button),
                        color = Color.White,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(end = 20.dp)
                    )
                }
            }

            //應用程式圖示
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 100.dp, end = 100.dp),
                    contentAlignment = Alignment.BottomEnd
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //尚未選擇圖片
                    if (selectedImg == null && selectedImg2 == null) {
                        Image(
                            painter = painterResource(id = R.drawable.default_icon),
                            contentDescription = "Default Icon",
                            modifier = Modifier
                                .border(2.dp, Color.Gray, CircleShape)
                                .size(150.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                    } else if (selectedImg2 != null) {
                        //編輯項目中 使用者之前選的圖片
                        //選擇圖片後的圖片顯示
                        AsyncImage(
                            model = selectedImg2,
                            contentDescription = "App Icon",
                            modifier = Modifier
                                .border(2.dp, Color.Gray, CircleShape)
                                .size(150.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                    } else {
                        //選擇圖片後的圖片顯示
                        AsyncImage(
                            model = selectedImg,
                            contentDescription = "App Icon",
                            modifier = Modifier
                                .border(2.dp, Color.Gray, CircleShape)
                                .size(150.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    //選擇圖片按鈕
                    ElevatedButton(onClick = {
                        //按下選擇圖片按鈕後的操作

                        //啟動圖片庫
                        photoPicker.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))

                    }, shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        ),
                        modifier = Modifier.size(50.dp)
                    ) {
                            Image(
                                painter = painterResource(id = R.drawable.gallery),
                                contentDescription = "Gallery Icon",
                                modifier = Modifier.size(20.dp)
                            )
                        }

                }

            }

            //應用程式資料欄位
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {


                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //應用程式名稱
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = userInputAppName.value,
                            onValueChange = {
                                userInputAppName.value = it
                            },
                            label = { Text(text = stringResource(R.string.addPage_app_name_field)) },
                            isError = appNameInputError,
                            supportingText = {
                                if (appNameInputError) {
                                    Text(
                                        text = stringResource(id = R.string.addPage_app_name_field_error),
                                        color = MaterialTheme.colorScheme.error
                                    )

                                } else {
                                    Text(
                                        text = stringResource(id = R.string.addPage_app_name_field_error),
                                        color = Color(0,0,0,1)
                                    )
                                    appNameInputError = false
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

                    //應用程式帳號
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(value = userInputUsername.value!!, onValueChange = {
                            userInputUsername.value = it
                        },
                            label = { Text(text = stringResource(id = R.string.addPage_app_username_field)) },
                            isError = usernameInputError,
                            supportingText = {
                                if (usernameInputError) {
                                    Text(
                                        text = stringResource(id = R.string.addPage_app_username_field_error),
                                        color = MaterialTheme.colorScheme.error
                                    )

                                } else {
                                    usernameInputError = false
                                    Text(
                                        text = stringResource(id = R.string.addPage_app_username_field_error),
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
                    //應用程式密碼
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(value = userInputPassword.value!!, onValueChange = {
                            userInputPassword.value = it
                        },
                            label = { Text(text = stringResource(id = R.string.addPage_app_password_field)) },
                            isError = passwordInputError,
                            supportingText = {
                                if (passwordInputError) {
                                    Text(
                                        text = stringResource(id = R.string.addPage_app_password_field_error),
                                        color = MaterialTheme.colorScheme.error
                                    )

                                } else {
                                    passwordInputError = false
                                    Text(
                                        text = stringResource(id = R.string.addPage_app_password_field_error),
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
                }

            }


        }

    }


}


//上傳圖片
fun uploadImg(context: Context, img: Uri, imgId: String, appData: AppData) {

    //將圖片推到雲端儲存庫 儲存位置為images/使用者信箱/圖片id.jpg
    var target = imageRef.child(userEmail + "/" + imgId + ".jpg")
    val uploadtask = target.putFile(img!!)
        .addOnCompleteListener() {
            updateDbRecord(context, appData)
        }
}

//更新使用者紀錄
fun updateDbRecord(context: Context, appData: AppData) {

    val data = hashMapOf(
        "dataId" to appData.DataId,
        "userEmail" to appData.userEmail,
        "appImgId" to appData.AppImgId,
        "appName" to appData.AppName,
        "appUsername" to appData.AppUsername,
        "appPassword" to appData.AppPassword
    )

    //新增至該使用者的collection中
    db.collection(userEmail)
        //以dataId作為該資料的參考
        .document(appData.DataId)
        .set(data)
        .addOnSuccessListener { documentReference ->
            Log.d("MSG", "DocumentSnapshot added with ID: " + appData.DataId)
            val activity = context as Activity
            //儲存成功後 關閉畫面
            activity.finishAfterTransition()
        }
        .addOnFailureListener { e ->
            Log.w("MSG", "Error adding document", e)
        }
}