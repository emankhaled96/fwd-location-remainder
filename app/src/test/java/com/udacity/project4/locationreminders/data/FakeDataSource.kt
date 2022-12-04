package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var remainders: MutableList<ReminderDTO> = mutableListOf()) :
    ReminderDataSource {
    private var returnError = false

    fun setError(value: Boolean) {
        returnError = value
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (returnError) {
            return Result.Error("Test Exception")
        }
        return Result.Success(remainders)

    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        remainders.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (remainders.firstOrNull { it.id == id } == null) {
            return Result.Error("Reminder not found!")
        }
        return Result.Success(data = remainders.firstOrNull {
            it.id == id
        }!!)
    }


    override suspend fun deleteAllReminders() {
        remainders.clear()
    }


}