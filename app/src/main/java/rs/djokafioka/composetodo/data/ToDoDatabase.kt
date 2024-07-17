package rs.djokafioka.composetodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import rs.djokafioka.composetodo.data.models.ToDoTask

/**
 * Created by Djordje on 6.4.2024..
 */
@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {

    abstract fun toDoDao(): ToDoDao
}