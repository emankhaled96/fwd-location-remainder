package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.dto.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {
    private lateinit var repository: RemindersLocalRepository
    private lateinit var database: RemindersDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        repository = RemindersLocalRepository(
            database.reminderDao(),
            Dispatchers.Main
        )
    }

    @After
    fun clean() {
        database.close()
        stopKoin()
    }

    @Test
    fun saveRemainders_GetAllRemainders() = runBlocking {
        val newRemainder = ReminderDTO(
            "title 1",
            "description 1",
            "location 1",
            0.0, 0.0
        )
        repository.saveReminder(newRemainder)
        val result = repository.getReminders()
        Assert.assertThat(result.succeeded, `is`(true))

        result as Result.Success
        result.data.forEach {
            assertThat(it.title , `is`("title 1"))
            assertThat(it.description , `is`("description 1"))
            assertThat(it.longitude , `is`(0.0))
            assertThat(it.latitude , `is`(0.0))
            assertThat(it.location , `is`("location 1"))

        }
    }
}