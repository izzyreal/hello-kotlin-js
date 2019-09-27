package repository

import org.jetbrains.exposed.sql.Table

open class RecordException(message: String) : Exception(message) {
    override fun toString(): String {
        return "Type: ${this.javaClass.simpleName}, Message: ${this.message}"
    }
}

class RecordNotFound(id: Any, table: Table) : RecordException(
    "Record $id not found in ${table.javaClass.simpleName}"
)
