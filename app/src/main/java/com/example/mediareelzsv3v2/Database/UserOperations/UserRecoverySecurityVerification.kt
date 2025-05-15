package com.example.mediareelzsv3v2.Database.UserOperations


import android.content.Context
import com.example.mediareelzsv3v2.Database.DataBaseHandler
import com.example.mediareelzsv3v2.Database.USERS_TABLE_NAME
import com.example.mediareelzsv3v2.Database.View_Security_Profile_VIEW
import com.example.mediareelzsv3v2.Objects.User

class UserRecoverySecurityVerification(context: Context) {

    var dbHandler: DataBaseHandler = DataBaseHandler(context)


    fun verifyUserNameForRecoveryMethod(userName: String): Boolean {
        val db = dbHandler.readableDatabase
        val userNameIndex:Int
        val sql = "SELECT userName FROM $USERS_TABLE_NAME Where userName = ?"
        db.rawQuery(sql, arrayOf(userName)).use {


            if (it.moveToFirst()) {

                val result = User()
                userNameIndex = it.getColumnIndex("userName")
                result.userName = if (userNameIndex != -1)it.getString(userNameIndex)else ""
                return true


            }


        }

        db.close()

        return false


    }
    //Edge case testing passed 3/19/23 10:54AM//

    fun getSecurityQuestion1(userName: String): String {

        val db = dbHandler.readableDatabase
        val securityQuestion1Index:Int


        val sql = "SELECT securityQuestion1 FROM $View_Security_Profile_VIEW Where userName = ?"
        db.rawQuery(sql, arrayOf(userName)).use {
            if (it.moveToFirst()) {
                val result = User()
                securityQuestion1Index = it.getColumnIndex("securityQuestion1")
                result.securityQuestion1 = if (securityQuestion1Index != -1)it.getString(securityQuestion1Index)else ""

                return result.securityQuestion1


            }


        }


        return "There was a problem retrieving security question 1"


    }

    //Edge case testing passed 3/19/23 10:53AM//

    fun getSecurityQuestion2(userName: String): String {

        val db = dbHandler.readableDatabase
        val securityQuestion2Index:Int

        val sql = "SELECT securityQuestion2 FROM $View_Security_Profile_VIEW Where userName = ?"
        db.rawQuery(sql, arrayOf(userName)).use {
            if (it.moveToFirst()) {
                val result = User()
                securityQuestion2Index = it.getColumnIndex("securityQuestion2")
                result.securityQuestion2 = if (securityQuestion2Index != -1)it.getString(securityQuestion2Index)else ""

                return result.securityQuestion2


            }


        }
        db.close()

        return "There was a problem retrieving security question 2"


    }

    //Edge case testing passed 3/19/23 10:53AM//

    fun getSecurityQuestion3(userName: String): String {

        val db = dbHandler.readableDatabase
        val securityQuestion3Index:Int

        val sql = "SELECT securityQuestion3 FROM $View_Security_Profile_VIEW Where userName = ?"
        db.rawQuery(sql, arrayOf(userName)).use {
            if (it.moveToFirst()) {
                val result = User()
                securityQuestion3Index = it.getColumnIndex("securityQuestion3")
                result.securityQuestion3 = if (securityQuestion3Index != -1)it.getString(securityQuestion3Index)else ""
                return result.securityQuestion3


            }


        }


        return "There was a problem retrieving security question 3"


    }

    //Edge case testing passed 3/19/23 10:51AM//

    fun getSecurityQuestion1Answer(userName: String): String {

        val db = dbHandler.readableDatabase
        val securityQuestion1AnswerIndex:Int

        val sql = "SELECT securityQuestion1Answer FROM $View_Security_Profile_VIEW Where userName = ?"

        db.rawQuery(sql, arrayOf(userName)).use {
            if (it.moveToFirst()) {
                val result = User()
                securityQuestion1AnswerIndex = it.getColumnIndex("securityQuestion1Answer")
                result.securityQuestion1Answer = if (securityQuestion1AnswerIndex != -1)it.getString(securityQuestion1AnswerIndex)else ""
                return result.securityQuestion1Answer


            }

        }


        return "There was a problem retrieving security question 1 answer"


    }
    //Edge case testing passed 3/19/23 10:50AM//

    fun getSecurityQuestion2Answer(userName: String): String {

        val db = dbHandler.readableDatabase
        val securityQuestion2AnswerIndex:Int
        val sql = "SELECT securityQuestion2Answer FROM $View_Security_Profile_VIEW Where userName = ?"
        db.rawQuery(sql, arrayOf(userName)).use {

            if (it.moveToFirst()) {
                val result = User()
                securityQuestion2AnswerIndex = it.getColumnIndex("securityQuestion2Answer")
                result.securityQuestion2Answer = if (securityQuestion2AnswerIndex != -1)it.getString(securityQuestion2AnswerIndex)else ""

                return result.securityQuestion2Answer


            }


        }


        return "There was a problem retrieving security question 2 answer"

    }

    //Edge case testing passed 3/19/23 10:50AM//

    fun getSecurityQuestion3Answer(userName: String): String {

        val db = dbHandler.readableDatabase
        val securityQuestion3AnswerIndex:Int
        val sql = "SELECT securityQuestion3Answer FROM $View_Security_Profile_VIEW Where userName = ?"
        db.rawQuery(sql, arrayOf(userName)).use {
            if (it.moveToFirst()) {

                val result = User()
                securityQuestion3AnswerIndex = it.getColumnIndex("securityQuestion3Answer")
                result.securityQuestion3Answer = if (securityQuestion3AnswerIndex != -1)it.getString(securityQuestion3AnswerIndex)else ""
                return result.securityQuestion3Answer


            }

        }

        return "There was a problem retrieving security question 3 answer"

    }



}