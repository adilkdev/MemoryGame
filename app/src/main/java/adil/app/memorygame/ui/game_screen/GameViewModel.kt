package adil.app.memorygame.ui.game_screen

import adil.app.memorygame.data.local.db.entity.Player
import adil.app.memorygame.data.model.Card
import adil.app.memorygame.data.repository.PlayerRepository
import adil.app.memorygame.utils.AppConstants
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Adil on 28/05/2021
 */
@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var cards: List<Card>

    /**
     * Variables for saving the first and the second card,
     * which are to be matched, along with their positions.
     */
    private var firstCard: Card? = null
    private var secondCard: Card? = null
    private var firstPos = -1
    private var secondPos = -1

    /**
     * LiveData to notify the activity
     * 1. if there's a need to reset the cards on a wrong match,
     * 2. disable cards on correct match
     * 3. when the game is completed.
     */
    var itemToReset = MutableLiveData<HashMap<String, Int>>()
    var pairMatched = MutableLiveData<HashMap<String, Int>>()
    var isGameComplete = MutableLiveData<Boolean>()

    /**
     * Maintaining the score using the variable.
     */
    private var score = 0

    /**
     * Repository to store player in the database.
     */
    @Inject
    lateinit var repository: PlayerRepository

    /**
     * Checking if the cards are of same colour.
     */
    private fun checkForMatch(firstCard: Card?, secondCard: Card?): Boolean =
        firstCard?.colourId == secondCard?.colourId

    /**
     * perform the required action on the cards based on the result of the comparison.
     */
    private fun performActionOnTwoCards(firstCard: Card?, secondCard: Card?) {
        firstCard?.let {
            secondCard?.let {
                if (checkForMatch(firstCard, secondCard)) matched() else resetCards()
            }
        }
    }

    /**
     * If the selected cards are of two different color, we reset the cards to their initial state, facing down.
     * This is done by informing the activity through livedata where the UI based changes are needed.
     */
    private fun resetCards() {
        updateScore(AppConstants.OPERATION_DECREMENT)
        cards[firstPos].isFaceUp = false
        cards[secondPos].isFaceUp = false
        itemToReset.postValue(getHashMapOfCardPositions())
        resetAllData()
    }

    /**
     * If the selected cards are of same color, we remove the card from the board by informing the activity
     * where the UI based changes are needed.
     */
    private fun matched() {
        updateScore(AppConstants.OPERATION_INCREMENT)
        cards[firstPos].isMatched = true
        cards[secondPos].isMatched = true
        pairMatched.postValue(getHashMapOfCardPositions())
        resetAllData()

        if (doAllCardsFaceUp())
            isGameComplete.postValue(true)
    }

    /**
     * Helper method which @return a hash map of first and second card position.
     */
    private fun getHashMapOfCardPositions(): HashMap<String, Int> =
        hashMapOf<String, Int>().apply {
            this[AppConstants.FIRST_CARD_POSITION] = firstPos
            this[AppConstants.SECOND_CARD_POSITION] = secondPos
        }

    /**
     * Resetting all the variables to their initial state.
     */
    private fun resetAllData() {
        firstCard = null
        secondCard = null
        firstPos = -1
        secondPos = -1
    }

    /**
     * The method tells us if all the cards are facing up and hence the game is completed.
     * @return true means all cards are facing up and game is completed.
     * @return false means some cards are still facing down and hence game is not complete.
     */
    private fun doAllCardsFaceUp(): Boolean = (cards.find { !it.isFaceUp }) == null

    /**
     * Incrementing the score if player gets a right match and
     * deducting the score if the player selects a wrong pair.
     */
    private fun updateScore(opcode: Int) =
        run {
            score += if (opcode == AppConstants.OPERATION_INCREMENT) AppConstants.POINTS_SCORED
            else AppConstants.POINTS_LOST
        }

    /**
     * setting up the first and the second card and matching when two cards are chosen.
     * @param position of the card in the cards list
     * @param card is the card item chosen.
     */
    fun chooseCard(position: Int, card: Card) {
        if (firstCard == null) {
            firstCard = card
            firstPos = position
        } else if (secondCard == null) {
            secondCard = card
            secondPos = position
            performActionOnTwoCards(firstCard, secondCard)
        }
    }

    /**
     * @returns the current score of the player
     */
    fun getScore(): Int = score

    /**
     * Saving the player in database after he completes the game.
     * @param name is the name of the player
     * @param score is the score achieved by the player.
     * @return is the uid (unique player id) of the player which is an auto-incremented value
     * generated by the database on storing a player.
     */
    fun saveUserInDb(name: String, score: Int): Long =
        repository.savePlayer(Player(name = name, score = score))

    /**
     * Reset the game to the very initial stage.
     */
    fun resetGame() {
        resetAllData()
        score = 0
        cards.map {
            it.isMatched = false
            it.isFaceUp = false
        }
    }

}