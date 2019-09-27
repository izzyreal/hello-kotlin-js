package repository

import model.raw.User
import model.raw.Users
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepository {
    fun getById(id: Int): User {
        try {
            return transaction {
                Users.select { Users.id eq id }.map { r -> User.fromRow(r) }.first()
            }
        } catch (e: NoSuchElementException) {
            throw RecordNotFound(id, Users)
        }
    }

    fun getUsers(): List<User> {
        return transaction {
            Users.selectAll().map { r -> User.fromRow(r) }
        }
    }
}
