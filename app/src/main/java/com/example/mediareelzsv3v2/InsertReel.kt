package com.example.mediareelzsv3v2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mediareelzsv3v2.Database.DataBaseHandler
import com.example.mediareelzsv3v2.Database.ReelOperations.ReelSingleInsert
import com.example.mediareelzsv3v2.Objects.BulkImportStatsHolder
import com.example.mediareelzsv3v2.Objects.Reel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.OutputStreamWriter

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("SuspiciousIndentation")
@Composable
fun InsertReel(navController: NavHostController) {
    val context: Context = LocalContext.current
    var insertReelNameTextFieldState by remember { mutableStateOf("") }
    var insertReelArtistTextFieldState by remember { mutableStateOf("") }
    var insertReelYearTextFieldState by remember { mutableStateOf("") }
    var insertReelTypeTextFieldState by remember { mutableStateOf("") }
    var insertReelGenreTextFieldState by remember { mutableStateOf("") }
    var insertReelRatingTextFieldState by remember { mutableStateOf("") }
    var insertReelNotesTextFieldState by remember { mutableStateOf("") }
    var insertReelIsOriginalCheckboxState by remember { mutableStateOf(false) }
    var insertReelIsArchivedCheckboxState by remember { mutableStateOf(false) }
    var viewButtonEnabled by remember { mutableStateOf(true) }
    var aboutButtonEnabled by remember { mutableStateOf(true) }
    var insertButtonEnabled by remember { mutableStateOf(true) }
    var bulkInsertButtonEnabled by remember { mutableStateOf(true) }
    val exportReelzsButtonEnabled by remember { mutableStateOf(true) }
    var modifyUserButtonEnabled by remember { mutableStateOf(true) }
    var backButtonEnabled by remember { mutableStateOf(true) }
    val bulkImportViewModel = viewModel<ReelzsBulkImportViewModel>()


    val documentPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uri: Uri? = data?.data

                uri?.let { csvUri ->
                    bulkImportViewModel.loadCsvFile(csvUri, context)
                }
            }
        }

            bulkImportViewModel.operationStatus.collectAsState().value?.let { success ->


                if (success) {
                    Toast.makeText(
                        context,
                        "bulk insert completed",
                        Toast.LENGTH_LONG
                    ).show()
                    println("Bulk Insert Completed")

                } else {
                    Toast.makeText(
                        context,
                        "There was an error completing the bulk insert please make sure all fields excluding the notes and rating are filled",
                        Toast.LENGTH_LONG
                    ).show()
                    println("Bulk Insert Problem")



                }





            }

















    Surface(

        color = MaterialTheme.colors.background

    ) {

        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {

            InsertReelLabel()

            TextField(
                value = insertReelNameTextFieldState,
                label = {
                    Text("Name")
                },
                onValueChange = {
                    insertReelNameTextFieldState = it
                },
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 30.dp, top = 80.dp)
                    )
                    .size(width = 150.dp, height = 60.dp)
            )

            TextField(
                value = insertReelArtistTextFieldState,
                label = {
                    Text("Artist")
                },
                onValueChange = {
                    insertReelArtistTextFieldState = it
                },
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 220.dp, top = 80.dp)
                    )
                    .size(width = 150.dp, height = 60.dp)
            )

            TextField(
                value = insertReelYearTextFieldState,
                label = {
                    Text("Year")
                },
                onValueChange = {
                    insertReelYearTextFieldState = it
                },
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 30.dp, top = 150.dp)
                    )
                    .size(width = 150.dp, height = 60.dp)
            )



            TextField(
                value = insertReelTypeTextFieldState,
                label = {
                    Text("Type")
                },
                onValueChange = {
                    insertReelTypeTextFieldState = it
                },
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 220.dp, top = 150.dp)
                    )
                    .size(width = 150.dp, height = 60.dp)
            )


            TextField(
                value = insertReelGenreTextFieldState,
                label = {
                    Text("Genre")
                },
                onValueChange = {
                    insertReelGenreTextFieldState = it
                },
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 30.dp, top = 220.dp)
                    )
                    .size(width = 150.dp, height = 60.dp)
            )


            TextField(
                value = insertReelRatingTextFieldState,
                label = {
                    Text("Rating")
                },
                onValueChange = {
                    insertReelRatingTextFieldState = it
                },
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 220.dp, top = 220.dp)
                    )
                    .size(width = 150.dp, height = 60.dp)
            )

            TextField(
                value = insertReelNotesTextFieldState,
                label = {
                    Text("Notes")
                },
                onValueChange = {
                    insertReelNotesTextFieldState = it
                },
                singleLine = true,
                modifier = Modifier
                    .padding(
                        PaddingValues(start = 30.dp, top = 290.dp)
                    )
                    .size(width = 150.dp, height = 60.dp)
            )







            Checkbox(
                checked = insertReelIsOriginalCheckboxState,

                modifier = Modifier.padding(PaddingValues(start = 230.dp, top = 430.dp)),

                onCheckedChange = {
                    insertReelIsOriginalCheckboxState = it

                },

                )

            Text(
                text = "Original",
                modifier = Modifier.padding(PaddingValues(start = 280.dp, top = 444.dp))


            )

            Checkbox(
                checked = insertReelIsArchivedCheckboxState,

                modifier = Modifier.padding(PaddingValues(start = 20.dp, top = 510.dp)),

                onCheckedChange = {
                    insertReelIsArchivedCheckboxState = it

                },

                )

            Text(
                text = "Archived",
                modifier = Modifier.padding(PaddingValues(start = 70.dp, top = 524.dp))
            )
            val singleInsertButtonScope = rememberCoroutineScope()
            Button(
                onClick = {

                    viewButtonEnabled = false
                    bulkInsertButtonEnabled = false
                    aboutButtonEnabled = false
                    modifyUserButtonEnabled = false
                    backButtonEnabled = false



                    singleInsertButtonScope.launch {

                        withContext(Dispatchers.IO) {


                            val validInsert: Boolean =
                                verifyRequiredFieldsAreNotNull(
                                    context,
                                    insertReelNameTextFieldState,
                                    insertReelArtistTextFieldState,
                                    insertReelYearTextFieldState,
                                    insertReelTypeTextFieldState,
                                    insertReelGenreTextFieldState,
                                    insertReelRatingTextFieldState,
                                    insertReelNotesTextFieldState,
                                    insertReelIsOriginalCheckboxState,
                                    insertReelIsArchivedCheckboxState
                                )

                            if (validInsert) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "reel successfully inserted",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    aboutButtonEnabled = true
                                    viewButtonEnabled = true
                                    bulkInsertButtonEnabled = true
                                    modifyUserButtonEnabled = true
                                    backButtonEnabled = true
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "There was and issue inserting the reel",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    aboutButtonEnabled = true
                                    viewButtonEnabled = true
                                    bulkInsertButtonEnabled = true
                                    modifyUserButtonEnabled = true
                                    backButtonEnabled = true
                                }
                            }

                        }
                    }

                },
                enabled = insertButtonEnabled,
                modifier = Modifier
                    .padding(PaddingValues(start = 20.dp, top = 590.dp))
                    .width(100.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Insert")


            }




            Button(
                onClick = {


                    navController.navigate(Destination.ReelzsViewer.route)
                },
                enabled = viewButtonEnabled,


                modifier = Modifier
                    .padding(PaddingValues(start = 150.dp, top = 590.dp))
                    .width(100.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "View")


            }

            val bulkImportButtonScope = rememberCoroutineScope()
            Button(
                onClick = {
                    viewButtonEnabled = false
                    insertButtonEnabled = false
                    aboutButtonEnabled = false
                    modifyUserButtonEnabled = false
                    backButtonEnabled = false
                    bulkImportButtonScope.launch {
                        withContext(Dispatchers.IO) {
                            val bulkInsertSuccess = bulkImport(documentPickerLauncher)
                            
                            if (bulkInsertSuccess.equals(true)) {
                                withContext(Dispatchers.Main) {
                                    val numberOfBulkReelzsSuccessfullyImported =
                                        BulkImportStatsHolder.getNumberSuccessfulReelzsImport()
                                    val numberOfDuplicateBulkReelzs =
                                        BulkImportStatsHolder.getNumberOfDuplicateReelzsImport()
                                    Toast.makeText(
                                        context,
                                        "Bulk Import finished " + numberOfBulkReelzsSuccessfullyImported + "reelzs Successfully Imported" + numberOfDuplicateBulkReelzs + "reelzs Skipped",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    viewButtonEnabled = true
                                    insertButtonEnabled = true
                                    aboutButtonEnabled = true
                                    modifyUserButtonEnabled = true
                                    backButtonEnabled = true

                                }

                            } else if (bulkInsertSuccess.equals(false)) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "Bulk Records insert error",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    viewButtonEnabled = true
                                    insertButtonEnabled = true
                                    aboutButtonEnabled = true
                                    modifyUserButtonEnabled = true
                                    backButtonEnabled = true
                                }
                            }

                        }


                    }

                    viewButtonEnabled = true
                    insertButtonEnabled = true
                    aboutButtonEnabled = true
                    modifyUserButtonEnabled = true
                    backButtonEnabled = true


                },
                enabled = bulkInsertButtonEnabled,
                modifier = Modifier
                    .padding(PaddingValues(start = 280.dp, top = 590.dp))
                    .width(100.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Bulk Insert")


            }

            Button(
                onClick = {

                    exportReelzs(context, "test4.csv")

                },
                enabled = exportReelzsButtonEnabled,
                modifier = Modifier.padding(PaddingValues(start = 20.dp, top = 650.dp)),
                shape = RoundedCornerShape(20.dp),


                ) {

                Text(text = "Export Reelzs")

            }






            Button(
                onClick = {
                    navController.navigate(Destination.About.route)
                },
                enabled = aboutButtonEnabled,
                modifier = Modifier
                    .padding(PaddingValues(start = 180.dp, top = 650.dp))
                    .width(100.dp),
                shape = RoundedCornerShape(20.dp)
            )


            {


                Text(text = "About")


            }




            Button(
                onClick = {

                    navController.navigate(Destination.Login.route)

                },
                enabled = backButtonEnabled,
                modifier = Modifier
                    .padding(PaddingValues(start = 150.dp, top = 710.dp))
                    .width(100.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Back")


            }


            Button(
                onClick = {
                    navController.navigate(Destination.ModifyUser.route)


                },
                enabled = modifyUserButtonEnabled,
                modifier = Modifier
                    .padding(PaddingValues(start = 280.dp, top = 710.dp))
                    .width(100.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Modify User")

            }

        }

    }

}


suspend fun verifyRequiredFieldsAreNotNull(
    context: Context,
    InsertReelNameTextFieldState: String,
    InsertReelArtistTextFieldState: String,
    InsertReelYearTextFieldState: String,
    InsertReelTypeTextFieldState: String,
    InsertReelGenreTextFieldState: String,
    InsertReelRatingTextFieldState: String,
    InsertReelNotesTextFieldState: String,
    InsertReelIsOriginalCheckboxState: Boolean,
    InsertReelIsArchivedCheckboxState: Boolean
): Boolean {
    var insertSuccessful = false
    val singleReelInserter = ReelSingleInsert(context)
    val verifyAllRequiredFieldsForSingleInsertCheck = InsertReelNameTextFieldState == "" || InsertReelArtistTextFieldState == ""|| InsertReelYearTextFieldState == ""||
            InsertReelTypeTextFieldState == ""|| InsertReelGenreTextFieldState == ""


    if (verifyAllRequiredFieldsForSingleInsertCheck){
        return false
    }else {



            val validInsert = singleReelInserter.verifyReelIsNotDuplicate(
                InsertReelNameTextFieldState,
                InsertReelArtistTextFieldState,
                InsertReelYearTextFieldState.toInt(),
                InsertReelTypeTextFieldState,
                InsertReelGenreTextFieldState,
                InsertReelRatingTextFieldState,
                convertBooleanToInt1(InsertReelIsOriginalCheckboxState),
                InsertReelNotesTextFieldState,
                convertBooleanToInt1(InsertReelIsArchivedCheckboxState)

            )

            insertSuccessful = validInsert


    }

    return  insertSuccessful

}

fun convertBooleanToInt1(status:Boolean): Int {
    var endStatus = 0
    if (status) {
        endStatus = 1



    }
    return endStatus
}


fun bulkImport(
    documentPickerLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {



            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }

            documentPickerLauncher.launch(intent)
        }



// Chat Gpt largely contributed to this section //

//fun loadCsvFile(uri: Uri?, context: Context): Boolean {
//
//    val reelBulkImporter = ReelBulkImport(context)
//    val bulkReelList = ArrayList<Reel>()
//
//    try {
//        val inputStream = uri?.let { context.contentResolver.openInputStream(it) }
//        val reader = BufferedReader(InputStreamReader(inputStream))
//        val lines = mutableListOf<String>()
//        var line: String? = reader.readLine()
//        var header: String? = null
//        if (line != null) {
//            header = line
//            line = reader.readLine()
//        }
//        while (line != null) {
//
//            lines.add(line)
//            line = reader.readLine()
//
//
//        }
//
//        reader.close()
//        inputStream?.close()
//
//// Now you can process the lines of the CSV file
//        if (header != null) {
//            val headerFields = header.split(",")
//            // Do something with the header fields
//            println(headerFields)
//        }
//        for (line in lines) {
//
//            val bulkArray = line.split(",")
//            val bulkReel = Reel()
//
//            bulkArray[0].let { bulkReel.setReelName(it) }
//            bulkArray[1].let { bulkReel.setReelArtist(it) }
//            bulkArray[2].let { bulkReel.setReelYear(it.toInt()) }
//            bulkArray[3].let { bulkReel.setReelType(it) }
//            bulkArray[4].let { bulkReel.setReelGenre(it) }
//            bulkArray[5].let { bulkReel.setReelRating(it) }
//            bulkArray[6].let { bulkReel.setSelectedOriginalStatus(it.toInt()) }
//            bulkArray[7].let { bulkReel.setReelNotes(it) }
//            bulkArray[8].let { bulkReel.setReelLocationName(it) }
//            bulkArray[9].let { bulkReel.setReelLocationPageNumber(it.toInt()) }
//            bulkArray[10].let { bulkReel.setReelLocationPageSize(it.toInt()) }
//            bulkArray[11].let { bulkReel.setReelLocationSlotNumber(it.toInt()) }
//            bulkArray[12].let { bulkReel.setSelectedArchivedStatus(it.toInt()) }
//            bulkReelList.add(bulkReel)
//
//
//            // Do something with the fields
//        }
//
//
//
//        println("import list Size " + bulkReelList.size)
//
//
//        reelBulkImporter.filterBulkReelzsForDuplicateLocationCombination(bulkReelList)
//        return true
//    }catch (e:Exception){
//
//        println( e.stackTrace)
//        return false
//
//
//    }
//
//
//}


@RequiresApi(Build.VERSION_CODES.Q)
fun exportReelzs(context: Context, fileName: String) {
    val dbHandler = DataBaseHandler(context)
    val data: ArrayList<Reel> = dbHandler.getReelzs()
    try {
        val headers = "Reel Name,Reel Artist,Reel Year,Reel Type,Reel Genre,Reel Rating,Reel Original,Reel Notes,Reel Location Name,Reel Digitally Archived Status\n"

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
        }

        val resolver: ContentResolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), values)
        uri?.let { fileUri ->
            resolver.openOutputStream(fileUri)?.use { outputStream ->
                BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
                    writer.write(headers)
                    for (reel in data) {
                        val text = "${reel.getReelName()},${reel.getReelArtist()},${reel.getReelYear()},${reel.getReelType()},${reel.getReelGenre()},${reel.getReelRating()},${convertCheckboxResultToInt2(reel.getSelectedOriginalStatus2())},${reel.getReelNotes()}," +
                                ",${convertCheckboxResultToInt2(reel.getSelectedArchivedStatus2())}\n"
                        writer.write(text)
                    }
                }
            }
        }

        Toast.makeText(context,"Export Completed",Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context,"Export Error",Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }

}






@Composable
fun InsertReelLabel(){

    Text(text = "Insert Reel",

        modifier =  Modifier.padding(PaddingValues(start = 170.dp, top = 25.dp ))


    )


}

