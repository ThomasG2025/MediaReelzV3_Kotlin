package com.example.mediareelzsv3v2

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mediareelzsv3v2.Database.UserOperations.UserLogin
import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2Mode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Edge case testing passed 3/18/23 11:42 AM//
@SuppressLint("SuspiciousIndentation")
@Composable
fun Login(navController: NavHostController){

    val context: Context = LocalContext.current
    var userNameLoginTextFieldState by remember {  mutableStateOf("") }
    var userPasswordPasswordDotsFieldState by rememberSaveable{ mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Surface(

        color = MaterialTheme.colors.background


    ) {

        Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {


            MediaReelzsLoginLabel()

            TextField(
                value = userNameLoginTextFieldState,
                label = {
                    Text("Username")
                },
                onValueChange = {
                    userNameLoginTextFieldState = it
                },
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 60.dp, top = 200.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)
            )

            // code in large part adapted from  https://www.youtube.com/watch?v=eNAhOqF83Kg //
            // and https://semicolonspace.com/jetpack-compose-textfield/  //

            TextField(
                value = userPasswordPasswordDotsFieldState,
                onValueChange = {

                    userPasswordPasswordDotsFieldState = it

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
                        PaddingValues(start = 60.dp, top = 280.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)

            )

            val loginButtonScope = rememberCoroutineScope()
            Button(
                onClick = {

                    loginButtonScope.launch(Dispatchers.IO) {

                        val validLogin: Boolean =
                            loginUser(
                                context,
                                userName = userNameLoginTextFieldState,
                                userKey = userPasswordPasswordDotsFieldState
                            )

                        withContext(Dispatchers.Main) {
                            if (validLogin) {


                                navController.navigate(Destination.InsertReel.route)
                                Toast.makeText(
                                    context,
                                    "Login Successful", Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "User credentials are not valid or do not exist please make sure all fields are filled",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }

                    }.start()

                },
                modifier = Modifier
                    .padding(PaddingValues(start = 100.dp, top = 420.dp))
                    .width(200.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Login")

            }

            Button(
                onClick = {
                    navController.navigate(Destination.CreateUser.route)

                },
                modifier = Modifier
                    .padding(PaddingValues(start = 100.dp, top = 485.dp))
                    .width(200.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Create User")
            }

            Button(
                onClick = {
                    navController.navigate(Destination.VerifyUserNameForRecovery.route)
                },
                modifier = Modifier
                    .padding(PaddingValues(start = 100.dp, top = 555.dp))
                    .width(200.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Forgot Password")
            }

        }
    }
}
@Composable
fun MediaReelzsLoginLabel() {
    Text(text = "mediaReelzs", modifier =  Modifier.padding(PaddingValues(start =170.dp, top = 100.dp )))
}

suspend fun loginUser(context: Context, userName: String, userKey: String): Boolean {
    val verificationSuccessful: Boolean
    val verifyUserNameAndUserKeyAreFilledCheck = userName == "" || userKey == ""
    return if(verifyUserNameAndUserKeyAreFilledCheck){


        false
    }else {

        verificationSuccessful = hashPassword(context, userName, userKey)

        println(verificationSuccessful)
        verificationSuccessful
    }

}

suspend fun hashPassword(context: Context, userName: String, userKey:String): Boolean {

    val userLogin = UserLogin(context)
    val argon2 = Argon2Kt()
    val password: ByteArray = userKey.toByteArray()
    try{
        val storedHash: String = userLogin.getStoredHashByUserName(userName)
        val verify: Boolean = argon2.verify(
            mode = Argon2Mode.ARGON2_ID,
            encoded = storedHash,
            password = password
        )

        return if (verify){
            userLogin.setVerifiedUserId(userName,storedHash)

            true

        }else {

            false

        }

    }catch (e:Exception){
        withContext(Dispatchers.Main){
            Toast.makeText(context,"User does not exist", Toast.LENGTH_LONG).show()

        }
        return  false
    }
}

