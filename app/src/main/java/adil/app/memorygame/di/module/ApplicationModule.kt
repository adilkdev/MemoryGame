package adil.app.memorygame.di.module

import adil.app.memorygame.data.local.db.DatabaseService
import adil.app.memorygame.data.model.Card
import adil.app.memorygame.data.repository.PlayerRepository
import adil.app.memorygame.utils.AppConstants
import adil.app.memorygame.utils.CardsProvider
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideDatabaseService(@ApplicationContext appContext: Context): DatabaseService =
        Room.databaseBuilder(
            appContext,
            DatabaseService::class.java, AppConstants.DATABASE_NAME
        ).build()

    @Provides
    fun providePlayerRepository(databaseService: DatabaseService) =
        PlayerRepository(databaseService)

    @Singleton
    @Provides
    fun provideCards(): List<Card> = CardsProvider.cards()
}