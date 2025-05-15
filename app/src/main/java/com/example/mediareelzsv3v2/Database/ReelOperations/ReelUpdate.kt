package com.example.mediareelzsv3v2.Database.ReelOperations


import android.content.ContentValues
import android.content.Context
import com.example.mediareelzsv3v2.Database.COL_REEL_ARTIST
import com.example.mediareelzsv3v2.Database.COL_REEL_DIGITALLY_ARCHIVED_STATUS
import com.example.mediareelzsv3v2.Database.COL_REEL_GENRE
import com.example.mediareelzsv3v2.Database.COL_REEL_ID
import com.example.mediareelzsv3v2.Database.COL_REEL_IS_ORIGINAL_STATUS
import com.example.mediareelzsv3v2.Database.COL_REEL_NAME
import com.example.mediareelzsv3v2.Database.COL_REEL_RATING
import com.example.mediareelzsv3v2.Database.COL_REEL_TYPE
import com.example.mediareelzsv3v2.Database.COL_REEL_YEAR
import com.example.mediareelzsv3v2.Database.Col_Note
import com.example.mediareelzsv3v2.Database.DataBaseHandler
import com.example.mediareelzsv3v2.Database.REELZS_TABLE_NAME
import com.example.mediareelzsv3v2.Database.UNIQUE_REEL_ATTRIBUTES_TABLE_NAME
import com.example.mediareelzsv3v2.Database.USER_REELS_TABLE_NAME
import com.example.mediareelzsv3v2.Objects.User

class ReelUpdate(context: Context) {

    var dbHandler: DataBaseHandler = DataBaseHandler(context)




    fun updateReel(
        reelId: Int, reelName: String, reelArtist: String, reelYear: Int, reelType: String,
        reelGenre: String, reelRating: String, reelIsOriginalStatus: Int, reelNotes: String,
         reelLocationName: String,
        selectedArchivedStatus: Int
    ) {
        val db = dbHandler.writableDatabase



        val values = ContentValues().apply {
            put(COL_REEL_NAME, reelName)
            put(COL_REEL_ARTIST, reelArtist)
            put(COL_REEL_YEAR, reelYear)
            put(COL_REEL_TYPE, reelType)
            put(COL_REEL_GENRE, reelGenre)
            put(COL_REEL_RATING, reelRating)
            put(COL_REEL_IS_ORIGINAL_STATUS, reelIsOriginalStatus)
            put(COL_REEL_ID, reelId)
        }

        val rowsAffectedForReelzsTableUpdate: Int =
            db.update(REELZS_TABLE_NAME, values, "reelId = ? ", arrayOf(reelId.toString()))
        println(rowsAffectedForReelzsTableUpdate)
        db.close()

        val userReelId:Int = getUserReelId2(reelId)

        updateUniqueReelAttributesRecord(
            userReelId,
            reelNotes,
            selectedArchivedStatus
        )


    }


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

        return 0
    }


    private fun  updateUniqueReelAttributesRecord(
        userReelId: Int,
        reelNotes: String,
        selectedArchivedStatus: Int
    ) {

        val db = dbHandler.writableDatabase

        val values = ContentValues().apply {
            put(Col_Note, reelNotes)
            put(COL_REEL_DIGITALLY_ARCHIVED_STATUS, selectedArchivedStatus)


        }

        db.update(
            UNIQUE_REEL_ATTRIBUTES_TABLE_NAME, values, "userReelId = ?",
            arrayOf(userReelId.toString())
        )
        db.close()

    }



}