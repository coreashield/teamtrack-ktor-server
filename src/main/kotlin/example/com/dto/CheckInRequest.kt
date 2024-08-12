package example.com.dto

import kotlinx.serialization.Serializable

// DTO 정의
@Serializable
data class CheckInRequest(
    val employeeId: Int,
    val checkInTime: String, // "YYYY-MM-DD HH:mm:ss" 형식
    val remarks: String? = null
)