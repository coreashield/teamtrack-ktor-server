package example.com.dto

import kotlinx.serialization.Serializable

@Serializable
data class TaskAssignmentsDTO(
    val id: Int? = null,  // Optional로 변경
    val projectId: Int,   // Project ID 추가
    val assignedUser: String,
    val taskName: String,
    val isAssignedByLeader: Boolean,
    val taskStatus: String,
    val createdAt: String,
    val updatedAt: String
)
