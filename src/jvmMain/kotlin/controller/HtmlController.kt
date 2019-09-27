package controller

import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.html.*
import sample.Sample
import sample.hello

fun Route.htmlController() {
    get("/") {
        call.respondHtml {
            head {
                title("Hello from Ktor!")
            }
            body {
                +"${hello()} from Ktor. Check me value: ${Sample().checkMe()}"
                div {
                    id = "js-response"
                    +"Loading..."
                }
                script(src = "/static/hello-kotlin-js-3.js") {}
            }
        }
    }
    static("/static") {
        resource("hello-kotlin-js-3.js")
    }
}