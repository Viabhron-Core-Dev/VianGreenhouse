package com.foss.aihub.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "notes")
data class VGHNote(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "prompts")
data class VGHQuickPrompt(
    @PrimaryKey val id: String,
    val name: String,
    val content: String,
    val sortOrder: Int
)

@Dao
interface VGHDao {
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<VGHNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: VGHNote)

    @Delete
    suspend fun deleteNote(note: VGHNote)

    @Query("SELECT * FROM prompts ORDER BY sortOrder ASC")
    fun getAllPrompts(): Flow<List<VGHQuickPrompt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrompt(prompt: VGHQuickPrompt)

    @Delete
    suspend fun deletePrompt(prompt: VGHQuickPrompt)
}

@Database(entities = [VGHNote::class, VGHQuickPrompt::class], version = 1, exportSchema = false)
abstract class VGHDatabase : RoomDatabase() {
    abstract fun vghDao(): VGHDao
}
