package rs.djokafioka.composetodo.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import rs.djokafioka.composetodo.util.Constants.DATABASE_TABLE

/**
 * Created by Djordje on 6.4.2024..
 */
@Entity(tableName = DATABASE_TABLE)
data class ToDoTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority
)
