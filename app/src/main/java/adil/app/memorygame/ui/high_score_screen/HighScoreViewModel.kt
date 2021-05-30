package adil.app.memorygame.ui.high_score_screen

import adil.app.memorygame.data.local.db.entity.Player
import adil.app.memorygame.data.repository.PlayerRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HighScoreViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var repository: PlayerRepository

    /**
     * Fetching the list of players
     */
    fun getAllPlayers(): List<Player> = repository.getAllPlayers()


}