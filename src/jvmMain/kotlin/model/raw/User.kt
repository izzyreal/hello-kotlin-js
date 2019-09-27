package model.raw

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

data class User(
    var id: Int,
    var firstName: String
) {

    companion object {

        fun fromRow(r: ResultRow): User {
            return User(
                id = r[Users.id],
                firstName = r[Users.firstName]
            )
        }
    }
}

object Users : Table("buurtbuik.users") {
    val id = integer("id").primaryKey()
    val firstName = varchar("first_name", 255)
}
