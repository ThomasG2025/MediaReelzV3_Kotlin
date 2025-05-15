package com.example.mediareelzsv3v2.Objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Reel2(
    val reel2Id:Int,
    //val reel2DisplayId,
    val reel2Name:String,
    val reel2Artist:String,
    val reel2Year:Int,
    val reel2Type:String,
    val reel2Genre:String,
    val reel2Rating:String,
    val reel2selectedOriginalStatus:Int,
    val reel2Notes:String,
    val reel2selectedArchivedStatus:Int
):Parcelable