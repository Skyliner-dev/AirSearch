# AirSearch App
## An Android Practice App which
> Uses Room for local data storage (In this case: airports & favourites)

> Uses Datastore (preference) for storing key value pair of textfield value (search bar content)

*You can either download the zip or clone this repository and open directly in Android studio (Recommended to update compileSdk version atleast to 35)*

### Project Structure 
```
Src 
  |_ data 
     |_ AirDao
     |_ AirDatabase
     |_ Airport
     |_ Favourite
     |_ AppContainer
     |_ Flights
     |_ OfflineAirRepository
  |_ ui
     |_ screens
  |_ AirSearchApplication
  |_ AppViewModelProvider
  |_ MainActivity
```  
**Note** : We prepopulated the database with airport entity defined as 
```
@Entity(tableName = "airports")
data class Airport(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "iata_code")
    val iataCode: String,
    val name: String,
    val city: String,
    val country: String,

)
```
and using LaunchedEffect(Unit) to instantiate the database just once, as this is not the intended part of the application, It's used just to insert all the airports from [Flights.kt](https://github.com/Skyliner-dev/AirSearch/blob/master/app/src/main/java/com/example/airsearch/data/Flights.kt)

Now Using preferences datastore we store the key value pair for value as the textfield in the search bar:
```
    private companion object {
        const val TAG = "UserSearchPreferenceRepo"
        val TEXT_FIELD= stringPreferencesKey("text_field")
        val IS_PREPOPULATED = booleanPreferencesKey("is_prepopulated")
    }
    suspend fun saveTextFieldPreference(value: String) {
        dataStore.edit {
            preferences ->
            preferences[TEXT_FIELD] = value
        }
    }
    suspend fun saveIsPrepopulatedPreference(value: Boolean) {
        dataStore.edit {
            preferences -> preferences[IS_PREPOPULATED] = value
        }
    }
    val isPrepopulated: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading pref boolean", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            preferences -> preferences[IS_PREPOPULATED] ?: false
        }
    val textFieldValue: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading pref", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
        preferences -> preferences[TEXT_FIELD] ?: ""
    }
```
Room Implementation is done by creating the database and having two of our entities `entities = [Airport::class, Favourite::class]` respectively.

The Sql Part of this application is done in the Dao of retrieving the airports using LIKE keyword `@Query("SELECT * FROM airports WHERE iata_code LIKE :code OR LOWER(name) LIKE :name")` and we use wildcard inside the airportViewModel to acheive this
```
 viewModelScope.launch {
            airportsRepository.getAirportSuggestions(
                code =
                if (text.isEmpty()) "" else
                text.uppercase() + "%",
                name =
                if (text.isEmpty()) "" else
                text.lowercase() + "%"
            ).map { AirportUiState(it) }  .collect {
                _airportUiState.value = _airportUiState.value.copy(airports = it.airports)
            }
        }
```
This is used in the OutlinedTextFieldComposable as
```
onValueChange = {
                    userPreferencesViewModel.updateSearchPreferences(it)
                    coroutineScope.launch {
                        airportsViewModel.getAirportSuggestions(it)
                        airportsViewModel.resetSelected()
                    }
                    searchFieldText = it
                }
```
whenever input changes it updates the userPreference, launches coroutine scoping to this lifecycle itself and gets the suggestions as the user types when user clicks the suggestion the screen is changed, however when the user changes the input it needs to get reset so that
It again shows the suggestion instead of showing previously selected airports available flights.

### ScreenShots :
![Available Flights from Airport](https://github.com/Skyliner-dev/AirSearch/blob/master/s1.png)
![Adding a flight to favourites by liking it](https://github.com/Skyliner-dev/AirSearch/blob/master/s2.png)
![Viewing The Favourites](https://github.com/Skyliner-dev/AirSearch/blob/master/s3.png) *Only Happens when the textField is Empty*
