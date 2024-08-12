import example.com.models.EmployeeAttendance
import example.com.plugins.CheckOutRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

fun Route.checkoutRoute() {
    put("/employee/{employeeId}/checkout") {
        val employeeId = call.parameters["employeeId"]?.toIntOrNull()
        if (employeeId == null) {
            call.respondText("Invalid or missing employee ID", status = HttpStatusCode.BadRequest)
            return@put
        }

        val request = call.receive<CheckOutRequest>()
        val attendanceRemarks = request.remarks ?: "퇴근"

        // 디버깅 로그 추가
        println("Attempting to update checkout for employeeId: $employeeId on date: ${request.checkOutTime.split(" ")[0]}")

        val updatedRows = transaction {
            EmployeeAttendance.update({
                EmployeeAttendance.employeeId eq employeeId and
                        (EmployeeAttendance.date eq request.checkOutTime.split(" ")[0])
            }) { row ->
                row[EmployeeAttendance.checkOutTime] = request.checkOutTime
                row[EmployeeAttendance.attendanceStatus] = "퇴근" // 여기에서 출근 상태를 "퇴근"으로 변경합니다.
                row[EmployeeAttendance.updatedAt] = LocalDateTime.now()
            }
        }

        if (updatedRows > 0) {
            call.respondText("Check-out recorded for employee ID: $employeeId", status = HttpStatusCode.OK)
        } else {
            call.respondText("No check-in record found for employee ID: $employeeId", status = HttpStatusCode.NotFound)
        }
    }
}
