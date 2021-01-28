package me.spanchenko.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dao.Notes
import io.ktor.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class DatabaseInitializer {
    fun initDatabase() {
        val dbConfig = HikariConfig().apply {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:test"
            maximumPoolSize = 3
        }

        val dataSource = HikariDataSource(dbConfig)
        Database.connect(dataSource)
        createTables()
        LoggerFactory.getLogger(Application::class.simpleName).info("Initialized Database")
    }

    private fun createTables() = transaction {
        SchemaUtils.create(Notes)
    }
}


