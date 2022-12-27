package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.data.ScreenState

interface StateLocalDataSource {
    fun getScreenStates(): List<ScreenState>
    fun insertScreenStates(screenStates: List<ScreenState>)
    fun deleteScreenState(screenState: ScreenState)
}