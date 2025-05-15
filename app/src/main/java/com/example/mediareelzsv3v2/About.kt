package com.example.mediareelzsv3v2


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun About(navController: NavHostController) {

    Surface(

        color = MaterialTheme.colors.background

    ) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {


            AboutLabel()
            AboutTextBox()
            BackToInsertButton(navController)

        }
    }
}

@Composable
fun AboutLabel(){
    Text(text = "About", modifier = Modifier.padding(PaddingValues(start = 190.dp, top = 70.dp)))
}

@Composable
fun AboutTextBox(){
    Text(
        text = "Media Reelzs is a program for" +
                " users to keep track  of their physical media such as cds and dvds by 13 data points " +
                "Including:" +"\n"+
                "\n"+
                " name " +"\n"+
                " artist " +"\n"+
                " year" +"\n"+
                "type(cd/dvd/etc)" +"\n"+
                "genre" +"\n"+
                "rating(g/pg/r/NR/NA/ETC)" +"\n"+
                "original status( is an original release or re issue)" +"\n"+
                "location name, page number, page size , and slot number" +"\n"+
                "notes" +"\n"+
                "digitally archived status" +"\n"+
                "\n" +
                "This system is designed to make keeping track" + "\n"+
                " of your collection easier allowing for both" + "\n"+
                "single record insert  and bulk record insert( via csv files). " +"\n"+
                "While also having the option to update , " + "\n"+
                "delete records(reels) and search records with ease." +"\n"+
                "This application also supports multiple users allowing for you " + "\n" +
                " to keep track of your collections on one device with top of the line security to keep your collection safe." + "\n" +
                "There is also a feature to export your collection to csv files to keep a backup of " +
                "your collection in case of moving devices or accidental data deletion. " +
                "\n" +
                "Happy Collecting" +
                "\n" +
                "\n"+
                "Thomas Gustafson",
        modifier = Modifier
            .padding(PaddingValues(start = 20.dp, top = 110.dp))
            .size(350.dp, 500.dp)
            .verticalScroll(rememberScrollState())

    )
}
@Composable
fun BackToInsertButton(navController: NavHostController) {
    Button(onClick = {
        navController.navigate(Destination.InsertReel.route)
    },
        modifier = Modifier
            .padding(PaddingValues(start = 90.dp, top = 685.dp))
            .width(200.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(text =  "Back To Login")
    }
}
