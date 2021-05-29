package adil.app.memorygame.ui.high_score_screen

import adil.app.memorygame.data.local.db.DatabaseService
import adil.app.memorygame.data.local.db.entity.User
import adil.app.memorygame.data.repository.UserRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.room.Room

class HighScoreViewModel(application: Application): AndroidViewModel(application) {

    private var repository: UserRepository

    private val db = Room.databaseBuilder(
        application,
        DatabaseService::class.java, "game_db"
    ).build()

    init {
        repository = UserRepository(db)
    }

    /**
     * Ranking all the players based on their scores
     */
    private fun setRankToAllUsers(users: List<User>): List<User> {
        val list = users.sortedByDescending { it.score }
        var prevRank = 1
        var prevScore = Int.MIN_VALUE
        list.map {user ->
            when {
                prevScore == Int.MIN_VALUE -> {
                    user.rank = prevRank
                }
                user.score==prevScore -> user.rank = prevRank
                else -> user.rank = ++prevRank
            }
            prevScore = user.score
        }
        return list
    }

    /**
     * Fetching the list of players
     */
    fun getAllPlayers(): List<User> = setRankToAllUsers(repository.getAllPlayers())


}