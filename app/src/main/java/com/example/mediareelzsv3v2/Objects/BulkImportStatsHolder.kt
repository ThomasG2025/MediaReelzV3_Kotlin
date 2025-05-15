package com.example.mediareelzsv3v2.Objects

class BulkImportStatsHolder {


    companion object{

        private var  numberSuccessfulReelzsImport:Int = 0
        private var  numberOfDuplicateReelzsImport:Int = 0


    fun getNumberSuccessfulReelzsImport():Int{

        return  numberSuccessfulReelzsImport

    }

    fun setNumberSuccessfulReelzsImport(numberSuccessfulReelzsImport:Int){

        Companion.numberSuccessfulReelzsImport = numberSuccessfulReelzsImport


    }

    fun getNumberOfDuplicateReelzsImport():Int{

        return  numberOfDuplicateReelzsImport


    }

    fun setNumberOfDuplicateReelzsImport(numberOfDuplicateReelzsImport:Int){

        Companion.numberOfDuplicateReelzsImport = numberOfDuplicateReelzsImport


    }







    }










}