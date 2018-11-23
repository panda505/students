package repositories.impl

import models.Group
import models.GroupDetails
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import repositories.GroupRepository


@Repository
class GroupRepositoryImpl(val jdbcTemplate: NamedParameterJdbcTemplate) : GroupRepository {

    override fun addGroup(name: String, description: String): GroupDetails?{
        val parameters = MapSqlParameterSource()
                .addValue("name", name)
                .addValue("description", description)
        val sql = "INSERT INTO groups(name, description) values(:name, :description)"
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(sql, parameters, keyHolder)
        val id =  keyHolder.getKeys()?.get("id") as Int
        return getGroupDetails(id)
    }

    override fun allGroups(): List<Group?>{
        val parameters = emptyMap<String, Any>()
        return jdbcTemplate.queryForList("SELECT id, name FROM groups", parameters).map {
            Group(it["id"] as Int, it["name"] as String)
        }
    }

    override fun getGroupDetails(id: Int): GroupDetails?{
        val parameters = mapOf<String, Any>("id" to id)
        val sql = "SELECT id, name, description FROM groups WHERE id = :id"
        val group = jdbcTemplate.queryForMap(sql, parameters)
        return GroupDetails(group["id"] as Int, group["name"] as String, group["description"] as String)
    }
}

