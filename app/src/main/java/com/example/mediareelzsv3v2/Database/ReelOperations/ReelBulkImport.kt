package com.example.mediareelzsv3v2.Database.ReelOperations

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mediareelzsv3v2.Objects.BulkImportStatsHolder
import com.example.mediareelzsv3v2.Database.COL_REEL_ARTIST
import com.example.mediareelzsv3v2.Database.COL_REEL_DIGITALLY_ARCHIVED_STATUS
import com.example.mediareelzsv3v2.Database.COL_REEL_DISPLAY_ID
import com.example.mediareelzsv3v2.Database.COL_REEL_GENRE
import com.example.mediareelzsv3v2.Database.COL_REEL_ID
import com.example.mediareelzsv3v2.Database.COL_REEL_IS_ORIGINAL_STATUS
import com.example.mediareelzsv3v2.Database.COL_REEL_NAME
import com.example.mediareelzsv3v2.Database.COL_REEL_RATING
import com.example.mediareelzsv3v2.Database.COL_REEL_TYPE
import com.example.mediareelzsv3v2.Database.COL_REEL_YEAR
import com.example.mediareelzsv3v2.Database.COL_USER_ID
import com.example.mediareelzsv3v2.Database.Col_Note
import com.example.mediareelzsv3v2.Database.Col_USER_REEL_ID
import com.example.mediareelzsv3v2.Database.DataBaseHandler
import com.example.mediareelzsv3v2.Database.MovieDisplayIdReelIdJointTable
import com.example.mediareelzsv3v2.Database.MusicDisplayIdReelIdJointTable
import com.example.mediareelzsv3v2.Database.MusicShelfDisplayIdJointTable
import com.example.mediareelzsv3v2.Database.OtherDisplayIdReelIdJointTable
import com.example.mediareelzsv3v2.Database.REELZS_TABLE_NAME
import com.example.mediareelzsv3v2.Database.UNIQUE_REEL_ATTRIBUTES_TABLE_NAME
import com.example.mediareelzsv3v2.Database.USER_REELS_TABLE_NAME
import com.example.mediareelzsv3v2.Database.UserReelIdDisplayIdTable
import com.example.mediareelzsv3v2.Objects.Reel
import com.example.mediareelzsv3v2.Objects.ReelDisplayIdPrefixAssigner
import com.example.mediareelzsv3v2.Objects.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ReelBulkImport(context: Context) {

    var dbHandler: DataBaseHandler = DataBaseHandler(context)
    var displayIdPreFixer: ReelDisplayIdPrefixAssigner = ReelDisplayIdPrefixAssigner()







    @SuppressLint("SuspiciousIndentation")
    suspend fun checkForDuplicateReelzs(
        bulkReelList: ArrayList<Reel>,

        ): Boolean {
        val db = dbHandler.readableDatabase
        val bulkReelListForUserReelDuplicateCheck: ArrayList<Reel> = ArrayList()
        val approvedBulkReelzsList: ArrayList<Reel> = ArrayList()
        val numberOfBulkReelzsInsertApproved: Int = approvedBulkReelzsList.size
        val numberOfBulkReelzsInsertNotApproved: Int = bulkReelListForUserReelDuplicateCheck.size
        var bulkUserReelIdIsNotDuplicateStatus = false
        var addBulkReelzsStatus = false
        withContext(Dispatchers.IO) {
        for (reel in bulkReelList) {
            val reelName = reel.getReelName()
            val reelArtist = reel.getReelArtist()
            val reelYear = reel.getReelYear()
            val reelType = reel.getReelType()
            val sql =
                "SELECT * FROM $REELZS_TABLE_NAME Where reelName = ? AND reelArtist = ? AND reelYear = ? AND reelType = ?"
            db.rawQuery(sql, arrayOf(reelName, reelArtist, reelYear.toString(), reelType)).use {

                if (it.moveToFirst()) {
                    bulkReelListForUserReelDuplicateCheck.addAll(
                        listOf(
                            Reel(
                                it.getColumnIndex("reelId"),
                                reel.getReelName(),
                                reel.getReelArtist(),
                                reel.getReelYear(),
                                reel.getReelType(),
                                reel.getReelGenre(),
                                reel.getReelRating(),
                                reel.getSelectedOriginalStatus(),
                                reel.getReelNotes(),
                                reel.getSelectedArchivedStatus()

                            )
                        )

                    )

                }
                if (!it.moveToFirst()) {

                    approvedBulkReelzsList.addAll(
                        listOf(
                            Reel(
                                reel.getReelName(),
                                reel.getReelArtist(),
                                reel.getReelYear(),
                                reel.getReelType(),
                                reel.getReelGenre(),
                                reel.getReelRating(),
                                reel.getSelectedOriginalStatus(),
                                reel.getReelNotes(),
                                reel.getSelectedArchivedStatus()

                            )
                        )
                    )

                }


            }


        }

        db.close()
        println("approved bulk reelzs after reel duplicate check: " + approvedBulkReelzsList.size)
        println("duplicates : " + bulkReelListForUserReelDuplicateCheck.size)
    }
    bulkUserReelIdIsNotDuplicateStatus =    verifyBulkUserReelIdIsNotDuplicate(bulkReelListForUserReelDuplicateCheck)
      addBulkReelzsStatus =  addBulkReelzs(approvedBulkReelzsList, numberOfBulkReelzsInsertApproved,
            numberOfBulkReelzsInsertNotApproved)

        if (bulkUserReelIdIsNotDuplicateStatus&&addBulkReelzsStatus){
            return true
        }else{
            return false
        }

            }



    suspend fun  verifyBulkUserReelIdIsNotDuplicate(duplicateBulkReelLocations: ArrayList<Reel>): Boolean {

        val duplicateBulkReelListForUserReelDuplicateCheck2 = ArrayList<Reel>()
        val db = dbHandler.readableDatabase
        var bulkUserReelIdAddStatus = false
        withContext(Dispatchers.IO) {
        for (reel in duplicateBulkReelLocations) {


            val sql =
                "SELECT userReelId FROM $USER_REELS_TABLE_NAME Where userId = ? AND reelId = ? "
            val cursor: Cursor =
                db.rawQuery(sql, arrayOf(User.getUserId().toString(), reel.getReelId().toString()))

            if (!cursor.moveToFirst()) {

                duplicateBulkReelListForUserReelDuplicateCheck2.add(reel)


            }
            cursor.close()


        }
        db.close()
    }
        bulkUserReelIdAddStatus = addBulkUserReelIdRecord(duplicateBulkReelListForUserReelDuplicateCheck2)

        if (bulkUserReelIdAddStatus){
            return true
        }else{
            return false
        }


    }

    private suspend fun addBulkReelzs(
        approvedBulkReelLocations: ArrayList<Reel>,
        numberOfBulkReelzsInsertApproved: Int,
        numberOfBulkReelzsInsertNotApproved: Int
    ): Boolean {

        BulkImportStatsHolder.setNumberSuccessfulReelzsImport(numberOfBulkReelzsInsertApproved)
        BulkImportStatsHolder.setNumberOfDuplicateReelzsImport(numberOfBulkReelzsInsertNotApproved)
        val bulkReelList2: ArrayList<Reel> = ArrayList()
        var bulkUserReelIdInsert2Status = false
        val db = dbHandler.writableDatabase
        withContext(Dispatchers.IO) {
            for (reel in approvedBulkReelLocations) {
                val values = ContentValues().apply {
                    put(COL_REEL_NAME, reel.getReelName())
                    put(COL_REEL_ARTIST, reel.getReelArtist())
                    put(COL_REEL_YEAR, reel.getReelYear())
                    put(COL_REEL_TYPE, reel.getReelType())
                    put(COL_REEL_GENRE, reel.getReelGenre())
                    put(COL_REEL_RATING, reel.getReelRating())
                    put(COL_REEL_IS_ORIGINAL_STATUS, reel.getSelectedOriginalStatus())
                }

                val newBulkReelId: Int = db.insert(REELZS_TABLE_NAME, null, values).toInt()

                bulkReelList2.addAll(
                    listOf(
                        Reel(
                            newBulkReelId,
                            reel.getReelName(),
                            reel.getReelArtist(),
                            reel.getReelYear(),
                            reel.getReelType(),
                            reel.getReelGenre(),
                            reel.getReelRating(),
                            reel.getSelectedOriginalStatus(),
                            reel.getReelNotes(),
                            reel.getSelectedArchivedStatus()

                        )
                    )
                )

            }


            db.close()
        }
       bulkUserReelIdInsert2Status = addBulkUserReelIdRecord2(bulkReelList2)

    if (bulkUserReelIdInsert2Status){
        return true
    }else{
        return false
    }

    }



    suspend fun addBulkUserReelIdRecord(duplicateBulkReelListForUserReelDuplicateCheck2: ArrayList<Reel>): Boolean {

        val db = dbHandler.writableDatabase
        var bulkUniqueReelAttributeInsertStatus = false
        withContext(Dispatchers.IO) {
            for (reel in duplicateBulkReelListForUserReelDuplicateCheck2.iterator()) {

                val values = ContentValues().apply {
                    put(COL_USER_ID, User.getUserId())
                    put(COL_REEL_ID, reel.getReelId())
                }

                db.insert(USER_REELS_TABLE_NAME, null, values)

            }

            db.close()
        }

        bulkUniqueReelAttributeInsertStatus = insertBulkUniqueReelAttributeRecords(duplicateBulkReelListForUserReelDuplicateCheck2)


        if (bulkUniqueReelAttributeInsertStatus){
            return true
        }else {
            return false
        }

    }

    private suspend fun addBulkUserReelIdRecord2(bulkReelList2: ArrayList<Reel>): Boolean {

        val db = dbHandler.writableDatabase
        val bulkUniqueReelAttributeInsertStatus: Boolean
        val bulkReelDisplayIdInsertStatus: Boolean


        for (reel in bulkReelList2) {
            val values = ContentValues().apply {
                put(COL_USER_ID, User.getUserId())
                put(COL_REEL_ID, reel.getReelId())
            }

            db.insert(USER_REELS_TABLE_NAME, null, values)

        }
        db.close()

        bulkUniqueReelAttributeInsertStatus =  insertBulkUniqueReelAttributeRecords(bulkReelList2)
        bulkReelDisplayIdInsertStatus =  determineDisplayIdType(bulkReelList2)

        if (bulkUniqueReelAttributeInsertStatus&&bulkReelDisplayIdInsertStatus){
            return true
        }else {
            return false
        }
    }




    private suspend fun insertBulkUniqueReelAttributeRecords(duplicateBulkReelListForUserReelDuplicateCheck2: ArrayList<Reel>): Boolean {


        val db = dbHandler.writableDatabase
        var bulkReelUniqueReelAttributesRecordsInsertStatus = true
        withContext(Dispatchers.IO) {
            try {
                for (reel in duplicateBulkReelListForUserReelDuplicateCheck2) {

                    val userReelIdForInsert = getUserReelId2(reel.getReelId())
                    val reelNotes = reel.getReelNotes()
                    val selectedArchivedStatus = reel.getSelectedArchivedStatus()

                    val values = ContentValues().apply {

                        put(Col_USER_REEL_ID, userReelIdForInsert)
                        put(Col_Note, reelNotes)
                        put(COL_REEL_DIGITALLY_ARCHIVED_STATUS, selectedArchivedStatus)

                    }


                    db.insert(UNIQUE_REEL_ATTRIBUTES_TABLE_NAME, null, values)


                }

            } catch (e: Exception) {

                bulkReelUniqueReelAttributesRecordsInsertStatus = false


            }
        }
        db.close()

        return  bulkReelUniqueReelAttributesRecordsInsertStatus







    }



    suspend fun determineDisplayIdType( bulkReelList2: ArrayList<Reel>): Boolean {
        val moviePrefix = displayIdPreFixer.getMovieDisplayIdPrefix()
        val musicPrefix = displayIdPreFixer.getMusicDisplayIdPrefix()
        val musicShelfPrefix = displayIdPreFixer.getMusicShelfDisplayIdPrefix()
        val otherPrefix = displayIdPreFixer.getOtherDisplayIdPrefix()
        var movieDisplayIdInserted = true
        var musicDisplayIdInserted = true
        var musicShelfDisplayId = true
        var otherDisplayIdInserted = true

    withContext(Dispatchers.IO) {
        try {
            for (reel in bulkReelList2) {
                if (reel.getReelType().equals("dvd", ignoreCase = true)
                    || reel.getReelType().equals("blu ray", ignoreCase = true) ||
                    reel.getReelType().equals("4K", ignoreCase = true)
                ) {
                    movieDisplayIdInserted = movieDisplayIdMethod(reel, moviePrefix)
                } else if (reel.getReelType().equals("cd", ignoreCase = true) ||
                    reel.getReelType().equals("vinyl", ignoreCase = true)
                ) {

                    musicDisplayIdInserted = musicDisplayIdMethod(reel, musicPrefix)

                } else if (reel.getReelType().equals("scd", ignoreCase = true)) {

                    musicShelfDisplayId = musicShelfDisplayIdMethod(reel, musicShelfPrefix)
                } else {

                    otherDisplayIdInserted = otherDisplayIdMethod(reel, otherPrefix)

                }


            }
        } catch (e: Exception) {

            println("error on line 537 in reel bulk import")

        }


    }


        return movieDisplayIdInserted||musicDisplayIdInserted
                ||musicShelfDisplayId||otherDisplayIdInserted


    }

    suspend fun movieDisplayIdMethod(reel: Reel, moviePrefix: String): Boolean {

        var newMovieDisplayId = 0
        val reelDisplayIdInserted: Boolean
        val userReelId = getUserReelId2(reel.getReelId())
        val db = dbHandler.writableDatabase
        withContext(Dispatchers.IO) {
            try {


                val values = ContentValues().apply {

                    put(Col_USER_REEL_ID, userReelId)


                }
                newMovieDisplayId = db.insert(MovieDisplayIdReelIdJointTable, null, values).toInt()
                db.close()


            } catch (e: Exception) {
                println("Error line 579  in reel bulk import ")
            }

        }
        val fullMovieDisplayId = moviePrefix + newMovieDisplayId
        reelDisplayIdInserted =  insertUserReelDisplayId(userReelId, fullMovieDisplayId)

       if (reelDisplayIdInserted){
           return true
       }else{
           return false
       }



    }

    suspend fun musicDisplayIdMethod(reel: Reel, musicPrefix: String): Boolean {


        var newMusicDisplayId = 0
        var reelDisplayIdInserted = false
        val userReelId = getUserReelId2(reel.getReelId())
        val db = dbHandler.writableDatabase

withContext(Dispatchers.IO) {
    try {


        val values = ContentValues().apply {

            put(Col_USER_REEL_ID, userReelId)


        }
        newMusicDisplayId = db.insert(MusicDisplayIdReelIdJointTable, null, values).toInt()
        db.close()


    } catch (e: Exception) {
        println("error on line 601 in reel bulk import")
    }

}

        val fullMusicDisplayId = musicPrefix + newMusicDisplayId
        reelDisplayIdInserted =  insertUserReelDisplayId(userReelId, fullMusicDisplayId)

        if (reelDisplayIdInserted){
            return true
        }else{
            return false
        }


    }

    suspend fun musicShelfDisplayIdMethod(reel: Reel, musicShelfPrefix: String): Boolean {

        var newMusicShelfDisplayId = 0
        var reelDisplayIdInserted = false
        val userReelId = getUserReelId2(reel.getReelId())
        val db = dbHandler.writableDatabase
        withContext(Dispatchers.IO) {
            try {


                val values = ContentValues().apply {

                    put(Col_USER_REEL_ID, userReelId)


                }
                newMusicShelfDisplayId =
                    db.insert(MusicShelfDisplayIdJointTable, null, values).toInt()
                db.close()


            } catch (e: Exception) {
                println("error on line 601 in reel bulk import")
            }

        }


        val fullMusicShelfDisplayId = musicShelfPrefix + newMusicShelfDisplayId
        reelDisplayIdInserted =  insertUserReelDisplayId(userReelId, fullMusicShelfDisplayId)

        if (reelDisplayIdInserted){
            return true
        }else{
            return false
        }


    }

    suspend fun otherDisplayIdMethod(reel: Reel, otherPrefix: String): Boolean {

        var newOtherDisplayId = 0
        var reelDisplayIdInserted = false
        val userReelId = getUserReelId2(reel.getReelId())
        val db = dbHandler.writableDatabase
        withContext(Dispatchers.IO) {
            try {


                val userReelIdForInsert = getUserReelId2(reel.getReelId())
                val values = ContentValues().apply {

                    put(Col_USER_REEL_ID, userReelIdForInsert)


                }
                newOtherDisplayId = db.insert(OtherDisplayIdReelIdJointTable, null, values).toInt()
                db.close()


            } catch (e: Exception) {

                println("Problem in bulk reel insert line 635")
            }
        }

        val fullOtherDisplayId = otherPrefix + newOtherDisplayId
        reelDisplayIdInserted =  insertUserReelDisplayId(userReelId, fullOtherDisplayId)

        if (reelDisplayIdInserted){
            return true
        }else{
            return false
        }

    }

    suspend fun insertUserReelDisplayId(userReelId:Int,fullReelDisplayId:String): Boolean {

        val db = dbHandler.writableDatabase
        var newUserReelDisplayIdInserted = 0
        withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put(Col_USER_REEL_ID, userReelId)
                put(COL_REEL_DISPLAY_ID, fullReelDisplayId)
            }

            newUserReelDisplayIdInserted = db.insert(UserReelIdDisplayIdTable, null, values).toInt()
            db.close()
        }
    if (newUserReelDisplayIdInserted != 0){
        return true
    }else{
        return false
    }

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




