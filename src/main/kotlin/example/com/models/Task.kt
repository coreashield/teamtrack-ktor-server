package example.com.models

import example.models.ProjectList
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.ReferenceOption

object TaskAssignments : IntIdTable() {
    val projectId = integer("project_id").references(ProjectList.id, onDelete = ReferenceOption.CASCADE)  // 프로젝트 ID 추가
    val assignedUser = varchar("assigned_user", 50)
    val taskName = varchar("task_name", 50)
    val isAssignedByLeader = bool("is_assigned_by_leader")
    val taskStatus = varchar("task_status", 20)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
