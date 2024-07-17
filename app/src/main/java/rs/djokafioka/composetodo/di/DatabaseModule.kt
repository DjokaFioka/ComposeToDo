package rs.djokafioka.composetodo.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import rs.djokafioka.composetodo.data.ToDoDatabase
import rs.djokafioka.composetodo.util.Constants.DATABASE_NAME
import javax.inject.Singleton

/**
 * Created by Djordje on 6.4.2024..
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ToDoDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: ToDoDatabase) = database.toDoDao()

}