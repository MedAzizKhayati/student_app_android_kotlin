package com.gl4.tp2.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    val firstName: String,
    val lastName: String,
    val sex: Sex,
    val registeredCourses: ArrayList<String> = arrayListOf(),
    val attendedCourses: ArrayList<String> = arrayListOf(),
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    enum class Sex {
        MALE,
        FEMALE
    }
}
