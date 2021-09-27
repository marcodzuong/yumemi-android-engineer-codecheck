package jp.co.yumemi.android.code_check.features.screen.browser

import android.webkit.WebSettings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.code_check.common.view.WebViewProgress

@Composable
fun BrowserScreen(url: String, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
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

            }
            WebViewProgress(
                url = url,
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