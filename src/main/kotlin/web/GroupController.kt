package web

import models.Group
import models.GroupDetails
import org.springframework.web.bind.annotation.*
import repositories.GroupRepository
//import services.GitHubService
//import services.User
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/groups")
class GroupController(val groupRepository: GroupRepository) {

    private fun groupExist(id: Int, response: HttpServletResponse){
        val group = groupRepository.getGroupDetails(id)
        if (group == null) response.status = 400
    }

    @GetMapping("/")
    fun allGroups(): List<Group?> = groupRepository.allGroups()

    @GetMapping("/{id}")
    fun selectById(@PathVariable id: Int, response: HttpServletResponse): GroupDetails?{
        groupExist(id, response)
        return groupRepository.getGroupDetails(id)
    }

    @GetMapping("/add/{name}")
    fun addGroup(@PathVariable name: String, response: HttpServletResponse) {
        groupRepository.addGroup(name, "description")
//        groupExist(id, response)
    }
}

