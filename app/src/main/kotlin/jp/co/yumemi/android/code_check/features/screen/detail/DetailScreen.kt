package jp.co.yumemi.android.code_check.features.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.data.model.Item


@Composable
fun DetailScreen(item: Item) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val context = LocalContext.current
            Spacer(modifier = Modifier
                .height(30.dp)
                .padding(horizontal = 20.dp))
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
            Row(modifier = Modifier
                .wrapContentWidth()
                ,
                ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = "start",
                    modifier = Modifier.size(20.dp,20.dp)
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
                    modifier = Modifier.size(20.dp,20.dp)
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
            Row(modifier = Modifier
                .wrapContentWidth()) {
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

            Row(modifier = Modifier
                .wrapContentWidth()) {
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

        }
    }
}