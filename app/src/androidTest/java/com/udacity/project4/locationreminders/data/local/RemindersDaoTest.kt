package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

private lateinit var database: RemindersDatabase
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }
    @After
    fun closeDb() {

        database.close()
        stopKoin()
    }

    @Test
    fun insertRemainderAndGetById()= runBlockingTest {
        // GIVEN - insert Remainder
        val remainder = ReminderDTO(
        title = "Remainder 1 ",
            description = "remainder 1 description",
            location = "location 1 ",
            latitude = 0.0,
            longitude = 0.0,
            id = "R1"
        )
        database.reminderDao().saveReminder(remainder)

        // WHEN - GET Remainder By Id from Database

        val remainderFromDatabase = database.reminderDao().getReminderById(remainder.id)
        assertThat(remainderFromDatabase , notNullValue())
        assertThat(remainderFromDatabase?.id , `is`(remainder.id))
        assertThat(remainderFromDatabase?.title , `is`(remainder.title))
        assertThat(remainderFromDatabase?.description , `is`(remainder.description))
        assertThat(remainderFromDatabase?.location , `is`(remainder.location))
        assertThat(remainderFromDatabase?.latitude , `is`(remainder.latitude))
        assertThat(remainderFromDatabase?.longitude , `is`(remainder.longitude))


    }
}