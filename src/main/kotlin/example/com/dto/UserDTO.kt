package example.com.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int? = null,  // id 필드 추가
    val userName: String,
    val teamName: String,
    val rank: String,
    val auth: String,
    val email: String,
    val phoneNumber: String,
    val createdAt: String,
    val updatedAt: String,
    val state: String,
    val profileImageUrl: String


)
