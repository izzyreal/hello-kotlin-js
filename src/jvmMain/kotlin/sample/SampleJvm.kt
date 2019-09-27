package sample

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import controller.htmlController
import controller.users
import io.ktor.application.*
import io.ktor.features.CORS
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.io.*

val applicationConfig by lazy { ConfigFactory.load()!! }

actual class Sample {
    actual fun checkMe() = 42
}

actual object Platform {
    actual val name: String = "JVM"
}

fun Application.module() {

    connectToDB

    install(Routing) {
        htmlController()
        users()
    }
}

fun main() {
    embeddedServer(Netty, 8080, watchPaths = listOf(""), module = Application::module).start(wait = true)
}

private fun dataSource(url: String, username: String, password: String): HikariDataSource {
    val config = HikariConfig()
    config.jdbcUrl = url
    config.username = username
    config.password = password
    config.addDataSourceProperty("cachePrepStmts", "true")
    config.addDataSourceProperty("prepStmtCacheSize", "250")
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
    config.connectionTimeout = 10000
    config.maximumPoolSize = 3
    return HikariDataSource(config)
}

private val connectToDB by lazy {
    // For Heroku we use JDBC_DATABASE_URL
    val url = System.getenv("JDBC_DATABASE_URL") ?: applicationConfig.getString("database.url")!!
    val user = applicationConfig.getString("database.user")!!
    val password = applicationConfig.getString("database.password")!!
    val ds = dataSource(url, user, password)
    Database.connect(ds)
    TransactionManager.manager.defaultRepetitionAttempts = 0
}