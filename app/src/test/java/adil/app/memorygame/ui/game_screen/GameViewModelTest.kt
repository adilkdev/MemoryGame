package adil.app.memorygame.ui.game_screen

import adil.app.memorygame.R
import adil.app.memorygame.data.model.Card
import adil.app.memorygame.utils.AppConstants
import adil.app.memorygame.utils.CardsProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    private lateinit var gameViewModel: GameViewModel

    @Before
    fun setup() {
        gameViewModel = GameViewModel()
        gameViewModel.cards = CardsProvider.cards()
    }

    @Test
    fun ifAllCardsFaceUp_returnsFalse() {
        gameViewModel.cards = CardsProvider.cards()
        val value = gameViewModel.doAllCardsFaceUp()
        assertThat(value).isFalse()
    }

    @Test
    fun ifAllCardsFaceUp_returnsTrue() {
        gameViewModel.cards.also { it.map { it.isFaceUp = true } }
        val value = gameViewModel.doAllCardsFaceUp()
        assertThat(value).isTrue()
    }

    @Test
    fun checkForMatch_returnsTrue() {
        val firstCard = Card(1, false, R.drawable.colour1, R.drawable.card_bg, false)
        val secondCard = Card(2, false, R.drawable.colour1, R.drawable.card_bg, false)
        val value = gameViewModel.checkForMatch(firstCard, secondCard)
        assertThat(value).isTrue()
    }

    @Test
    fun checkForMatch_returnsFalse() {
        val firstCard = Card(1, false, R.drawable.colour1, R.drawable.card_bg, false)
        val secondCard = Card(2, false, R.drawable.colour2, R.drawable.card_bg, false)
        val value = gameViewModel.checkForMatch(firstCard, secondCard)
        assertThat(value).isFalse()
    }

    @Test
    fun updateScoreByIncrement() {
        val score = gameViewModel.getScore()
        gameViewModel.updateScore(AppConstants.OPERATION_INCREMENT)
        assertThat(gameViewModel.getScore()).isEqualTo(score + 2)
    }

    @Test
    fun updateScoreByDecrement() {
        val score = gameViewModel.getScore()
        gameViewModel.updateScore(AppConstants.OPERATION_DECREMENT)
        assertThat(gameViewModel.getScore()).isEqualTo(score - 1)
    }

}