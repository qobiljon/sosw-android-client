package io.github.qobiljon.stressapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.core.database.AppDatabase
import io.github.qobiljon.stressapp.core.database.dao.*
import javax.inject.Singleton

@Module
@InstallIn(Singleton::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            context.getString(R.string.room_db_name)
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesSelfReportDao(database: AppDatabase): SelfReportDao {
        return database.selfReportDao()
    }

    @Provides
    @Singleton
    fun providesActivityRecognitionDao(database: AppDatabase): ActivityRecognitionDao {
        return database.activityRecognitionDao()
    }

    @Provides
    @Singleton
    fun providesActivityTransitionDao(database: AppDatabase): ActivityTransitionDao {
        return database.activityTransitionDao()
    }

    @Provides
    @Singleton
    fun providesCalendarEventDao(database: AppDatabase): CalendarEventDao {
        return database.calendarEventDao()
    }

    @Provides
    @Singleton
    fun providesCallLogDao(database: AppDatabase): CallLogDao {
        return database.callLogDao()
    }

    @Provides
    @Singleton
    fun providesLocationDao(database: AppDatabase): LocationDao {
        return database.locationDao()
    }

    @Provides
    @Singleton
    fun providesScreenStateDao(database: AppDatabase): ScreenStateDao {
        return database.screenStateDao()
    }
}