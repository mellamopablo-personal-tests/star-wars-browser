package io.github.mellamopablo.starwarsbrowser.model

data class Person(
	val id: Int,
	val name: String,
    val height: Int,
    val mass: Int,
    val gender: String
) {
	override fun toString(): String = this.name
}
