package example.com.plugins

import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.transactions.transaction
import example.com.models.EmployeeAttendance
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime

@Serializable
data class CheckOutRequest(
    val checkOutTime: String, // "YYYY-MM-DD HH:mm:ss" 형식
    val remarks: String? = null
)
