package example.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.date

object ProjectList : IntIdTable("project_list") {
    val projectName = varchar("project_name", 255)
    val leaderId = integer("leader_id")
    val status = varchar("status", 50)
    val startDate = date("start_date")
    val endDate = date("end_date")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}
