package adil.app.memorygame.ui.game_screen

import adil.app.memorygame.data.model.Card
import adil.app.memorygame.data.model.CardsProvider
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Adil on 28/05/2021
 */

class GameViewModel: ViewModel() {

    /**
     * Variables for saving the first and the second card,
     * which are to be matched, along with their positions.
     */
    var cards = CardsProvider.cards()
    var firstCard: Card? = null
    var secondCard: Card? = null
    var firstPos = -1
    var secondPos = -1
    var itemToReset = MutableLiveData<HashMap<Int, Int>>()
    var pairMatched = MutableLiveData<HashMap<Int, Int>>()

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
            checkForMatch()
        }
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
        cards[firstPos].isFaceUp = false
        cards[secondPos].isFaceUp = false
        val hashMap = hashMapOf<Int, Int>()
        hashMap[1] = firstPos
        hashMap[2] = secondPos
        itemToReset.postValue(hashMap)
        resetAllData()
    }

    /**
     * If the selected cards are of same color, we disable the card for future use by informing the activity
     * where the UI based changes are made.
     */
    private fun matched() {
        cards[firstPos].isMatched = true
        cards[secondPos].isMatched = true
        val hashMap = hashMapOf<Int, Int>()
        hashMap[1] = firstPos
        hashMap[2] = secondPos
        pairMatched.postValue(hashMap)
        resetAllData()
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

}