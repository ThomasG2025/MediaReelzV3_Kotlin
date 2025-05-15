package com.example.mediareelzsv3v2

import android.annotation.SuppressLint
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
import com.example.mediareelzsv3v2.Objects.User
import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2Mode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Edge case testing passed 3/18/23 11:08 AM//
@SuppressLint( "SuspiciousIndentation", "CoroutineCreationDuringComposition")
@Composable

fun AlternativeUserVerification(navController: NavHostController) {
    val context: Context = LocalContext.current
    var alternativeUserVerificationSecurityQuestion1TextFieldState by remember { mutableStateOf("") }
    var alternativeUserVerificationSecurityQuestion2TextFieldState by remember { mutableStateOf("") }
    var alternativeUserVerificationSecurityQuestion3TextFieldState by remember { mutableStateOf("") }
    var alternativeUserVerificationSecurityQuestion1AnswerTextFieldState by remember {
        mutableStateOf(
            ""
        )
    }
    var alternativeUserVerificationSecurityQuestion2AnswerTextFieldState by remember {
        mutableStateOf(
            ""
        )
    }
    var alternativeUserVerificationSecurityQuestion3AnswerTextFieldState by remember {
        mutableStateOf(
            ""
        )
    }
    val getSecurityQuestionsScope = rememberCoroutineScope()

    getSecurityQuestionsScope.launch(Dispatchers.IO) {

        val securityQuestion1 = getSecurityQuestion1(context)
        val securityQuestion2 = getSecurityQuestion2(context)
        val securityQuestion3 = getSecurityQuestion3(context)


        withContext(Dispatchers.Main) {

            alternativeUserVerificationSecurityQuestion1TextFieldState = securityQuestion1
            alternativeUserVerificationSecurityQuestion2TextFieldState = securityQuestion2
            alternativeUserVerificationSecurityQuestion3TextFieldState = securityQuestion3

        }

    }

    Surface(

        color = MaterialTheme.colors.background

    ) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
            AlternativeUserVerificationSecurityVerificationLabel()
            TextField(
                value = alternativeUserVerificationSecurityQuestion1TextFieldState,
                label = {
                    Text("Security Question 1")
                },
                onValueChange = {
                    alternativeUserVerificationSecurityQuestion1TextFieldState = it
                },
                readOnly = true,
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 60.dp, top = 130.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)
            )

            TextField(
                value = alternativeUserVerificationSecurityQuestion1AnswerTextFieldState,
                label = {
                    Text("Security Question 1 Answer")
                },
                onValueChange = {
                    alternativeUserVerificationSecurityQuestion1AnswerTextFieldState = it
                },
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 60.dp, top = 220.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)
            )

            TextField(
                value = alternativeUserVerificationSecurityQuestion2TextFieldState,
                label = {
                    Text("Security Question 2")
                },
                onValueChange = {
                    alternativeUserVerificationSecurityQuestion2TextFieldState = it
                },
                readOnly = true,
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 60.dp, top = 310.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)
            )

            TextField(
                value = alternativeUserVerificationSecurityQuestion2AnswerTextFieldState,
                label = {
                    Text("Security Question 2 Answer")
                },
                onValueChange = {
                    alternativeUserVerificationSecurityQuestion2AnswerTextFieldState = it
                },
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 60.dp, top = 400.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)
            )

            TextField(
                value = alternativeUserVerificationSecurityQuestion3TextFieldState,
                label = {
                    Text("Security Question 3")
                },
                onValueChange = {
                    alternativeUserVerificationSecurityQuestion3TextFieldState = it

                },
                readOnly = true,
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 60.dp, top = 490.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)
            )

            TextField(
                value = alternativeUserVerificationSecurityQuestion3AnswerTextFieldState,
                label = {
                    Text("Security Question 3 Answer")
                },
                onValueChange = {
                    alternativeUserVerificationSecurityQuestion3AnswerTextFieldState = it
                },
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 60.dp, top = 580.dp)
                    )
                    .size(width = 300.dp, height = 60.dp)
            )
            val verifyUserSecurityCredentialsBTNScope = rememberCoroutineScope()
            Button(
                onClick = {
                    verifyUserSecurityCredentialsBTNScope.launch {

                        withContext(Dispatchers.IO) {

                            val proceedWithReset = hashSecurityQuestionAnswers(
                                context,
                                alternativeUserVerificationSecurityQuestion1TextFieldState,
                                alternativeUserVerificationSecurityQuestion1AnswerTextFieldState,
                                alternativeUserVerificationSecurityQuestion2TextFieldState,
                                alternativeUserVerificationSecurityQuestion2AnswerTextFieldState,
                                alternativeUserVerificationSecurityQuestion3TextFieldState,
                                alternativeUserVerificationSecurityQuestion3AnswerTextFieldState
                            )

                            if (proceedWithReset) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context, "User Security Profile Successfully Verified",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate(Destination.ForgotUserKey.route)

                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "There was a problem verifying one or more security questions",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                        }

                    }

                },

                modifier = Modifier
                    .padding(PaddingValues(start = 20.dp, top = 650.dp))
                    .width(350.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Verify Security Credentials")

            }

            AlternativeUserVerificationBackButton(navController)

        }
    }

}





@Composable
fun AlternativeUserVerificationSecurityVerificationLabel(){

    Text(text = "Security Verification", modifier = Modifier.padding(PaddingValues(start = 150.dp, top = 60.dp)))

}

@Composable
fun AlternativeUserVerificationBackButton(navController: NavHostController) {

    Button(onClick = {

        navController.navigate(Destination.VerifyUserNameForRecovery.route)

    },

        modifier = Modifier
            .padding(PaddingValues(start = 160.dp, top = 710.dp))
            .width(100.dp),
        shape = RoundedCornerShape(20.dp)
    ){
        Text(text = "Back")
    }
}

fun getSecurityQuestion1(context: Context):String{

    val securityProfileVerifier = UserRecoverySecurityVerification(context)
    val securityQuestion1Text:String = securityProfileVerifier.getSecurityQuestion1(User.getUserName())

    return if(securityQuestion1Text!=""){

        securityQuestion1Text
    }else{
        "Could not get securityQuestion 1 text"
    }

}

fun getSecurityQuestion2(context: Context):String{

    val securityProfileVerifier = UserRecoverySecurityVerification(context)
    val securityQuestion2Text:String = securityProfileVerifier.getSecurityQuestion2(User.getUserName())

    return if(securityQuestion2Text!=""){

        securityQuestion2Text
    }else{
        "Could not get securityQuestion 2 text"
    }

}

fun getSecurityQuestion3(context: Context):String{

    val securityProfileVerifier = UserRecoverySecurityVerification(context)
    val securityQuestion3Text:String = securityProfileVerifier.getSecurityQuestion3(User.getUserName())

    return if(securityQuestion3Text!=""){

        securityQuestion3Text
    }else{
        "Could not get securityQuestion 3 text"
    }

}

fun  hashSecurityQuestionAnswers(context: Context,
                                         AlternativeUserVerificationSecurityQuestion1TextFieldState: String,
                                         AlternativeUserVerificationSecurityQuestion1AnswerTextFieldState:String,
                                         AlternativeUserVerificationSecurityQuestion2TextFieldState: String,
                                         AlternativeUserVerificationSecurityQuestion2AnswerTextFieldState: String,
                                         AlternativeUserVerificationSecurityQuestion3TextFieldState: String,
                                         AlternativeUserVerificationSecurityQuestion3AnswerTextFieldState: String
): Boolean {
    val securityProfileVerifier = UserRecoverySecurityVerification(context)
    val argon2 = Argon2Kt()
    var securityQuestion1AnswerCorrect = false
    var securityQuestion2AnswerCorrect = false
    var securityQuestion3AnswerCorrect = false
    var proceedWithPasswordReset = false
    val question1: String = AlternativeUserVerificationSecurityQuestion1TextFieldState
    val answer1: String = AlternativeUserVerificationSecurityQuestion1AnswerTextFieldState
    val question2: String = AlternativeUserVerificationSecurityQuestion2TextFieldState
    val answer2: String = AlternativeUserVerificationSecurityQuestion2AnswerTextFieldState
    val question3: String = AlternativeUserVerificationSecurityQuestion3TextFieldState
    val answer3: String = AlternativeUserVerificationSecurityQuestion3AnswerTextFieldState
    val question1InputCheck = question1 != "" && answer1 != ""
    if (question1InputCheck) {

        val answer1Converted = answer1.toByteArray()
        val storedAnswer1 = securityProfileVerifier.getSecurityQuestion1Answer(User.getUserName())
        val answer1Verify = argon2.verify(
            mode = Argon2Mode.ARGON2_ID,
            encoded = storedAnswer1,
            password = answer1Converted
        )
        if (answer1Verify) {
            securityQuestion1AnswerCorrect = true
        }

    }
    val question2InputCheck = question2 != "" && answer2 != ""
    if (question2InputCheck) {
        val answer2Converted = answer2.toByteArray()
        val storedAnswer2 = securityProfileVerifier.getSecurityQuestion2Answer(User.getUserName())
        val answer2Verify = argon2.verify(
            mode = Argon2Mode.ARGON2_ID,
            encoded = storedAnswer2,
            password = answer2Converted
        )
        if (answer2Verify) {
            securityQuestion2AnswerCorrect = true
        }
    }
    val question3InputCheck = question3 != "" && answer3 != ""
    if(question3InputCheck){
        val answer3Converted = answer3.toByteArray()
        val storedAnswer3 = securityProfileVerifier.getSecurityQuestion3Answer(User.getUserName())
        val answer3Verify = argon2.verify(
            mode = Argon2Mode.ARGON2_ID,
            encoded = storedAnswer3,
            password = answer3Converted
        )
        if (answer3Verify){
            securityQuestion3AnswerCorrect = true
        }

    }
    val securityQuestionAnswersValidation = securityQuestion1AnswerCorrect && securityQuestion2AnswerCorrect && securityQuestion3AnswerCorrect
    if (securityQuestionAnswersValidation){

        proceedWithPasswordReset = true

    }

    return  proceedWithPasswordReset
}
