package example.com.routes

import example.com.dto.MeetingRecordsDTO
import example.com.models.MeetingRecords
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

fun Route.meetingRoutes() {
    // 전체 회의 기록을 가져오는 엔드포인트
    get("/projects/{projectid}/meetings") {
        val projectId = call.parameters["projectid"]?.toIntOrNull()
        if (projectId == null) {
            call.respondText("Invalid project ID", status = HttpStatusCode.BadRequest)
            return@get
        }

        val meetingRecords = transaction {
            MeetingRecords.selectAll().where { MeetingRecords.projectId eq projectId }
                .map { meet: ResultRow ->
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

        if (meetingRecords.isEmpty()) {
            call.respondText("No meetings found for the specified project ID.", status = HttpStatusCode.NotFound)
        } else {
            val jsonResponse = Json.encodeToString(meetingRecords)
            call.respondText(jsonResponse, ContentType.Application.Json)
        }
    }

    // 특정 프로젝트의 특정 날짜의 회의 기록을 가져오는 엔드포인트
    get("/projects/{projectid}/meetings/{date}") {
        val projectId = call.parameters["projectid"]?.toIntOrNull()
        val dateParam = call.parameters["date"]
        if (projectId != null && dateParam != null) {
            val meetingDate = LocalDate.parse(dateParam)
            val meetingRecords = transaction {
                MeetingRecords.selectAll().where {
                    (MeetingRecords.projectId eq projectId) and
                            (MeetingRecords.meetingDate eq meetingDate.atStartOfDay())
                }.map { meet: ResultRow ->
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

            if (meetingRecords.isEmpty()) {
                call.respondText("No meetings found for the specified date and project ID.", status = HttpStatusCode.NotFound)
            } else {
                val jsonResponse = Json.encodeToString(meetingRecords)
                call.respondText(jsonResponse, ContentType.Application.Json)
            }
        } else {
            call.respondText("Invalid project ID or date format.", status = HttpStatusCode.BadRequest)
        }
    }
}
