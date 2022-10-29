package com.gl4.tp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gl4.tp2.adapters.StudentListAdapter
import com.gl4.tp2.daos.StudentDao
import com.gl4.tp2.databases.StudentsDatabase
import com.gl4.tp2.models.Student

class MainActivity : AppCompatActivity() {
    private val spinner: Spinner by lazy { findViewById(R.id.spinner) }
    private val studentRecyclerView: RecyclerView by lazy { findViewById(R.id.recycler_view) }
    private val studentSearch: EditText by lazy { findViewById(R.id.search_student) }
    private var courses = listOf("Lecture", "Practical Work")
    private var students = arrayListOf<Student>()
    private lateinit var studentDao: StudentDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = StudentsDatabase.getDatabase(this)
        studentDao = db.studentDao()
        initCourseSpinner()
        initStudentRecyclerView()
        initStudentSearch()
    }

    private fun initStudentSearch() {
        studentSearch.addTextChangedListener {
            val studentAdapter = studentRecyclerView.adapter as StudentListAdapter
            studentAdapter.filter.filter(studentSearch.text)
        }
    }

    private fun fillStudents(n: Int) {
        students = studentDao.getAll() as ArrayList<Student>
        if (students.size == 0) {
            for (i in 1..n) {
                val registeredCourses = getRandomListOfCourses()
                val attendedCourses = randomChoice(registeredCourses)
                students.add(
                    Student(
                        "Firstname$i",
                        "Lastname",
                        if (i % 2 == 0) Student.Sex.FEMALE
                        else Student.Sex.MALE,
                        registeredCourses,
                        attendedCourses
                    )
                )
            }
            studentDao.insertAll(students)
        }
    }

    private fun randomChoice(list: ArrayList<String>): ArrayList<String> {
        val result = arrayListOf<String>()
        for (item in list) {
            if (Math.random() < 0.5) {
                result.add(item)
            }
        }
        return result
    }

    private fun getRandomListOfCourses(): ArrayList<String> {
        val courses = arrayListOf<String>()
        when ((0..2).random()) {
            0 -> courses.add("Lecture")
            1 -> courses.add("Practical Work")
            else -> {
                courses.add("Lecture")
                courses.add("Practical Work")
            }
        }
        return courses
    }

    private fun initStudentRecyclerView() {
        // Create A List Of Students
        fillStudents(30)

        // Apply The Adapter To The RecyclerView
        studentRecyclerView.apply {
            adapter = StudentListAdapter(students, this@MainActivity)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun initCourseSpinner() {
        spinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_dropdown_item_1line, courses
        )
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Toast
                Toast.makeText(
                    this@MainActivity,
                    "You Selected ${courses[position]}",
                    Toast.LENGTH_SHORT
                ).show()
                val studentAdapter = studentRecyclerView.adapter as StudentListAdapter
                studentAdapter.course = courses[position]
                studentAdapter.filter.filter(studentSearch.text)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }
    }
}