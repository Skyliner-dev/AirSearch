package com.example.airsearch.data


//list of total airports for our data
val flights = listOf(
    Airport(iataCode = "LAX", name =  "Los Angeles International Airport", city =  "Los Angeles", country =  "United States"),
    Airport(iataCode = "JFK", name =  "John F. Kennedy International Airport", city =  "New York", country =  "United States"),
    Airport(iataCode = "SFO", name =  "San Francisco International Airport", city =  "San Francisco", country =  "United States"),
    Airport(iataCode = "ORD", name =  "Chicago O'Hare International Airport", city =  "Chicago", country =  "United States"),
    Airport(iataCode = "HND", name =  "Tokyo International Airport", city =  "Tokyo", country =  "Japan"),
    Airport(iataCode = "DXB", name =  "Dubai International Airport", city =  "Dubai", country =  "United Arab Emirates"),
    Airport(iataCode = "SIN", name =  "Singapore Changi Airport", city =  "Singapore", country =  "Singapore"),
    Airport(iataCode = "IST", name =  "Istanbul Airport", city =  "Istanbul", country =  "Turkey"),
    Airport(iataCode = "CDG", name =  "Charles de Gaulle Airport", city =  "Paris", country =  "France"),
    Airport(iataCode = "FRA", name =  "Frankfurt Airport", city =  "Frankfurt", country =  "Germany"),
    Airport(iataCode = "AMS", name =  "Amsterdam Airport", city =  "Amsterdam", country =  "Netherlands"),
    Airport(iataCode = "LHR", name =  "London Heathrow Airport", city =  "London", country =  "United Kingdom"),
    Airport(iataCode = "KUL", name = "Kuala Lumpur International Airport", city = "Kuala Lumpur", country = "Malaysia"),
    Airport(iataCode = "MAA", name = "Chennai International Airport", city = "Chennai", country = "India"), // Chennai Airport
    Airport(iataCode = "DEL", name = "Indira Gandhi International Airport", city = "Delhi", country = "India"),
    Airport(iataCode = "BOM", name = "Chhatrapati Shivaji Maharaj International Airport", city = "Mumbai", country = "India"),
    Airport(iataCode = "BLR", name = "Kempegowda International Airport", city = "Bangalore", country = "India"),
    Airport(iataCode = "HYD", name = "Rajiv Gandhi International Airport", city = "Hyderabad", country = "India"),
    Airport(iataCode = "CCU", name = "Netaji Subhas Chandra Bose International Airport", city = "Kolkata", country = "India")
)

//using list to prepopulate mapping each airport to every other airport
fun flightListBuilder(from: Airport):List<Airport> =
flights.filter { it.iataCode != from.iataCode }




fun getAirportName(iataCode: String): String? {
    val name = when (iataCode.uppercase()) {
        "LAX" -> "Los Angeles International Airport"
        "JFK" -> "John F. Kennedy International Airport"
        "SFO" -> "San Francisco International Airport"
        "ORD" -> "Chicago O'Hare International Airport"
        "HND" -> "Tokyo International Airport"
        "DXB" -> "Dubai International Airport"
        "SIN" -> "Singapore Changi Airport"
        "IST" -> "Istanbul Airport"
        "CDG" -> "Charles de Gaulle Airport"
        "FRA" -> "Frankfurt Airport"
        "AMS" -> "Amsterdam Airport"
        "LHR" -> "London Heathrow Airport"
        "KUL" -> "Kuala Lumpur International Airport"
        "MAA" -> "Chennai International Airport"
        "DEL" -> "Indira Gandhi International Airport"
        "BOM" -> "Chhatrapati Shivaji Maharaj International Airport"
        "BLR" -> "Kempegowda International Airport"
        "HYD" -> "Rajiv Gandhi International Airport"
        "CCU" -> "Netaji Subhas Chandra Bose International Airport"
        else -> null
    }
    return name
}