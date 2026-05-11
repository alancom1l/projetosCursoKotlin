package com.example.crudapicep.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.crudapicep.model.Contato

@Database(entities = [Contato::class], version = 1, exportSchema = false)
abstract class ContatoDatabase : RoomDatabase() {
    abstract fun contatoDao(): ContatoDao

    companion object {
        @Volatile
        private var INSTANCE: ContatoDatabase? = null

        fun getDatabase(context: Context): ContatoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContatoDatabase::class.java,
                    "contato_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}