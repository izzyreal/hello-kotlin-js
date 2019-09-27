package sample

import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.*

actual class Sample {
    actual fun checkMe() = 12
}

actual object Platform {
    actual val name: String = "JS"
}

@Suppress("unused")
@JsName("helloWorld")
fun helloWorld(salutation: String) {
    var xhttp: dynamic = XMLHttpRequest();
    xhttp.open("GET", "http://localhost:8080/users", true);
    xhttp.onreadystatechange = fun() {
        println(xhttp.readyState)
        println(xhttp.status)
        console.log(xhttp.responseText)
    }
    xhttp.send();
    val message = "$salutation from Kotlin.JS ${hello()}, check me value: ${Sample().checkMe()}"
    document.getElementById("js-response")?.textContent = message
}

fun main() {
    document.addEventListener("DOMContentLoaded", {
        helloWorld("Hi!")
    })
}                