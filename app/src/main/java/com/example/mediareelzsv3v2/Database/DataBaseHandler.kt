package com.example.mediareelzsv3v2.Database

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import com.example.mediareelzsv3v2.Objects.Reel
import com.example.mediareelzsv3v2.Objects.User

const val DATABASE_NAME = "Reelzs2.db"

// User table

const val USERS_TABLE_NAME = "Users"
const val SECURITY_PROFILE_TABLE_NAME = "securityProfiles"
const val COL_USERNAME = "userName"
const val COL_HASH = "hash"

// Security profile table

const val COL_SecurityProfileId = "securityProfileId"
const val COL_SECURITY_QUESTION1 = "securityQuestion1"
const val COL_SECURITY_QUESTION1ANSWER = "securityQuestion1Answer"
const val COL_SECURITY_QUESTION2 = "securityQuestion2"
const val COL_SECURITY_QUESTION2ANSWER = "securityQuestion2Answer"
const val COL_SECURITY_QUESTION3 = "securityQuestion3"
const val COL_SECURITY_QUESTION3ANSWER = "securityQuestion3Answer"
const val COL_USER_ID = "userId"

// reelzs table
const val REELZS_TABLE_NAME = "reelzs"
const val COL_REEL_ID = "reelId"
const val COL_REEL_NAME = "reelName"
const val COL_REEL_ARTIST = "reelArtist"
const val COL_REEL_YEAR = "reelYear"
const val COL_REEL_TYPE = "reelType"
const val COL_REEL_GENRE = "reelGenre"
const val COL_REEL_RATING = "reelRating"
const val COL_REEL_IS_ORIGINAL_STATUS = "reelIsOriginalStatus"

// unique reel attributes table //

const val UNIQUE_REEL_ATTRIBUTES_TABLE_NAME = "uniqueReelAttributes"
const val Col_Note = "note"
const val COL_REEL_DIGITALLY_ARCHIVED_STATUS = "reelDigitallyArchivedStatus"

// reel Location table



// user reel table //

const val USER_REELS_TABLE_NAME = "User_Reels"
const val Col_USER_REEL_ID = "userReelId"

// views

const val View_REEL_VIEW = "reelView"
const val View_Security_Profile_VIEW = "userSecurityView"

// movie Display Id Table //

const val MovieDisplayIdReelIdJointTable = "movieDisplayIdReelIdTable"
const val COL_MOVIE_DISPLAY_ID = "movieReelDisplayId"

// music  Display Id Table //

const val MusicDisplayIdReelIdJointTable = "musicDisplayIdReelIdTable"
const val COL_MUSIC_DISPLAY_ID = "musicReelDisplayId"

// other Display Id Table //

const val OtherDisplayIdReelIdJointTable = "otherDisplayIdReelIdTable"
const val COL_OTHER_DISPLAY_ID = "otherReelDisplayId"


// music Shelf Display Id Table //

const val MusicShelfDisplayIdJointTable = "musicShelfDisplayIdReelIdTable"
const val COL_MUSIC_SHELF_DISPLAY_ID = "musicShelfDisplayId"

// userReelIdDisplayIdTable //

const val UserReelIdDisplayIdTable = "reelDisplay"
const val COL_REEL_DISPLAY_ID = "reelDisplayId"




class DataBaseHandler(var context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(db: SQLiteDatabase?) {


        val createSecurityProfileTable: String =
            " CREATE TABLE IF NOT EXISTS " + SECURITY_PROFILE_TABLE_NAME + "( " +
                    COL_SecurityProfileId + " INTEGER PRIMARY KEY not Null," +
                    COL_SECURITY_QUESTION1 + " TEXT not NULL," +
                    COL_SECURITY_QUESTION1ANSWER + " TEXT not NULL," +
                    COL_SECURITY_QUESTION2 + " TEXT not NULL," +
                    COL_SECURITY_QUESTION2ANSWER + " TEXT not NULL," +
                    COL_SECURITY_QUESTION3 + " TEXT not NULL," +
                    COL_SECURITY_QUESTION3ANSWER + " TEXT not NULL)"

        db?.execSQL(createSecurityProfileTable)


        val createUsersTable: String = "CREATE TABLE " + USERS_TABLE_NAME + " (" +
                COL_USER_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT NOT Null," +
                COL_USERNAME + " TEXT not NULL," +
                COL_HASH + " TEXT not NULL," +
                COL_SecurityProfileId + " INTEGER," +
                " FOREIGN KEY(" + COL_SecurityProfileId + ") REFERENCES " + SECURITY_PROFILE_TABLE_NAME + "(" + COL_SecurityProfileId + "))"

        db?.execSQL(createUsersTable)


        val createReelzsTable: String = " CREATE TABLE IF NOT EXISTS " + REELZS_TABLE_NAME + "( " +
                COL_REEL_ID + " INTEGER PRIMARY KEY not Null," +
                COL_REEL_NAME + " TEXT not NULL ," +
                COL_REEL_ARTIST + " TEXT not NULL," +
                COL_REEL_YEAR + " INTEGER not NULL," +
                COL_REEL_TYPE + " TEXT not NULL," +
                COL_REEL_GENRE + " TEXT," +
                COL_REEL_RATING + " TEXT ," +
                COL_REEL_IS_ORIGINAL_STATUS + " TinyInt)"


        db?.execSQL(createReelzsTable)


        val createUniqueReelAttributesTable: String =
            "CREATE TABLE IF NOT EXISTS " + UNIQUE_REEL_ATTRIBUTES_TABLE_NAME + "( " +
                    Col_USER_REEL_ID + " INTEGER  NOT NULL ," +
                    Col_Note + " Text ," +
                    COL_REEL_DIGITALLY_ARCHIVED_STATUS + " TinyInt , " +
                    " FOREIGN KEY(" + Col_USER_REEL_ID + ") REFERENCES " + USER_REELS_TABLE_NAME + "(" + Col_USER_REEL_ID + ")) "


        db?.execSQL(createUniqueReelAttributesTable)





        val createUserReelzsTable: String =
            "CREATE TABLE IF NOT EXISTS " + USER_REELS_TABLE_NAME + "( " +
                    Col_USER_REEL_ID + " INTEGER PRIMARY KEY not Null , " +
                    COL_USER_ID + " INTEGER NOT NULL , " +
                    COL_REEL_ID + " INTEGER NOT NULL , " +
                    " FOREIGN KEY (" + COL_USER_ID + ") REFERENCES " + USERS_TABLE_NAME + "(" + COL_USER_ID + "), " +
                    " FOREIGN KEY (" + COL_REEL_ID + ") REFERENCES " + REELZS_TABLE_NAME + "(" + COL_REEL_ID + ")) "

        db?.execSQL(createUserReelzsTable)


        val createMovieIdReelIdJointTable: String =
            "CREATE TABLE IF NOT EXISTS " + MovieDisplayIdReelIdJointTable + "(" +
                    Col_USER_REEL_ID + " INTEGER   , " +
                    COL_MOVIE_DISPLAY_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT  , " +
                    " FOREIGN KEY(" + Col_USER_REEL_ID + ") REFERENCES " + USER_REELS_TABLE_NAME + "(" + Col_USER_REEL_ID + ")) "
        db?.execSQL(createMovieIdReelIdJointTable)


        val createMusicIdReelIdJointTable: String =
            "CREATE TABLE IF NOT EXISTS " + MusicDisplayIdReelIdJointTable + "(" +
                    Col_USER_REEL_ID + " INTEGER, " +
                    COL_MUSIC_DISPLAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT  , " +
                    " FOREIGN KEY (" + Col_USER_REEL_ID + ") REFERENCES " + USER_REELS_TABLE_NAME + "(" + Col_USER_REEL_ID + ")) "
        db?.execSQL(createMusicIdReelIdJointTable)


        val createOtherIdReelIdJointTable: String =
            "CREATE TABLE IF NOT EXISTS " + OtherDisplayIdReelIdJointTable + "(" +
                    Col_USER_REEL_ID + " INTEGER , " +
                    COL_OTHER_DISPLAY_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT  ," +
                    " FOREIGN KEY (" + Col_USER_REEL_ID + ") REFERENCES " + USER_REELS_TABLE_NAME + "(" + Col_USER_REEL_ID + ")) "
        db?.execSQL(createOtherIdReelIdJointTable)

        val createMusicShelfIdReelIdJointTable: String =
            "CREATE TABLE IF NOT EXISTS " + MusicShelfDisplayIdJointTable + "(" +
                    Col_USER_REEL_ID + " INTEGER , " +
                    COL_MUSIC_SHELF_DISPLAY_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT  ," +

                    " FOREIGN KEY (" + Col_USER_REEL_ID + ") REFERENCES " + USER_REELS_TABLE_NAME + "(" + Col_USER_REEL_ID + ")) "
        db?.execSQL(createMusicShelfIdReelIdJointTable)

        val createUserReelDisplayIdTable:String =
            "CREATE TABLE IF NOT EXISTS " + UserReelIdDisplayIdTable + "(" +
                    Col_USER_REEL_ID + " INTEGER , " +
                    COL_REEL_DISPLAY_ID + " TEXT  ," +

                    " FOREIGN KEY (" + Col_USER_REEL_ID + ") REFERENCES " + USER_REELS_TABLE_NAME + "(" + Col_USER_REEL_ID + ")) "
        db?.execSQL(createUserReelDisplayIdTable)


        val createReelView: String =
            """
    CREATE VIEW IF NOT EXISTS reelView AS
    SELECT ur.userReelId,
           ur.userId,
           ur.reelId,
           
           r.reelName,
           r.reelArtist,
           r.reelYear,
           r.reelType,
           r.reelGenre,
           r.reelRating,
           r.reelIsOriginalStatus,
           rua.note,
           rua.reelDigitallyArchivedStatus
    FROM User_Reels AS ur
    INNER JOIN reelzs AS r ON ur.reelId = r.reelId
    INNER JOIN uniqueReelAttributes AS rua ON ur.userReelId = rua.userReelId
    
""".trimIndent()
        db?.execSQL(createReelView)


        val createReelView2: String =
            """
    CREATE VIEW IF NOT EXISTS reelView AS
    SELECT ur.userReelId,
           ur.userId,
           ur.reelId,
           rdi.reelDisplayId,
           r.reelName,
           r.reelArtist,
           r.reelYear,
           r.reelType,
           r.reelGenre,
           r.reelRating,
           r.reelIsOriginalStatus,
           rua.note,
           rua.reelDigitallyArchivedStatus
    FROM User_Reels AS ur
    INNER JOIN userReelDisplayId AS rdi ON ur.userReelId = rdi.userReelId
    INNER JOIN reelzs AS r ON ur.reelId = r.reelId
    INNER JOIN uniqueReelAttributes AS rua ON ur.userReelId = rua.userReelId
    
""".trimIndent()
        db?.execSQL(createReelView2)





        val createSecurityView: String =
            """
       CREATE VIEW IF NOT EXISTS userSecurityView  AS 
       SELECT u.userID,
              u.userName,
              u.hash,
              u.securityProfileId,
              sp.securityQuestion1,
              sp.securityQuestion1Answer,
              sp.securityQuestion2,
              sp.securityQuestion2Answer,
              sp.securityQuestion3,
              sp.securityQuestion3Answer
       FROM Users AS u
       INNER JOIN securityProfiles AS sp ON u.securityProfileId = sp.securityProfileId       
       """.trimIndent()
        db?.execSQL(createSecurityView)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    @SuppressLint("Range", "SuspiciousIndentation")
    fun getReelzs(): ArrayList<Reel> {
        val userId: Int = User.getUserId()
        val reelzsList: ArrayList<Reel> = ArrayList()
        val stringPlaceholderValue = ""
        val intPlaceholderValue = 1111
        val booleanPlaceholderValue = 0

        val db = this.readableDatabase

        val sql = "SELECT * FROM $View_REEL_VIEW WHERE userId = ?"
        db.rawQuery(sql, arrayOf(userId.toString())).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val reel = Reel().apply {
                        val reelId = getColumnInt(cursor, "reelId", intPlaceholderValue)
                        setReelId(reelId)

                        val reelName = getColumnString(cursor, "reelName", stringPlaceholderValue)
                        setReelName(reelName)
                        val reelArtist =
                            getColumnString(cursor, "reelArtist", stringPlaceholderValue)
                        setReelArtist(reelArtist)
                        val reelYear = getColumnInt(cursor, "reelYear", intPlaceholderValue)
                        setReelYear(reelYear)
                        val reelType = getColumnString(cursor, "reelType", stringPlaceholderValue)
                        setReelType(reelType)
                        val reelGenre = getColumnString(cursor, "reelGenre", stringPlaceholderValue)
                        setReelGenre(reelGenre)
                        val reelRating =
                            getColumnString(cursor, "reelRating", stringPlaceholderValue)
                        setReelRating(reelRating)
                        val reelSelectedOriginalStatus =
                            getColumnInt(cursor, "reelIsOriginalStatus", booleanPlaceholderValue)
                        setSelectedOriginalStatus(reelSelectedOriginalStatus)
                        val reelNote = getColumnString(cursor, "note", stringPlaceholderValue)
                        setReelNotes(reelNote)
                        val selectedArchivedStatus = getColumnInt(
                            cursor,
                            "reelDigitallyArchivedStatus",
                            booleanPlaceholderValue
                        )
                        setSelectedArchivedStatus(selectedArchivedStatus)

                    }
                    reelzsList.add(reel)
                } while (cursor.moveToNext())
            }
        }

        return reelzsList
    }


//    @SuppressLint("Range", "SuspiciousIndentation")
//    fun getReelzs2(): ArrayList<Reel> {
//        val userId: Int = User.getUserId()
//        val reelzsList: ArrayList<Reel> = ArrayList()
//        val stringPlaceholderValue = ""
//        val intPlaceholderValue = 1111
//        val booleanPlaceholderValue = 0
//
//        val db = this.readableDatabase
//
//        val sql = "SELECT * FROM $View_REEL_VIEW WHERE userId = ?"
//        db.rawQuery(sql, arrayOf(userId.toString())).use { cursor ->
//            if (cursor.moveToFirst()) {
//                do {
//                    val reel = Reel().apply {
//                        val reelId = getColumnInt(cursor, "reelId", intPlaceholderValue)
//                        setReelId(reelId)
//                        val reelDisplayId = getColumnString(cursor,"reelDisplayId",stringPlaceholderValue)
//                        setReelDisplayId2(reelDisplayId)
//                        val reelName = getColumnString(cursor, "reelName", stringPlaceholderValue)
//                        setReelName(reelName)
//                        val reelArtist =
//                            getColumnString(cursor, "reelArtist", stringPlaceholderValue)
//                        setReelArtist(reelArtist)
//                        val reelYear = getColumnInt(cursor, "reelYear", intPlaceholderValue)
//                        setReelYear(reelYear)
//                        val reelType = getColumnString(cursor, "reelType", stringPlaceholderValue)
//                        setReelType(reelType)
//                        val reelGenre = getColumnString(cursor, "reelGenre", stringPlaceholderValue)
//                        setReelGenre(reelGenre)
//                        val reelRating =
//                            getColumnString(cursor, "reelRating", stringPlaceholderValue)
//                        setReelRating(reelRating)
//                        val reelSelectedOriginalStatus =
//                            getColumnInt(cursor, "reelIsOriginalStatus", booleanPlaceholderValue)
//                        setSelectedOriginalStatus(reelSelectedOriginalStatus)
//                        val reelNote = getColumnString(cursor, "note", stringPlaceholderValue)
//                        setReelNotes(reelNote)
//                        val selectedArchivedStatus = getColumnInt(
//                            cursor,
//                            "reelDigitallyArchivedStatus",
//                            booleanPlaceholderValue
//                        )
//                        setSelectedArchivedStatus(selectedArchivedStatus)
//
//                    }
//                    reelzsList.add(reel)
//                } while (cursor.moveToNext())
//            }
//        }
//
//        return reelzsList
//    }

    fun getColumnInt(cursor: Cursor, columnName: String, defaultValue: Int): Int {
        val columnIndex = cursor.getColumnIndex(columnName)
        return if (columnIndex != -1) cursor.getInt(columnIndex) else defaultValue
    }

    fun getColumnString(cursor: Cursor, columnName: String, defaultValue: String): String {
        val columnIndex = cursor.getColumnIndex(columnName)
        return if (columnIndex != -1) cursor.getString(columnIndex) else defaultValue
    }



}