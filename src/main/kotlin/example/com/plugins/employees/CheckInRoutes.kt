package example.com.plugins.employees

import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import example.com.models.EmployeeAttendance
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime

@Serializable
data class CheckInRequest(
    val checkInTime: String, // "YYYY-MM-DD HH:mm:ss" 형식
    val remarks: String? = null
)

fun Route.checkInRoute() {
    post("/employee/{employeeId}/checkin") {
        val employeeId = call.parameters["employeeId"]?.toIntOrNull()
        if (employeeId == null) {
            call.respondText("Invalid or missing employee ID", status = HttpStatusCode.BadRequest)
            return@post
        }

        val request = call.receive<CheckInRequest>()
        val attendanceRemarks = request.remarks ?: "정상 출근"

        val newRecordId = transaction {
            EmployeeAttendance.insertAndGetId { row ->
                row[EmployeeAttendance.employeeId] = employeeId
                row[EmployeeAttendance.date] = request.checkInTime.split(" ")[0] // 날짜 부분만 추출
                row[EmployeeAttendance.checkInTime] = request.checkInTime // String으로 저장
                row[EmployeeAttendance.attendanceStatus] = "출근"
                row[EmployeeAttendance.remarks] = attendanceRemarks
                row[EmployeeAttendance.createdAt] = LocalDateTime.now()
                row[EmployeeAttendance.updatedAt] = LocalDateTime.now()
            }.value
        }

        call.respondText("Check-in recorded with ID: $newRecordId", status = HttpStatusCode.Created)
    }
}
