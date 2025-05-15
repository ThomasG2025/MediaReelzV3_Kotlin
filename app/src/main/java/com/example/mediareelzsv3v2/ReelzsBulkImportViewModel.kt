package com.example.mediareelzsv3v2

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediareelzsv3v2.Database.ReelOperations.ReelBulkImport
import com.example.mediareelzsv3v2.Objects.Reel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader




class ReelzsBulkImportViewModel: ViewModel() {




    private val _operationStatus = MutableStateFlow<Boolean?>(null)
    val operationStatus: StateFlow<Boolean?>
        get() = _operationStatus

     fun loadCsvFile(uri: Uri?, context: Context) {


        val reelBulkImporter = ReelBulkImport(context)
        val bulkReelList = ArrayList<Reel>()
        var bulkReelLocationFilterOperationStatus:Boolean = false
        viewModelScope.launch {

            try {
                val inputStream = uri?.let { context.contentResolver.openInputStream(it) }
                val reader = BufferedReader(InputStreamReader(inputStream))
                val lines = mutableListOf<String>()
                var line: String? =
                    withContext(Dispatchers.IO) {
                        reader.readLine()
                    }
                var header: String? = null
                if (line != null) {
                    header = line
                    line = withContext(Dispatchers.IO) {
                        reader.readLine()
                    }
                }
                while (line != null) {

                    lines.add(line)
                    line = withContext(Dispatchers.IO) {
                        reader.readLine()
                    }


                }

                withContext(Dispatchers.IO) {
                    reader.close()
                }
                withContext(Dispatchers.IO) {
                inputStream?.close()
            }

// Now you can process the lines of the CSV file
                if (header != null) {
                    val headerFields = header.split(",")
                    // Do something with the header fields
                    println(headerFields)
                }
                for (line in lines) {

                    val bulkArray = line.split(",")
                    val bulkReel = Reel()

                    bulkArray[0].let { bulkReel.setReelName(it) }
                    bulkArray[1].let { bulkReel.setReelArtist(it) }
                    bulkArray[2].let { bulkReel.setReelYear(it.toInt()) }
                    bulkArray[3].let { bulkReel.setReelType(it) }
                    bulkArray[4].let { bulkReel.setReelGenre(it) }
                    bulkArray[5].let { bulkReel.setReelRating(it) }
                    bulkArray[6].let { bulkReel.setSelectedOriginalStatus(it.toInt()) }
                    bulkArray[7].let { bulkReel.setReelNotes(it) }
                    bulkArray[8].let { bulkReel.setSelectedArchivedStatus(it.toInt()) }
                    bulkReelList.add(bulkReel)


                    // Do something with the fields
                }



                println("import list Size " + bulkReelList.size)

            bulkReelLocationFilterOperationStatus = reelBulkImporter.checkForDuplicateReelzs(bulkReelList)


            } catch (e: Exception) {

                println(e.stackTrace)



            }

            if (bulkReelLocationFilterOperationStatus){

                _operationStatus.value = true

            }else{

                _operationStatus.value = false

            }
        }

    }



    }






