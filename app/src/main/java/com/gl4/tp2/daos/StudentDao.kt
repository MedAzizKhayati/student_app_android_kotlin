package com.gl4.tp2.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gl4.tp2.models.Student

@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    fun getAll(): List<Student>

    @Query("SELECT * FROM students WHERE id = :id")
    fun getById(id: Int): Student

    @Query("SELECT * FROM students WHERE registeredCourses LIKE '%' || :course ||'%' ")
    fun getByRegisteredCourse(course: String): List<Student>

    @Insert
    fun insert(student: Student)

    @Insert
    fun insertAll(students: List<Student>)

    @Update
    fun update(student: Student)

}