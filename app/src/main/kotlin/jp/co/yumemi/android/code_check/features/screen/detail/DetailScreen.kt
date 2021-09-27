package jp.co.yumemi.android.code_check.features.screen.detail

import android.content.Intent
import android.webkit.WebSettings
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.common.view.AppToast
import jp.co.yumemi.android.code_check.common.view.ToastStyle
import jp.co.yumemi.android.code_check.common.view.WebViewProgress
import jp.co.yumemi.android.code_check.data.model.Item

@ExperimentalCoilApi
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    item: Item,
    onBack: () -> Unit,
    requestLogin: () -> Unit
) {
    viewModel.checkBookMark(url = "https://github.com/${item.name}")
    val uiState by viewModel.uiState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(enabled = true, state = scrollState)
        ) {
            AppBar(
                onBack = onBack,
                item = item,
                isBookMarked = uiState.isBookMark,
                onBookMark = {
                    viewModel.bookMarkGithubPage(it)
                },
                requestLogin = requestLogin
            )
            val context = LocalContext.current
            Spacer(
                modifier = Modifier
                    .height(15.dp)
                    .padding(horizontal = 20.dp)
            )
            Image(
                painter = rememberImagePainter(
                    data = item.ownerIconUrl,
                    onExecute = { _, _ -> true },
                    builder = {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                    }
                ),
                contentDescription = "",
                modifier = Modifier
                    .size(150.dp)
                    .align(CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                modifier = Modifier
                    .wrapContentWidth(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = "start",
                    modifier = Modifier.size(20.dp, 20.dp)
                )

                Text(
                    text = context.getString(
                        R.string.txt_count_stars,
                        item.stargazersCount.toString()
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_fork),
                    contentDescription = "start",
                    modifier = Modifier.size(20.dp, 20.dp)
                )
                Text(
                    text = context.getString(
                        R.string.txt_count_forks,
                        item.forksCount.toString()
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_watcher),
                    contentDescription = "start",
                    modifier = Modifier
                        .size(20.dp, 20.dp)
                )
                Text(
                    text = context.getString(
                        R.string.txt_count_watchers,
                        item.watchersCount.toString()
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
            Row(
                modifier = Modifier
                    .wrapContentWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_watcher),
                    contentDescription = "start",
                    modifier = Modifier
                        .size(20.dp, 20.dp)
                )
                Text(
                    text = context.getString(
                        R.string.txt_count_open_issues,
                        item.openIssuesCount.toString()
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )
            }

            Row(
                modifier = Modifier
                    .wrapContentWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_watcher),
                    contentDescription = "start",
                    modifier = Modifier
                        .size(20.dp, 20.dp)
                )
                Text(
                    text = context.getString(
                        R.string.written_language,
                        item.language
                    ),
                    modifier = Modifier.padding(start = 5.dp)
                )
            }

            WebViewProgress(
                url = "https://github.com/${item.name}/blob/master/README.md",
                initSettings = { settings ->
                    settings?.apply {
                        javaScriptEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        setSupportZoom(true)
                        builtInZoomControls = true
                        displayZoomControls = true
                        javaScriptCanOpenWindowsAutomatically = true
                        cacheMode = WebSettings.LOAD_NO_CACHE
                    }
                },
                onBack = { webView ->
                    if (webView?.canGoBack() == true) {
                        webView.goBack()
                    } else {
                        onBack()
                    }
                },
            )
        }
    }
}

@Composable
fun AppBar(
    onBack: () -> Unit,
    item: Item,
    isBookMarked: Boolean,
    onBookMark: (String) -> Unit,
    requestLogin: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Filled.ArrowBack,
            contentDescription = "Localized description",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onBack()
                })
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = "詳細",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        val context = LocalContext.current

        Image(
            painter = painterResource(id = if (isBookMarked) R.drawable.ic_bookmark_inactive else R.drawable.ic_bookmark),
            contentDescription = "share",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    if (jp.co.yumemi.android.code_check.features.LoginManager.isLogin.value) {
                        if (!isBookMarked){
                            onBookMark("https://github.com/${item.name}")
                        }else{
                            AppToast.createToast(ToastStyle.DONE).setText("このページをフォローしています。").show(context = context)
                        }
                    } else {
                        requestLogin()
                    }

                }
        )
        Spacer(modifier = Modifier.width(10.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_share),
            contentDescription = "share",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    try {
                        val shareMessage =
                            "これは私が興味を持っているgithubページです。\n\n https://github.com/${item.name}"
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareMessage)
                            putExtra(Intent.EXTRA_SUBJECT, "Yumemi App")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    } catch (e: Exception) {

                    }
                }
        )
    }
}