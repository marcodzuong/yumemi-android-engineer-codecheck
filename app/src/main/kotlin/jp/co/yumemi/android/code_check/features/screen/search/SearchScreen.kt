package jp.co.yumemi.android.code_check.features.screen.search

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.common.view.AppToast
import jp.co.yumemi.android.code_check.common.view.ProgressDialog
import jp.co.yumemi.android.code_check.common.view.ToastStyle
import jp.co.yumemi.android.code_check.data.model.Item

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    onNavigate: (Item) -> Unit
) {
    MaterialTheme {
        Scaffold(backgroundColor = Color.White) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(15.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_yumemi),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(CenterHorizontally)
                        .height(50.dp)
                )
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    "input search",
                    searchViewModel)
                Spacer(modifier = Modifier.height(5.dp))
                SearchList(searchViewModel, onNavigate)
            }

        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, hint: String = "",viewModel: SearchViewModel) {
    var text by remember { mutableStateOf("") }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }
    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it

            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused && text.isEmpty()
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    viewModel.searchOnGithub(text)
                }
            )
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )

        }
    }
}

@Composable
fun SearchList(searchViewModel: SearchViewModel, onNavigate: (Item) -> Unit) {
    val searchList by remember {
        searchViewModel.searchList
    }
    val isLoading by remember {
        searchViewModel.isLoading
    }
    val isSearchError by remember {
        searchViewModel.isSearchError
    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(searchList){item ->
            Surface(modifier = Modifier.clickable {
                onNavigate(item)
            }) {
                Text(
                    text = item.name,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,

                    )
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            ProgressDialog()
        }
    }
    if (isSearchError){
        val context = LocalContext.current
        AppToast.createToast(ToastStyle.ERROR).setText("Network Error!").setDuration(Toast.LENGTH_SHORT).show(context = context)
//        Toast.makeText(context,"Network Error!",Toast.LENGTH_SHORT).show()
    }

}

