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
    fun saveRemainder_getRemainderByID_Success() = runBlocking {
        // GIVEN : New Remainder and Save it
        val newRemainder = ReminderDTO(
            "title 1",
            "description 1",
            "location 1",
            0.0, 0.0, "R1"
        )

        repository.saveReminder(newRemainder)
        // WHEN : get remainder by Id
        val result = repository.getReminder(newRemainder.id)
        // THEN: Verify result

        result as Result.Success
        result.data.let {
            assertThat(it.title, `is`(newRemainder.title))
            assertThat(it.description, `is`(newRemainder.description))
            assertThat(it.longitude, `is`(newRemainder.longitude))
            assertThat(it.latitude, `is`(newRemainder.latitude))
            assertThat(it.location, `is`(newRemainder.location))

        }
    }

    @Test
    fun getRemainderByID_Error() = runBlocking {
        val result = repository.getReminder("R1")

        Assert.assertTrue((result is Result.Error))
        result as Result.Error
        assertThat(result.message, `is`("Reminder not found!"))
    }

    @Test
    fun saveRemainders_GetAllRemainders() = runBlocking {
        // GIVEN : New Remainder and Save it
        val newRemainder = ReminderDTO(
            "title 1",
            "description 1",
            "location 1",
            0.0, 0.0
        )

        repository.saveReminder(newRemainder)

        // WHEN : get remainders
        val result = repository.getReminders()

        // THEN: Verify result
        Assert.assertThat(result.succeeded, `is`(true))

        result as Result.Success
        result.data.forEach {
            assertThat(it.title, `is`("title 1"))
            assertThat(it.description, `is`("description 1"))
            assertThat(it.longitude, `is`(0.0))
            assertThat(it.latitude, `is`(0.0))
            assertThat(it.location, `is`("location 1"))

        }
    }
}