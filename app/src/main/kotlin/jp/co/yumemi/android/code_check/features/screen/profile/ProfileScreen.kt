package jp.co.yumemi.android.code_check.features.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.FirebaseAuth


@ExperimentalCoilApi
@Composable
fun ProfileScreen(onBack: () -> Unit, onLogOut: () -> Unit,onShowBookMark:()->Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = CenterVertically,
            ) {
                Icon(Icons.Filled.ArrowBack,
                    contentDescription = "Localized description",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            onBack()
                        })
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "個人情報",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            FirebaseAuth.getInstance().currentUser?.let { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = user.photoUrl,
                            onExecute = { _, _ -> true },
                            builder = {
                                crossfade(true)
                                transformations(CircleCropTransformation())
                            }
                        ),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .align(CenterVertically)
                    ) {
                        Text(
                            text = user.displayName ?: "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Card(
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .padding(horizontal = 15.dp)

                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 15.dp).clickable {
                                    onShowBookMark()
                                },
                            verticalAlignment = CenterVertically
                        ) {
                            Text(text = "ブックマーク", modifier = Modifier.weight(1f))

                        }

                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    CardInfo(hint = "表示名", user.displayName)
                    Spacer(modifier = Modifier.height(20.dp))
                    CardInfo(hint = "メール", user.email)
                    Spacer(modifier = Modifier.height(20.dp))
                    CardInfo(hint = "電話番号", user.phoneNumber)
                    Spacer(modifier = Modifier.height(35.dp))
                    LogoutButton(onLogOut = onLogOut)
                }
            }
        }
    }
}

@Composable
fun CardInfo(hint: String, content: String? = null) {
    Card(
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(horizontal = 15.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 15.dp),
            verticalAlignment = CenterVertically
        ) {
            Text(text = hint, modifier = Modifier.weight(1f))
            Text(text = content ?: "分らない")
        }

    }
}

@Composable
fun LogoutButton(onLogOut: () -> Unit) {
    Card(shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(horizontal = 15.dp)
            .clickable {
                onLogOut()
            }

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = "ログアウト",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}