package example.com.routes

import example.com.models.EmployeeAttendance
import example.com.dto.EmployeeAttendanceDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll

fun Route.employeeRoutes() {

    get("/employee") {
        val attendees = transaction {
            EmployeeAttendance.selectAll().map { row ->
                EmployeeAttendanceDTO(
                    id = row[EmployeeAttendance.id].value,
                    employeeId = row[EmployeeAttendance.employeeId],
                    date = row[EmployeeAttendance.date],
                    checkInTime = row[EmployeeAttendance.checkInTime],
                    checkOutTime = row[EmployeeAttendance.checkOutTime],
                    attendanceStatus = row[EmployeeAttendance.attendanceStatus],
                    remarks = row[EmployeeAttendance.remarks],
                    createdAt = row[EmployeeAttendance.createdAt].toString(),
                    updatedAt = row[EmployeeAttendance.updatedAt].toString()
                )
            }
        }
        val jsonResponse = Json.encodeToString(attendees)
        if (jsonResponse.isBlank()) {
            call.respondText("No attendees found.")
        } else {
            call.respondText(jsonResponse, ContentType.Application.Json)
        }
    }
}
