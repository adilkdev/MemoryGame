package adil.app.memorygame.ui.high_score_screen

import adil.app.memorygame.R
import adil.app.memorygame.data.local.db.entity.User
import adil.app.memorygame.databinding.ItemScoreBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ScoreAdapter : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    private var scores: List<User> = listOf()

    fun setScoreData(scores: List<User>) {
        this.scores = scores
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        return ScoreViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_score, parent, false))
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]
        holder.binding.textViewRank.text = "#${score.rank.toString()}"
        holder.binding.textViewName.text = score.name
        holder.binding.textViewScore.text = score.score.toString()
    }

    override fun getItemCount(): Int = scores.size

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = ItemScoreBinding.bind(itemView)
    }
}