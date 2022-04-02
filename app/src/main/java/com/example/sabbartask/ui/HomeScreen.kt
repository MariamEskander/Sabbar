package com.example.sabbartask.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.sabbartask.ui.theme.ComposeSampleTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import com.example.sabbartask.ui.features.selectIdentity.IdentityScreen
import com.example.sabbartask.ui.features.selectIdentity.IdentityViewModel
import com.example.sabbartask.ui.features.userDriver.UserDriverScreen
import com.example.sabbartask.ui.features.userDriver.UserDriverViewModel
import com.example.sabbartask.ui.home.NavigationKeys.Arg.IDENTITY


// Single Activity per app
@AndroidEntryPoint
class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSampleTheme {
                SabbarApp()
            }
        }
    }
}

@Composable
private fun SabbarApp(){
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.SELECT_IDENTITY) {
        composable(route = NavigationKeys.Route.SELECT_IDENTITY) {
            SelectIdentityDestination(navController)
        }
        composable(
            route = NavigationKeys.Route.MAP_SCREEN,
            arguments = listOf(navArgument(IDENTITY) {
                type = NavType.BoolType
            })
        ) {
            UserDriverScreenDestination()
        }
    }
}


@Composable
private fun SelectIdentityDestination(navController: NavHostController) {
    val viewModel: IdentityViewModel = hiltViewModel()
    IdentityScreen(
        onNavigationRequested = { isUser ->
            navController.navigate("${NavigationKeys.Route.SELECT_IDENTITY}/${isUser}")
        }
    )
}

@Composable
fun UserDriverScreenDestination() {
    val viewModel: UserDriverViewModel = hiltViewModel()
    UserDriverScreen(state = viewModel.state)
}



object NavigationKeys {

    object Arg {
        const val IDENTITY = "identity"
    }

    object Route {
        const val SELECT_IDENTITY = "select_identity"
        const val MAP_SCREEN = "$SELECT_IDENTITY/{$IDENTITY}"
    }

}