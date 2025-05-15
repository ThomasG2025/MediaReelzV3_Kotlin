package com.example.mediareelzsv3v2
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

import com.example.mediareelzsv3v2.Database.DataBaseHandler
import com.example.mediareelzsv3v2.Destination
import com.example.mediareelzsv3v2.Objects.Reel2
import com.example.mediareelzsv3v2.ReelzsSearchViewViewModel
import com.example.mediareelzsv3v2.ui.theme.MediaReelzsV3V2Theme


@Composable
fun  ReelzsViewer(navController: NavHostController, context: Context) {

    val backButtonEnabled by remember { mutableStateOf(true) }
    val editButtonEnabled by remember { mutableStateOf(true) }



    MediaReelzsV3V2Theme {
        // A surface container using the 'background' color from the theme

        val viewModel = viewModel<ReelzsSearchViewViewModel>()
        val searchText by viewModel.searchText.collectAsState()
        val reelzs by viewModel.reelzs.collectAsState()
        val searchResults = viewModel.amountOfResultsFromSearch.collectAsState()


        DataBaseHandler(context = context)

        viewModel.FetchReelzs(viewViewModel = viewModel)
        

        Surface(

            color = MaterialTheme.colors.background

        ) {


            Spacer(modifier = Modifier.height(60.dp))
            Box(modifier = Modifier.fillMaxSize()) {

                Text(text = "${searchResults.value} records")
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(bottom = 80.dp),
                    contentPadding = PaddingValues(top = 60.dp)
                ) {
                    
                    items(reelzs) { reel ->

                        Card(
                            shape = MaterialTheme.shapes.large,
                            elevation = 8.dp,

                            ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(PaddingValues(top = 50.dp))
                            ) {


                                Row {
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(text = reel.getReelId().toString())
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Text(text = reel.getReelName())

                                }

                                Spacer(modifier = Modifier.height(20.dp))
                                Row {
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(text = reel.getReelArtist())
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(text = reel.getReelYear().toString())
                                    Spacer(modifier = Modifier.width(20.dp))

                                }
                                Spacer(modifier = Modifier.height(25.dp))
                                Row {

                                    Spacer(modifier = Modifier.width(20.dp))
                                    Text(text = reel.getReelType())
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(text = reel.getReelRating())
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(text = reel.getReelGenre())



                                }

                                Spacer(modifier = Modifier.height(25.dp))

                                Row {
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Text(text = reel.getReelNotes())

                                }

                                Spacer(modifier = Modifier.height(15.dp))



                                Spacer(modifier = Modifier.height(30.dp))


                                Row {
                                    Spacer(modifier = Modifier.width(10.dp))

                                    Checkbox(
                                        checked = reel.getSelectedOriginalStatus2(),
                                        onCheckedChange = null
                                    )
                                    Text(text = "Original")
                                    Spacer(modifier = Modifier.width(10.dp))



                                    Spacer(modifier = Modifier.width(10.dp))

                                    Checkbox(
                                        checked = reel.getSelectedArchivedStatus2(),
                                        onCheckedChange = null
                                    )
                                    Text(text = "Archived")

                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Row {
                                    Spacer(modifier = Modifier.width(30.dp))

                                    Button(
                                        onClick = {

                                            val reelId = reel.getReelId()
                                            val reelName = reel.getReelName()
                                            val reelArtist = reel.getReelArtist()
                                            val reelYear = reel.getReelYear()
                                            val reelType = reel.getReelType()
                                            val reelGenre = reel.getReelGenre()
                                            val reelRating = reel.getReelRating()
                                            val selectedOriginalStatus =
                                                reel.getSelectedOriginalStatus()
                                            val reelNotes = reel.getReelNotes()
                                            val selectedArchivedStatus =
                                                reel.getSelectedArchivedStatus()

                                            val reel2 = Reel2(
                                                reelId,
                                                reelName,
                                                reelArtist,
                                                reelYear,
                                                reelType,
                                                reelGenre,
                                                reelRating,
                                                selectedOriginalStatus,
                                                reelNotes,
                                                selectedArchivedStatus
                                            )
                                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                                key = "reel2",
                                                value = reel2
                                            )
                                            navController.navigate(Destination.ModifyReel.route)


                                        },
                                        enabled = editButtonEnabled,
                                        modifier = Modifier.padding(PaddingValues(start = 130.dp))

                                        ) {

                                        Text(text = "Modify")
                                    }

                                    Spacer(modifier = Modifier.width(30.dp))






                                }
                            }
                        }

                    }
                }

                Button(
                    onClick = {

                        navController.navigate(Destination.InsertReel.route)


                    },
                    enabled = backButtonEnabled,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)

                ) {
                    Text(text = "Back")
                }

                Button(
                    onClick = {

                        navController.navigate(Destination.Login.route)


                    },
                    enabled = backButtonEnabled,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp)

                ) {
                    Text(text = "Back To Login")
                }





                TextField(
                    value = searchText,
                    onValueChange = viewModel::onSearchTextChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Search") }
                )

            }


        }


    }


}
    fun convertCheckboxResultToInt2(checkBoxStatus: Boolean): Int {
        var convertedState = 0

        if (checkBoxStatus) {
            convertedState = 1
        }

        return convertedState
    }











