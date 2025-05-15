package com.example.mediareelzsv3v2.Objects

class Reel {

    private var reelId: Int  = 0
    private var reelDisplayId = ""
    private var reelName = ""
    private var reelArtist  = ""
    private var reelYear: Int = 2000
    private var reelType : String = ""
    private var reelGenre = ""
    private var reelRating = ""
    private var selectedOriginalStatus:Int = 0
    private var reelNotes = ""
    private var selectedArchivedStatus: Int = 0



    constructor(){

    }












    constructor( reelId:Int,reelDisplayId:String,reelName: String, reelArtist: String, reelYear: Int, reelType: String, reelGenre: String, reelRating: String,
                 selectedOriginalStatus: Int, reelNotes: String ,
                 selectedArchivedStatus: Int){

        this.reelId = reelId
        this.reelDisplayId = reelDisplayId
        this.reelName = reelName
        this.reelArtist = reelArtist
        this.reelYear = reelYear
        this.reelType = reelType
        this.reelGenre = reelGenre
        this.reelRating = reelRating
        this.selectedOriginalStatus = selectedOriginalStatus
        this.reelNotes = reelNotes
        this.selectedArchivedStatus = selectedArchivedStatus


    }


    constructor( reelName: String, reelArtist: String, reelYear: Int, reelType: String,  reelGenre: String, reelRating: String,
                 selectedOriginalStatus: Int, reelNotes: String ,
                 selectedArchivedStatus: Int){


        this.reelName = reelName
        this.reelArtist = reelArtist
        this.reelYear = reelYear
        this.reelType = reelType
        this.reelGenre = reelGenre
        this.reelRating = reelRating
        this.selectedOriginalStatus = selectedOriginalStatus
        this.reelNotes = reelNotes
        this.selectedArchivedStatus = selectedArchivedStatus


    }

    constructor( reelId:Int,reelName: String, reelArtist: String, reelYear: Int, reelType: String, reelGenre: String, reelRating: String,
                 selectedOriginalStatus: Int, reelNotes: String ,
                 selectedArchivedStatus: Int){

        this.reelId = reelId
        this.reelName = reelName
        this.reelArtist = reelArtist
        this.reelYear = reelYear
        this.reelType = reelType
        this.reelGenre = reelGenre
        this.reelRating = reelRating
        this.selectedOriginalStatus = selectedOriginalStatus
        this.reelNotes = reelNotes
        this.selectedArchivedStatus = selectedArchivedStatus


    }


    fun getReelId(): Int {
        return reelId
    }

    fun setReelId(reelId: Int) {
        this.reelId = reelId
    }

    fun getReelName(): String {
        return reelName
    }

    fun setReelName(reelName: String) {
        this.reelName = reelName
    }

    fun getReelArtist(): String {
        return reelArtist
    }

    fun setReelArtist(reelArtist: String) {
        this.reelArtist = reelArtist
    }

    fun getReelYear(): Int {
        return reelYear
    }

    fun setReelYear(reelYear: Int) {
        this.reelYear = reelYear
    }

    fun getReelType(): String {
        return reelType
    }

    fun setReelType(reelType: String) {
        this.reelType = reelType
    }



    fun getReelGenre(): String {
        return reelGenre
    }

    fun setReelGenre(reelGenre: String) {
        this.reelGenre = reelGenre
    }


    fun getReelRating(): String {
        return reelRating
    }

    fun setReelRating(reelRating: String) {
        this.reelRating = reelRating
    }





    fun getSelectedOriginalStatus():Int {
        return this.selectedOriginalStatus
    }


    fun getReelNotes(): String {
        return reelNotes
    }

    fun setReelNotes(reelNotes: String) {
        this.reelNotes = reelNotes
    }










    fun getSelectedArchivedStatus(): Int {
        return selectedArchivedStatus

    }


    fun setSelectedArchivedStatus(selectedArchivedStatus: Int){
        this.selectedArchivedStatus = selectedArchivedStatus
    }


    fun getSelectedOriginalStatus2(): Boolean {

        val statusToConvert = getSelectedOriginalStatus()
        var finalStatus = false
        if(statusToConvert == 1){
            finalStatus = true

        }else if (statusToConvert == 0){
            finalStatus =  false
        }

        return  finalStatus

    }




    fun getSelectedArchivedStatus2():Boolean {

        val statusToConvert = getSelectedArchivedStatus()
        var finalStatus = false
        if(statusToConvert == 1){
            finalStatus = true
        }else if (statusToConvert == 0){
            finalStatus =  false
        }

        return  finalStatus



    }

    fun setSelectedOriginalStatus(selectedOriginalStatus: Int) {
        this.selectedOriginalStatus = selectedOriginalStatus
    }


    fun getReelDisplayId():String{
        return reelDisplayId

    }
    fun setReelDisplayId(reelType:String,displayId:Int){

    var reelTypePrefix =  determineReelTypePrefix(reelType)

        // in order for this to work I will have to  call the  get reel type method
       // then pass that to a method which returns a prefix
        // mov for movies,
        // mus for music,
        // oth for other
        // returning string
        this.reelDisplayId = reelTypePrefix+displayId

    }

    fun setReelDisplayId2(reelDisplayId: String){




        this.reelDisplayId = reelDisplayId

    }



    fun determineReelTypePrefix(reelType:String):String{
        if (reelType.equals("dvd",ignoreCase = true)||
            reelType.equals("blu ray",ignoreCase = true)
            ||reelType.equals("4K",ignoreCase = true)){

            return "MOV"

        }else if (reelType.equals("cd",ignoreCase = true)|| reelType.equals("vinyl",ignoreCase = true)){

            return "MUS"

        }else{
            return "OTH"
        }

    }



    fun doesMatchSearchQuery(query:String): Boolean{

        return  reelId.toString().contains(query, ignoreCase = true)||
                reelName.contains(query,ignoreCase = true)||
                reelArtist.contains(query,ignoreCase = true)||
                reelYear.toString().contains(query,ignoreCase = true)||
                reelType.contains(query,ignoreCase = true)
                ||reelGenre.contains(query,ignoreCase = true)
                ||reelRating.contains(query,ignoreCase = true)
                ||selectedOriginalStatus.toString().contains(query,ignoreCase = true)
                ||reelNotes.contains(query,ignoreCase = true)






    }
















    }


