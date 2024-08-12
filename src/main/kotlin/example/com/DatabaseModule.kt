package example.com.plugins

import example.com.models.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory


fun initDatabase() {
    val logger = LoggerFactory.getLogger("DatabaseInit")

    try {
        Database.connect(
            url = "jdbc:mysql://localhost:3306/ktor_db",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "kim",
            password = "1123"
        )
        transaction {
            SchemaUtils.create(Users)
        }
        logger.info("Database connected and Users table created/checked successfully.")
    } catch (e: Exception) {
        logger.error("Error connecting to database or creating Users table", e)
    }
}