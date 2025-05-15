package com.example.mediareelzsv3v2.Database.UserOperations

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import com.example.mediareelzsv3v2.Database.COL_HASH
import com.example.mediareelzsv3v2.Database.COL_USERNAME
import com.example.mediareelzsv3v2.Database.DataBaseHandler
import com.example.mediareelzsv3v2.Database.USERS_TABLE_NAME
import com.example.mediareelzsv3v2.Objects.User

class UserUpdate(context: Context) {

    var dbHandler: DataBaseHandler = DataBaseHandler(context)



    fun getStoredHashByUserId(userId: Int): String {

        val db = dbHandler.readableDatabase
        val hashIndex:Int
        val sql = "SELECT hash FROM $USERS_TABLE_NAME Where userId = ?"
        db.rawQuery(sql, arrayOf(userId.toString())).use {
            if (it.moveToFirst()) {
                val result = User()
                hashIndex = it.getColumnIndex("hash")
                result.hash = if (hashIndex != -1)it.getString(hashIndex)else ""
                return result.hash

            }


        }

        return "error retrieving stored hash for user login"

    }

    @SuppressLint("SuspiciousIndentation")
    fun updateUserHashByUserId(hash: String, userId: Int): Boolean {
        val successfulUserHashByIdDatabaseUpdate:Boolean
        val db = dbHandler.readableDatabase
        val values = ContentValues().apply {
            put(COL_HASH, hash)
        }
        val  userRowsUpdated =  db.update(USERS_TABLE_NAME, values, "userId = ?", arrayOf(userId.toString()))

        successfulUserHashByIdDatabaseUpdate = if (userRowsUpdated == 1 ){
            true
        }else{
            false
        }
        return successfulUserHashByIdDatabaseUpdate

    }

    fun updateUserHashByUserName(hash: String): Boolean {
        var success = false
        val db = dbHandler.readableDatabase

        val userName = User.getUserName()
        val values = ContentValues().apply {
            put(COL_HASH, hash)
            put(COL_USERNAME, userName)
        }
        println(values.toString())
        val rowsUpdated = db.update(USERS_TABLE_NAME, values, "userName = ?", arrayOf(userName))
        db.close()
        if (rowsUpdated >= 1) {
            success = true
        }

        return success


    }

}