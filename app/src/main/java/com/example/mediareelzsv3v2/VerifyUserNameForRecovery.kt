package com.example.mediareelzsv3v2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mediareelzsv3v2.Database.UserOperations.UserRecoverySecurityVerification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//Edge case testing passed 3/18/23 11:43 AM//

@SuppressLint("SuspiciousIndentation")
@Composable
fun VerifyUserNameForRecovery(navController: NavHostController) {

    val context: Context = LocalContext.current
    var verifyUserNameTextFieldState by remember { mutableStateOf("") }
    Surface(

        color = MaterialTheme.colors.background

    ) {

        Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

            VerifyUserNameLabel()

            TextField(
                value = verifyUserNameTextFieldState,
                label = {
                    Text("Username")
                },
                onValueChange = {
                    verifyUserNameTextFieldState = it
                },
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 60.dp, top = 250.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)
            )

            val verifyUserNameButtonScope = rememberCoroutineScope()
            // from chatGpt
            val activity = context as Activity
            var validUserFound: Boolean
            Button(onClick = {

                verifyUserNameButtonScope.launch {

                    withContext(Dispatchers.IO) {
                        validUserFound =
                            verifyUserName(context, verifyUserNameTextFieldState)
                        println(validUserFound)
                    }

                    withContext(Dispatchers.Main) {

                        activity.runOnUiThread {
                            validUserCheck(validUserFound, context, navController)
                        }
                    }

                }


            },
                modifier = Modifier
                    .padding(PaddingValues(start = 105.dp, top = 380.dp))
                    .width(200.dp),
                shape = RoundedCornerShape(20.dp)) {
                Text(text = "Verify User Name")


            }













            BackToLoginButton(navController)

        }


    }
}


@Composable

fun VerifyUserNameLabel(){
    Text(text = "Verify User Name", modifier = Modifier.padding(PaddingValues(start = 150.dp, top = 150.dp)))


}



@Composable

fun BackToLoginButton(navController: NavHostController) {

    Button(onClick = {

        navController.navigate(Destination.Login.route)

    },

        modifier = Modifier
            .padding(PaddingValues(start = 105.dp, top = 450.dp))
            .width(200.dp),
        shape = RoundedCornerShape(20.dp)
    ){
        Text(text = "Back To Login")


    }

}

fun verifyUserName(context: Context, VerifyUserNameTextFieldState: String): Boolean {
    val securityProfileVerifier = UserRecoverySecurityVerification(context)


    return securityProfileVerifier.verifyUserNameForRecoveryMethod(VerifyUserNameTextFieldState)


}

fun  validUserCheck(validUserFound: Boolean, context: Context, navController: NavHostController) {

    if (validUserFound) {
        Toast.makeText(
            context,
            "User found",
            Toast.LENGTH_LONG
        ).show()

        navController.navigate(Destination.AlternativeUserVerification.route)

    } else {

        Toast.makeText(
            context,
            "could not find valid user with provided credentials",
            Toast.LENGTH_LONG
        ).show()
    }

}
