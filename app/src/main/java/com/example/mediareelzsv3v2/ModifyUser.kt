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
import com.example.mediareelzsv3v2.Database.UserOperations.UserDelete
import com.example.mediareelzsv3v2.Database.UserOperations.UserUpdate
import com.example.mediareelzsv3v2.Objects.ModifyUserButtonStateManager
import com.example.mediareelzsv3v2.Objects.User
import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2KtResult
import com.lambdapioneer.argon2kt.Argon2Mode

@SuppressLint("SuspiciousIndentation")
@Composable
fun ModifyUser(navController: NavHostController) {
    val context = LocalContext.current
//    val dbHelper = DataBaseHandler(context)
    val userUpdater = UserUpdate(context)
    var currentPasswordState by remember { mutableStateOf("") }
    var newPasswordState by remember { mutableStateOf("") }
    var confirmNewPasswordState by remember { mutableStateOf("") }
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmNewPassword by remember { mutableStateOf(false) }
    ModifyUserButtonStateManager.setModifyUserBackButtonButtonState(true)
    ModifyUserButtonStateManager.setModifyUserUpdateUserButtonState(true)
    ModifyUserButtonStateManager.setModifyUserDeleteUserButtonState(true)







    Surface(

        color = MaterialTheme.colors.background

    ) {

        Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

            ModifyUserLabel()
            AlertLaunch(context,navController)

            // code in large part adapted from  https://www.youtube.com/watch?v=eNAhOqF83Kg //
            // and https://semicolonspace.com/jetpack-compose-textfield/  //


            TextField(
                value = currentPasswordState,
                onValueChange = {

                    currentPasswordState = it

                },
                placeholder = { Text(text = "Password") },
                label = { Text(text = "Current Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = " lock Icon"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                        Icon(
                            imageVector = if (showCurrentPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (showCurrentPassword) "Show Password" else "Hide Password"
                        )
                    }
                },
                visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 60.dp, top = 180.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)


            )

            // code in large part adapted from  https://www.youtube.com/watch?v=eNAhOqF83Kg //
            // and https://semicolonspace.com/jetpack-compose-textfield/  //


            TextField(
                value = newPasswordState,
                onValueChange = {

                    newPasswordState = it

                },
                placeholder = { Text(text = "Password") },
                label = { Text(text = "New  Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = " lock Icon"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showNewPassword = !showNewPassword }) {
                        Icon(
                            imageVector = if (showNewPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (showNewPassword) "Show Password" else "Hide Password"
                        )
                    }
                },
                visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 60.dp, top = 290.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)


            )


            // code in large part adapted from  https://www.youtube.com/watch?v=eNAhOqF83Kg //
            // and https://semicolonspace.com/jetpack-compose-textfield/  //


            TextField(
                value = confirmNewPasswordState,
                onValueChange = {

                    confirmNewPasswordState = it

                },
                placeholder = { Text(text = "Password") },
                label = { Text(text = "Confirm New  Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = " lock Icon"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showConfirmNewPassword = !showConfirmNewPassword }) {
                        Icon(
                            imageVector = if (showConfirmNewPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (showConfirmNewPassword) "Show Password" else "Hide Password"
                        )
                    }
                },
                visualTransformation = if (showConfirmNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 60.dp, top = 400.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)


            )


            Button(
                onClick = {
                    // navController.navigate(Destination.ModifyUser.route)
                    ModifyUserButtonStateManager.setModifyUserBackButtonButtonState(false)
                    ModifyUserButtonStateManager.setModifyUserUpdateUserButtonState(false)
                    ModifyUserButtonStateManager.setModifyUserDeleteUserButtonState(false)
                    val verifyAllRequiredResetFieldsAreFilledCheck =
                        currentPasswordState == "" || newPasswordState == ""
                                || confirmNewPasswordState == "" || newPasswordState != confirmNewPasswordState
                    if (!verifyAllRequiredResetFieldsAreFilledCheck) {


                        val validCurrentPasscode: Boolean =
                            verifyUserCurrentPassword(context, currentPasswordState)
                        if (validCurrentPasscode) {

                            val hash: String =
                                hashUpdateUserPassword(newPasswordState, confirmNewPasswordState)


                            val updateHashByIdSuccessful =
                                userUpdater.updateUserHashByUserId(hash, User.getUserId())
                            if (updateHashByIdSuccessful) {
                                Toast.makeText(
                                    context,
                                    "User Password successfully update",
                                    Toast.LENGTH_LONG
                                ).show()
                                ModifyUserButtonStateManager.setModifyUserBackButtonButtonState(true)
                                ModifyUserButtonStateManager.setModifyUserDeleteUserButtonState(true)
                                ModifyUserButtonStateManager.setModifyUserUpdateUserButtonState(true)

                            } else {
                                Toast.makeText(
                                    context,
                                    "error updating User Password",
                                    Toast.LENGTH_LONG
                                ).show()
                                ModifyUserButtonStateManager.setModifyUserBackButtonButtonState(true)
                                ModifyUserButtonStateManager.setModifyUserDeleteUserButtonState(true)
                                ModifyUserButtonStateManager.setModifyUserUpdateUserButtonState(true)

                            }


                        } else {
                            Toast.makeText(
                                context,
                                "There was a problem verifying your current passcode",
                                Toast.LENGTH_LONG
                            ).show()
                            ModifyUserButtonStateManager.setModifyUserBackButtonButtonState(true)
                            ModifyUserButtonStateManager.setModifyUserDeleteUserButtonState(true)
                            ModifyUserButtonStateManager.setModifyUserUpdateUserButtonState(true)

                        }

                    } else {
                        Toast.makeText(
                            context, "please make sure all fields are filled for password reset",
                            Toast.LENGTH_LONG
                        ).show()
                        ModifyUserButtonStateManager.setModifyUserBackButtonButtonState(true)
                        ModifyUserButtonStateManager.setModifyUserDeleteUserButtonState(true)
                        ModifyUserButtonStateManager.setModifyUserUpdateUserButtonState(true)

                    }


                },

                enabled = ModifyUserButtonStateManager.getModifyUserUpdateUserButtonState(),

                modifier = Modifier
                    .padding(PaddingValues(start = 70.dp, top = 525.dp))
                    .width(100.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Update User")
                println(ModifyUserButtonStateManager.getModifyUserUpdateUserButtonState())

            }



            Button(
                onClick = {
                    navController.navigate(Destination.InsertReel.route)


                },
                enabled = ModifyUserButtonStateManager.getModifyUserBackButtonButtonState(),
                modifier = Modifier
                    .padding(PaddingValues(start = 170.dp, top = 610.dp))
                    .width(100.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Back")


            }


        }

    }

}

@Composable

fun ModifyUserLabel(){

    Text(text = "Modify User", modifier = Modifier.padding(PaddingValues(start = 150.dp, top = 60.dp)))



}

fun verifyUserCurrentPassword(context: Context, currentPasswordState: String): Boolean {

    val userUpdater = UserUpdate(context)
    val argon2 = Argon2Kt()
    val password: ByteArray = currentPasswordState.toByteArray()
    val storedHash: String = userUpdater.getStoredHashByUserId(User.getUserId())

    return argon2.verify(
        mode = Argon2Mode.ARGON2_ID,
        encoded = storedHash,
        password = password
    )

}

fun hashUpdateUserPassword(newPasswordState: String, confirmNewPasswordState: String): String {

    val argon2 = Argon2Kt()
    val password: ByteArray = confirmNewPasswordState.toByteArray()
    val salt = getSalt().toByteArray()
    val checkNewUserKeyAndConfirmNewUserKeyMatchCheck = newPasswordState == confirmNewPasswordState
    if (checkNewUserKeyAndConfirmNewUserKeyMatchCheck) {

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


    }else{
        println("Please make sure  the new password and confirm password")
    }
    return ""
}


@Composable

fun  AlertLaunch(context: Context, navController: NavHostController) {
    val openDialog = remember {
        mutableStateOf(false)
    }
    val userConfirmedDeletion = remember {
        mutableStateOf(false)
    }

    Button(onClick = {

        openDialog.value = true

    },
        enabled = ModifyUserButtonStateManager.getModifyUserDeleteUserButtonState(),

        modifier = Modifier
            .padding(PaddingValues(start = 270.dp, top = 525.dp))
            .width(100.dp),
        shape = RoundedCornerShape(20.dp)
    ){
        Text(text = "Delete User")


    }
    if (openDialog.value) {


        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(text = "User Deletion Warning")
            },
            text = {
                Text(text = "Please confirm you want to delete this user")
            },
            confirmButton = {
                TextButton(onClick = {
                    userConfirmedDeletion.value = true
                    println(userConfirmedDeletion.value)
                    openDialog.value = false
                }) {


                    Text(text = "Confirm")


                }

            },

            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false

                    }
                ) {
                    Text(text = "Cancel")

                }

            },

            )

    }
    if (userConfirmedDeletion.value){
        ModifyUserButtonStateManager.setModifyUserBackButtonButtonState(false)
        ModifyUserButtonStateManager.setModifyUserDeleteUserButtonState(false)
        ModifyUserButtonStateManager.setModifyUserUpdateUserButtonState(false)
        val userDeleter = UserDelete(context)
        val userDeletedSuccessfully:Boolean = userDeleter.getUserReelIdsForUserDeletion()
        ModifyUserButtonStateManager.setModifyUserBackButtonButtonState(true)
        ModifyUserButtonStateManager.setModifyUserDeleteUserButtonState(true)
        ModifyUserButtonStateManager.setModifyUserUpdateUserButtonState(true)
        if (userDeletedSuccessfully){
            navController.navigate(Destination.Login.route)
            Toast.makeText(context,
                "user Successfully deleted",
                Toast.LENGTH_LONG).show()

        }else {

            Toast.makeText(context,
                "user deletion error",
                Toast.LENGTH_LONG).show()

        }

    }
}





