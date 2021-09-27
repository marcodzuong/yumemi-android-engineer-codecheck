package jp.co.yumemi.android.code_check.common.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.launch

@Composable
fun WebViewProgress(modifier: Modifier = Modifier, url:String, onBack: (webView: WebView?) -> Unit, initSettings: (webSettings: WebSettings?) -> Unit = {}){
    var rememberWebViewProgress: Int by remember { mutableStateOf(-1) }
    Box {
        CustomWebView(
            modifier = modifier,
            url = url,
            onProgressChange = { progress ->
                rememberWebViewProgress = progress
            },
            initSettings = initSettings, onBack = onBack, onReceivedError = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                }
            }
        )
        LinearProgressIndicator(
            progress = rememberWebViewProgress * 1.0F / 100F,
            modifier = Modifier
                .fillMaxWidth()
                .height(if (rememberWebViewProgress == 100) 0.dp else 5.dp),
            color = Color.Red
        )
    }
}

@Composable
fun CustomWebView(modifier: Modifier = Modifier,
                  url:String,
                  onBack: (webView: WebView?) -> Unit,
                  onProgressChange: (progress:Int)->Unit = {},
                  initSettings: (webSettings: WebSettings?) -> Unit = {},
                  onReceivedError: (error: WebResourceError?) -> Unit = {}){
    val webViewChromeClient = object: WebChromeClient(){
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            onProgressChange(newProgress)
            super.onProgressChanged(view, newProgress)
        }
    }
    val webViewClient = object: WebViewClient(){
        override fun onPageStarted(view: WebView?, url: String?,
                                   favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            onProgressChange(-1)
        }
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            onProgressChange(100)
        }
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if(null == request?.url) return false
            val showOverrideUrl = request.url.toString()
            try {
                if (!showOverrideUrl.startsWith("http://")
                    && !showOverrideUrl.startsWith("https://")) {
                    Intent(Intent.ACTION_VIEW, Uri.parse(showOverrideUrl)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        view?.context?.applicationContext?.startActivity(this)
                    }
                    return true
                }
            }catch (e:Exception){
                return true
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            onReceivedError(error)
        }
    }
    var webView:WebView? = null
    val coroutineScope = rememberCoroutineScope()
    AndroidView(modifier = modifier,factory = { ctx ->
        WebView(ctx).apply {
            this.webViewClient = webViewClient
            this.webChromeClient = webViewChromeClient
            initSettings(this.settings)
            webView = this
            loadUrl(url)
        }
    })
    BackHandler {
        coroutineScope.launch {
            onBack(webView)
        }
    }
}
