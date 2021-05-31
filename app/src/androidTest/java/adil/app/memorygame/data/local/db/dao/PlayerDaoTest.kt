package adil.app.memorygame.data.local.db.dao

import adil.app.memorygame.data.local.db.DatabaseService
import adil.app.memorygame.data.local.db.entity.Player
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PlayerDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var databaseService: DatabaseService
    private lateinit var playerDao: PlayerDao

    @Before
    fun setup() {
        databaseService = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DatabaseService::class.java
        ).allowMainThreadQueries().build()
        playerDao = databaseService.playerDao()
    }

    @After
    fun tearDown() {
        databaseService.close()
    }

    @Test
    fun insertPlayer() = runBlockingTest {
        val player = Player(1, "adil", 10)
        playerDao.insert(player)

        val allPlayers = playerDao.getAllUsers()
        assertThat(allPlayers).contains(player)
    }

}