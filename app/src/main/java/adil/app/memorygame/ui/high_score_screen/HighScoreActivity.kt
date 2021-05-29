package adil.app.memorygame.ui.high_score_screen


import adil.app.memorygame.data.local.db.DatabaseService
import adil.app.memorygame.data.local.db.entity.User
import adil.app.memorygame.data.repository.UserRepository
import adil.app.memorygame.databinding.ActivityScoresBinding
import adil.app.memorygame.ui.game_screen.GameViewModel
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room

class HighScoreActivity: AppCompatActivity() {

    private lateinit var binding: ActivityScoresBinding
    private lateinit var viewModel: HighScoreViewModel
    private lateinit var scoreAdapter: ScoreAdapter
    var highlightedUserPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            DatabaseService::class.java, "game_db"
        ).build()
        var repo = UserRepository(db)
        viewModel = ViewModelProvider(this).get(HighScoreViewModel::class.java)
        viewModel.setTheRepository(repo)

        binding = ActivityScoresBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setup()
    }

    private fun setup() {
        scoreAdapter = ScoreAdapter()
        scoreAdapter.setScoreData(viewModel.getAllUsers())
        binding.recyclerviewScore.adapter = scoreAdapter

        binding.imageViewBack.setOnClickListener {
            onBackPressed()
        }

        scrollToShowUserHisStats()
    }

    private fun scrollToShowUserHisStats() {
        val uid = intent.getLongExtra("user_id", -1L)
        if (uid != -1L) {
            val user = viewModel.getAllUsers().find { it.uid ==  uid}
            user?.let {
                val index = viewModel.getAllUsers().indexOf(user)
                if(index != -1) {
                    scoreAdapter.highlightUser(index)
                    binding.recyclerviewScore.scrollToPosition(index)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        highlightedUserPosition?.let {
            viewModel.getAllUsers()[it].shouldHighlight = false
        }
        highlightedUserPosition = null
    }

}