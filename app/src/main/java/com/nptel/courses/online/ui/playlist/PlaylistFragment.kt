package com.nptel.courses.online.ui.playlist

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.nptel.courses.online.BuildConfig
import com.nptel.courses.online.R
import com.nptel.courses.online.adapters.QuickCollectionListAdapter
import com.nptel.courses.online.databinding.AddToCollectionBinding
import com.nptel.courses.online.entities.Collection
import com.nptel.courses.online.entities.Video
import com.nptel.courses.online.errorMessage
import com.nptel.courses.online.interfaces.ListItemClickListener
import com.nptel.courses.online.retrofit.RetrofitFactory
import com.nptel.courses.online.ui.AppTheme
import com.nptel.courses.online.ui.login.LoginActivity
import com.nptel.courses.online.utility.ColorUtility
import com.nptel.courses.online.utility.ISO8601
import com.nptel.courses.online.utility.getUser
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaylistFragment : Fragment() {

    private lateinit var composeView: ComposeView

    //    private var actionBar: ActionBar? = null
//    private lateinit var adapter: VideoListAdapter
    private var youTubePlayer: YouTubePlayer? = null
    private var screenConfiguration: Configuration? = null
    private var mode: Mode? = null
    private lateinit var id: String
    private var courseName: String? = null
    private var playlistName: String? = null
    private lateinit var loginLauncher: ActivityResultLauncher<Intent>
    private fun showLoginDialog() {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        dialogBuilder.setTitle("Login to bookmark, create your collections and share content with friends.")
        dialogBuilder.setPositiveButton("Login") { _: DialogInterface?, _: Int ->
           /* loginLauncher.launch(
//                Intent(requireContext(), LoginActivity::class.java)
            )*/
        }
        dialogBuilder.setNegativeButton("Cancel") { _: DialogInterface?, _: Int -> }
        dialogBuilder.show()
    }

    private val videoListItemClickListener: ListItemClickListener<Video> =
        object : ListItemClickListener<Video> {
            override fun onClick(video: Video) {
                playVideo(video)
            }

            override fun onOptionClicked(video: Video, optionId: Int) {
                when (optionId) {
                    R.id.share_video -> {
                        shareVideo(video)
                    }
                    R.id.favourite -> {
                        if (getUser(requireContext()) == null) {
                            showLoginDialog()
                            return
                        }
                        if (video.isFavorite) {
                            /*RetrofitFactory.getRetrofitService(requireContext())
                                .removeFromCollection("favorite", video.id)
                                .enqueue(object : Callback<Void?> {
                                    override fun onResponse(
                                        call: Call<Void?>,
                                        response: Response<Void?>
                                    ) {
                                        fetch()
                                        if (response.isSuccessful) {
                                            Toast.makeText(
                                                requireContext(),
                                                "Video removed from favorite",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else Toast.makeText(
                                            requireContext(),
                                            response.errorBody().toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                                        fetch()
                                        Toast.makeText(
                                            requireContext(),
                                            t.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })*/
                        } else {
                            /*RetrofitFactory.getRetrofitService(requireContext())
                                .addToCollection("favorite", video.id)
                                .enqueue(object : Callback<Void?> {
                                    override fun onResponse(
                                        call: Call<Void?>,
                                        response: Response<Void?>
                                    ) {
                                        fetch()
                                        if (response.isSuccessful) {
                                            Toast.makeText(
                                                requireContext(),
                                                "Video marked favorite",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else Toast.makeText(
                                            requireContext(),
                                            response.errorBody().toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                                        fetch()
                                        Toast.makeText(
                                            requireContext(),
                                            t.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })*/
                        }
                        video.isFavorite = !video.isFavorite
                    }
                    R.id.add_to_playlist -> {
                        if (getUser(requireContext()) == null) {
                            showLoginDialog()
                            return
                        }
                        addToPlaylist(video)
                    }
                    R.id.delete_video -> {
                        /*RetrofitFactory.getRetrofitService(requireContext())
                            .removeFromCollection(id, video.id).enqueue(object : Callback<Void?> {
                                override fun onResponse(
                                    call: Call<Void?>,
                                    response: Response<Void?>
                                ) {
                                    fetch()
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Video removed from the collection.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else Toast.makeText(
                                        requireContext(),
                                        "Something went wrong.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                override fun onFailure(call: Call<Void?>, throwable: Throwable) {
                                    fetch()
                                    Toast.makeText(
                                        requireContext(),
                                        "Something went wrong.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })*/
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mode = Mode.valueOf(requireArguments().getString(ARG_PARAM_MODE)!!)
            id = requireArguments().getString(ARG_PARAM_ID).toString()
            courseName = requireArguments().getString(ARG_PARAM_COURSE_NAME)
            playlistName = requireArguments().getString(ARG_PARAM_PLAYLIST_NAME)
        }
        loginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) Toast.makeText(
                    requireContext(),
                    "You have successfully signed in.",
                    Toast.LENGTH_SHORT
                ).show() else Toast.makeText(requireContext(), "Sign In failed", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*binding.youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                this@PlaylistFragment.youTubePlayer = youTubePlayer
            }
        })*/
//        lifecycle.addObserver(binding.youtubePlayerView)
//        adapter = VideoListAdapter(videoListItemClickListener)
//        binding.recyclerView.adapter = adapter
        if (mode == null) {
            Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show()
            if (requireActivity() is PlaylistActivity) requireActivity().finish()
        }
//        actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        fetch()
        composeView = ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        return composeView
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        screenConfiguration = newConfig
        /*if (binding.youtubePlayerContainer.visibility == View.GONE) return
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.youtubePlayerView.enterFullScreen()
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            if (requireActivity() is MainActivity) (requireActivity() as MainActivity).toggleFullScreen(
                true
            )
            if (actionBar != null) actionBar!!.hide()
        } else {
            binding.youtubePlayerView.exitFullScreen()
            requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            if (requireActivity() is MainActivity) (requireActivity() as MainActivity).toggleFullScreen(
                false
            )
            if (actionBar != null) actionBar!!.show()
        }*/
    }

    private fun fetch() {
        when (mode) {
            Mode.PLAYLIST -> showYoutubePlaylist()
            Mode.COLLECTION -> showCustomPlaylist()
            Mode.SHARED_VIDEO -> {
//                if (actionBar != null) actionBar!!.title = "Shared Video"
                showSharedVideo()
            }
            Mode.SEARCHED -> {
//                if (actionBar != null) actionBar!!.setTitle("Searched Video")
                showSearchedVideo()
            }
            else -> {
                Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show()
                if (requireActivity() is PlaylistActivity) requireActivity().finish()
            }
        }
    }

    private fun setVideoList(videos: List<Video>) {
        setVideoList(videos, null)
    }

    private fun setVideoList(videos: List<Video>, runnable: Runnable?) {
//        adapter.submitList(videos, runnable)
    }

    private fun showYoutubePlaylist() {
        /*if (actionBar != null) {
            actionBar!!.title = courseName
            actionBar!!.subtitle = playlistName
        }*/

        lifecycleScope.launchWhenResumed {
            try {
                val videos = RetrofitFactory.getRetrofitService(requireContext())
                    .getVideos(id, "position ASC")
                composeView.setContent {
                    Main(videos = videos)
                }
            } catch (throwable: Throwable) {
                composeView.setContent {
                    Text(text = throwable.errorMessage())
                }
            }
        }
        /*RetrofitFactory.getRetrofitService(requireContext()).getVideos(id, "position ASC")
            .enqueue(object : Callback<List<Video>> {
                override fun onResponse(call: Call<List<Video>>, response: Response<List<Video>>) {
                    if (response.isSuccessful) {
                        setVideoList(response.body()!!)
                        if (response.body()!!.isEmpty()) {
                            binding.nothingFound.root.visibility = View.VISIBLE
                            binding.nothingFound.retry.setOnClickListener { view: View? ->
                                binding.nothingFound.root.visibility = View.GONE
                                adapter.startShimmer()
                                showYoutubePlaylist()
                            }
                        }
                    } else {
                        adapter.submitList(null)
                        binding.serverError.root.visibility = View.VISIBLE
                        binding.serverError.retry.setOnClickListener {
                            binding.serverError.root.visibility = View.GONE
                            showYoutubePlaylist()
                            adapter.startShimmer()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Video>>, throwable: Throwable) {
                    adapter.submitList(null)
                    binding.serverError.root.visibility = View.VISIBLE
                    binding.serverError.retry.setOnClickListener { view: View? ->
                        binding.serverError.root.visibility = View.GONE
                        showYoutubePlaylist()
                        adapter.startShimmer()
                    }
                }
            })*/
    }

    private fun showCustomPlaylist() {
        /*if (actionBar != null) actionBar!!.title = courseName
        if (id != "favorite") adapter.setShowDelete()*/

        lifecycleScope.launchWhenResumed {
            try {
                val videos = RetrofitFactory.getRetrofitService(requireContext()).getCollection(id)
                composeView.setContent {
                    Main(videos = videos)
                }
            } catch (throwable: Throwable) {
                composeView.setContent {
                    Text(text = throwable.errorMessage())
                }
            }
        }
        /*RetrofitFactory.getRetrofitService(requireContext()).getCollection(id)
            .enqueue(object : Callback<List<Video>> {
                override fun onResponse(call: Call<List<Video>>, response: Response<List<Video>>) {
                    if (response.isSuccessful) {
                        setVideoList(response.body()!!)
                        if (response.body()!!.isEmpty()) {
                            binding.nothingFound.root.visibility = View.VISIBLE
                            binding.nothingFound.retry.setOnClickListener { view: View? ->
                                binding.nothingFound.root.visibility = View.GONE
                                adapter.startShimmer()
                                showCustomPlaylist()
                            }
                        }
                    } else {
                        adapter.submitList(null)
                        binding.serverError.root.visibility = View.VISIBLE
                        binding.serverError.retry.setOnClickListener { view: View? ->
                            binding.serverError.root.visibility = View.GONE
                            showCustomPlaylist()
                            adapter.startShimmer()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Video>>, throwable: Throwable) {
                    adapter.submitList(null)
                    binding.serverError.root.visibility = View.VISIBLE
                    binding.serverError.retry.setOnClickListener {
                        binding.serverError.root.visibility = View.GONE
                        showCustomPlaylist()
                        adapter.startShimmer()
                    }
                }
            })*/
    }

    private fun showSharedVideo() {

        lifecycleScope.launchWhenResumed {
            try {
                val videos =
                    RetrofitFactory.getRetrofitService(requireContext()).getSharedVideos(id)
                composeView.setContent {
                    Main(videos = videos)
                }
            } catch (throwable: Throwable) {
                composeView.setContent {
                    Text(text = throwable.errorMessage())
                }
            }
        }
        /*RetrofitFactory.getRetrofitService(requireContext()).getSharedVideos(id)
            .enqueue(object : Callback<List<Video>> {
                override fun onResponse(call: Call<List<Video>>, response: Response<List<Video>>) {
                    if (response.isSuccessful) {
                        adapter.submitList(response.body())
                        if (response.body()!!.isEmpty()) {
                            binding.nothingFound.root.visibility = View.VISIBLE
                            binding.nothingFound.retry.setOnClickListener {
                                binding.nothingFound.root.visibility = View.GONE
                                adapter.startShimmer()
                                showYoutubePlaylist()
                            }
                        }
                    } else {
                        adapter.submitList(null)
                        binding.serverError.root.visibility = View.VISIBLE
                        binding.serverError.retry.setOnClickListener {
                            binding.serverError.root.visibility = View.GONE
                            showYoutubePlaylist()
                            adapter.startShimmer()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Video>>, throwable: Throwable) {
                    adapter.submitList(null)
                    binding.serverError.root.visibility = View.VISIBLE
                    binding.serverError.retry.setOnClickListener {
                        binding.serverError.root.visibility = View.GONE
                        showYoutubePlaylist()
                        adapter.startShimmer()
                    }
                }
            })*/
    }

    private fun showSearchedVideo() {

        lifecycleScope.launchWhenResumed {
            try {
                val video =
                    RetrofitFactory.getRetrofitService(requireContext()).getSearchedVideo(id)
                composeView.setContent {
                    Main(videos = listOf(video))
                }
            } catch (throwable: Throwable) {
                composeView.setContent {
                    Text(text = throwable.errorMessage())
                }
            }
        }
        /*RetrofitFactory.getRetrofitService(requireContext()).getSearchedVideo(id)
            .enqueue(object : Callback<Video> {
                override fun onResponse(call: Call<Video>, response: Response<Video>) {
                    if (response.isSuccessful) {
                        adapter.submitList(listOf(response.body()))
                        if (response.body() == null) {
                            binding.nothingFound.root.visibility = View.VISIBLE
                            binding.nothingFound.retry.setOnClickListener {
                                binding.nothingFound.root.visibility = View.GONE
                                adapter.startShimmer()
                                showYoutubePlaylist()
                            }
                        }
                    } else {
                        adapter.submitList(null)
                        binding.serverError.root.visibility = View.VISIBLE
                        binding.serverError.retry.setOnClickListener {
                            binding.serverError.root.visibility = View.GONE
                            showYoutubePlaylist()
                            adapter.startShimmer()
                        }
                    }
                }

                override fun onFailure(call: Call<Video>, throwable: Throwable) {
                    adapter.submitList(null)
                    binding.serverError.root.visibility = View.VISIBLE
                    binding.serverError.retry.setOnClickListener {
                        binding.serverError.root.visibility = View.GONE
                        showYoutubePlaylist()
                        adapter.startShimmer()
                    }
                }
            })*/
    }

    fun playVideo(video: Video) {
//        binding.youtubePlayerContainer.visibility = View.VISIBLE
        youTubePlayer?.loadVideo(video.youtubeVideoId, 0f)
            ?: Handler(Looper.getMainLooper()).postDelayed({ playVideo(video) }, 100)
//        binding.recyclerView.smoothScrollToPosition(adapter.currentList.indexOf(video))
        if (screenConfiguration != null) onConfigurationChanged(screenConfiguration!!)
    }

    fun shareVideo(video: Video) {
        val url = "https://youtu.be/" + video.youtubeVideoId
        Toast.makeText(requireContext(), "Creating share link...", Toast.LENGTH_SHORT).show()
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(url))
            .setDomainUriPrefix("https://nptel.page.link")
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
            .addOnCompleteListener(
                requireActivity(),
                OnCompleteListener<ShortDynamicLink?> { task: Task<ShortDynamicLink?> ->
                    if (task.isSuccessful && task.result != null) {
                        val shortLink: Uri? = task.result?.shortLink
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, shortLink?.toString() ?: "error")
                        startActivity(Intent.createChooser(intent, "Share URL"))
                    } else {
                        if (BuildConfig.DEBUG) Toast.makeText(
                            requireContext(),
                            task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show() else Toast.makeText(
                            requireContext(),
                            "Some error occurred. Can't share now.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }

    fun addToPlaylist(video: Video) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val binding = AddToCollectionBinding.inflate(bottomSheetDialog.layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.show()
        binding.video.text = Html.fromHtml("Add <b>" + video.title + "</b> to")
        val adapter = QuickCollectionListAdapter(video.id)
        binding.recyclerView.adapter = adapter
        /*RetrofitFactory.getRetrofitService(requireContext()).collections.enqueue(object :
            Callback<List<Collection>> {
            override fun onResponse(
                call: Call<List<Collection>>,
                response: Response<List<Collection>>
            ) {
                if (response.isSuccessful) adapter.submitList(response.body())
            }

            override fun onFailure(call: Call<List<Collection>>, throwable: Throwable) {}
        })*/
        binding.save.setOnClickListener { view: View? ->
            val playlistName: String
            if (adapter.selectedCollection == null) {
                playlistName = binding.collectionName.text.toString()
                if (playlistName.isEmpty()) {
                    binding.collectionName.error =
                        "Provide playlist name" + if (adapter.currentList.size > 0) " or choose from above list" else ""
                    return@setOnClickListener
                }
            } else playlistName = adapter.selectedCollection?.name!!
            /*RetrofitFactory.getRetrofitService(requireContext())
                .addToCollection(playlistName, video.id).enqueue(object : Callback<Void?> {
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Video added to playlist",
                                Toast.LENGTH_SHORT
                            ).show()
                            bottomSheetDialog.dismiss()
                        } else Toast.makeText(
                            requireContext(),
                            "Can't save video. Encountered some error.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(call: Call<Void?>, throwable: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "Can't save video. Encountered some error.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })*/
        }
    }

    enum class Mode {
        PLAYLIST, COLLECTION, SHARED_VIDEO, SHARED_COLLECTION, SEARCHED, ERROR
    }

    companion object {
        private const val ARG_PARAM_MODE = "mode"
        private const val ARG_PARAM_ID = "id"
        private const val ARG_PARAM_COURSE_NAME = "coursename"
        private const val ARG_PARAM_PLAYLIST_NAME = "playlist name"
        fun newInstance(
            mode: Mode,
            id: String?,
            courseName: String?,
            playlistName: String?
        ): PlaylistFragment {
            val fragment = PlaylistFragment()
            val args = Bundle()
            args.putString(ARG_PARAM_MODE, mode.toString())
            args.putString(ARG_PARAM_ID, id)
            args.putString(ARG_PARAM_COURSE_NAME, courseName)
            args.putString(ARG_PARAM_PLAYLIST_NAME, playlistName)
            fragment.arguments = args
            return fragment
        }
    }

    @Composable
    fun Main(videos: List<Video>) {
        val dark = isSystemInDarkTheme();
        AppTheme {
            LazyColumn(contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)) {
                val colors =
                    ColorUtility.getRandomColors(150, videos.size, dark).map { Color(it) }
                items(videos zip colors) { (video, color) ->
                    Video(video = video, color = color)

                }
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun MainPreview() {
        Main(videos = Video.dummies(10))
    }

    @Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
    @Composable
    fun MainDarkPreview() {
        MainPreview()
    }

    @Composable
    fun Video(video: Video, color: Color) {
        Box(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(horizontal = 8.dp)
                .padding(bottom = 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .clickable {

                    },
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
            ) {
                Column(modifier = Modifier.background(color = color.copy(alpha = 0.15f))) {
                    Row {
                        Image(
                            painter = rememberImagePainter(data = video.image, builder = {
                                transformations(RoundedCornersTransformation(8f))
                            }),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(4.dp)
                                .size(128.dp, 96.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(IntrinsicSize.Max)
                        ) {
                            Text(
                                text = video.title,
                                modifier = Modifier.width(IntrinsicSize.Max),
                                color = color,
                                maxLines = 3,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .background(
                                        color.copy(alpha = 0.15f),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_round_av_timer_24),
                                    contentDescription = "",
                                    tint = color,
                                    modifier = Modifier.size(12.5.dp)
                                )
                                Text(
                                    text = "${ISO8601.getTime(video.duration)}",
                                    color = color,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 2.dp),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    if ((video.course != null) or (video.playlist != null))
                        Text(
                            text = "${video.course?.title} > ${video.playlist?.title}",
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .padding(8.dp),
                            color = color,
                            fontWeight = FontWeight.Thin,
                            fontSize = 12.sp
                        )
                    Row {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = if (video.isFavorite) R.drawable.ic_star_black_24dp else R.drawable.ic_star_border_black_24dp),
                                contentDescription = "",
                                tint = color
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_playlist_add_black_24dp),
                                contentDescription = "",
                                tint = color
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_share_black_24dp),
                                contentDescription = "",
                                tint = color
                            )
                        }
                    }
                }

            }
        }
    }

    @Preview()
    @Composable
    fun VideoPreview() {
        Video(
            video = Video.dummy(),
            color = Color(ColorUtility.getRandomColor(150, 150, 150, isSystemInDarkTheme()))
        )
    }

    @Preview(uiMode = UI_MODE_NIGHT_YES)
    @Composable
    fun VideoDarkPreview() {
        VideoPreview()
    }
}