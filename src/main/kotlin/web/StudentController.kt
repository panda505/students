package web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import repositories.GroupRepository
import repositories.StudentRepository
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/students")
class StudentController(val studentRepository: StudentRepository,
                        val groupRepository: GroupRepository) {

    private fun studentExist(id: Int, response: HttpServletResponse) {
        val student = studentRepository.selectById(id)
        if (student == null) response.status = 400
    }

    private fun groupExist(group_id: Int, response: HttpServletResponse) {
        val group= groupRepository.getGroupDetails(group_id)
        if (group == null) response.status = 400
    }

    @GetMapping("/move-to-another-group/sutdent-id={id}&group-id={group_id}")
    fun moveToAnotherGroup(@PathVariable id: Int, @PathVariable group_id: Int, response: HttpServletResponse) {
        studentExist(id, response)
        groupExist(group_id, response)
        studentRepository.moveToAnotherGroup(id, group_id)
    }

    @GetMapping("/students-by-group/{group_id}")
    fun selectById(@PathVariable group_id: Int, response: HttpServletResponse){
        groupExist(group_id,response)
        studentRepository.groupStudents(group_id)
    }

    @GetMapping("/add/name={name}&age={age}&group_id={group_id}")
    fun deleteById(@PathVariable name: String,
                   @PathVariable age: Int,
                   @PathVariable group_id: Int,
                   response: HttpServletResponse) {
        studentRepository.addStudent(name, age, group_id)
//        studentExist(id, response)
    }

    @GetMapping("/remove/{id}")
    fun allGroups(@PathVariable id: Int, response: HttpServletResponse) {
        studentExist(id, response)
        studentRepository.removeStudent(id)
    }
}

