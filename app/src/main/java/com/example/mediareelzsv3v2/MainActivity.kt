package com.example.mediareelzsv3v2

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mediareelzsv3v2.Objects.Reel2
import com.example.mediareelzsv3v2.ui.theme.MediaReelzsV3V2Theme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MediaReelzsV3V2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavigationAppHost(navController = navController)
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavigationAppHost(navController: NavHostController) {

    val context = LocalContext.current



    NavHost(navController = navController, startDestination = "Login") {

        composable(Destination.Login.route) { Login(navController) }

        composable(Destination.CreateUser.route) { CreateUser(navController) }
        composable(Destination.VerifyUserNameForRecovery.route) {
            VerifyUserNameForRecovery(
                navController
            )
        }
        composable(Destination.AlternativeUserVerification.route) {
            AlternativeUserVerification(
                navController
            )
        }
        composable(Destination.ForgotUserKey.route) { ForgotUserKey(navController) }
        composable(Destination.InsertReel.route) { InsertReel(navController) }
        composable(Destination.ReelzsViewer.route) { ReelzsViewer(navController, context) }
        composable(Destination.About.route) { About(navController) }
        composable(route = Destination.ModifyReel.route) {

            navController.previousBackStackEntry?.savedStateHandle?.get<Reel2>("reel2")

            ModifyReel(navController)
        }
        composable(route = Destination.ModifyUser.route) { ModifyUser(navController) }

    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MediaReelzsV3V2Theme {

    }
}