package example.com.dto

import kotlinx.serialization.Serializable

@Serializable
data class MeetingRecordsDTO(
    val id: Int,  // id 필드
    val meetingDate: String,  // 회의 날짜 및 시간
    val agenda: String,  // 회의 안건
    val content: String,  // 회의 내용
    val summary: String,  // 회의 요약
    val attendees: String,  // 참석자 ID 목록
    val isContentVisible: Boolean,  // 회의 내용 표시 여부
    val createdAt: String,  // 생성 날짜
    val updatedAt: String  // 업데이트 날짜
)
