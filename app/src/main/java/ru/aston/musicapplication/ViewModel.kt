/*package ru.aston.musicapplication

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.aston.musicapplication.MainActivity.Companion.appContext

class ViewModel: ViewModel() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var musicService: MusicService? = null
        var currentTrack = 0
        val listOfMusic = listOf(R.raw.music1, R.raw.music2, R.raw.music3)
        var currentPosition = 0
    }

   // val playData: MutableStateFlow<PlayData> = MutableStateFlow(PlayData(isPlaying = false, trackId = R.raw.music1))
   val playData: MutableStateFlow<PlayData> = MutableStateFlow(PlayData(isPlaying = false, trackId = currentTrack))

    fun createMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer =
                MediaPlayer.create(appContext, listOfMusic[playData.value.trackId])
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
        } catch (e: Exception) {
            return
        }
    }

     fun audioPlay() {
        if (!musicService!!.mediaPlayer!!.isPlaying) {
            musicService!!.mediaPlayer = MediaPlayer.create(appContext, listOfMusic[playData.value.trackId])
            musicService!!.mediaPlayer!!.start()
            playData.value = playData.value.copy(isPlaying = true)
            musicService!!.showNotification(R.drawable.baseline_pause_mini)
            if (playData.value.currentPosition != 0) {
                musicService!!.mediaPlayer!!.seekTo(playData.value.currentPosition)
            }
        } else {
            musicService!!.mediaPlayer!!.pause()
            musicService!!.showNotification(R.drawable.baseline_play_arrow_mini)
            currentPosition = musicService!!.mediaPlayer!!.currentPosition
            playData.value = playData.value.copy(isPlaying = false, currentPosition =  musicService!!.mediaPlayer!!.currentPosition)
        }
    }

    fun audioNext() {
        if (musicService!!.mediaPlayer!!.isPlaying) musicService!!.mediaPlayer!!.stop()
        if (currentTrack < listOfMusic.size - 1) {
            currentTrack++
        } else {
           currentTrack = 0
        }
        playData.value = playData.value.copy(isPlaying = true,trackId = currentTrack, currentPosition = 0)
        currentPosition = musicService!!.mediaPlayer!!.currentPosition
        audioPlay()
    }

    fun audioPrevious() {
        if (musicService!!.mediaPlayer!!.isPlaying) musicService!!.mediaPlayer!!.stop()
        if (currentTrack  > 0) {
            currentTrack --
        } else {
            currentTrack  = listOfMusic.size - 1
        }
        playData.value = playData.value.copy(isPlaying = true,trackId = currentTrack, currentPosition = 0)
        currentPosition = musicService!!.mediaPlayer!!.currentPosition
        audioPlay()
    }

}*/

data class PlayData(
    val isPlaying: Boolean,
    val trackId: Int,
    val currentPosition: Int = 0,
)

