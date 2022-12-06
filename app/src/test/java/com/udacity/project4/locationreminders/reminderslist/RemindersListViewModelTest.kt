package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    private lateinit var remainderListViewModel: RemindersListViewModel
    private lateinit var dataSource: FakeDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUpViewModel() {
        dataSource = FakeDataSource()
        remainderListViewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }

    @Test
    fun getRemainders_testLoadingData() = mainCoroutineRule.runBlockingTest {
        val remainderTest = ReminderDTO(
            "Remainder 1 Title ",
            "Remainder 1 Description",
            "Remainder 1 Location",
            0.0, 0.0
        )
        dataSource.saveReminder(remainderTest)
        mainCoroutineRule.pauseDispatcher()
        remainderListViewModel.loadReminders()
        Assert.assertTrue(
            "Loading State",
            remainderListViewModel.showLoading.getOrAwaitValue()
        )
        mainCoroutineRule.resumeDispatcher()
        Assert.assertFalse(
            "Finish Loading State",
            remainderListViewModel.showLoading.getOrAwaitValue()
        )
        Assert.assertFalse("We Have Data", remainderListViewModel.showNoData.getOrAwaitValue())
    }

    @Test
    fun getRemainders_testNoData() {
        remainderListViewModel.loadReminders()
        Assert.assertTrue("We Have No Data", remainderListViewModel.showNoData.getOrAwaitValue())
    }

    @Test
    fun getRemainders_testError() {
        dataSource.setError(true)
        remainderListViewModel.loadReminders()
        Assert.assertEquals("Test Exception", remainderListViewModel.showSnackBar.getOrAwaitValue())
    }

    @After
    fun clear() {
        stopKoin()
    }

}