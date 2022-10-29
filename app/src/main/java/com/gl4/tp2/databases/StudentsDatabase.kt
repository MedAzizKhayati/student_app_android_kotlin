package com.gl4.tp2.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gl4.tp2.daos.StudentDao
import com.gl4.tp2.models.Student

@Database(entities= [Student::class], version = 1)
@TypeConverters(Converters::class)
abstract class StudentsDatabase: RoomDatabase() {
    abstract fun studentDao(): StudentDao
    companion object {
        @Volatile
        private var instance: StudentsDatabase? = null
        fun getDatabase(context: Context): StudentsDatabase {
            if(instance != null)
                return instance!!
            val db = Room.databaseBuilder(
                context.applicationContext,
                StudentsDatabase::class.java, "students_database"
            )
                .allowMainThreadQueries()
                .build()
            instance = db
            return instance!!
        }
    }
}