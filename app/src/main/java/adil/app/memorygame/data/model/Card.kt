package adil.app.memorygame.data.model

data class Card(
    val id: Int,
    var isFaceUp: Boolean,
    val colourId: Int,
    val cardBg: Int,
    var isMatched: Boolean
)