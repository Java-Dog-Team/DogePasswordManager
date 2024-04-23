package com.example.dogepasswordmanager

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.compose.AsyncImage
import com.example.dogepasswordmanager.ui.theme.BackGroundColor
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

class AddRecordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        auth = Firebase.auth


        userEmail = auth.currentUser?.email.toString()

        setContent {
            AddRecordPage(context = this@AddRecordActivity, intent)
        }


    }
}

//firebase 資料庫
private val db = Firebase.firestore

//firebase 儲存空間物件
private val storage = Firebase.storage

//儲存空間根目錄參考
private val root = storage.reference

//根目錄->images資料夾
private var imageRef = root.child("images")
private lateinit var auth: FirebaseAuth

//使用者信箱
private lateinit var userEmail: String

@Composable
fun AddRecordPage(context: Context, intent: Intent) {

    val activity: Activity = LocalContext.current as Activity


    //使用者選擇的圖片
    var selectedImg: Uri? by remember { mutableStateOf(null) }

    //使用者輸入帳號
    var userInputUsername by remember {
        mutableStateOf("")
    }

    //使用者輸入密碼
    var userInputPassword by remember {
        mutableStateOf("")
    }

    //使用者輸入應用程式名稱
    var userInputAppName by remember {
        mutableStateOf("")
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
                selectedImg = uri
            })




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
                        text = "新增項目",
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


                            //輸入均正確
                            if (!usernameInputError && !passwordInputError && !appNameInputError) {
                                var imgId = System
                                    .currentTimeMillis()
                                    .toString()
                                //此紀錄的id
                                var dataId = System
                                    .currentTimeMillis()
                                    .toString()

                                if (selectedImg != null) {
                                    //存照片到該使用者的雲端儲存庫
                                    uploadImg(
                                        selectedImg!!, imgId
                                    )
                                } else
                                    imgId = ""


                                //將此項紀錄覆蓋或新增到資料庫
                                updateDbRecord(
                                    AppData(
                                        DataId = dataId,
                                        userEmail = userEmail,
                                        AppName = userInputAppName,
                                        AppUsername = userInputUsername,
                                        AppPassword = userInputPassword,
                                        AppImgId = imgId
                                    )
                                )

                                //結束此頁面
                                activity.finish()

                            }


                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "儲存",
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //尚未選擇圖片
                    if (selectedImg == null) {

                        Image(
                            painter = painterResource(id = R.drawable.default_icon),
                            contentDescription = "Default Icon",
                            modifier = Modifier
                                .border(2.dp, Color.Gray, CircleShape)
                                .size(200.dp)
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
                                .size(200.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    //選擇圖片按鈕
                    Button(onClick = {
                        //按下選擇圖片按鈕後的操作

                        //啟動圖片庫
                        photoPicker.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))

                    }, shape = RectangleShape, modifier = Modifier.padding(top = 10.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.gallery),
                            contentDescription = "Gallery Icon",
                            modifier = Modifier.size(50.dp)
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
                        TextField(
                            value = userInputAppName,
                            onValueChange = {
                                userInputAppName = it
                            },
                            label = { Text(text = "應用程式名稱") },
                            isError = appNameInputError,
                            supportingText = {
                                if (appNameInputError) {
                                    Text(
                                        text = "應用程式名稱請勿留白",
                                        color = MaterialTheme.colorScheme.error
                                    )

                                } else {
                                    appNameInputError = false
                                }
                            }
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
                        TextField(value = userInputUsername, onValueChange = {
                            userInputUsername = it
                        },
                            label = { Text(text = "應用程式使用者名稱") },
                            isError = usernameInputError,
                            supportingText = {
                                if (usernameInputError) {
                                    Text(
                                        text = "應用程式使用者名稱請勿留白",
                                        color = MaterialTheme.colorScheme.error
                                    )

                                } else {
                                    usernameInputError = false
                                }
                            })
                    }
                    //應用程式密碼
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextField(value = userInputPassword, onValueChange = {
                            userInputPassword = it
                        },
                            label = { Text(text = "應用程式密碼") },
                            isError = passwordInputError,
                            supportingText = {
                                if (passwordInputError) {
                                    Text(
                                        text = "應用程式名稱請勿留白",
                                        color = MaterialTheme.colorScheme.error
                                    )

                                } else {
                                    passwordInputError = false
                                }
                            })
                    }
                }

            }


        }

    }


}


//上傳圖片
fun uploadImg(img: Uri, imgId: String) {

    //將圖片推到雲端儲存庫 儲存位置為images/使用者信箱/圖片id.jpg
    var target = imageRef.child(userEmail + "/" + imgId + ".jpg")


    val uploadtask = target.putFile(img!!)
}

//更新使用者紀錄
fun updateDbRecord(appData: AppData) {


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
            Log.d(TAG, "DocumentSnapshot added with ID: " + appData.DataId)
        }
        .addOnFailureListener { e ->
            Log.w(TAG, "Error adding document", e)
        }
}