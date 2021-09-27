package jp.co.yumemi.android.code_check.features.screen.login

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.common.view.AppToast
import jp.co.yumemi.android.code_check.common.view.ToastStyle
import jp.co.yumemi.android.code_check.features.LoginManager


@Composable
fun LoginScreen(viewModel: LoginViewModel, onNavigation: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    LoginScreen(
        uiState = uiState,
        updateLoadingGoogle = {
            viewModel.updateLoadingGoogle(it)
        },
        onNavigation = onNavigation,
        updateLoadingFaceBook = {
            viewModel.updateLoadingFacebook(it)
        }
    )

}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    updateLoadingGoogle: (Boolean) -> Unit,
    updateLoadingFaceBook: (Boolean) -> Unit,
    onNavigation: () -> Unit
) {
    val mAuth = FirebaseAuth.getInstance()
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val taskSign = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            updateLoadingGoogle(false)
            try {
                val account = taskSign.getResult(ApiException::class.java)
                val token = account?.idToken
                if (token != null) {
                    val credential = GoogleAuthProvider.getCredential(token, null)
                    mAuth.signInWithCredential(credential)
                        .addOnCompleteListener()
                        {
                            LoginManager.isLogin.value = mAuth.currentUser != null
                            onNavigation()
                        }
                        .addOnFailureListener { e ->
                            e.printStackTrace()
                        }
                }
            } catch (e: ApiException) {
                LoginManager.isLogin.value = false
            }
        }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SignInButton(
                text = "Sign in with Google",
                loadingText = "Signing in...",
                isLoading = uiState.isGoogleLoading,
                icon = painterResource(id = R.drawable.ic_login_google),
                onClick = {
                    updateLoadingGoogle(true)
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("130095027277-m7mkh4tt7k4av8e0407d6ubckc5r6q2o.apps.googleusercontent.com")
                        .requestEmail()
                        .build()

                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                }
            )
            Spacer(modifier = Modifier.height(40.dp))
            SignInButton(
                text = "Sign in with Facebook",
                loadingText = "Signing in...",
                isLoading = uiState.isFacebookLoading,
                icon = painterResource(id = R.drawable.ic_login_facebook),
                onClick = {
                    updateLoadingFaceBook(false)
                    AppToast.createToast(ToastStyle.WARNING).setText("申し訳ありませんが、この関数は開発中です。")
                        .setDuration(
                            Toast.LENGTH_LONG
                        ).show(context = context)
                }
            )

        }
        Text(text = "後で",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
                .clickable {
                    onNavigation()
                })
    }
}

@Composable
fun SignInButton(
    text: String,
    loadingText: String = "Signing in...",
    icon: Painter,
    isLoading: Boolean = false,
    borderColor: Color = Color.LightGray,
    backgroundColor: Color = MaterialTheme.colors.surface,
    progressIndicatorColor: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable(
                enabled = !isLoading,
                onClick = onClick
            )
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        border = BorderStroke(width = 1.dp, color = borderColor),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = "SignInButton",
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(text = if (isLoading) loadingText else text)
            if (isLoading) {
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
            }
        }
    }
}