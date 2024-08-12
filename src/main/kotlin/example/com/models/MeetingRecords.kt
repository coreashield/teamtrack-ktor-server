package example.com.models

import example.models.ProjectList
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.ReferenceOption
import java.time.LocalDateTime

object MeetingRecords : IntIdTable() {
    val projectId = integer("project_id").references(id, onDelete = ReferenceOption.CASCADE) // 프로젝트 ID
    val meetingDate = datetime("meeting_date") // 회의 날짜 및 시간
    val agenda = varchar("agenda", 255) // 회의 안건
    val content = text("content") // 회의 내용
    val summary = varchar("summary", 255) // 회의 요약
    val attendees = varchar("attendees", 255) // 참석자 ID 목록
    val isContentVisible = bool("is_content_visible").default(true) // 회의 내용 표시 여부
    val createdAt = datetime("created_at").default(LocalDateTime.now())  // 생성 날짜
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())  // 업데이트 날짜
}
