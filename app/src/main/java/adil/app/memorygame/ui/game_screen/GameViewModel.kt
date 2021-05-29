package adil.app.memorygame.ui.game_screen

import adil.app.memorygame.data.model.Card
import adil.app.memorygame.data.model.CardsProvider
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Adil on 28/05/2021
 */

class GameViewModel : ViewModel() {

    /**
     * Variables for saving the first and the second card,
     * which are to be matched, along with their positions.
     */
    var cards = CardsProvider.cards()
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
    var singleItemReset = MutableLiveData<Int>()
    var itemToReset = MutableLiveData<HashMap<Int, Int>>()
    var pairMatched = MutableLiveData<HashMap<Int, Int>>()
    var isGameComplete = MutableLiveData<Boolean>()

    /**
     * Maintaining the score using the variable.
     */
    private var score = 0

    /**
     * setting up the first and the second card and matching when two cards are chosen.
     */
    fun chooseCard(position: Int, card: Card, view: View) {
        if (firstCard == null) {
            firstCard = card
            firstPos = position
        } else if (secondCard == null) {
            secondCard = card
            secondPos = position
            checkIfBothAreSame()
        }
    }

    private fun checkIfBothAreSame() {
        if (firstCard?.id == secondCard?.id)
            resetCardWithNoScore()
            //resetCards()
        else
            checkForMatch()
    }

    /**
     * Checking if the cards are of same colour.
     */
    private fun checkForMatch() {
        if (firstCard?.colourId == secondCard?.colourId) {
            matched()
        } else {
            resetCards()
        }
    }

    /**
     * If the selected cards are of two different color, we reset the cards to their initial state, facing down.
     * This is done by informing the activity where the UI based changes are made.
     */
    private fun resetCards() {
        decrementScore()
        cards[firstPos].isFaceUp = false
        cards[secondPos].isFaceUp = false
        val hashMap = hashMapOf<Int, Int>()
        hashMap[1] = firstPos
        hashMap[2] = secondPos
        itemToReset.postValue(hashMap)
        resetAllData()
    }

    private fun resetCardWithNoScore() {
        cards[firstPos].isFaceUp = false
        cards[secondPos].isFaceUp = false
        singleItemReset.postValue(firstPos)
        resetAllData()
    }

    /**
     * If the selected cards are of same color, we disable the card for future use by informing the activity
     * where the UI based changes are made.
     */
    private fun matched() {
        incrementScore()
        cards[firstPos].isMatched = true
        cards[secondPos].isMatched = true
        val hashMap = hashMapOf<Int, Int>()
        hashMap[1] = firstPos
        hashMap[2] = secondPos
        pairMatched.postValue(hashMap)
        resetAllData()

        if (doAllCardsFaceUp())
            isGameComplete.postValue(true)
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
     */
    private fun doAllCardsFaceUp(): Boolean = (cards.find { !it.isFaceUp }) == null

    /**
     * Returns the current score of the player
     */
    fun getScore():Int = score

    /**
     * Incrementing the score if player gets a right match and
     * deducting the score if the player selects a wrong pair.
     */
    private fun incrementScore() = run { score += 2 }
    private fun decrementScore() = run { score -= 1 }

}