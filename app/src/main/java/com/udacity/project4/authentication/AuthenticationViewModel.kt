package com.udacity.project4.authentication

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.udacity.project4.FirebaseUserLiveData
import com.udacity.project4.base.BaseViewModel

class AuthenticationViewModel(val app: Application) : BaseViewModel(app) {
    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }

    val authenticationState = FirebaseUserLiveData().map { user->
        if(user!= null){
            AuthenticationState.AUTHENTICATED
        }else{
            AuthenticationState.UNAUTHENTICATED
        }
    }
    @Suppress("UNCHECKED_CAST")
    class AuthenticationViewModelFactory (
        private val app: Application
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            (AuthenticationViewModel(app) as T)
    }
}