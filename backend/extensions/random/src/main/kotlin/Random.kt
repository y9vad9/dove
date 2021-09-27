import kotlin.random.Random

private val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')

fun Random.nextString(size: Int): String = (1..size).map {
    chars.random(this)
}.joinToString("").also { print(it.length) }