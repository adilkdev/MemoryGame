package adil.app.memorygame.ui.game_screen

import adil.app.memorygame.R
import adil.app.memorygame.data.model.Card
import adil.app.memorygame.utils.AppConstants
import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Adil on 28/05/2021
 */

@Suppress("DEPRECATION")
class GameCardAdapter(val context: Context, var cards: List<Card>) :
    RecyclerView.Adapter<GameCardAdapter.GameItemViewHolder>() {

    /**
     *  Click listener for RecyclerView and an instance of Vibrator.
     */
    private lateinit var clickListener: ClickListener
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    /**
     * On item click listener for our cards in the recyclerview
     */
    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameItemViewHolder {
        return GameItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false),
            cards = cards, vibrator = vibrator, clickListener = clickListener
        )
    }

    override fun onBindViewHolder(holder: GameItemViewHolder, position: Int) {
        val card = cards[position]
        holder.imageViewCardBack.setImageResource(card.cardBg)
        holder.imageViewCardFront.setImageResource(card.colourId)
    }

    override fun getItemCount(): Int = cards.size

    /**
     * Since all the items are shown at once and nothing is being recycled,
     * touch handling and the card flip animation is performed in the view holder.
     */
    class GameItemViewHolder(
        itemView: View,
        cards: List<Card>,
        clickListener: ClickListener,
        val vibrator: Vibrator
    ) : RecyclerView.ViewHolder(itemView) {
        /**
         * Initialising the view components of the card
         */
        private val rootLayout: FrameLayout = itemView.findViewById(R.id.rootLayout)
        val imageViewCardFront: ImageView = itemView.findViewById(R.id.imageViewCardFront)
        val imageViewCardBack: ImageView = itemView.findViewById(R.id.imageViewCardBack)

        /**
         * The Animator variables
         */
        private val frontAnim = AnimatorInflater.loadAnimator(
            itemView.context,
            R.animator.front_animation
        ) as AnimatorSet
        private val backAnim = AnimatorInflater.loadAnimator(
            itemView.context,
            R.animator.back_animation
        ) as AnimatorSet

        /**
         * Helper variables used to maintain the state i.e. start and end of the running animation
         */
        var isAnimationFinished = true
        var isClickAllowed = true

        /**
         * Called when the view holder is created. All the touch handling is done here.
         */
        init {
            rootLayout.setOnClickListener {
                if (isAnimationFinished && isClickAllowed &&
                    !cards[bindingAdapterPosition].isFaceUp &&
                    !cards[bindingAdapterPosition].isMatched
                ) {
                    if (!cards[bindingAdapterPosition].isFaceUp) {
                        showCard()
                        hapticFeedback(AppConstants.HAPTIC_TOUCH_DURATION)
                        cards[bindingAdapterPosition].isFaceUp =
                            !cards[bindingAdapterPosition].isFaceUp
                        clickListener.onCardFaceUp(
                            bindingAdapterPosition,
                            cards[bindingAdapterPosition]
                        )
                    }
                }
            }
        }

        /**
         * The card is facing down and we are running the animation
         * to flip the background of the card with a fade out,
         * at the same time flip the card content towards the screen to view with a fade in.
         */
        private fun showCard() {
            frontAnim.setTarget(imageViewCardBack)
            backAnim.setTarget(imageViewCardFront)
            backAnim.start()
            frontAnim.start()
            frontAnim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    isAnimationFinished = false
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isAnimationFinished = true
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationRepeat(animation: Animator?) {

                }
            })
        }

        /**
         * The card is facing up and we are running the animation
         * to flip the content of the card with a fade out,
         * at the same time flip the card background towards the screen to view with a fade in.
         */
        fun hideCard() {
            frontAnim.setTarget(imageViewCardFront)
            backAnim.setTarget(imageViewCardBack)
            frontAnim.start()
            backAnim.start()
            backAnim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    isAnimationFinished = false
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isAnimationFinished = true
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationRepeat(animation: Animator?) {

                }
            })
        }

        /**
         * providing a haptic feedback on touching the card to face up.
         * @param duration specifies the time duration for the vibration.
         */
        private fun hapticFeedback(duration: Long) =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        duration,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(duration)
            }

        /**
         * Once the card is matched the user need not to interact with it,
         * so it is hidden from the board.
         */
        fun removeCard() {
            rootLayout.animate()
                .alpha(0.0f)
                .scaleX(0.0f).scaleY(0.0f)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(300)
                .start()
            rootLayout.alpha = 0f
        }

    }

    /**
     * This listener informs the activity when the card is performed face down and when it performed face up.
     */
    interface ClickListener {
        fun onCardFaceUp(position: Int, card: Card)
        fun onCardFaceDown(position: Int, card: Card)
    }
}