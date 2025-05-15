package com.example.mediareelzsv3v2.Database.ReelOperations

import android.content.ContentValues
import android.content.Context
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

class ReelSingleInsert(context: Context) {

    var dbHandler: DataBaseHandler = DataBaseHandler(context)
    var displayIdPreFixer:ReelDisplayIdPrefixAssigner = ReelDisplayIdPrefixAssigner()






    suspend fun verifyReelIsNotDuplicate(
        reelName: String, reelArtist: String, reelYear: Int, reelType: String,
        reelGenre: String, reelRating: String, reelIsOriginalStatus: Int, reelNotes: String,
        selectedArchivedStatus: Int
    ): Boolean {

        println("single insert operation starting")
        var reelIdIndex:Int
        var reelId: Int
        var reelTypeIndex:String
        var singleInsertDone = false

        val db = dbHandler.readableDatabase

        withContext(Dispatchers.IO) {
            val sql =
                "SELECT reelId FROM $REELZS_TABLE_NAME Where reelName = ? AND reelArtist = ? AND reelYear = ? AND reelType = ?"
            db.rawQuery(sql, arrayOf(reelName, reelArtist, reelYear.toString(), reelType)).use {

                if (it.moveToFirst()) {
                    val result = Reel()
                    reelIdIndex = it.getColumnIndex("reelId")
                    reelTypeIndex = it.getColumnIndex("reelType").toString()
                    reelId = if (reelIdIndex != -1) it.getInt(reelIdIndex) else 0
                    result.setReelId(reelId)
                    result.getReelId()
                    result.setReelType(reelTypeIndex)
                    result.getReelType()
                    db.close()
                    val potentialDuplicateReelInsertionSuccess = verifyUserReelIdIsNotDuplicate(
                        result.getReelId(),
                        reelNotes,
                        selectedArchivedStatus
                    )
                    if (potentialDuplicateReelInsertionSuccess) {
                        singleInsertDone = true
                    }
                } else {

                    val reelInserted: Boolean = addReel(
                        reelName, reelArtist, reelYear, reelType,
                        reelGenre, reelRating, reelIsOriginalStatus, reelNotes,
                        selectedArchivedStatus
                    )

                    if (reelInserted) {
                        singleInsertDone = true
                    }
                }
            }
        }
        db.close()

        return singleInsertDone

    }

    private suspend fun verifyUserReelIdIsNotDuplicate(
        foundReelId: Int,
        reelNotes: String,
        selectedArchivedStatus: Int
    ): Boolean {

        var newUniqueReelAttributesIdCreated = false
        val db = dbHandler.readableDatabase
        withContext(Dispatchers.IO){
        val sql =
            "SELECT userReelId FROM $USER_REELS_TABLE_NAME Where userId = ? AND reelId = ?"
        db.rawQuery(sql, arrayOf(User.getUserId().toString(), foundReelId.toString())).use {

            if (it.moveToFirst()) {
                db.close()
                println("insert completed a")
            } else {

                val newUserReelId: Int = addUserReelIdRecord(foundReelId)
                newUniqueReelAttributesIdCreated = insertUniqueReelAttributesRecord(
                    newUserReelId,
                    reelNotes,
                    selectedArchivedStatus
                )




            }
        }
db.close()
        }

        return if (newUniqueReelAttributesIdCreated){
            true
        }else{
            false
        }

    }

    private suspend fun addReel(
        reelName: String, reelArtist: String, reelYear: Int, reelType: String,
        reelGenre: String, reelRating: String,
        reelIsOriginalStatus: Int, reelNotes: String,
        selectedArchivedStatus: Int
    ): Boolean {


        val newUniqueReelAttributesIdCreated: Boolean
        val newReelDisplayId:Boolean

        val db = dbHandler.writableDatabase
        withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put(COL_REEL_NAME, reelName)
                put(COL_REEL_ARTIST, reelArtist)
                put(COL_REEL_YEAR, reelYear)
                put(COL_REEL_TYPE, reelType)
                put(COL_REEL_GENRE, reelGenre)
                put(COL_REEL_RATING, reelRating)
                put(COL_REEL_IS_ORIGINAL_STATUS, reelIsOriginalStatus)

            }
            val newReelId = db.insert(REELZS_TABLE_NAME, null, values)

            db.close()

            val userReelId: Int = addUserReelIdRecord(newReelId.toInt())
            newUniqueReelAttributesIdCreated = insertUniqueReelAttributesRecord(
                userReelId,
                reelNotes,
                selectedArchivedStatus
            )
            println("userReelId = "+userReelId)
            println("reel type  = " +reelType)


           newReelDisplayId = determineDisplayIdPrefix(userReelId,reelType)

            println("display id insert status is " + newReelDisplayId)
            println("insert successful ")
        }
        return if (newUniqueReelAttributesIdCreated||newReelDisplayId){
            true
        }else{
            false
        }


    }



    private suspend fun insertUniqueReelAttributesRecord(
        userReelId: Int,
        reelNotes: String,
        selectedArchivedStatus: Int
    ):Boolean{
        var newUniqueReelAttributesId: Int = 0
        val db = dbHandler.writableDatabase

        withContext(Dispatchers.IO) {
            val values = ContentValues().apply {


                put(Col_USER_REEL_ID, userReelId)
                put(Col_Note, reelNotes)
                put(COL_REEL_DIGITALLY_ARCHIVED_STATUS, selectedArchivedStatus)

            }

             newUniqueReelAttributesId =
                db.insert(UNIQUE_REEL_ATTRIBUTES_TABLE_NAME, null, values).toInt()
            db.close()
        }
        return if (newUniqueReelAttributesId != 0){
            true
        }else{
            false
        }


    }



    private suspend fun addUserReelIdRecord(newReelId: Int): Int {
        var newUserReelId:Long
        val db = dbHandler.writableDatabase
        withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put(COL_USER_ID, User.getUserId())
                put(COL_REEL_ID, newReelId)
            }

            newUserReelId = db.insert(USER_REELS_TABLE_NAME, null, values)
            db.close()
        }
        return newUserReelId.toInt()



    }

   private suspend fun determineDisplayIdPrefix(userReelId: Int,reelType: String): Boolean {
       val moviePrefix = displayIdPreFixer.getMovieDisplayIdPrefix()
       val musicPrefix = displayIdPreFixer.getMusicDisplayIdPrefix()
       val musicShelfPrefix = displayIdPreFixer.getMusicShelfDisplayIdPrefix()
       val otherPrefix = displayIdPreFixer.getOtherDisplayIdPrefix()
       var movieDisplayIdPrefixRecordInserted = false
       var musicDisplayIdPrefixRecordInserted = false
       var musicShelfDisplayIdPrefixRecordInserted = false
       var otherDisplayIdPrefixRecordInserted = false

       if (reelType.equals("dvd",ignoreCase = true)
           || reelType.equals("blu ray",ignoreCase = true)||
           reelType.equals("4K",ignoreCase = true)){

       movieDisplayIdPrefixRecordInserted =  insertMovieDisplayIdRecord(userReelId ,moviePrefix)

       }else if (reelType.equals("cd",ignoreCase = true)||reelType.equals("vinyl")){

       musicDisplayIdPrefixRecordInserted = insertMusicDisplayIdRecord(userReelId,musicPrefix)

       }else if (reelType.equals("scd",ignoreCase = true) ){

       musicShelfDisplayIdPrefixRecordInserted  =  insertMusicShelfDisplayIdRecord(userReelId,musicShelfPrefix)

       }else{

       otherDisplayIdPrefixRecordInserted =  insertOtherDisplayIdRecord(userReelId,otherPrefix)

       }

       if (movieDisplayIdPrefixRecordInserted
           ||musicDisplayIdPrefixRecordInserted
           ||musicShelfDisplayIdPrefixRecordInserted||
           otherDisplayIdPrefixRecordInserted){

           return true
       }else{
           return false
       }
   }


    suspend fun insertMovieDisplayIdRecord(userReelId: Int, moviePrefix: String): Boolean {

        var newMovieDisplayId:Long
        var reelDisplayIdInserted = false
        val db = dbHandler.writableDatabase
        withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put(Col_USER_REEL_ID, userReelId)

            }

            newMovieDisplayId = db.insert(MovieDisplayIdReelIdJointTable, null, values)
            db.close()
        }



            val fullMovieDisplayId = moviePrefix + newMovieDisplayId
        reelDisplayIdInserted =    insertUserReelDisplayId(userReelId, fullMovieDisplayId)

        if (reelDisplayIdInserted){
            return true

        }else{
            return  false
        }






    }

    suspend fun insertMusicDisplayIdRecord(userReelId: Int, musicPrefix: String): Boolean {

        var newMusicDisplayId:Long
        var reelDisplayIdInserted = false
        val db = dbHandler.writableDatabase
        withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put(Col_USER_REEL_ID, userReelId)

            }

            newMusicDisplayId = db.insert(MusicDisplayIdReelIdJointTable, null, values)
            db.close()
           val fullMusicDisplayId = musicPrefix + newMusicDisplayId
            println("full music display id : " + fullMusicDisplayId)
          reelDisplayIdInserted = insertUserReelDisplayId(userReelId,fullMusicDisplayId)

        }
        if (reelDisplayIdInserted){
            return true
        }else{
            return false
        }




    }

    suspend fun insertMusicShelfDisplayIdRecord(userReelId: Int, musicShelfPrefix: String): Boolean {

        var newMusicShelfDisplayId:Long
        var reelDisplayIdInserted = false
        val db = dbHandler.writableDatabase
        withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put(Col_USER_REEL_ID, userReelId)

            }

            newMusicShelfDisplayId = db.insert(MusicShelfDisplayIdJointTable, null, values)
            db.close()
        }
        val fullMusicShelfDisplayId = musicShelfPrefix +  newMusicShelfDisplayId
        insertUserReelDisplayId(userReelId,fullMusicShelfDisplayId)


        if (reelDisplayIdInserted){
            return true
        }else{
            return false
        }

    }

    suspend fun insertOtherDisplayIdRecord(userReelId: Int, otherShelfPrefix: String): Boolean {

        var newOtherDisplayId:Long
        var reelDisplayIdInserted = false
        val db = dbHandler.writableDatabase
        withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put(Col_USER_REEL_ID, userReelId)

            }

            newOtherDisplayId = db.insert(OtherDisplayIdReelIdJointTable, null, values)
            db.close()
        }
        val fullOtherDisplayId = otherShelfPrefix + newOtherDisplayId
        reelDisplayIdInserted = insertUserReelDisplayId(userReelId,fullOtherDisplayId)
        if (reelDisplayIdInserted){
            return true
        }else{
            return false
        }


    }


    suspend fun insertUserReelDisplayId(userReelId: Int, fullDisplayId:String): Boolean {

        var newReelDisplayId:Int
        if (fullDisplayId == "MOV"|| fullDisplayId == "MUS"
            || fullDisplayId == "OTH"|| fullDisplayId == "SMU"){
            return false
        }

        val db = dbHandler.writableDatabase
        withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put(Col_USER_REEL_ID, userReelId)
                put(COL_REEL_DISPLAY_ID,fullDisplayId)

            }

            newReelDisplayId = db.insert(UserReelIdDisplayIdTable, null, values).toInt()
            db.close()

            }
        if (newReelDisplayId != 0){
            return true
        }else{
            return false
        }




    }

}