package example.com.plugins.tasks

import example.com.dto.TaskAssignmentsDTO
import example.com.models.TaskAssignments
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

fun Route.tasksRoutes() {
    get("/projects/{projectid}/tasks") {
        val projectId = call.parameters["projectid"]?.toIntOrNull()
        if (projectId == null) {
            call.respondText("Invalid project ID", status = HttpStatusCode.BadRequest)
            return@get
        }

        val tasks = transaction {
            TaskAssignments.selectAll().where { TaskAssignments.projectId eq projectId }.map { task ->
                TaskAssignmentsDTO(
                    id = task[TaskAssignments.id].value,
                    projectId = task[TaskAssignments.projectId],  // projectId 추가
                    assignedUser = task[TaskAssignments.assignedUser],
                    taskName = task[TaskAssignments.taskName],
                    isAssignedByLeader = task[TaskAssignments.isAssignedByLeader],
                    taskStatus = task[TaskAssignments.taskStatus],
                    createdAt = task[TaskAssignments.createdAt].toString(),
                    updatedAt = task[TaskAssignments.updatedAt].toString(),
                )
            }
        }

        val jsonResponse = Json.encodeToString(tasks)
        if (tasks.isEmpty()) {
            call.respondText("No TaskAssignments found for project ID $projectId.", status = HttpStatusCode.NotFound)
        } else {
            call.respondText(jsonResponse, ContentType.Application.Json)
        }
    }

    get("/projects/{projectid}/tasks/{assignedUser}") {
        val projectId = call.parameters["projectid"]?.toIntOrNull()
        val assignedUser = call.parameters["assignedUser"]

        if (projectId == null || assignedUser == null) {
            call.respondText("Invalid project ID or assignedUser", status = HttpStatusCode.BadRequest)
            return@get
        }

        val tasks = transaction {
            TaskAssignments.selectAll()
                .where { (TaskAssignments.projectId eq projectId) and (TaskAssignments.assignedUser eq assignedUser) }
                .map { task ->
                TaskAssignmentsDTO(
                    id = task[TaskAssignments.id].value,
                    projectId = task[TaskAssignments.projectId],  // projectId 추가
                    assignedUser = task[TaskAssignments.assignedUser],
                    taskName = task[TaskAssignments.taskName],
                    isAssignedByLeader = task[TaskAssignments.isAssignedByLeader],
                    taskStatus = task[TaskAssignments.taskStatus],
                    createdAt = task[TaskAssignments.createdAt].toString(),
                    updatedAt = task[TaskAssignments.updatedAt].toString(),
                )
            }
        }

        if (tasks.isEmpty()) {
            call.respondText("No tasks found for assignedUser: $assignedUser in project ID $projectId.", status = HttpStatusCode.NotFound)
        } else {
            val jsonResponse = Json.encodeToString(tasks)
            call.respondText(jsonResponse, ContentType.Application.Json)
        }
    }

    post("/projects/{projectid}/tasks") {
        val projectId = call.parameters["projectid"]?.toIntOrNull()
        if (projectId == null) {
            call.respondText("Invalid project ID", status = HttpStatusCode.BadRequest)
            return@post
        }

        val task = call.receive<TaskAssignmentsDTO>()
        transaction {
            TaskAssignments.insert {
                it[this.projectId] = projectId
                it[assignedUser] = task.assignedUser
                it[taskName] = task.taskName
                it[isAssignedByLeader] = task.isAssignedByLeader
                it[taskStatus] = task.taskStatus
                it[createdAt] = LocalDateTime.parse(task.createdAt)
                it[updatedAt] = LocalDateTime.parse(task.updatedAt)
            }
        }

        call.respondText("Task added successfully to project ID $projectId", status = HttpStatusCode.Created)
    }
}
