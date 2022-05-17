package nl.frankkie.dmvideos2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.frankkie.dmvideos2.databinding.ActivityMainBinding
import nl.frankkie.dmvideos2.network.DailymotionService
import nl.frankkie.dmvideos2.network.DmUserPlaylists
import nl.frankkie.dmvideos2.network.DmUserRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var dmUsername: String? = null
    private lateinit var retrofit: Retrofit
    private lateinit var dailymotionService: DailymotionService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init UI
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        //init Retrofit
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.dailymotion.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        dailymotionService = retrofit.create<DailymotionService>()
    }

    override fun onResume() {
        super.onResume()
        if (dmUsername != null) {
            getUserInfo()
            return
        }
        //Make dialog to ask for username
        val builder = AlertDialog.Builder(this)
        val editText = EditText(this)
        editText.hint = "e.g. PioRuN_PL"
        editText.setText(getUsernameFromPrefs())
        builder.setTitle("Give the username of a Dailymotion uploader")
        builder.apply {
            setView(editText)
            setPositiveButton(android.R.string.ok) { _, _ ->
                dmUsername = editText.text.toString()
                getUserInfo()

                //Save username for next time
                saveUsernameInPrefs()
            }
            setNegativeButton(android.R.string.cancel) { _, _ -> finish() }
        }
        builder.create().show()
    }

    private fun getUserInfo() {
        if (dmUsername == null) return
        //with `?.let`, I can be sure it will not be null; Magic. Don't ask me how it works.
        dmUsername?.let { safeDmUsername ->
            val coroutineScope = CoroutineScope(Dispatchers.IO)
            coroutineScope.launch {
                val repo = DmUserRepository(dailymotionService)
                val userInfo = repo.getUserInfo(safeDmUsername)
                launch(Dispatchers.Main) {
                    if (userInfo.error != null) {
                        Toast.makeText(
                            applicationContext,
                            "Error: User not found",
                            Toast.LENGTH_LONG
                        ).show()
                        dmUsername = null
                    } else {
                        //All good
                        getUserPlaylists()
                    }
                }
            }
        }
    }

    private fun getUserPlaylists() {
        if (dmUsername == null) return
        dmUsername?.let { safeDmUsername ->
            val coroutineScope = CoroutineScope(Dispatchers.IO)
            // Don't run network calls on the Main thread.
            // For reasons Google can probably explain better.
            coroutineScope.launch {
                val repo = DmUserRepository(dailymotionService)
                val playlists = repo.getUserPlaylists(safeDmUsername)
                // But do run UI-call on the main thread.
                launch(Dispatchers.Main) {
                    if (playlists.error != null) {
                        Toast.makeText(
                            applicationContext,
                            "Error: playlists not found",
                            Toast.LENGTH_LONG
                        ).show()
                        dmUsername = null
                    } else if (playlists.total == 0) {
                        Toast.makeText(
                            applicationContext,
                            "Error: no playlists found",
                            Toast.LENGTH_LONG
                        ).show()
                        dmUsername = null
                    } else {
                        createPlaylistsUI(playlists)
                    }
                }
            }
        }
    }

    private fun createPlaylistsUI(playlists: DmUserPlaylists) {
        val listContainer = findViewById<ViewGroup>(R.id.playlists_container)
        listContainer.removeAllViews()
        playlists.list?.let { safeList ->
            safeList.forEach { safePlaylist ->
                val button = Button(this@MainActivity)
                button.text = safePlaylist.name
                button.setOnClickListener {
                    clickedPlaylist(safePlaylist.id!!)
                }
                listContainer.addView(button)
            }
        }
    }

    private fun clickedPlaylist(playlistId: String) {
        val playlistIntent = Intent(applicationContext, PlaylistActivity::class.java)
        playlistIntent.putExtra(PlaylistActivity.EXTRA_PLAYLIST_ID, playlistId)
        startActivity(playlistIntent)
    }

    private fun saveUsernameInPrefs() {
        val prefs = this@MainActivity.getPreferences(Context.MODE_PRIVATE)
        prefs.edit().putString(PREFS_USERNAME, dmUsername).apply()
    }

    private fun getUsernameFromPrefs(): String {
        val prefs = this@MainActivity.getPreferences(Context.MODE_PRIVATE)
        return prefs.getString(PREFS_USERNAME, "") ?: ""
    }

    companion object {
        const val PREFS_USERNAME = "username"
    }
}