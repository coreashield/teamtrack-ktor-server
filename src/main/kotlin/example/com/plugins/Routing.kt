// src/example/plugins/Routing.kt
package example.com.plugins

import checkoutRoute
import example.com.dto.MeetingRecordsDTO
import example.com.dto.UserDTO
import example.com.models.MeetingRecords
import example.com.models.Users
import example.com.plugins.employees.checkInRoute
import example.com.plugins.tasks.tasksRoutes
import example.com.routes.employeeRoutes
import example.routes.projectRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json() // JSON 형식의 데이터를 처리할 수 있도록 설정
    }

    routing {
        get("/") {
            call.respondText("Hello Ktor!")
        }

        get("/users") {
            val users = transaction {
                Users.selectAll().map { user ->
                    UserDTO(
                        id = user[Users.id].value,
                        userName = user[Users.userName],
                        teamName = user[Users.teamName],
                        rank = user[Users.rank],
                        auth = user[Users.auth],
                        email = user[Users.email],
                        phoneNumber = user[Users.phoneNumber],
                        createdAt = user[Users.createdAt].toString(),
                        updatedAt = user[Users.updatedAt].toString(),
                        state = user[Users.state],
                        profileImageUrl = user[Users.profileImageUrl]
                    )
                }
            }

            val jsonResponse = Json.encodeToString(users)
            if (jsonResponse.isBlank()) {
                call.respondText("No users found.")
            } else {
                call.respondText(jsonResponse, ContentType.Application.Json)
            }
        }

        get("/users/{id}") {
            val userId = call.parameters["id"]?.toIntOrNull()

            if (userId == null) {
                call.respondText("Invalid user ID", status = HttpStatusCode.BadRequest)
                return@get
            }

            val user = transaction {
                Users.selectAll().where { Users.id eq userId }.map { user ->
                    UserDTO(
                        id = user[Users.id].value,
                        userName = user[Users.userName],
                        teamName = user[Users.teamName],
                        rank = user[Users.rank],
                        auth = user[Users.auth],
                        email = user[Users.email],
                        phoneNumber = user[Users.phoneNumber],
                        createdAt = user[Users.createdAt].toString(),
                        updatedAt = user[Users.updatedAt].toString(),
                        state = user[Users.state],
                        profileImageUrl = user[Users.profileImageUrl]
                    )
                }.singleOrNull()
            }

            if (user == null) {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            } else {
                val jsonResponse = Json.encodeToString(user)
                call.respondText(jsonResponse, ContentType.Application.Json)
            }
        }

        get("/meeting") {
            val meetingRecords = transaction {
                MeetingRecords.selectAll().map { meet ->
                    MeetingRecordsDTO(
                        id = meet[MeetingRecords.id].value,
                        meetingDate = meet[MeetingRecords.meetingDate].toString(),
                        agenda = meet[MeetingRecords.agenda],
                        content = meet[MeetingRecords.content],
                        summary = meet[MeetingRecords.summary],
                        attendees = meet[MeetingRecords.attendees],
                        isContentVisible = meet[MeetingRecords.isContentVisible],
                        createdAt = meet[MeetingRecords.createdAt].toString(),
                        updatedAt = meet[MeetingRecords.updatedAt].toString(),
                    )
                }
            }

            val jsonResponse = Json.encodeToString(meetingRecords)
            if (jsonResponse.isBlank()) {
                call.respondText("No meeting found.")
            } else {
                call.respondText(jsonResponse, ContentType.Application.Json)
            }
        }

        projectRoutes()
        employeeRoutes()
        tasksRoutes()  // 분리된 Task 라우트 추가
        checkInRoute() // 추가된 엔드포인트 라우트
        checkoutRoute()
    }
}
