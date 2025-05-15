package com.example.mediareelzsv3v2.Objects

class User {
    var userId : Int = 0
    var userName : String = ""
    var hash : String = ""
    var securityQuestion1 = ""
    var securityQuestion1Answer = ""
    var securityQuestion2 = ""
    var securityQuestion2Answer = ""
    var securityQuestion3 = ""
    var securityQuestion3Answer = ""



    constructor(){
    }

    companion object{

        private var userId: Int = 0
        private var userName: String = ""


        @JvmName("getUserId1")
        fun getUserId(): Int {

            return userId


        }

        @JvmName("setUserId1")
        fun setUserId(userId: Int){
            Companion.userId = userId


        }

        @JvmName("getUserName1")
        fun getUserName():String {

            return userName

        }

        @JvmName("setUserName1")
        fun setUserName(userName: String){
            Companion.userName = userName

        }




    }
}