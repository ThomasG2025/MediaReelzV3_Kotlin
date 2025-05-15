package com.example.mediareelzsv3v2.Database.ReelOperations

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import com.example.mediareelzsv3v2.Database.DataBaseHandler
import com.example.mediareelzsv3v2.Database.REELZS_TABLE_NAME
import com.example.mediareelzsv3v2.Database.UNIQUE_REEL_ATTRIBUTES_TABLE_NAME
import com.example.mediareelzsv3v2.Database.USER_REELS_TABLE_NAME
import com.example.mediareelzsv3v2.Objects.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReelDelete(context: Context) {

    var dbHandler: DataBaseHandler = DataBaseHandler(context)

    suspend fun checkNumberOfUserReelIdsWithReelId(reelId: Int) {


        val db = dbHandler.readableDatabase
        val sql = "SELECT * FROM $USER_REELS_TABLE_NAME  Where reelId = ?"
        val cursor: Cursor = db.rawQuery(sql, arrayOf(reelId.toString()))
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        reelIdCountProcessor(cursorCount,reelId)


    }

    private suspend fun reelIdCountProcessor(count: Int, reelId: Int) {
        val userReelId = getUserReelId2(reelId)

        if (count == 0) {
            println("no record exists")
        }
         if (count == 1) {
             removeUniqueReelAttributeRecord(userReelId)
             removeUserReelRecord(userReelId)
              deleteReel(reelId)
             println(" hi A")
         }
         if (count > 1){

            removeUniqueReelAttributeRecord(userReelId)
            removeUserReelRecord(userReelId)

            println("hi B")
        }


    }


    @SuppressLint("SuspiciousIndentation")
    suspend fun deleteReel(reelId: Int) {

        val db = dbHandler.writableDatabase
        withContext(Dispatchers.IO) {
            db.delete(REELZS_TABLE_NAME, "reelId = ?", arrayOf(reelId.toString()))
        }
        db.close()
    }

    private fun removeUniqueReelAttributeRecord(userReelId: Int) {

        val db = dbHandler.writableDatabase

        db.delete(
            UNIQUE_REEL_ATTRIBUTES_TABLE_NAME,
            "userReelId = ?",
            arrayOf(userReelId.toString())
        )
        db.close()


    }



    private fun removeUserReelRecord(userReelId: Int) {

        val db = dbHandler.writableDatabase
        db.delete(USER_REELS_TABLE_NAME, "userReelId = ?", arrayOf(userReelId.toString()))
        db.close()

    }

    @SuppressLint( "SuspiciousIndentation")
    fun getUserReelId2(reelId: Int): Int {
        val db = dbHandler.readableDatabase

        val sql =
            "SELECT userReelId FROM $USER_REELS_TABLE_NAME Where userId = ? AND reelId = ?"
        var userReelIdIndex:Int
        var userReelId: Int

        db.rawQuery(sql, arrayOf(User.getUserId().toString(), reelId.toString())).use {

            if (it.moveToFirst()) {

                userReelIdIndex = it.getColumnIndex("userReelId")
                userReelId = if (userReelIdIndex != -1)it.getInt(userReelIdIndex)else 0


                return userReelId


            }

        }
        db.close()

        return 0
    }
}