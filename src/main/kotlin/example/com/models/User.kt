package example.com.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Users : IntIdTable("users") {
    val userName = varchar("userName", 50)
    val teamName = varchar("teamName", 50)
    val rank = varchar("rank", 50)
    val auth = varchar("auth", 20)
    val email = varchar("email", 100)
    val phoneNumber = varchar("phoneNumber", 20)
    val createdAt = datetime("createdAt")
    val updatedAt = datetime("updatedAt")
    val state = varchar("state", 20)
    val profileImageUrl = varchar("profileImageUrl", 255)
}
