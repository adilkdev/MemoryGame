package adil.app.memorygame.ui.game_screen

import adil.app.memorygame.R
import adil.app.memorygame.data.model.Card
import adil.app.memorygame.databinding.ActivityGameBinding
import adil.app.memorygame.ui.high_score_screen.HighScoreActivity
import adil.app.memorygame.utils.AppConstants
import adil.app.memorygame.utils.VerticalSpaceItemDecorator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Adil on 28/05/2021
 */

@AndroidEntryPoint
class GameActivity : AppCompatActivity(), GameCardAdapter.ClickListener, AddUserListener {

    /**
     * View binding, view model
     */
    private lateinit var binding: ActivityGameBinding

    private lateinit var viewModel: GameViewModel

    /**
     * Adapter is provided to us by Hilt and we will not instantiate it.
     */
    @Inject
    lateinit var gameCardAdapter: GameCardAdapter

    /**
     * This flag turns true and we reset the game in the onResume method.
     * This happens when the player on completing the game returns from the score activity.
     */
    private var isResetPending = false

    /**
     * onCreate is a one time called method so all the initialization setup is being done here
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        binding = ActivityGameBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()

        binding.textViewScoresBtn.setOnClickListener {
            startActivity(Intent(this, HighScoreActivity::class.java))
        }
    }

    /**
     * Resetting the game after the player has returned from the score activity on completing the game.
     */
    override fun onResume() {
        super.onResume()
        if (isResetPending) {
            resetGameAfterUserIsAdded()
            isResetPending = false
        }
    }

    /**
     * Callbacks of the card on the player interaction.
     * @param position tells about the position of card in the cards list
     * @param card is the card item which is touched.
     */
    override fun onCardFaceUp(position: Int, card: Card) {
        viewModel.chooseCard(position, card)
    }

    override fun onCardFaceDown(position: Int, card: Card) {

    }

    /**
     * Callback on clicking the add button from the bottom sheet dialog.
     * @param name is the name of the player which he enters on completing the game.
     */
    override fun onAddUserButtonClicked(name: String) {
        val playerId = viewModel.saveUserInDb(name, viewModel.getScore())
        isResetPending = true
        showUserTheStatsAfterGameCompletion(playerId)
    }

    /**
     * Setting up the view models used in the activity, which observe and notify whenever
     * there's a change in their value, to update the UI accordingly.
     * When the game is completed, view model notifies here with isGameComplete livedata.
     */
    private fun setupObservers() {
        viewModel.itemToReset.observe(this, {
            val firstCardViewHolder = it["first_card_position"]?.let { position ->
                binding.recyclerviewGame.findViewHolderForAdapterPosition(position)
            } as GameCardAdapter.GameItemViewHolder
            val secondCardViewHolder = it["second_card_position"]?.let { position ->
                binding.recyclerviewGame.findViewHolderForAdapterPosition(position)
            } as GameCardAdapter.GameItemViewHolder
            setCardToBeTouchable(false)
            Handler(Looper.getMainLooper()).postDelayed({
                firstCardViewHolder.hideCard()
                secondCardViewHolder.hideCard()
                setCardToBeTouchable(true)
            }, 1000)

            animateScoreOnMove(binding.textViewScoreOnMove, "-${AppConstants.POINTS_LOST}")
        })

        viewModel.pairMatched.observe(this, {
            val viewHolder1 = it[1]?.let { position ->
                binding.recyclerviewGame.findViewHolderForAdapterPosition(position)
            } as GameCardAdapter.GameItemViewHolder
            val viewHolder2 = it[2]?.let { position ->
                binding.recyclerviewGame.findViewHolderForAdapterPosition(position)
            } as GameCardAdapter.GameItemViewHolder
            Handler(Looper.getMainLooper()).postDelayed({
                viewHolder1.removeCard()
                viewHolder2.removeCard()
            }, 300)

            animateScoreOnMove(binding.textViewScoreOnMove, "+${AppConstants.POINTS_SCORED}")
        })

        viewModel.isGameComplete.observe(this, {
            Handler(Looper.getMainLooper()).postDelayed({
                val bottomSheet = AddScoreBottomSheet()
                bottomSheet.listener = this
                bottomSheet.isCancelable = false
                bottomSheet.show(supportFragmentManager, "add_score_sheet")
            }, 700)
        })
    }

    /**
     * Setting the recyclerview with item decorator for vertical spacing, grid layout manager and
     * attaching adapter to the recyclerview.
     */
    private fun setupRecyclerView() {
        gameCardAdapter.setClickListener(this)

        val gridLayoutManager = GridLayoutManager(this, AppConstants.GRID_COUNT)
        binding.recyclerviewGame.layoutManager = gridLayoutManager
        binding.recyclerviewGame.adapter = gameCardAdapter

        binding.recyclerviewGame.addItemDecoration(
            VerticalSpaceItemDecorator(AppConstants.VERTICAL_SPACING_BETWEEN_CARDS)
        )
    }

    /**
     * Animating Score on each match
     * @param view is the textview which displays the points gained or lost.
     * @param text is the count of scores being gained or lost.
     */
    private fun animateScoreOnMove(view: TextView, text: String) {
        binding.textViewScore.text = getString(R.string.score, viewModel.getScore())

        view.text = text
        view.alpha = 1.0f
        view.scaleX = 1.0f
        view.scaleY = 1.0f
        view.animate()
            .alpha(0.0f)
            .scaleX(2.0f).scaleY(2.0f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(1000)
            .start()
    }

    /**
     * Once either the card is matched or reset, the card is removed or faced down respectively,
     * during the transition towards the required state, the card should stop responding to the touch events.
     * As soon as the required transition is completed, we restore the interaction behaviour of the card.
     * This is a helper method to achieve the same.
     * @param value tells about the behaviour of the touch events i.e. should it respond on touch or not
     */
    private fun setCardToBeTouchable(value: Boolean) {
        viewModel.cards.forEachIndexed { index, _ ->
            (binding.recyclerviewGame.findViewHolderForAdapterPosition(index) as GameCardAdapter.GameItemViewHolder)
                .isClickAllowed = value
        }
    }

    /**
     * Resetting the game
     */
    private fun resetGameAfterUserIsAdded() {
        viewModel.resetGame()
        Handler(Looper.getMainLooper()).postDelayed({
            /**
             * We are manually resetting adapter so that recyclerview has to refresh,
             * sometimes the recyclerview doesn't refresh even after using notifyDataSetChanged() method.
             */
            binding.recyclerviewGame.adapter = null
            gameCardAdapter = GameCardAdapter(this, viewModel.cards)
            setupRecyclerView()

            binding.textViewScore.text = getString(R.string.score, viewModel.getScore())
        }, 300)
    }

    /**
     * Once the player completes the game and finishes entering his name for saving in database,
     * we need to show him his stats i.e. scores and ranks.
     */
    private fun showUserTheStatsAfterGameCompletion(playerId: Long) {
        startActivity(
            Intent(this, HighScoreActivity::class.java).putExtra(
                AppConstants.PLAYER_ID,
                playerId
            )
        )
    }

}