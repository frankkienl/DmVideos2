package nl.frankkie.dmvideos2.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DmUserRepository constructor(private val dailymotionService: DailymotionService) {
    suspend fun getUserInfo(username: String): DmUserInfo {
        return withContext(Dispatchers.IO) {
            dailymotionService.getUser(username)
        }
    }

    suspend fun getUserPlaylists(username: String): DmUserPlaylists {
        return withContext(Dispatchers.IO) {
            dailymotionService.getUserPlaylists(username)
        }
    }

    suspend fun getPlaylistVideos(playlistId: String): DmPlaylist {
        return withContext(Dispatchers.IO) {
            dailymotionService.getPlaylistVideos(playlistId)
        }
    }
}