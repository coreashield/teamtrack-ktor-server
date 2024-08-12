package example.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProjectListDTO(
    val projectName: String,
    val leaderId: Int,
    val status: String,
    val startDate: String,
    val endDate: String,
    val createdAt: String,
    val updatedAt: String
)

//Get용
@Serializable
data class ProjectResponseDTO(
    val id: Int,
    val projectName: String,
    val leaderId: Int,
    val status: String,
    val startDate: String,
    val endDate: String,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class ProjectCreateRequestDTO(
    val projectName: String,
    val leaderId: Int,
    val status: String,
    val startDate: String,
    val endDate: String
)