package io.github.qobiljon.stressapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.qobiljon.stressapp.core.database.data.SelfReport
import io.github.qobiljon.stressapp.data.source.local.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SourceModule {

    @get:Binds
    @get:Singleton
    val DefaultCallLogLocalDataSource.bindCallLog: CallLogLocalDataSource

    @get:Binds
    @get:Singleton
    val DefaultStateLocalDataSource.bindState: StateLocalDataSource

    @get:Binds
    @get:Singleton
    val DefaultSelfReportLocalDataSource.bindSelfReport: SelfReportLocalDataSource

    @get:Binds
    @get:Singleton
    val DefaultLocationLocalDataSource.bindLocation: LocationLocalDataSource

    @get:Binds
    @get:Singleton
    val DefaultRecognitionLocalDataSource.bindRecognition: RecognitionLocalDataSource

    @get:Binds
    @get:Singleton
    val DefaultEventLocalDataSource.bindEvent: EventLocalDataSource

    @get:Binds
    @get:Singleton
    val DefaultTransitionLocalDataSource.bindTransition: TransitionLocalDataSource

}