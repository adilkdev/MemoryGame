package adil.app.memorygame.ui.high_score_screen

import adil.app.memorygame.data.local.db.entity.User
import adil.app.memorygame.data.repository.UserRepository
import androidx.lifecycle.ViewModel

class HighScoreViewModel: ViewModel() {

    private lateinit var repository: UserRepository

    fun setTheRepository(repository: UserRepository) {
        this.repository = repository
    }

    fun getAllUsers(): List<User> = setRankToAllUsers(repository.getAllUsers())

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


}