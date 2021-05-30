package adil.app.memorygame.di.module

import adil.app.memorygame.data.local.db.entity.Player
import adil.app.memorygame.data.model.Card
import adil.app.memorygame.data.repository.PlayerRepository
import adil.app.memorygame.ui.game_screen.GameCardAdapter
import adil.app.memorygame.ui.high_score_screen.ScoreAdapter
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun provideGameCardAdapter(@ActivityContext context: Context, cards: List<Card>) =
        GameCardAdapter(context, cards)

    @Provides
    fun provideScoreAdapter(list: List<Player>): ScoreAdapter = ScoreAdapter(list)

    @Provides
    fun providePlayerList(playerRepository: PlayerRepository): List<Player> =
        playerRepository.getAllPlayers()
}