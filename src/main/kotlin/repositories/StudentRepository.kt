package repositories

import models.Student

interface StudentRepository{
    fun addStudent(name: String, age: Int, group_id: Int): Student?
    fun groupStudents(group_id: Int): List<Student?>
    fun removeStudent(id: Int)
    fun moveToAnotherGroup(id: Int, group_id: Int)
    fun selectById(id: Int): Student?
}

