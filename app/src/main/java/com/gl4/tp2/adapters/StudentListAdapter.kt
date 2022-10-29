package com.gl4.tp2.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gl4.tp2.R
import com.gl4.tp2.daos.StudentDao
import com.gl4.tp2.databases.StudentsDatabase
import com.gl4.tp2.models.Student
import java.util.Locale

class StudentListAdapter(private var data: ArrayList<Student>, context: Context) :
    RecyclerView.Adapter<StudentListAdapter.ViewHolder>(), Filterable {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)
        return ViewHolder(view)
    }

    var dataFilterList = ArrayList<Student>()
    var course = ""
    private val dao: StudentDao
    init {
        dataFilterList = data
        val db = StudentsDatabase.getDatabase(context)
        dao = db.studentDao()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = dataFilterList[position]
        holder.itemView.findViewById<TextView>(R.id.student_name).text =
            "${student.firstName} ${student.lastName}"
        holder.itemView.findViewById<ImageView>(R.id.student_logo).setImageResource(
            when (student.sex) {
                Student.Sex.MALE -> when (position % 3) {
                    0 -> R.drawable.man1
                    1 -> R.drawable.man2
                    else -> R.drawable.man3
                }
                else -> when (position % 3) {
                    0 -> R.drawable.woman1
                    1 -> R.drawable.woman2
                    else -> R.drawable.woman3
                }
            }
        )
        val presentCheckBox = holder.itemView.findViewById<CheckBox>(R.id.is_present)
        presentCheckBox.isChecked =
            student.attendedCourses.contains(
                course
            )

        presentCheckBox.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed) {
                val isCoursePresent = student.attendedCourses.contains(course)
                if (isChecked && !isCoursePresent) {
                    student.attendedCourses.add(course)
                    dao.update(student)
                } else if (!isChecked && isCoursePresent) {
                    student.attendedCourses.remove(course)
                    dao.update(student)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                dataFilterList = data.filter {
                    it.registeredCourses.contains(course) && (charSearch.isEmpty() || (
                            it.firstName.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                            ))
                } as ArrayList<Student>

                val filterResults = FilterResults()
                filterResults.values = dataFilterList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // check if values are castable to arraylist<student>
                dataFilterList = results?.values as ArrayList<Student>
                notifyDataSetChanged()
            }

        }
    }
}