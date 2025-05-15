package com.example.mediareelzsv3v2

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mediareelzsv3v2.Database.ReelOperations.ReelDelete
import com.example.mediareelzsv3v2.Database.ReelOperations.ReelUpdate
import com.example.mediareelzsv3v2.Objects.Reel
import com.example.mediareelzsv3v2.Objects.Reel2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// //Edge case testing passed 3/21/23 11:57 AM//
@Composable
fun ModifyReel( navController: NavHostController

){




    Surface(

        color = MaterialTheme.colors.background

    ) {

        val result2 =
            navController.previousBackStackEntry?.savedStateHandle?.get<Reel2>("reel2")

        val reel = Reel()
        result2?.reel2Id?.let { reel.setReelId(it) }
        result2?.reel2Name?.let { reel.setReelName(it) }
        result2?.reel2Artist?.let { reel.setReelArtist(it) }
        result2?.reel2Year?.let { reel.setReelYear(it) }
        result2?.reel2Type?.let { reel.setReelType(it) }
        result2?.reel2Genre?.let { reel.setReelGenre(it) }
        result2?.reel2Rating?.let { reel.setReelRating(it) }
        result2?.reel2selectedOriginalStatus?.let { reel.setSelectedOriginalStatus(it) }
        result2?.reel2Notes?.let { reel.setReelNotes(it) }
        result2?.reel2selectedArchivedStatus?.let { reel.setSelectedArchivedStatus(it) }


        Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

            EditReelLabel()
            ModifyReelCard(reel, navController)


        }

    }

}

@Composable

fun EditReelLabel(){

    Text("Edit Reel", modifier = Modifier.padding(PaddingValues(start = 170.dp, top = 30.dp)))

}

@SuppressLint("SuspiciousIndentation", "CoroutineCreationDuringComposition")
@Composable

fun ModifyReelCard(reel: Reel, navController: NavHostController) {
    val context: Context = LocalContext.current
    val reelUpdater = ReelUpdate(context)
    val openReelDeletionDialog = remember {
        mutableStateOf(false)
    }
    val userConfirmedReelDeletion = remember {
        mutableStateOf(false)
    }
    var modifyReelReelIdTextFieldState by remember { mutableStateOf("") }
    var modifyReelReelNameTextFieldState by remember { mutableStateOf("") }
    var modifyReelReelArtistTextFieldState by remember { mutableStateOf("") }
    var modifyReelReelYearTextFieldState by remember { mutableStateOf("") }
    var modifyReelReelTypeTextFieldState by remember { mutableStateOf("") }
    var modifyReelReelRatingTextFieldState by remember { mutableStateOf("") }
    var modifyReelReelGenreTextFieldState by remember { mutableStateOf("") }
    var modifyReelReelNotesTextFieldState by remember { mutableStateOf("") }
    var modifyReelReelLocationNameTextFieldState by remember { mutableStateOf("") }
    var modifyReelIsOriginalCheckboxState by remember { mutableStateOf(false) }
    var modifyReelIsArchivedCheckboxState by remember { mutableStateOf(false) }
    modifyReelReelIdTextFieldState = reel.getReelId().toString()
    modifyReelReelNameTextFieldState = reel.getReelName()
    modifyReelReelArtistTextFieldState = reel.getReelArtist()
    modifyReelReelYearTextFieldState = reel.getReelYear().toString()
    modifyReelReelTypeTextFieldState = reel.getReelType()
    modifyReelReelRatingTextFieldState = reel.getReelRating()
    modifyReelReelGenreTextFieldState = reel.getReelGenre()
    modifyReelReelNotesTextFieldState = reel.getReelNotes()
    modifyReelIsOriginalCheckboxState = reel.getSelectedOriginalStatus2()
    modifyReelIsArchivedCheckboxState = reel.getSelectedArchivedStatus2()

    Card {


        Row(modifier = Modifier.padding(PaddingValues(top = 40.dp))) {
            Text(
                text = reel.getReelId().toString(),
                modifier = Modifier.padding(PaddingValues(start = 30.dp))
            )
            TextField(
                value = modifyReelReelNameTextFieldState,
                label = {
                    Text(text = "reel Name")
                },
                onValueChange = {
                    modifyReelReelNameTextFieldState = it
                },
                modifier = Modifier
                    .padding(PaddingValues(start = 50.dp))
                    .height(80.dp)
            )

        }

        // new row here//

        Row(modifier = Modifier.padding(PaddingValues(top = 135.dp))) {
            Spacer(modifier = Modifier.width(10.dp))
            Spacer(modifier = Modifier.width(10.dp))
            TextField(
                value = modifyReelReelArtistTextFieldState,
                label = {
                    Text("Artist")
                },

                onValueChange = {

                    modifyReelReelArtistTextFieldState = it

                },
                modifier = Modifier.size(width = 350.dp, height = 60.dp)

            )


        }
        Row(modifier = Modifier.padding(PaddingValues(top = 210.dp))) {

            TextField(value = modifyReelReelYearTextFieldState,

                label = {
                    Text("Year")
                },

                onValueChange = {

                    modifyReelReelYearTextFieldState = it

                },

                modifier = Modifier.padding(PaddingValues(start = 20.dp)).size(width = 100.dp, height = 60.dp)

            )

            Spacer(modifier = Modifier.width(20.dp))

            TextField(
                value = modifyReelReelTypeTextFieldState,
                label = {
                    Text("Type")
                },

                onValueChange = {

                    modifyReelReelTypeTextFieldState = it

                },

                modifier = Modifier.size(width = 100.dp, height = 60.dp)

            )
            Spacer(modifier = Modifier.width(15.dp))
            TextField(
                value = modifyReelReelRatingTextFieldState,
                label = {
                    Text("Rating")
                },

                onValueChange = {

                    modifyReelReelRatingTextFieldState = it

                },
                modifier = Modifier.size(width = 100.dp, height = 60.dp)

            )
        }
            Spacer(modifier = Modifier.width(15.dp))
            Row(modifier = Modifier.padding(PaddingValues(start = 20.dp, top = 285.dp))) {


            TextField(
                value = modifyReelReelGenreTextFieldState,
                label = {
                    Text("Genre")
                },

                onValueChange =
                {
                    modifyReelReelGenreTextFieldState = it
                },
                modifier = Modifier.size(width = 140.dp, height = 60.dp)
            )
        }



        Row(modifier = Modifier.padding(PaddingValues(top = 355.dp))) {
            Spacer(modifier = Modifier.width(20.dp))
            TextField(value = modifyReelReelNotesTextFieldState,

                label = {
                    Text("Notes")
                },


                onValueChange = {

                    modifyReelReelNotesTextFieldState = it

                },
                modifier = Modifier.size(width = 350.dp, height = 60.dp)


            )

        }

        Row(modifier = Modifier.padding(PaddingValues(top = 420.dp))) {

            Spacer(modifier = Modifier.width(20.dp))
            TextField(
                value = modifyReelReelLocationNameTextFieldState,

                label = {
                    Text("Location Name")
                },

                onValueChange = {

                    modifyReelReelLocationNameTextFieldState = it

                },

                modifier = Modifier.size(width = 120.dp, height = 60.dp)

            )



        }



        Row(modifier = Modifier.padding(PaddingValues(top = 560.dp))) {
            Spacer(modifier = Modifier.width(10.dp))
            Checkbox(checked = modifyReelIsOriginalCheckboxState,
                onCheckedChange = { modifyReelIsOriginalCheckboxState = it })
            Text(text = "Original" , modifier = Modifier.padding(PaddingValues(top = 12.dp)))
            Spacer(modifier = Modifier.width(10.dp))




            Checkbox(checked = modifyReelIsArchivedCheckboxState,
                onCheckedChange = { modifyReelIsArchivedCheckboxState = it },
                modifier = Modifier.padding(PaddingValues(start = 121.dp, bottom = 10.dp)))
            Text(text = "Archived" , modifier = Modifier.padding(PaddingValues(top = 12.dp)))


        }

        Button(
            onClick = {

                if (modifyReelReelIdTextFieldState.toInt() == 0 || modifyReelReelNameTextFieldState == "" ||
                    modifyReelReelArtistTextFieldState == "" || modifyReelReelYearTextFieldState == ""||
                     modifyReelReelTypeTextFieldState == "" || modifyReelReelGenreTextFieldState == "" ||
                     modifyReelReelLocationNameTextFieldState == "" ){



                    Toast.makeText(context,
                        "There was an error updating the reel please make sure all fields excluding notes and archival and original status are filled",
                        Toast.LENGTH_LONG).show()



                }else{




                        reelUpdater.updateReel( modifyReelReelIdTextFieldState.toInt(),
                            modifyReelReelNameTextFieldState, modifyReelReelArtistTextFieldState,
                            modifyReelReelYearTextFieldState.toInt(), modifyReelReelTypeTextFieldState,
                            modifyReelReelGenreTextFieldState, modifyReelReelRatingTextFieldState,
                            convertBooleanToInt(modifyReelIsOriginalCheckboxState),
                            modifyReelReelNotesTextFieldState,
                            modifyReelReelLocationNameTextFieldState,
                            convertBooleanToInt(modifyReelIsArchivedCheckboxState)
                        )
                        navController.navigate(Destination.ReelzsViewer.route)
                        Toast.makeText(context,
                            "reel Successfully updated",
                            Toast.LENGTH_LONG).show()





                }



            },
            modifier = Modifier.padding(PaddingValues(start = 40.dp, top = 640.dp))
        ) {
            Text(text = "Update Reel")
        }

        // delete button will go here
        val deleteUserButtonScope = rememberCoroutineScope()
        Button(onClick = {

            openReelDeletionDialog.value = true


        },
            modifier = Modifier.padding(PaddingValues(start = 250.dp, top = 638.dp))

            ) {

            Text(text = "Delete Reel")

        }

        if (openReelDeletionDialog.value) {

            AlertDialog(
                onDismissRequest = { openReelDeletionDialog.value = false },
                title = {
                    Text(text = "Reel Deletion Warning")
                },
                text = {
                    Text(text = "Please confirm you want to delete this reel")
                },
                confirmButton = {
                    TextButton(onClick = {
                        userConfirmedReelDeletion.value = true
                        println(userConfirmedReelDeletion.value)
                        openReelDeletionDialog.value = false
                    }) {


                        Text(text = "Confirm")


                    }

                },

                dismissButton = {
                    TextButton(
                        onClick = {
                            openReelDeletionDialog.value = false

                        }
                    ) {
                        Text(text = "Cancel")

                    }

                },

                )


        }
        deleteUserButtonScope.launch {
        withContext(Dispatchers.IO) {
            if (userConfirmedReelDeletion.value) {

                val reelDeleter = ReelDelete(context)
                val reelId: Int = reel.getReelId()
                userConfirmedReelDeletion.value = false
                reelDeleter.checkNumberOfUserReelIdsWithReelId(reelId)
            }

        }
            navController.navigate(Destination.ReelzsViewer.route)

        }

        Button(
            onClick = {
                navController.navigate(Destination.ReelzsViewer.route)
            },
            modifier = Modifier.padding(PaddingValues(start = 175.dp, top = 690.dp))
        ) {

            Text(text = "Back")

        }
    }

}


fun convertBooleanToInt(status:Boolean):Int{
    return if (status){
        1
    }else {
        0
    }}
