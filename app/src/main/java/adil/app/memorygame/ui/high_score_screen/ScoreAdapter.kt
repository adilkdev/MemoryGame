package adil.app.memorygame.ui.high_score_screen

import adil.app.memorygame.R
import adil.app.memorygame.data.local.db.entity.Player
import adil.app.memorygame.databinding.ItemScoreBinding
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ScoreAdapter(private var scores: List<Player>) :
    RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    /**
     * highlighting the player only when he's navigated to scores screen
     * to show him his stats
     * @param position of the player to be highlighted.
     */
    fun highlightPlayer(position: Int) {
        scores[position].shouldHighlight = true
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        return ScoreViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_score, parent, false)
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]
        holder.binding.textViewRank.text =
            holder.binding.root.context.getString(R.string.hash, score.rank)
        holder.binding.textViewName.text = score.name
        holder.binding.textViewScore.text = score.score.toString()

        /**
         * if the player is highlighted, item's background is changed to teal colour otherwise
         * there's no need to set colour.
         */
        if (score.shouldHighlight) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.binding.rootLayout.setBackgroundColor(
                    holder.binding.rootLayout.context
                        .getColor(R.color.color_score_highlight)
                )
            } else {
                holder.binding.rootLayout.setBackgroundColor(R.color.color_score_highlight)
            }
        } else {
            holder.binding.rootLayout.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun getItemCount(): Int = scores.size

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = ItemScoreBinding.bind(itemView)
    }
}