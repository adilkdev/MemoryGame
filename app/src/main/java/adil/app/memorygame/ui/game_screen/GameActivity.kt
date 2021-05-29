package adil.app.memorygame.ui.game_screen

import adil.app.memorygame.data.local.db.DatabaseService
import adil.app.memorygame.data.local.db.entity.User
import adil.app.memorygame.data.model.Card
import adil.app.memorygame.data.repository.UserRepository
import adil.app.memorygame.databinding.ActivityGameBinding
import adil.app.memorygame.ui.high_score_screen.HighScoreActivity
import adil.app.memorygame.utils.VerticalSpaceItemDecorator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room

/**
 * Created by Adil on 28/05/2021
 */

class GameActivity : AppCompatActivity(), GameCardAdapter.ClickListener, AddUserListener {

    /**
     * View binding, view model and game card adapter
     */
    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: GameViewModel
    private lateinit var gameCardAdapter: GameCardAdapter

    private lateinit var repo: UserRepository

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

        val db = Room.databaseBuilder(
            applicationContext,
            DatabaseService::class.java, "game_db"
        ).build()
        repo = UserRepository(db)

        repo.getAllUsers().forEach {
            println("${it.name} -> ${it.uid}")
        }
    }

    /**
     * Setting up the view models used in the activity, which observe and notify whenever
     * there's a change in their value, to update the UI accordingly.
     * When the game is completed, view model notifies here with isGameComplete livedata.
     */
    private fun setupObservers() {
        viewModel.itemToReset.observe(this, {
            val viewHolder1 = it[1]?.let { position ->
                binding.recyclerviewGame.findViewHolderForAdapterPosition(position)
            } as GameCardAdapter.GameItemViewHolder
            val viewHolder2 = it[2]?.let { position ->
                binding.recyclerviewGame.findViewHolderForAdapterPosition(position)
            } as GameCardAdapter.GameItemViewHolder
            setCardToBeTouchable(false)
            Handler().postDelayed({
                viewHolder1.hideCard()
                viewHolder2.hideCard()
                setCardToBeTouchable(true)
            }, 1000)

            animateScoreOnMove(binding.textViewScoreOnMove, "-1")
        })

        viewModel.pairMatched.observe(this, {
            val viewHolder1 = it[1]?.let { position ->
                binding.recyclerviewGame.findViewHolderForAdapterPosition(position)
            } as GameCardAdapter.GameItemViewHolder
            val viewHolder2 = it[2]?.let { position ->
                binding.recyclerviewGame.findViewHolderForAdapterPosition(position)
            } as GameCardAdapter.GameItemViewHolder
            Handler().postDelayed({
                viewHolder1.disableCard()
                viewHolder2.disableCard()
            }, 300)

            animateScoreOnMove(binding.textViewScoreOnMove, "+2")
        })

        viewModel.isGameComplete.observe(this, {
            Handler().postDelayed({
                val bottomSheet = AddScoreBottomSheet()
                bottomSheet.listener = this
                bottomSheet.isCancelable = false
                bottomSheet.show(supportFragmentManager, "add_score_sheet")
            }, 700)
        })
    }

    /**
     * Animating Score on each match
     */
    private fun animateScoreOnMove(view: TextView, text: String) {
        binding.textViewScore.text = "Score : ${viewModel.getScore()}"

        view.text = text
        view.alpha = 1.0f;
        view.scaleX = 1.0f;
        view.scaleY = 1.0f;
        view.animate()
            .alpha(0.0f)
            .scaleX(2.0f).scaleY(2.0f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(1000)
            .start()
    }

    /**
     * Once either the card is matched or reset, the card is made to face down or become faded respectively,
     * during the transition towards the required state, the card should stop responding to the touch events.
     * As soon as the required transition is completed, we restore the interaction behaviour of the card.
     * This is a helper method to achieve the same.
     */
    private fun setCardToBeTouchable(value: Boolean) {
        viewModel.cards.forEachIndexed { index, _ ->
            (binding.recyclerviewGame.findViewHolderForAdapterPosition(index) as GameCardAdapter.GameItemViewHolder)
                .isClickAllowed = value
        }
    }

    /**
     * Setting the recyclerview with item decorator for vertical spacing, grid layout manager and
     * attaching adapter to the recyclerview.
     */
    private fun setupRecyclerView() {
        gameCardAdapter = GameCardAdapter(this)
        gameCardAdapter.setClickListener(this)
        gameCardAdapter.setCards(viewModel.cards)

        val gridLayoutManager = GridLayoutManager(this, 4)
        binding.recyclerviewGame.layoutManager = gridLayoutManager
        binding.recyclerviewGame.adapter = gameCardAdapter

        binding.recyclerviewGame.addItemDecoration(
            VerticalSpaceItemDecorator(8)
        )
    }

    private fun resetGameAfterUserIsAdded() {
        viewModel.resetGame()
        Handler().postDelayed({
            gameCardAdapter.notifyItemRangeChanged(0, 16)
            binding.textViewScore.text = "Score : ${viewModel.getScore()}"
        },1000)
    }

    private fun showUserTheStatsAfterGameCompletion(userId: Long) {
        startActivity(Intent(this, HighScoreActivity::class.java).putExtra("user_id", userId))
    }

    /**
     * Callbacks of the card on the user interaction.
     */
    override fun onCardFaceUp(position: Int, card: Card, view: View) {
        viewModel.chooseCard(position, card, view)
    }

    override fun onCardFaceDown(position: Int, card: Card, view: View) {

    }

    override fun onUserAdded(name: String) {
        val userId = repo.saveUser(User(name = name, score = viewModel.getScore()))
        isResetPending = true
        showUserTheStatsAfterGameCompletion(userId)
    }

    override fun onResume() {
        super.onResume()
        if (isResetPending) {
            resetGameAfterUserIsAdded()
            isResetPending = false
        }
    }

}