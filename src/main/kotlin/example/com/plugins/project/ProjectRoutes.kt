package example.routes

import example.dto.ProjectCreateRequestDTO
import example.dto.ProjectResponseDTO
import example.models.ProjectList
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.LocalDateTime

fun Route.projectRoutes() {

    // 모든 프로젝트 리스트를 가져오는 엔드포인트
    get("/projects") {
        val projects = transaction {
            ProjectList.selectAll().map { project ->
                ProjectResponseDTO(
                    id = project[ProjectList.id].value,
                    projectName = project[ProjectList.projectName],
                    leaderId = project[ProjectList.leaderId],
                    status = project[ProjectList.status],
                    startDate = project[ProjectList.startDate].toString(),
                    endDate = project[ProjectList.endDate].toString(),
                    createdAt = project[ProjectList.createdAt].toString(),
                    updatedAt = project[ProjectList.updatedAt].toString()
                )
            }
        }
        call.respondText(Json.encodeToString(projects), ContentType.Application.Json)
    }

    // 새로운 프로젝트를 추가하는 엔드포인트
    post("/projects") {
        try {
            val projectRequest = call.receive<ProjectCreateRequestDTO>()

            // 프로젝트 저장 로직
            val projectId = transaction {
                ProjectList.insert {
                    it[projectName] = projectRequest.projectName
                    it[leaderId] = projectRequest.leaderId
                    it[status] = projectRequest.status
                    it[startDate] = LocalDate.parse(projectRequest.startDate)
                    it[endDate] = LocalDate.parse(projectRequest.endDate)
                    it[createdAt] = LocalDateTime.now()
                    it[updatedAt] = LocalDateTime.now()
                } get ProjectList.id
            }

            call.respondText("Project added successfully with ID $projectId", status = HttpStatusCode.Created)
        } catch (e: Exception) {
            call.respondText("Failed to parse request: ${e.message}", status = HttpStatusCode.BadRequest)
        }
    }
}

