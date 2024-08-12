package example.com.models

import com.mysql.cj.xdevapi.Column
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object EmployeeAttendance : IntIdTable() {
    val employeeId = integer("employee_id") // 직원 ID
    val date = varchar("date", 255) // 출근 날짜
    val checkInTime = text("check_in_time").nullable() // 출근 시간
    val checkOutTime = varchar("check_out_time", 255).nullable() // 퇴근 시간
    val attendanceStatus = varchar("attendance_status", 255) // 출근 상태
    val remarks = varchar("remarks", 255).nullable() // 비고
    val createdAt = datetime("created_at")  // 생성 날짜
    val updatedAt = datetime("updated_at")  // 업데이트 날짜
}