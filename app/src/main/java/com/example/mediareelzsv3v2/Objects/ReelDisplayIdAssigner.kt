package com.example.mediareelzsv3v2.Objects

class ReelDisplayIdPrefixAssigner {

    private val musicDisplayIdPrefix = "MUS"
    private  val musicShelfDisplayIdPrefix = "SMU"
    private  val movieDisplayIdPrefix = "MOV"
    private  val otherDisplayIdPrefix = "OTH"

    fun getMusicDisplayIdPrefix():String{
        return Companion.musicDisplayIdPrefix
    }
    fun getMusicShelfDisplayIdPrefix():String{
        return Companion.musicShelfDisplayIdPrefix
    }
    fun getMovieDisplayIdPrefix():String{
        return Companion.movieDisplayIdPrefix
    }
    fun getOtherDisplayIdPrefix():String{
        return Companion.otherDisplayIdPrefix
    }


    companion object{

        private val musicDisplayIdPrefix = "MUS"
        private  val musicShelfDisplayIdPrefix = "SMU"
        private  val movieDisplayIdPrefix = "MOV"
        private const val otherDisplayIdPrefix = "OTH"




        fun getMusicDisplayIdPrefix():String{
            return musicDisplayIdPrefix
        }
        fun getMusicShelfDisplayIdPrefix():String{
            return musicShelfDisplayIdPrefix
        }
        fun getMovieDisplayIdPrefix():String{
            return movieDisplayIdPrefix
        }
        fun getOtherDisplayIdPrefix():String{
            return otherDisplayIdPrefix
        }

    }









}