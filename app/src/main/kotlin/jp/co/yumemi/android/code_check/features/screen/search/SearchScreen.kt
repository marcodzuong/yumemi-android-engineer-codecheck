package jp.co.yumemi.android.code_check.features.screen.search

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.FirebaseAuth
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.common.view.AppToast
import jp.co.yumemi.android.code_check.common.view.ProgressDialog
import jp.co.yumemi.android.code_check.common.view.ToastStyle
import jp.co.yumemi.android.code_check.data.model.Item
import jp.co.yumemi.android.code_check.features.LoginManager


@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    onNavigate: (Item) -> Unit,
    onClickProfile: () -> Unit,
    onClickLogin: () -> Unit
) {
    val uiState by searchViewModel.uiState.collectAsState()
    SearchScreen(
        uiState = uiState,
        onNavigate = onNavigate,
        onClickProfile = onClickProfile,
        onClickLogin = onClickLogin,
        onSearch = {
            searchViewModel.searchOnGithub()
        },
        onQueryText = {
            searchViewModel.onQueryText(it)
        },
        onLoadMore = {
            searchViewModel.searchOnGithub(false)
        }
    )
}

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onNavigate: (Item) -> Unit,
    onClickProfile: () -> Unit,
    onClickLogin: () -> Unit,
    onQueryText: (String) -> Unit,
    onSearch: (String) -> Unit,
    onLoadMore:() ->Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        TopBar(
            onClickProfile = onClickProfile,
            onClickLogin = onClickLogin
        )
        SearchBar(
            searchText = uiState.searchText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            hint = "入力検索",
            onSearch = onSearch,
            onQueryText = onQueryText
        )
        Spacer(modifier = Modifier.height(5.dp))
        val scrollState = rememberLazyListState()
        LoadingContent(
            loading = uiState.loading,
            content = {
                SearchContent(
                    modifier = Modifier.fillMaxWidth(),
                    searchData = uiState.items,
                    isShowingErrors = uiState.isSearchError,
                    scrollState = scrollState,
                    onNavigate = onNavigate,
                    onLoadMore = onLoadMore,
                    loading = uiState.loading
                )
            }
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun TopBar(
    onClickProfile: () -> Unit,
    onClickLogin: () -> Unit
) {
    val isLogin by remember {
        LoginManager.isLogin
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo_yumemi),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.Center)
        )
        Image(
            painter = if (isLogin) {
                rememberImagePainter(
                    data = FirebaseAuth.getInstance().currentUser?.photoUrl,
                    onExecute = { _, _ -> true },
                    builder = {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                    },
                )
            } else {
                painterResource(R.drawable.ic_avatar_place_holder)
            },
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
                .align(Alignment.CenterEnd)
                .clickable {
                    if (isLogin) {
                        onClickProfile()
                    } else {
                        onClickLogin()
                    }
                }
        )
    }
}

@Composable
fun SearchBar(
    searchText: String,
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit,
    onQueryText: (String) -> Unit,
) {
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }
    Box(modifier = modifier) {
        BasicTextField(
            value = searchText,
            onValueChange = {
                onQueryText(it)
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
                    isHintDisplayed = !it.isFocused && searchText.isEmpty()
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(searchText)
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
private fun LoadingContent(
    loading: Boolean,
    content: @Composable () -> Unit
) {
    content()
    Box(modifier = Modifier.fillMaxSize()) {
        if (loading) {
            ProgressDialog()
        }
    }

}

@Composable
private fun SearchContent(
    searchData: List<Item>,
    isShowingErrors: Boolean,
    modifier: Modifier = Modifier,
    scrollState: LazyListState,
    onNavigate: (Item) -> Unit,
    onLoadMore:() ->Unit,
    loading: Boolean,
) {
    if (searchData.isNotEmpty()) {
        SearchList(searchData = searchData, modifier = modifier, scrollState, onNavigate,onLoadMore ,loading = loading)
    } else {
        Box(modifier.fillMaxSize()) { /* empty screen */ }
    }

    if (isShowingErrors) {
        val context = LocalContext.current
        AppToast.createToast(ToastStyle.ERROR).setText("ネットワークエラー！").setDuration(Toast.LENGTH_SHORT)
            .show(context = context)
    }
}

@Composable
private fun SearchList(
    searchData: List<Item>,
    modifier: Modifier,
    scrollState: LazyListState,
    onNavigate: (Item) -> Unit,
    onLoadMore:() ->Unit,
    loading: Boolean,
) {
    val lastIndex = searchData.lastIndex
    LazyColumn(modifier = modifier, state = scrollState) {
        itemsIndexed(searchData) { index,item ->
            Column(modifier = Modifier.fillMaxWidth()) {
                SearchItem(item = item, onNavigate = onNavigate)
                if (lastIndex == index) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text="もっとダウンロード",
                        modifier = Modifier
                            .wrapContentWidth()
                            .background(color = Color.Blue)
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                if (!loading) {
                                    onLoadMore()
                                }
                            },
                        color= Color.White,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }

        }
    }
}


@Composable
fun SearchItem(item : Item,onNavigate: (Item) -> Unit){
    Column(modifier = Modifier.clickable {
        onNavigate(item)
    }) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = item.name,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 20.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = "start",
                modifier = Modifier.size(15.dp)
            )

            Text(
                text = stringResource(
                    R.string.txt_count_stars,
                    item.stargazersCount.toString()),
                modifier = Modifier.padding(start = 5.dp),
                fontSize = 13.sp
            )
//            Spacer(modifier = Modifier.width(10.dp))
//            Image(
//                painter = painterResource(id = R.drawable.ic_fork),
//                contentDescription = "start",
//                modifier = Modifier.size(15.dp)
//            )
//            Text(
//                text = stringResource(
//                    R.string.txt_count_forks,
//                    item.forksCount.toString()
//                ),
//                modifier = Modifier.padding(start = 5.dp),
//                fontSize = 13.sp
//            )
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_watcher),
                contentDescription = "start",
                modifier = Modifier
                    .size(15.dp)
            )
            Text(
                text = stringResource(
                    R.string.txt_count_watchers,
                    item.watchersCount.toString()
                ),
                modifier = Modifier.padding(start = 5.dp),
                fontSize = 13.sp
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = Color.LightGray, thickness = 1.dp,modifier = Modifier.padding(horizontal =  5.dp))
    }

}