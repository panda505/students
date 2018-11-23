package repositories.impl

import models.Student
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import repositories.StudentRepository


@Repository
class StudentRepositoryImpl(val jdbcTemplate: NamedParameterJdbcTemplate) : StudentRepository {

    override fun selectById(id: Int): Student? = try {
        val parameters = mapOf<String, Any>("id" to id)
        val sql = "SELECT id, name, age FROM student WHERE id = :id"
        val student = jdbcTemplate.queryForMap(sql, parameters)
        Student(student["id"] as Int, student["name"] as String, student["age"] as Int)
    }  catch (e: DataAccessException){
        null
    }

    override fun addStudent(name: String, age: Int, group_id: Int): Student?{
        val parameters = MapSqlParameterSource()
                .addValue("name", name)
                .addValue("age", age)
                .addValue("group_id", group_id)
        val sql = "INSERT INTO student(name, age, group_id) values(:name, :age, :group_id)"
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(sql, parameters, keyHolder)
        val id =  keyHolder.getKeys()?.get("id") as Int
        return selectById(id)
    }

    override fun groupStudents(group_id: Int): List<Student?>{
        val parameters = emptyMap<String, Any>()
        val sql = "SELECT id, name, age FROM student INNER JOIN groups ON student.group_id = groups.id"
        return jdbcTemplate.queryForList(sql, parameters).map {
            Student(it["id"] as Int, it["name"] as String, it["age"] as Int)
        }
    }

    override fun removeStudent(id: Int){
        val parameters = mapOf<String, Any>("id" to id)
        jdbcTemplate.update("DELETE FROM student WHERE id = :id", parameters)
    }

    override fun moveToAnotherGroup(id: Int, group_id: Int){
        val parameters = mapOf<String, Any>("id" to id, "group_id" to group_id)
        jdbcTemplate.update("UPDATE student SET group_id = :group_id WHERE id = :id", parameters)
    }
}

