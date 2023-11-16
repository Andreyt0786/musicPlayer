package ru.aston.musicapplication

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {


    val playData: MutableStateFlow<PlayData> =
        MutableStateFlow(PlayData(isPlaying = false, trackId = R.raw.music1))

    companion object {
        var musicService: MusicService? = MusicService()
        private val mediaPlayer = MediaPlayer()
        val listOfMusic = listOf(R.raw.music1, R.raw.music2, R.raw.music3)
        var currentTrack = 0
        var currentPosition = 0
    }

    private var context: Context? = null

    init {
        musicService?.mediaPlayer = mediaPlayer
    }

    fun setContext(context: Context) {
        this.context = context
    }


    fun play() {
        if (!musicService!!.mediaPlayer!!.isPlaying) {
            musicService!!.mediaPlayer =
                MediaPlayer.create(MainActivity.appContext, listOfMusic[currentTrack])
            musicService!!.mediaPlayer!!.start()
            playData.value = playData.value.copy(isPlaying = true)

            MainActivity.buttonPlay.setImageResource(R.drawable.baseline_pause_24)
            musicService!!.showNotification(R.drawable.baseline_pause_mini)
            if (currentPosition != 0) {
                musicService?.mediaPlayer?.seekTo(currentPosition)
            }
            MainActivity.isPlaying = true
        } else {
            musicService!!.mediaPlayer!!.pause()
            currentPosition = musicService!!.mediaPlayer!!.currentPosition
            playData.value = playData.value.copy(isPlaying = false)

            MainActivity.buttonPlay.setImageResource(R.drawable.baseline_play_arrow_24)
            musicService!!.showNotification(R.drawable.baseline_play_arrow_mini)
            MainActivity.isPlaying = false
        }
    }


    fun next() {
        musicService?.mediaPlayer?.stop()
        currentTrack = if (currentTrack < listOfMusic.size - 1) {
            currentTrack + 1
        } else {
            0
        }
        currentPosition = 0
        playData.value = PlayData(true, trackId = listOfMusic[currentTrack], currentPosition = 0)
        play()

    }

    fun previous() {
        musicService?.mediaPlayer?.stop()
        currentTrack = if (currentTrack > 0) {
            currentTrack - 1
        } else {
            listOfMusic.size - 1
        }
        currentPosition = 0
        playData.value = PlayData(true, trackId = listOfMusic[currentTrack], currentPosition = 0)
        play()
    }


}

data class PlayData(
    val isPlaying: Boolean,
    val trackId: Int,
    val currentPosition: Int = 0,
)