package repositories

import models.Group
import models.GroupDetails

interface GroupRepository{
    fun addGroup(name: String, description: String): GroupDetails?
    fun allGroups(): List<Group?>
    fun getGroupDetails(id: Int): GroupDetails?
}

