package com.example.mediareelzsv3v2.Database.UserOperations


import android.content.Context
import com.example.mediareelzsv3v2.Database.DataBaseHandler
import com.example.mediareelzsv3v2.Database.USERS_TABLE_NAME
import com.example.mediareelzsv3v2.Objects.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserLogin(var context: Context) {

    var dbHandler: DataBaseHandler = DataBaseHandler(context)


    suspend fun getStoredHashByUserName(userName: String): String {
        val db = dbHandler.readableDatabase
        println(userName)
        val hashIndex:Int
        var userHash = ""

        withContext(Dispatchers.IO) {


            val sql = "SELECT hash FROM $USERS_TABLE_NAME Where userName = ?"
            db.rawQuery(sql, arrayOf(userName)).use {


                if (it.moveToFirst()) {
                    val result = User()
                    hashIndex = it.getColumnIndex("hash")
                    result.hash = if (hashIndex != -1) it.getString(hashIndex) else ""
                    println(result.hash)
                    userHash = result.hash
                }


            }

        }
        db.close()
        return userHash


    }


    suspend fun setVerifiedUserId(userName: String, hash: String) {

        val db = dbHandler.readableDatabase
        val userIdIndex:Int

        withContext(Dispatchers.IO){
        val sql = "SELECT userId FROM $USERS_TABLE_NAME Where userName = ? AND hash = ?"
        db.rawQuery(sql, arrayOf(userName, hash)).use {
            if (it.moveToFirst()) {
                val result = User()
                userIdIndex = it.getColumnIndex("userId")
                result.userId = if (userIdIndex != -1)it.getInt(userIdIndex)else 0
                println(result.userId)
                User.setUserId(result.userId)

            }


        }

        db.close()

    }



}
}