package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {
    private lateinit var saveRemainderViewModel: SaveReminderViewModel
    private lateinit var dataSource: FakeDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUpViewModel() {
        dataSource = FakeDataSource()
        saveRemainderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }
    @Test
    fun saveRemainder_testLoadingDataAndToast() = mainCoroutineRule.runBlockingTest {
        val remainderTest = ReminderDataItem(
            "Remainder 1 Title ",
            "Remainder 1 Description",
            "Remainder 1 Location",
            0.0, 0.0
        )
      mainCoroutineRule.pauseDispatcher()
        saveRemainderViewModel.saveReminder(remainderTest)
        Assert.assertTrue(
            "Loading State",
            saveRemainderViewModel.showLoading.getOrAwaitValue()
        )
        mainCoroutineRule.resumeDispatcher()
        Assert.assertFalse(
            "Finish Loading State",
            saveRemainderViewModel.showLoading.getOrAwaitValue()
        )
        Assert.assertEquals("Reminder Saved !", saveRemainderViewModel.showToast.getOrAwaitValue())
    }

    @Test
    fun validateData_titleNull(){
        val remainderTest = ReminderDataItem(
            null,
            "Remainder 1 Description",
            "Remainder 1 Location",
            0.0, 0.0
        )
        saveRemainderViewModel.validateEnteredData(remainderTest)
        Assert.assertEquals(R.string.err_enter_title , saveRemainderViewModel.showSnackBarInt.getOrAwaitValue())
    }

    @Test
    fun validateData_titleEmpty(){
        val remainderTest = ReminderDataItem(
            "",
            "Remainder 1 Description",
            "Remainder 1 Location",
            0.0, 0.0
        )
        saveRemainderViewModel.validateEnteredData(remainderTest)
        Assert.assertEquals(R.string.err_enter_title , saveRemainderViewModel.showSnackBarInt.getOrAwaitValue())
    }

    @Test
    fun validateData_LocationNull(){
        val remainderTest = ReminderDataItem(
            "Remainder 1 Title",
            "Remainder 1 Description",
            null,
            0.0, 0.0
        )
        saveRemainderViewModel.validateEnteredData(remainderTest)
        Assert.assertEquals(R.string.err_select_location , saveRemainderViewModel.showSnackBarInt.getOrAwaitValue())
    }

    @Test
    fun validateData_LocationEmpty(){
        val remainderTest = ReminderDataItem(
            "remainder 1 Title",
            "Remainder 1 Description",
            "",
            0.0, 0.0
        )
        saveRemainderViewModel.validateEnteredData(remainderTest)
        Assert.assertEquals(R.string.err_select_location , saveRemainderViewModel.showSnackBarInt.getOrAwaitValue())
    }
    @After
    fun clear(){
        stopKoin()
    }



}