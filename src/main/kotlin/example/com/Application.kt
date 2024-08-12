package example.com

import example.com.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") { // "0.0.0.0"으로 설정
        module()
    }.start(wait = true)
}
fun Application.module() {
    initDatabase()  // MySQL 데이터베이스 초기화
    configureRouting()  // 라우팅 설정
}
