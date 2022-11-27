package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity


/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {
    companion object {
        const val TAG = "AuthenticationActivity"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private lateinit var authButton: TextView
    private val viewModel by viewModels<AuthenticationViewModel> {
        AuthenticationViewModel.AuthenticationViewModelFactory(
            application
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        authButton = findViewById(R.id.auth_button)
        authButton.setOnClickListener {
            navigateToFirebaseUI()
        }

        setUpObservers()
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout

    }

    private fun setUpObservers() {
        viewModel.authenticationState.observe(this) {
            when (it) {
                AuthenticationViewModel.AuthenticationState.AUTHENTICATED -> {
                    startActivity(Intent(this, RemindersActivity::class.java))
                    finish()
                }
                else -> {
                    authButton.text = getString(R.string.login)
                    authButton.setOnClickListener {
                        navigateToFirebaseUI()
                    }

                }
            }
        }
    }

    private fun navigateToFirebaseUI() {
        val customLayout = AuthMethodPickerLayout.Builder(R.layout.auth_layout)
            .setEmailButtonId(R.id.email_button)
            .setGoogleButtonId(R.id.gmail_button)
            .build()
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.FirebaseTheme)
                .setAuthMethodPickerLayout(customLayout)
                .build(),
            AuthenticationActivity.SIGN_IN_RESULT_CODE
        )
    }
}
