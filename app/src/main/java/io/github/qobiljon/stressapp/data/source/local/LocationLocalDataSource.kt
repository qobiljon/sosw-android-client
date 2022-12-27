package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.data.Location

interface LocationLocalDataSource {
    fun getLocations(): List<Location>
    fun hasLocationExist(timestamp: Long): Boolean
    fun insertLocations(locations: List<Location>)
    fun deleteLocation(location: Location)
}