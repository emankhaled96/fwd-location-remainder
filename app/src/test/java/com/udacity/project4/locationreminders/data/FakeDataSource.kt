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
        if (returnError) {
            return Result.Error("Reminder not found!")
        }
        for(i in remainders){
            if(i.id == id){
                return Result.Success(i)
            }
            return Result.Error("Reminder not found!")

        }
        return Result.Error("Reminder not found!")
    }


    override suspend fun deleteAllReminders() {
        remainders.clear()
    }


}