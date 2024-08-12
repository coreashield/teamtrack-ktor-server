package example.com.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeAttendanceDTO(
    val id: Int,
    val employeeId: Int,
    val date: String,
    val checkInTime: String?,
    val checkOutTime: String?,
    val attendanceStatus: String,
    val remarks: String?,
    val createdAt: String,
    val updatedAt: String
)