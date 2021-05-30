package adil.app.memorygame.ui.high_score_screen


import adil.app.memorygame.databinding.ActivityScoresBinding
import adil.app.memorygame.utils.AppConstants
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HighScoreActivity : AppCompatActivity() {

    /**
     * view binding, view model, card adapter and highlighted player variable as a flag
     * to know if the player is being sent to this activity on finishing the game,
     * to show him the stats.
     */
    private lateinit var binding: ActivityScoresBinding
    private lateinit var viewModel: HighScoreViewModel

    @Inject
    lateinit var scoreAdapter: ScoreAdapter

    private var highlightedPlayerPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HighScoreViewModel::class.java)

        binding = ActivityScoresBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setup()
    }

    private fun setup() {
        binding.recyclerviewScore.adapter = scoreAdapter

        binding.imageViewBack.setOnClickListener {
            onBackPressed()
        }

        scrollToShowUserHisStats()
    }

    /**
     * This method fetches the position where the player is mentioned in the list
     * and recyclerview scrolls to that position.
     */
    private fun scrollToShowUserHisStats() {
        val uid = intent.getLongExtra(AppConstants.PLAYER_ID, -1L)
        if (uid != -1L) {
            val user = viewModel.getAllPlayers().find { it.uid == uid }
            user?.let {
                val index = viewModel.getAllPlayers().indexOf(user)
                if (index != -1) {
                    scoreAdapter.highlightPlayer(index)
                    binding.recyclerviewScore.scrollToPosition(index)
                }
            }
        }
    }

    /**
     * If the player is highlighted, when the user leaves the activity
     * we are resetting the highlighted player flag.
     */
    override fun onDestroy() {
        super.onDestroy()
        highlightedPlayerPosition?.let {
            viewModel.getAllPlayers()[it].shouldHighlight = false
        }
        highlightedPlayerPosition = null
    }

}