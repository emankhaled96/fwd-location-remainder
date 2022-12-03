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
        try {
            remainders.let { return Result.Success(ArrayList(it)) }
        } catch (e: Exception) {
            return Result.Error(
                e.localizedMessage
            )
        }

    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        remainders.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        return try {
            Result.Success(data = remainders.firstOrNull { it.id == id}!!)
        }catch (e:Exception){
            Result.Error(e.localizedMessage)
        }

    }


    override suspend fun deleteAllReminders() {
        remainders.clear()
    }


}