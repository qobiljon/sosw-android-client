package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.dao.ScreenStateDao
import io.github.qobiljon.stressapp.core.database.data.ScreenState
import javax.inject.Inject

class DefaultStateLocalDataSource @Inject constructor(
    private val screenStateDao: ScreenStateDao
) : StateLocalDataSource {
    override fun getScreenStates(): List<ScreenState> {
        return screenStateDao.getAll()
    }

    override fun insertScreenStates(screenStates: List<ScreenState>) {
        return screenStateDao.insertAll(screenStates)
    }

    override fun deleteScreenState(screenState: ScreenState) {
        return screenStateDao.delete(screenState)
    }
}