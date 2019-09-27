package controller

import getById
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import repository.UserRepository

fun Route.users() {

    route("/users") {
        getUsers()
    }
}

private fun Route.getUsers() {
    get("/{id?}") {
        if (call.parameters.isEmpty()) {
            call.respond(UserRepository.getUsers()[1].firstName)
        } else {
            getById(UserRepository::getById)
        }
    }
}
