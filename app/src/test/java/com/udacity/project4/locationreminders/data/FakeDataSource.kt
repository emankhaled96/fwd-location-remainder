package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var remainders: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {


    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        remainders?.let { return Result.Success(ArrayList(it)) }
        return Result.Error(
            "Remainders not found"
        )
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        remainders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        remainders?.firstOrNull { it.id == id }.let {
            if (it != null) {
                return Result.Success(it)
            } else {
                return Result.Error("remainder not found")
            }
        }

    }


    override suspend fun deleteAllReminders() {
        remainders?.clear()
    }


}