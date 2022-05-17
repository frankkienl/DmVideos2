package nl.frankkie.dmvideos2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.frankkie.dmvideos2.databinding.ActivityPlaylistBinding
import nl.frankkie.dmvideos2.network.DailymotionService
import nl.frankkie.dmvideos2.network.DmPlaylist
import nl.frankkie.dmvideos2.network.DmUserRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class PlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var retrofit: Retrofit
    private lateinit var dailymotionService: DailymotionService
    private lateinit var playlistId: String
    private var dmPlaylist: DmPlaylist? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init UI
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        //init Retrofit
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.dailymotion.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        dailymotionService = retrofit.create<DailymotionService>()

        //Playlist id
        val tempPlaylistId = intent?.getStringExtra(EXTRA_PLAYLIST_ID)
        if (tempPlaylistId == null) {
            finish()
        } else {
            playlistId = tempPlaylistId
        }
    }

    override fun onResume() {
        super.onResume()

        if (dmPlaylist != null) {
            return
        }

        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            val repo = DmUserRepository(dailymotionService)
            dmPlaylist = repo.getPlaylistVideos(playlistId)
            launch(Dispatchers.Main) {
                createPlaylistVideosUI()
            }
        }
    }

    private fun createPlaylistVideosUI() {
        val listContainer = findViewById<ViewGroup>(R.id.playlist_container)
        listContainer.removeAllViews()
        dmPlaylist?.list?.let { safeList ->
            safeList.forEach { safeVideo ->
                val button = Button(this@PlaylistActivity)
                button.text = safeVideo.title
                button.setOnClickListener {
                    clickedVideo(safeVideo.id!!)
                }
                listContainer.addView(button)
            }
        }
    }

    private fun clickedVideo(videoId: String){
        val url = "http://www.dailymotion.com/embed/video/$videoId"
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.setData(Uri.parse(url))
        startActivity(browserIntent)
    }

    companion object {
        public const val EXTRA_PLAYLIST_ID = "playlistId"
    }
}