package com.nptel.courses.online.ui.launch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Keep
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.nptel.courses.online.BuildConfig
import com.nptel.courses.online.R
import com.nptel.courses.online.errorMessage
import com.nptel.courses.online.retrofit.RetrofitServices
import com.nptel.courses.online.ui.AppTheme
import com.nptel.courses.online.ui.course.CourseSelectionActivity
import com.nptel.courses.online.ui.main.MainActivity
import com.nptel.courses.online.ui.playlist.PlaylistActivity
import com.nptel.courses.online.ui.playlist.PlaylistFragment
import com.nptel.courses.online.utility.getSelectedCourse
import com.nptel.courses.online.utility.getUser
import com.nptel.courses.online.utility.isLoginSkipped
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    @Inject
    lateinit var retrofitServices: RetrofitServices
    private lateinit var loginActivityLauncher: ActivityResultLauncher<Intent>

    override fun onStart() {
        super.onStart()
        loginActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { checkUser() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Container() }
        checkForUpdate()
    }

    private fun checkForDynamic() {
        FirebaseDynamicLinks
            .getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                if (pendingDynamicLinkData != null) {
                    val deepLink = pendingDynamicLinkData.link
                    if (deepLink == null) checkUser() else {
                        val url = deepLink.toString()
                        if (url.contains("youtu.be")) {
                            val id = url.replace("https://youtu.be/", "")
                            val intent = Intent(this, PlaylistActivity::class.java)
                            intent.putExtra(
                                PlaylistActivity.MODE,
                                PlaylistFragment.Mode.SHARED_VIDEO.toString()
                            )
                            intent.putExtra(PlaylistActivity.COURSE_NAME, "Shared")
                            intent.putExtra(PlaylistActivity.ID, id)
                            startActivity(intent)
                            getIntent().data = null
                        } else {
                            checkUser()
                        }
                    }
                } else {
                    checkUser()
                }
            }.addOnFailureListener(this) { checkUser() }
    }

    private fun checkForUpdate() {
        setContent { Container() }
        lifecycleScope.launchWhenResumed {
            try {
                val update =
                    retrofitServices.checkForUpdate()
                if (update.checkAvailable()) setContent { Container(update) } else checkForDynamic()
            } catch (throwable: Throwable) {
                setContent {
                    Container(errorMessage = throwable.errorMessage())
                }
            }
        }
    }

    private fun checkUser() {
        if (getUser(this) == null && !isLoginSkipped(this)) {
//            loginActivityLauncher.launch(Intent(this@SplashActivity, LoginActivity::class.java))
        } else {
            startActivity(
                Intent(
                    this,
                    if (getSelectedCourse(this) == null) CourseSelectionActivity::class.java else MainActivity::class.java
                )
            )
            finish()
        }
    }


    @Composable
    fun Container(
        update: Update? = null,
        errorMessage: String? = null
    ) {
        AppTheme {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(painter = painterResource(id = R.mipmap.ic_launcher_foreground), "logo")
                if (update?.checkAvailable() == true)
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("A mandatory update is available. This app will not work without this update.")
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            OutlinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    checkForDynamic()
                                    Toast.makeText(this@SplashActivity, "skip", Toast.LENGTH_LONG)
                                        .show()
                                },
                                contentPadding = PaddingValues(vertical = 12.dp)
                            ) {
                                Text(text = "Skip Update")
                            }
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    startActivity(
                                        Intent(Intent.ACTION_VIEW).setData(
                                            Uri.parse(
                                                update.updateLink
                                            )
                                        )
                                    )
                                }, contentPadding = PaddingValues(vertical = 12.dp)
                            ) {
                                Text(text = "Update")
                            }
                        }
                    }

                if (update == null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        if (errorMessage == null) {
                            Text(text = "Starting", modifier = Modifier.offset(y = 16.dp))
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .width(32.dp)
                                    .clip(RoundedCornerShape(50))
                            )
                        } else {
                            Text(text = errorMessage, modifier = Modifier.offset(y = 16.dp))
                            TextButton(
                                onClick = { checkForUpdate() },
                                modifier = Modifier.offset(y = 32.dp)
                            ) {
                                Text(text = "Retry")
                            }
                        }
                    }
                }
            }
        }

    }

    @Preview(showSystemUi = true)
    @Composable
    fun SplashWithUpdatePreview() {
        Container(Update(123, 123, "abc", true))
    }

    @Preview(showSystemUi = true)
    @Composable
    fun SplashStartingPreview() {
        Container()
    }

    @Keep
    data class Update(
        var current: Int,
        var minimum: Int,
        var updateLink: String,
        var isMandatory: Boolean = false
    ) {

        fun checkAvailable(): Boolean {
            isMandatory = BuildConfig.VERSION_CODE < minimum
            return BuildConfig.VERSION_CODE < current
        }
    }
}