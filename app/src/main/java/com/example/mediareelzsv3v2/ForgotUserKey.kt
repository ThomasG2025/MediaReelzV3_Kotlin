package com.example.mediareelzsv3v2

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mediareelzsv3v2.Database.UserOperations.UserUpdate
import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2KtResult
import com.lambdapioneer.argon2kt.Argon2Mode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Edge case testing passed 3/18/23 11:41 AM//

@SuppressLint("SuspiciousIndentation")
@Composable
fun ForgotUserKey(navController: NavHostController) {
    val context: Context = LocalContext.current
    var forgotUserKeyUserKeyTextFieldState by remember { mutableStateOf("") }
    var forgotUserKeyConfirmUserKeyTextFieldState by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    Surface(

        color = MaterialTheme.colors.background

    ) {

        Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

            ForgotUserKeyLabel()


            // code in large part adapted from  https://www.youtube.com/watch?v=eNAhOqF83Kg //
            // and https://semicolonspace.com/jetpack-compose-textfield/  //


            TextField(
                value = forgotUserKeyUserKeyTextFieldState,
                onValueChange = {

                    forgotUserKeyUserKeyTextFieldState = it

                },
                placeholder = { Text(text = "Password") },
                label = { Text(text = "Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = " lock Icon"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (showPassword) "Show Password" else "Hide Password"
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 60.dp, top = 250.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)
            )


            // code in large part adapted from  https://www.youtube.com/watch?v=eNAhOqF83Kg //
            // and https://semicolonspace.com/jetpack-compose-textfield/  //


            TextField(
                value = forgotUserKeyConfirmUserKeyTextFieldState,
                onValueChange = {

                    forgotUserKeyConfirmUserKeyTextFieldState = it

                },
                placeholder = { Text(text = "Password") },
                label = { Text(text = "Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = " lock Icon"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (showPassword) "Show Password" else "Hide Password"
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 60.dp, top = 350.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)
            )

            val resetPasswordBTNScope = rememberCoroutineScope()
            Button(
                onClick = {
                    resetPasswordBTNScope.launch {

                        withContext(Dispatchers.IO) {

                            val resetCompleted = resetUserKey(
                                context,
                                forgotUserKeyUserKeyTextFieldState,
                                forgotUserKeyConfirmUserKeyTextFieldState
                            )

                            if (resetCompleted) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "Password successfully reset please restart media reelzs",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "Error resetting user password",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            }

                        }

                    }


                },
                modifier = Modifier
                    .padding(PaddingValues(start = 100.dp, top = 460.dp))
                    .width(200.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Reset Password")


            }


            Button(
                onClick = {
                    navController.navigate(Destination.AlternativeUserVerification.route)


                },
                modifier = Modifier
                    .padding(PaddingValues(start = 100.dp, top = 530.dp))
                    .width(200.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Back")


            }


        }

    }
}

@Composable
fun ForgotUserKeyLabel(){

    Text(text = "Forgot Password", modifier = Modifier.padding(PaddingValues(start = 150.dp, top = 150.dp)))

}

suspend fun resetUserKey(
    context: Context,
    ForgotUserKeyUserKeyTextFieldState: String,
    ForgotUserKeyConfirmUserKeyTextFieldState: String
): Boolean {
    var successfulReset = false
    val userUpdater = UserUpdate(context)
//    val dbHelper = DataBaseHandler(context)

    val verifyAllFieldsFilledForUserKeyReset = ForgotUserKeyUserKeyTextFieldState != ""
            && ForgotUserKeyConfirmUserKeyTextFieldState != ""
            && ForgotUserKeyUserKeyTextFieldState == ForgotUserKeyConfirmUserKeyTextFieldState

    if (verifyAllFieldsFilledForUserKeyReset) {

        val updatedHash =
            hashRecoveryUserPassword(context, ForgotUserKeyConfirmUserKeyTextFieldState)

        val verifyHashIsNotNull = updatedHash != ""
        if (verifyHashIsNotNull) {

            val updateCleared = userUpdater.updateUserHashByUserName(updatedHash)

            if (updateCleared) {
                successfulReset = true
            }


        }
    }

    return  successfulReset

}

suspend fun hashRecoveryUserPassword(context: Context, userKey: String):String{

    val argon2 = Argon2Kt()
    val password = userKey.toByteArray()
    val salt: ByteArray = getSalt().toByteArray()


    val hashResult: Argon2KtResult = argon2.hash(
        mode = Argon2Mode.ARGON2_ID,
        password = password,
        salt = salt,
        tCostInIterations = 5,
        mCostInKibibyte = 65536
    )




    val verify: Boolean = argon2.verify(
        Argon2Mode.ARGON2_ID,
        hashResult.encodedOutputAsString(),
        password


    )

    if (verify) {


        return hashResult.encodedOutputAsString()




    }

    withContext(Dispatchers.Main){

        Toast.makeText(context,"There was a problem hashing your new password", Toast.LENGTH_LONG).show()

    }

    return "we had a problem"
}
