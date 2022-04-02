package com.example.sabbartask.ui.features.selectIdentity


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sabbartask.R
import com.example.sabbartask.ui.theme.ComposeSampleTheme


@Composable
fun IdentityScreen(
    onNavigationRequested: (isUser: Boolean) -> Unit
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        Box {
            SelectIdentity { isUser ->
                onNavigationRequested(isUser)
            }
        }

    }
}


@Composable
@Preview
fun SelectIdentity(
    onItemClicked: (isUser: Boolean) -> Unit = { }
) {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.choose_user),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = colorResource(id = R.color.black),
                modifier = Modifier
                    .padding(16.dp, 48.dp, 16.dp, 16.dp)

            )
            Button(
                onClick = { onItemClicked(false) },
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 16.dp, 16.dp, 16.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 12.dp,
                    end = 16.dp,
                    bottom = 12.dp
                ),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)

                ) {
                Text(stringResource(id = R.string.driver))
            }

            Button(onClick = { onItemClicked(true) },
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 16.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 12.dp,
                    end = 16.dp,
                    bottom = 12.dp
                ),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)

            ) {
                Text(stringResource(id = R.string.user))
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeSampleTheme {
        IdentityScreen({})
    }
}