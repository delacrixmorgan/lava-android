package com.delacrixmorgan.android.data.controller

object SuggestionDataController {
    private val food = arrayOf(
        "Breakfast", "Pineapple", "Coffee", "Croissant", "Green Tea Latte"
    )

    private val places = arrayOf(
        "Japan", "London", "Hong Kong", "Rome", "Venice", "Paris", "Vietnam", "Sydney", "New York"
    )

    private val animals = arrayOf(
        "Dogs", "Penguins", "Jigokudani Monkeys", "Parrot"
    )

    private val generic = arrayOf(
        "Beach",
        "Mountains",
        "Knolling",
        "Safari",
        "Sunset",
        "Sea",
        "Galaxy",
        "Forrest",
        "House",
        "Lego",
        "Halloween",
        "Christmas",
        "Watch",
        "Weddings",
        "Funny",
        "Graffiti"
    )

    private val suggestions = arrayOf(
        food, places, animals, generic
    ).flatten()

    fun randomSuggestion() = this.suggestions.random()
}