package nl.frankkie.dmvideos2.network

import retrofit2.http.GET
import retrofit2.http.Path

interface DailymotionService {

    @GET("/user/{username}")
    suspend fun getUser(@Path("username") username: String) : DmUserInfo

    @GET("/user/{username}/playlists")
    suspend fun getUserPlaylists(@Path("username") username: String) : DmUserPlaylists

    @GET("/playlist/{playlistId}/videos?limit=100")
    suspend fun getPlaylistVideos(@Path("playlistId") playlistId: String) : DmPlaylist
}