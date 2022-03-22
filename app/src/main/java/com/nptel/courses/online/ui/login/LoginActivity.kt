package com.nptel.courses.online.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.nptel.courses.online.BuildConfig
import com.nptel.courses.online.components.SubmitButton
import com.nptel.courses.online.components.SubmitButtonStyle
import com.nptel.courses.online.errorMessage
import com.nptel.courses.online.retrofit.RetrofitFactory
import com.nptel.courses.online.ui.AppTheme
import com.nptel.courses.online.utility.saveUser
import com.nptel.courses.online.utility.setLoginSkipped
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
class LoginActivity : AppCompatActivity() {

    private var signInIntent: Intent? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    override fun onStart() {
        super.onStart()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestIdToken(if (BuildConfig.BUILD_TYPE == "release") "862491076156-o5oehl0vqauqhcnrqab7ofbe2lb0lulo.apps.googleusercontent.com" else "862491076156-o5oehl0vqauqhcnrqab7ofbe2lb0lulo.apps.googleusercontent.com")
            .build()
        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
        signInIntent = googleSignInClient.signInIntent
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LoginScreen() }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            FirebaseCrashlytics.getInstance().recordException(e)
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        lifecycleScope.launch {
            try {
                val user = RetrofitFactory.getRetrofitService(this@LoginActivity)
                    .updateUser("google", account.idToken!!)
                saveUser(this@LoginActivity, user)
                setResult(RESULT_OK)
                finish()
            } catch (throwable: Throwable) {
                Toast.makeText(this@LoginActivity, throwable.errorMessage(), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    @Composable
    fun LoginScreen() {
        AppTheme {
//            val composition by rememberLottieComposition(LottieAnimationSpec.RawRes(R.raw.lotties_55931_mobile_marketings))
//            val progress by animateLottieCompositionAsState(composition)
            Scaffold(topBar = {
                TopAppBar(backgroundColor = MaterialTheme.colors.surface) {
                    Text(text = "Login")
                }
            }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Login to bookmark, create your collections and share content with friends.",
                        modifier = Modifier.padding(16.dp)
                    )
//                    LottieAnimation(composition, progress)
                    Spacer(modifier = Modifier.weight(1f, true))
                    SubmitButton(onClick = {
                        activityResultLauncher.launch(signInIntent)
                    }, modifier = Modifier, text = "Login With ", icon = Icons.Default.Facebook)
                    SubmitButton(
                        text = "Skip",
                        style = SubmitButtonStyle.Text,
                        onClick = {
                            setLoginSkipped(this@LoginActivity)
                            setResult(RESULT_CANCELED)
                            finish()
                        },
                        modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
                    )
                }
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun LoginScreenPreview() {
        LoginScreen()
    }
}