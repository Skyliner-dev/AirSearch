package com.example.airsearch.ui.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.airsearch.AppViewModelProvider
import com.example.airsearch.R
import com.example.airsearch.data.Airport
import com.example.airsearch.data.Favourite
import com.example.airsearch.data.flightListBuilder
import com.example.airsearch.data.flights
import com.example.airsearch.data.getAirportName
import com.example.airsearch.ui.theme.AirSearchTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirHomeScreen(
    airportsViewModel: AirportsViewModel = viewModel(factory = AppViewModelProvider.factory),
    favouriteViewModel: FavouriteViewModel = viewModel(factory = AppViewModelProvider.factory),
    userPreferencesViewModel: UserPreferencesViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val otherScope = rememberCoroutineScope()
    val isPrepopulated by userPreferencesViewModel.isPrepopulated.collectAsState()
    LaunchedEffect(Unit) {
        if (!isPrepopulated.isPrepopulated) {
//            coroutineScope.launch {
//                flights.forEach {
//                    airportsViewModel.insertAirport(it)
//                }
//            }
            userPreferencesViewModel.updateIsPrepopulatedPreferences(true)
        }

    }
//
//    var selectedAirport by remember {
//        mutableStateOf<Airport?>(value = null)
//    }

    val airports by airportsViewModel.airportUiState.collectAsState()
    val selectedAirport by airportsViewModel.selectedAirport.collectAsState()
    val searchText by userPreferencesViewModel.userPrefUiState.collectAsState()
    val favourites by favouriteViewModel.favouritesUiState.collectAsState()
    var searchFieldText by remember {
        mutableStateOf("")
    }
    searchFieldText = searchText.textFieldValue

    val focusRequester = remember { FocusRequester() }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Air Search", fontWeight = FontWeight.SemiBold, fontSize = 24.sp) }
            )
        }
    ) {
        innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            OutlinedTextField(
                value = searchFieldText,
                onValueChange = {
                    userPreferencesViewModel.updateSearchPreferences(it)
                    coroutineScope.launch {
                        airportsViewModel.getAirportSuggestions(it)
                        airportsViewModel.resetSelected()
                    }
                    searchFieldText = it
                },
                shape = RoundedCornerShape(20.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
                label = { Text("Search For Flights") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            otherScope.launch {
                                delay(1000)
                                focusRequester.requestFocus()
                            }
                        }
                    }
                    .focusable(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)


            )
//            Text("Hello + ${airports.airports.size}")
//            Text(searchText.textFieldValue)
//            Text("${favourites.favourites.size}")
//            Text("${selectedAirport!!}")
//            Text("${isPrepopulated.isPrepopulated}")
            if (searchFieldText.isEmpty() || searchText.textFieldValue.isEmpty()) {
                LazyColumn {
                    item {
                        Row(Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Text("Favourites", color = Color.Gray)
                            Spacer(Modifier.padding(4.dp))
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color.Gray,
                                modifier = Modifier.size(20.dp))
                        }
                    }
                    items(favourites.favourites) {
                        FavouriteCard(favourite = it)
                    }
                }
            } else if (selectedAirport?.airport == null){
                LazyColumn {
                    items(airports.airports) { airport ->
                        SuggestionItem(airport, onSuggestionClick = {
                            userPreferencesViewModel.updateSearchPreferences(it.iataCode)
                            searchFieldText = it.iataCode
                            airportsViewModel.selectAirport(it)
                            focusRequester.freeFocus()
                        })
                    }
                }
            } else {

                LazyColumn {
                    if (selectedAirport!!.airport !=null) {
                        items(flightListBuilder(selectedAirport?.airport!!)) { airport ->
                            FlightCard(
                                arrival = selectedAirport?.airport!!,
                                departure = airport,
                                isLiked = {
                                    if (it.third) {
                                        coroutineScope.launch {
                                            favouriteViewModel.addFavourite(
                                                Favourite(
                                                    departureCode = it.first,
                                                    destinationCode = it.second
                                                )
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }


        }
    }
}

@Composable
fun SuggestionItem(
    airport: Airport,
    onSuggestionClick:(Airport) -> Unit
) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 2.dp, horizontal = 16.dp)
        .clickable(
            onClick = { onSuggestionClick(airport) }
        )) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(airport.iataCode, fontSize = 16.sp ,fontWeight = FontWeight.SemiBold)
            Text(airport.name, fontSize = 12.sp, fontWeight = FontWeight.Light)
        }
    }
}

@Composable
fun FavouriteCard(
    favourite: Favourite
) {
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Arrival", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//               Icon(imageVector = Icons.Default.Favorite, contentDescription = null,
//                   modifier = Modifier.size(20.dp), tint = Color.Red)
                Icon(painter = painterResource(R.drawable.bookmark_heart), contentDescription = null,
                    tint = Color.Red)
            }
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(favourite.departureCode, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(getAirportName(favourite.departureCode)?:"NUL", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Icon(painter = painterResource(R.drawable.flight_takeoff), contentDescription = "takeOff")
            }
            HorizontalDivider()
            Text("Departure", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(favourite.destinationCode, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(getAirportName(favourite.destinationCode)?:"NUL", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Icon(painter = painterResource(R.drawable.flight_land), contentDescription = "land")
            }
        }
    }
}

@Composable
fun FlightCard(
    arrival: Airport,
    departure: Airport,
    isLiked:(Triple<String,String,Boolean>) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var liked by remember {
        mutableStateOf(false)
    }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.wrapContentWidth(align = Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(arrival.iataCode, fontWeight = FontWeight.Bold)
                    Text("- - -", fontWeight = FontWeight.ExtraBold)
                    Text(departure.iataCode, fontWeight = FontWeight.Bold)
                    Icon(imageVector = if (!liked) Icons.Default.FavoriteBorder else Icons.Default.Favorite, contentDescription = null,modifier = Modifier
                        .size(16.dp)
                        .clickable(
                            onClick = {
                                liked = !liked
                                isLiked(
                                    Triple(
                                        arrival.iataCode,
                                        departure.iataCode,
                                        liked
                                    )
                                )
                            }
                        ))
                }
                Icon(painter = painterResource(R.drawable.flight), contentDescription = null)

            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedCard(modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painter = painterResource(R.drawable.flight_takeoff),contentDescription = null)
                        Text(arrival.city)
                        Text(arrival.country, fontSize = 12.sp)
                    }
                }
                OutlinedCard(modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                        Icon(painter = painterResource(R.drawable.flight_land),contentDescription = null)
                        Text(departure.city)
                        Text(departure.country, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun FlightCardPreview() {
    AirSearchTheme {
        FlightCard(arrival = flights[0], departure = flights[1], isLiked = {  } )
    }
}
@Preview
@Composable
private fun FavouriteCardPreview() {
    AirSearchTheme {
        FavouriteCard(Favourite(departureCode = "LAX", destinationCode = "JFK"))
    }
}
@Preview
@Composable
private fun AirHomeScreenPreview() {
//    AirSearchTheme {
//        AirHomeScreen(
//         viewModel(factory = AppViewModelProvider.factory),
//       viewModel(factory = AppViewModelProvider.factory),
//            {}
//        )
//    }
}

@Preview
@Composable
private fun SuggestionItemPreview() {
    AirSearchTheme {
        SuggestionItem(flights[0],{})
    }

}