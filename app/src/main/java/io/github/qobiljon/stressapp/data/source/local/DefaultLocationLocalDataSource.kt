package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.dao.LocationDao
import io.github.qobiljon.stressapp.core.database.data.Location
import javax.inject.Inject

class DefaultLocationLocalDataSource @Inject constructor(
    private val locationDao: LocationDao
) : LocationLocalDataSource {

    override fun getLocations(): List<Location> {
        return locationDao.getAll()
    }

    override fun hasLocationExist(timestamp: Long): Boolean {
        return locationDao.exists(timestamp)
    }

    override fun insertLocations(locations: List<Location>) {
        return locationDao.insertAll(locations)
    }

    override fun deleteLocation(location: Location) {
        return locationDao.delete(location)
    }
}