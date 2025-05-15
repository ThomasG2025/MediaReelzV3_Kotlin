package com.example.mediareelzsv3v2

sealed class Destination(val route:String) {

    object Login: Destination("Login")
    object CreateUser: Destination("CreateUser")
    object InsertReel: Destination("InsertReel")
    object About: Destination("About")
    object VerifyUserNameForRecovery: Destination("VerifyUserNameForRecovery")
    object AlternativeUserVerification: Destination("AlternativeUserVerification")
    object ForgotUserKey: Destination("ForgotUserKey")
    object ReelzsViewer: Destination("ReelzsViewer")
    object ModifyReel: Destination("ModifyReel")
    object ModifyUser: Destination("ModifyUser")






}