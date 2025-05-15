package com.example.mediareelzsv3v2.Database.UserOperations

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import com.example.mediareelzsv3v2.Database.DataBaseHandler
import com.example.mediareelzsv3v2.Database.REELZS_TABLE_NAME
import com.example.mediareelzsv3v2.Database.SECURITY_PROFILE_TABLE_NAME
import com.example.mediareelzsv3v2.Database.UNIQUE_REEL_ATTRIBUTES_TABLE_NAME
import com.example.mediareelzsv3v2.Database.USERS_TABLE_NAME
import com.example.mediareelzsv3v2.Database.USER_REELS_TABLE_NAME
import com.example.mediareelzsv3v2.Database.View_REEL_VIEW
import com.example.mediareelzsv3v2.Objects.User

class UserDelete(val context: Context) {

    var dbHandler: DataBaseHandler = DataBaseHandler(context)





    fun getUserReelIdsForUserDeletion():Boolean{

        val reelIdsToCheckForDeletion: ArrayList<Int> = ArrayList()
        val userReelIdsToDelete: ArrayList<Int> = ArrayList()
        var userReelIdIndex:Int
        var userReelId: Int
        var reelIdIndex:Int
        var reelId:Int

        val db = dbHandler.readableDatabase
        val userId = User.getUserId()
        val sql = "SELECT * FROM $View_REEL_VIEW Where userId = ?"
        db.rawQuery(sql, arrayOf(userId.toString())).use {
            if (it.moveToFirst()) {
                do {
                     reelIdIndex = it.getColumnIndex("reelId")
                     reelId = if (reelIdIndex != -1)it.getInt(reelIdIndex)else 0
                     userReelIdIndex = it.getColumnIndex("userReelId")
                     userReelId = if (userReelIdIndex != -1)it.getInt(userReelIdIndex)else 0

                    reelIdsToCheckForDeletion.add(reelId)
                    userReelIdsToDelete.add(userReelId)



                } while (it.moveToNext())

            }
        }
        db.close()


        removeUniqueReelAttributeRecords(userReelIdsToDelete)
        checkForDuplicateOfReelIds(reelIdsToCheckForDeletion)
        val userReelsDeletedSuccessfully:Boolean = removeUserReelRecords()
        val userSecurityProfileDeletedSuccessfully:Boolean = removeUserSecurityProfile()
        val userDeletedSuccessfully:Boolean = deleteUser()

        if (userReelsDeletedSuccessfully || userSecurityProfileDeletedSuccessfully || userDeletedSuccessfully){

           return true



        }else{
            return  false

        }





    }





    private fun  removeUniqueReelAttributeRecords(userReelIdsToDelete: ArrayList<Int>) {

        val db = dbHandler.writableDatabase
        for (userReelId in userReelIdsToDelete ){

            db.delete(UNIQUE_REEL_ATTRIBUTES_TABLE_NAME, "userReelId = ?", arrayOf(userReelId.toString()))


        }
    db.close()

    }


    private fun checkForDuplicateOfReelIds(reelIdsToCheckForDeletion: ArrayList<Int>) {

        val db = dbHandler.readableDatabase
        for (id in reelIdsToCheckForDeletion){


            val sql = "SELECT COUNT(*) FROM $USER_REELS_TABLE_NAME  Where reelId = ?"
            val cursor: Cursor = db.rawQuery(sql, arrayOf(id.toString()))
            if (cursor.count == 1) {
                cursor.close()

                deleteReel(id)

            } else if (cursor.count > 1) {
                cursor.close()


            }

        }
        db.close()



    }

    private fun removeUserReelRecords(): Boolean {

        val db = dbHandler.writableDatabase
        val userId = User.getUserId()
        val  numberOfUserReelIdsDeleted:Int = db.delete(USER_REELS_TABLE_NAME, "userId = ?", arrayOf(userId.toString()))
        db.close()
        if (numberOfUserReelIdsDeleted < 1){

            return  false

        }else  {
            return true
        }


    }


    @SuppressLint("SuspiciousIndentation")
    fun deleteReel(reelId: Int) {

        val db = dbHandler.writableDatabase
        db.delete(REELZS_TABLE_NAME, "reelId = ?", arrayOf(reelId.toString()))
    }


    fun getSecurityProfileId(): Int {

        val db = dbHandler.readableDatabase

        val sql =
            "SELECT securityProfileId FROM $USERS_TABLE_NAME Where userId = ? "
        var securityProfileIdIndex:Int
        var securityProfileId = 0
        db.rawQuery(sql, arrayOf(User.getUserId().toString())).use {

            if (it.moveToFirst()) {
                securityProfileIdIndex = it.getColumnIndex("securityProfileId")
                securityProfileId = if (securityProfileIdIndex != -1)it.getInt(securityProfileIdIndex)else 0





            }
            db.close()

        }

        return securityProfileId
    }


    private fun removeUserSecurityProfile(): Boolean {
        val db = dbHandler.writableDatabase

        val securityProfileId = getSecurityProfileId()

        val userSecurityProfileDeleted:Int = db.delete(SECURITY_PROFILE_TABLE_NAME, "securityProfileId = ?", arrayOf(securityProfileId.toString()))

        if (userSecurityProfileDeleted!= 1){

            return  false

        }else  {
            return true
        }

    }


    @SuppressLint("SuspiciousIndentation")
    private fun deleteUser(): Boolean {
        val db = dbHandler.writableDatabase

        val userId = User.getUserId()

      val usersDeleted:Int =  db.delete(USERS_TABLE_NAME, "userId = ?", arrayOf(userId.toString()))
db.close()
        if (usersDeleted != 1){

            return  false

        }else  {
            return true
        }



    }



}