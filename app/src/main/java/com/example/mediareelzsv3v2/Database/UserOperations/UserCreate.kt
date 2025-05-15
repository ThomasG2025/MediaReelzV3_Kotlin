package com.example.mediareelzsv3v2.Database.UserOperations


import android.content.ContentValues
import android.content.Context
import com.example.mediareelzsv3v2.Database.COL_HASH
import com.example.mediareelzsv3v2.Database.COL_SECURITY_QUESTION1
import com.example.mediareelzsv3v2.Database.COL_SECURITY_QUESTION1ANSWER
import com.example.mediareelzsv3v2.Database.COL_SECURITY_QUESTION2
import com.example.mediareelzsv3v2.Database.COL_SECURITY_QUESTION2ANSWER
import com.example.mediareelzsv3v2.Database.COL_SECURITY_QUESTION3
import com.example.mediareelzsv3v2.Database.COL_SECURITY_QUESTION3ANSWER
import com.example.mediareelzsv3v2.Database.COL_SecurityProfileId
import com.example.mediareelzsv3v2.Database.COL_USERNAME
import com.example.mediareelzsv3v2.Database.COL_USER_ID
import com.example.mediareelzsv3v2.Database.DataBaseHandler
import com.example.mediareelzsv3v2.Database.SECURITY_PROFILE_TABLE_NAME
import com.example.mediareelzsv3v2.Database.USERS_TABLE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserCreate(context: Context) {


    var dbHandler: DataBaseHandler = DataBaseHandler(context)
    // users table

    //Edge case testing passed 3/19/23 11:21AM//


    suspend fun verifyUniqueUserName(userName: String): Boolean {
        var uniqueUserName = false
       withContext(Dispatchers.IO) {

           val db = dbHandler.readableDatabase
           val sql = "SELECT * FROM $USERS_TABLE_NAME Where userName = ?"
           val res = db.rawQuery(sql, arrayOf(userName))

           if (res.moveToFirst()) {

           uniqueUserName = false

           } else {

           uniqueUserName = true


           }
           res.close()
           db.close()

       }
        return uniqueUserName
    }

    //Edge case testing passed 3/19/23 11:22AM//
    // use a bridge method in main activity//
    suspend fun addUser(
        userName: String, hash: String, securityQuestion1: String, securityQuestion1Answer: String,
        securityQuestion2: String, securityQuestion2Answer: String, securityQuestion3: String,
        securityQuestion3Answer: String
    ): Boolean {
        var userSecurityProfileSuccessfullyCreated = false
        withContext(Dispatchers.IO) {
            try {


                val db = dbHandler.writableDatabase
                val values = ContentValues().apply {

                    put(COL_USERNAME, userName)
                    put(COL_HASH, hash)

                }
                val newUserId: Int = db.insert(USERS_TABLE_NAME, null, values).toInt()
                db.close()
                userSecurityProfileSuccessfullyCreated = createUserSecurityProfile(
                    newUserId,
                    securityQuestion1,
                    securityQuestion1Answer,
                    securityQuestion2,
                    securityQuestion2Answer,
                    securityQuestion3,
                    securityQuestion3Answer
                )

            } catch (e: Exception) {
                e.stackTrace

            }

        }
        return userSecurityProfileSuccessfullyCreated
    }


    private suspend fun createUserSecurityProfile(
        newUserId: Int,
        securityQuestion1: String,
        securityQuestion1Answer: String,
        securityQuestion2: String,
        securityQuestion2Answer: String,
        securityQuestion3: String,
        securityQuestion3Answer: String
    ): Boolean {

        var userSecurityProfileUpdated = false
        withContext(Dispatchers.IO) {
            try {
                val db = dbHandler.writableDatabase
                val values = ContentValues().apply {

                    put(COL_SECURITY_QUESTION1, securityQuestion1)
                    put(COL_SECURITY_QUESTION1ANSWER, securityQuestion1Answer)
                    put(COL_SECURITY_QUESTION2, securityQuestion2)
                    put(COL_SECURITY_QUESTION2ANSWER, securityQuestion2Answer)
                    put(COL_SECURITY_QUESTION3, securityQuestion3)
                    put(COL_SECURITY_QUESTION3ANSWER, securityQuestion3Answer)

                }

                val newSecurityProfileId: Int =
                    db.insert(SECURITY_PROFILE_TABLE_NAME, null, values).toInt()
                db.close()

                userSecurityProfileUpdated =
                    updateUserSecurityProfileId(newUserId, newSecurityProfileId)

            } catch (E: Exception) {
                E.stackTrace


            }


        }

        return  userSecurityProfileUpdated

    }

    private suspend fun updateUserSecurityProfileId(newUserId: Int, newSecurityProfileId: Int): Boolean {

        var userSecurityProfileUpdateStatus = false
        withContext(Dispatchers.IO){
        try {


            val db = dbHandler.readableDatabase
            val values = ContentValues().apply {
                put(COL_SecurityProfileId, newSecurityProfileId)
                put(COL_USER_ID, newUserId)

            }

            val rowsAffectedForUsersSecurityIdUpdate: Int =
                db.update(USERS_TABLE_NAME, values, "userId = ?", arrayOf(newUserId.toString()))
            println(rowsAffectedForUsersSecurityIdUpdate)

            db.close()




    }catch (e:Exception){
        e.stackTrace
        userSecurityProfileUpdateStatus = false

    }
        userSecurityProfileUpdateStatus = true
    }

        return  userSecurityProfileUpdateStatus
  }

}