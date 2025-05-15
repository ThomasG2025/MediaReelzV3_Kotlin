package com.example.mediareelzsv3v2

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediareelzsv3v2.Database.DataBaseHandler
import com.example.mediareelzsv3v2.Objects.Reel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//Edge bypass//

class ReelzsSearchViewViewModel : ViewModel() {





    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()


    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _reelzs = MutableStateFlow(ArrayList<Reel>())

    val reelzs = searchText.combine(_reelzs) { text, reelzs ->
        if (text.isBlank()) {
            reelzs
        } else {
            withContext(Dispatchers.IO) {



                reelzs.filter {

                    it.doesMatchSearchQuery(text)


                }


            }
        }

    }
        .stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(4000),


            _reelzs.value

        )
    private val _amountOfResultsFromSearch = MutableStateFlow(0)
    val amountOfResultsFromSearch: StateFlow<Int> = _amountOfResultsFromSearch.asStateFlow()



    fun onSearchTextChange(text: String) {
        _searchText.value = text

        updateSearchResultsCount()

    }


    @SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
    @Composable
    fun FetchReelzs(viewViewModel: ReelzsSearchViewViewModel){
        val context = LocalContext.current


        viewModelScope.launch {

            withContext(Dispatchers.IO) {

                val dbHelper = DataBaseHandler(context)

                val loadedReelzs = withContext(Dispatchers.IO) {


                    dbHelper.getReelzs()

                }








                viewViewModel._reelzs.value = loadedReelzs



            }

            updateSearchResultsCount()



        }}


    private fun updateSearchResultsCount() {
        val searchTextValue = _searchText.value
        val filteredReelzs = _reelzs.value.filter { it.doesMatchSearchQuery(searchTextValue) }
        _amountOfResultsFromSearch.value = filteredReelzs.size
    }







}


