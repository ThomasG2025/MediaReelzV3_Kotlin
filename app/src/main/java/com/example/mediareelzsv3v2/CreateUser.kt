package com.example.mediareelzsv3v2

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.example.mediareelzsv3v2.Database.UserOperations.UserCreate
import com.example.mediareelzsv3v2.ui.theme.MediaReelzsV3V2Theme
import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2KtResult
import com.lambdapioneer.argon2kt.Argon2Mode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.SecureRandom

@Composable
fun CreateUser(navController: NavHostController) {
    val context: Context = LocalContext.current
    val securityQuestions = listOf(
        "Please select a security Question", "What is your favorite color ?", "First pets name ?",
        "Favorite musical artist or band ?", "Favorite animal ?",
        "Favorite tv Show ?"
    )
    var expanded1 by remember { mutableStateOf(false) }
    var expanded2 by remember{ mutableStateOf(false) }
    var expanded3 by remember{ mutableStateOf(false) }
    var selectedSecurityQuestion1Text by remember { mutableStateOf(securityQuestions[0]) }
    var selectedSecurityQuestion2Text by remember { mutableStateOf(securityQuestions[0]) }
    var selectedSecurityQuestion3Text by remember { mutableStateOf(securityQuestions[0]) }
    val icon1 = if (expanded1) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    val icon2 = if (expanded2) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    val icon3 = if (expanded3) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }
    var createUserSecurityQuestion1AnswerState by remember { mutableStateOf("") }
    var createUserSecurityQuestion2AnswerState by remember { mutableStateOf("") }
    var createUserSecurityQuestion3AnswerState by remember { mutableStateOf("") }
    var createUserUserNameTextFieldState by remember { mutableStateOf("") }
    var createUserUserKeyTextFieldState by remember { mutableStateOf("") }
    var createUserConfirmUserKeyTextFieldState by remember { mutableStateOf("") }
    var showCreateUserPassword by remember { mutableStateOf(false) }
    var showCreateUserConfirmPassword by remember { mutableStateOf(false) }

    MediaReelzsV3V2Theme{

        Surface(

            color = MaterialTheme.colors.background

        ) {

            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)) {

                TextField(

                    value = selectedSecurityQuestion1Text,
                    onValueChange = { selectedSecurityQuestion1Text = it },
                    label = { Text(text = "Selected Security Question") },
                    trailingIcon = {
                        Icon(icon1, "", Modifier.clickable { expanded1 = !expanded1 })
                    },
                    modifier = Modifier
                        .padding(PaddingValues(start = 70.dp, top = 30.dp))
                        .size(width = 315.dp, height = 55.dp)
                )

                DropdownMenu(expanded = expanded1,
                    onDismissRequest = { expanded1 = false }
                ) {

                    securityQuestions.forEach { label ->
                        DropdownMenuItem(onClick = {

                            selectedSecurityQuestion1Text = label
                            expanded1 = true

                        }) {

                            Text(text = label)

                        }

                    }

                }

                TextField(
                    value = createUserSecurityQuestion1AnswerState,
                    label = {
                        Text("Answer 1")
                    },
                    onValueChange = {

                        createUserSecurityQuestion1AnswerState = it
                    },
                    singleLine = true,
                    modifier = Modifier
                        .padding(
                            PaddingValues(start = 70.dp, 100.dp)
                        )
                        .size(width = 210.dp, 60.dp)

                )

                TextField(
                    value = selectedSecurityQuestion2Text,
                    onValueChange = { selectedSecurityQuestion2Text = it },
                    label = { Text(text = "Selected Security Question") },
                    trailingIcon = {
                        Icon(icon2, "", Modifier.clickable { expanded2 = !expanded2 })
                    },
                    modifier = Modifier
                        .padding(PaddingValues(start = 70.dp, top = 170.dp))
                        .size(width = 315.dp, height = 55.dp)
                )

                DropdownMenu(expanded = expanded2,
                    onDismissRequest = { expanded2 = false }
                ) {

                    securityQuestions.forEach { label ->
                        DropdownMenuItem(onClick = {

                            selectedSecurityQuestion2Text = label
                            expanded2 = true

                        }) {

                            Text(text = label)

                        }

                    }

                }

                TextField(
                    value = createUserSecurityQuestion2AnswerState,
                    label = {
                        Text("Answer 2")
                    },
                    onValueChange = {

                        createUserSecurityQuestion2AnswerState = it
                    },
                    singleLine = true,
                    modifier = Modifier
                        .padding(
                            PaddingValues(start = 70.dp, 240.dp)
                        )
                        .size(width = 210.dp, 60.dp)

                )

                TextField(
                    value = selectedSecurityQuestion3Text,
                    onValueChange = { selectedSecurityQuestion3Text = it },
                    label = { Text(text = "Selected Security Question") },
                    trailingIcon = {
                        Icon(icon3, "", Modifier.clickable { expanded3 = !expanded3 })
                    },
                    modifier = Modifier
                        .padding(PaddingValues(start = 70.dp, top = 310.dp))
                        .size(width = 315.dp, height = 55.dp)
                )

                DropdownMenu(expanded = expanded3,
                    onDismissRequest = { expanded3 = false }
                ) {

                    securityQuestions.forEach { label ->
                        DropdownMenuItem(onClick = {

                            selectedSecurityQuestion3Text = label
                            expanded3 = true

                        }) {

                            Text(text = label)

                        }

                    }

                }

                TextField(
                    value = createUserSecurityQuestion3AnswerState,
                    label = {
                        Text("Answer 3")
                    },
                    onValueChange = {

                        createUserSecurityQuestion3AnswerState = it
                    },
                    singleLine = true,
                    modifier = Modifier
                        .padding(
                            PaddingValues(start = 70.dp, 380.dp)
                        )
                        .size(width = 210.dp, 60.dp)

                )

                TextField(
                    value = createUserUserNameTextFieldState,
                    label = {
                        Text("Username")
                    },
                    onValueChange = {

                        createUserUserNameTextFieldState = it

                    },
                    singleLine = true,
                    modifier = Modifier
                        .padding(
                            PaddingValues(start = 70.dp, 450.dp)
                        )
                        .size(width = 210.dp, 60.dp)

                )

                TextField(
                    value = createUserUserKeyTextFieldState,
                    label = {
                        Text("UserKey")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = " lock Icon"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { showCreateUserPassword = !showCreateUserPassword }) {
                            Icon(
                                imageVector = if (showCreateUserPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = if (showCreateUserPassword) "Show Password" else "Hide Password"
                            )
                        }
                    },
                    visualTransformation = if (showCreateUserPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    onValueChange = {

                        createUserUserKeyTextFieldState = it

                    },
                    singleLine = true,
                    modifier = Modifier
                        .padding(
                            PaddingValues(start = 70.dp, 520.dp)
                        )
                        .size(width = 210.dp, 60.dp)

                )

                TextField(
                    value = createUserConfirmUserKeyTextFieldState,
                    label = { Text("Confirm UserKey") },

                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = " lock Icon"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            showCreateUserConfirmPassword = !showCreateUserConfirmPassword
                        }) {
                            Icon(
                                imageVector = if (showCreateUserConfirmPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = if (showCreateUserConfirmPassword) "Show Password" else "Hide Password"
                            )
                        }
                    },
                    visualTransformation = if (showCreateUserConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    onValueChange = {

                        createUserConfirmUserKeyTextFieldState = it
                    },
                    singleLine = true,
                    modifier = Modifier
                        .padding(
                            PaddingValues(start = 70.dp, 590.dp)
                        )
                        .size(width = 210.dp, 60.dp)

                )
                val createUserButtonScope = rememberCoroutineScope()
                Button(
                    onClick = {
                        createUserButtonScope.launch {
                            withContext(Dispatchers.IO) {
                                val userCreationSuccessful: Boolean = createNewUser(
                                    context = context,
                                    securityQuestion1 = selectedSecurityQuestion1Text,
                                    securityQuestion1Answer = createUserSecurityQuestion1AnswerState,
                                    securityQuestion2 = selectedSecurityQuestion2Text,
                                    securityQuestion2Answer = createUserSecurityQuestion2AnswerState,
                                    securityQuestion3 = selectedSecurityQuestion3Text,
                                    securityQuestion3Answer = createUserSecurityQuestion3AnswerState,
                                    userName = createUserUserNameTextFieldState,
                                    userKey = createUserUserKeyTextFieldState,
                                    confirmUserKey = createUserConfirmUserKeyTextFieldState
                                )

                                println(userCreationSuccessful)

                                if (userCreationSuccessful) {


                                    withContext(Dispatchers.Main) {

                                        Toast.makeText(
                                            context,
                                            "User Successfully added",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                    }


                                } else {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Error Adding User",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                    }
                                }


                            }
                        }

                    },
                    modifier = Modifier
                        .padding(PaddingValues(start = 100.dp, top = 670.dp))
                        .width(200.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = "Create User")

                }

                Button(
                    onClick = {

                        navController.navigate(Destination.Login.route)

                    },
                    modifier = Modifier
                        .padding(PaddingValues(start = 100.dp, top = 720.dp))
                        .width(200.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = "Back To Login")

                }

            }

        }

    }

}

suspend fun createNewUser(context: Context, securityQuestion1:String, securityQuestion1Answer:String,
                          securityQuestion2:String, securityQuestion2Answer:String,
                          securityQuestion3:String, securityQuestion3Answer:String,
                          userName:String, userKey:String, confirmUserKey:String): Boolean {
    var userSuccessfullyCreated = false
    val userCreator = UserCreate(context)
    val verifyAllFieldsAreFilledForUserCreation = securityQuestion1 == "Please select a security Question" || securityQuestion1Answer == "" ||
            securityQuestion2 == "Please select a security Question" || securityQuestion2Answer == "" ||
            securityQuestion3 == "Please select a security Question" || securityQuestion3Answer == "" ||
            userName == "" || userKey == "" || confirmUserKey == ""

    if (verifyAllFieldsAreFilledForUserCreation) {

        return  false

    } else{

        val uniqueUserName: Boolean =
            userCreator.verifyUniqueUserName(userName)
        println(uniqueUserName)
        if (uniqueUserName){

            val hash: String =
                hashNewUserPassword(userKey,confirmUserKey)

            val answer1: String =
                hashSecurityQuestionAnswers(securityQuestion1Answer)

            val answer2: String =
                hashSecurityQuestionAnswers(securityQuestion2Answer)

            val answer3: String =
                hashSecurityQuestionAnswers(securityQuestion3Answer)
            if(hash!= "") {


                userSuccessfullyCreated =    userCreator.addUser(userName, hash, securityQuestion1, answer1, securityQuestion2, answer2, securityQuestion3, answer3
                )
            }else {
                return false
            }
        }

    }

    return userSuccessfullyCreated

}

fun hashNewUserPassword( userKey: String, confirmUserKey: String):String {


    val userKeyAndConfirmUserKeyComparison = userKey == confirmUserKey
//    val validPasswordComplexity = validateUserKeyComplexityRequirements(confirmUserKey)

    println(userKey)
    println(confirmUserKey)

    if (userKeyAndConfirmUserKeyComparison) {

        val password = userKey.toByteArray()
        val argon2 = Argon2Kt()
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

        return if (verify) {

            hashResult.encodedOutputAsString()

        } else {

            ""
        }

    } else {
        return ""

    }

}


//fun validateUserKeyComplexityRequirements(confirmUserKey: String) {
//    val specialCharacters = arrayOf("~","","!","@","#","$","%")
//    if (confirmUserKey.length>= 8&&confirmUserKey.contains("!"))
//
//}


fun getSalt(): String {

    val random = SecureRandom()
    val bytes = ByteArray(20)
    val generatedSalt = random.nextBytes(bytes)
    return  generatedSalt.toString()

}

fun hashSecurityQuestionAnswers(securityQuestionAnswer: String):String{

    val argon2 = Argon2Kt()
    val answer = securityQuestionAnswer.toByteArray()
    val salt = getSalt().toByteArray()

    val hashResult : Argon2KtResult = argon2.hash(
        mode = Argon2Mode.ARGON2_ID,
        password = answer,
        salt = salt,
        tCostInIterations = 5,
        mCostInKibibyte = 65536
    )

    val verify:Boolean = argon2.verify(
        Argon2Mode.ARGON2_ID,
        hashResult.encodedOutputAsString(),
        answer
    )

    if(verify){

        return  hashResult.encodedOutputAsString()

    }

    return  ""
}
