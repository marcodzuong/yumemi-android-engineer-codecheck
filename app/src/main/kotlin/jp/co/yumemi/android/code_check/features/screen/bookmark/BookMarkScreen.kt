package jp.co.yumemi.android.code_check.features.screen.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.code_check.common.view.ProgressDialog

@Composable
fun BookMarkScreen(
    viewModel: BookMarkViewModel,
    onBack: () -> Unit,
    onClickPage: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
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
                Text(
                    text = "ブックマーク",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            BookMarkLoadingContent(isLoading = uiState.loading,
                content = {
                    BookMarkList(listPage = uiState.listPage, onClickPage = onClickPage)
                })

        }
    }

}

@Composable
fun BookMarkLoadingContent(isLoading: Boolean, content: @Composable () -> Unit) {
    content()
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            ProgressDialog()
        }
    }
}

@Composable
fun BookMarkList(listPage: List<String>, onClickPage: (String) -> Unit) {
    LazyColumn {
        items(listPage) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClickPage(it)
                    }) {
                Text(
                    text = it, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp,vertical =  10.dp)
                )
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal =  5.dp)
                )
            }

        }
    }
}