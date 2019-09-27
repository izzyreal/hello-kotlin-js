import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import repository.RecordNotFound
import kotlin.reflect.KFunction1
import kotlin.reflect.full.memberProperties

suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.validatePostData(
    optional: List<String>
): T? {
    val body = try {
        call.receive<T>()
    } catch (e: Exception) {
        call.respond(HttpStatusCode.BadRequest, hashMapOf("error" to "Incorrect json"))
        return null
    }

    T::class.memberProperties.filterNot { prop -> optional.contains(prop.name) }
        .filter { prop ->
            prop.get(body) == null
        }.ifEmpty {
            return body
        }.map { prop -> prop.name }.also { missingProps ->
            call.respond(
                HttpStatusCode.BadRequest,
                hashMapOf("error" to "Missing parameters in json: ${missingProps.joinToString()}")
            )
        }
    return null
}

suspend inline fun <reified IdType> PipelineContext<Unit, ApplicationCall>.getById(
    getter: KFunction1<IdType, Any>
) {
    val idStr = call.parameters["id"]

    if (idStr == null) call.respond(
        HttpStatusCode.BadRequest,
        hashMapOf("error" to "ID parameter not found in request")
    )

    val id = idStr?.toIntOrNull().let { it } ?: idStr!!

    if (id is IdType) {
        try {
            val entity = getter(id)
            call.respond(entity)
        } catch (e: RecordNotFound) {
            call.respond(HttpStatusCode.NotFound, e.toString())
        }
    } else {
        call.respond(HttpStatusCode.BadRequest, hashMapOf("error" to "Malformed ID parameter: $id"))
    }
}
