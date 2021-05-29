package adil.app.memorygame.ui.game_screen

import adil.app.memorygame.data.model.Card
import adil.app.memorygame.databinding.ActivityGameBinding
import adil.app.memorygame.utils.VerticalSpaceItemDecorator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

/**
 * Created by Adil on 28/05/2021
 */

class GameActivity : AppCompatActivity(), GameCardAdapter.ClickListener {

    /**
     * View binding, view model and game card adapter
     */
    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: GameViewModel
    private lateinit var gameCardAdapter: GameCardAdapter

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
            val bottomSheet = AddScoreBottomSheet()
            bottomSheet.show(supportFragmentManager, "add_score_sheet")
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

        viewModel.singleItemReset.observe(this, {
            val viewHolder1 = it?.let { position ->
                binding.recyclerviewGame.findViewHolderForAdapterPosition(position)
            } as GameCardAdapter.GameItemViewHolder
            setCardToBeTouchable(false)
            Handler().postDelayed({
                viewHolder1.hideCard()
                setCardToBeTouchable(true)
            }, 1000)

            animateScoreOnMove(binding.textViewScoreOnMove, "0")
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
            Log.e("TAG", "complete ${viewModel.getScore()}")
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

    /**
     * Callbacks of the card on the user interaction.
     */
    override fun onCardFaceUp(position: Int, card: Card, view: View) {
        viewModel.chooseCard(position, card, view)
    }

    override fun onCardFaceDown(position: Int, card: Card, view: View) {

    }

}