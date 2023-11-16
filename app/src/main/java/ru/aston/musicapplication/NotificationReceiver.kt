package ru.aston.musicapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import ru.aston.musicapplication.MainViewModel.Companion.currentPosition
import ru.aston.musicapplication.MainViewModel.Companion.currentTrack
import ru.aston.musicapplication.MainViewModel.Companion.listOfMusic
import ru.aston.musicapplication.MainViewModel.Companion.musicService
import kotlin.system.exitProcess

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {

            ApplicationClass.PREVIOUS -> previousSong()

            ApplicationClass.NEXT -> nextSong()

            ApplicationClass.PLAY -> playMusic()

            ApplicationClass.EXIT -> {
                musicService!!.stopForeground(true)
                musicService = null
                exitProcess(1)
            }
        }
    }


    private fun playMusic() {
        if (!musicService!!.mediaPlayer!!.isPlaying) {
            musicService!!.mediaPlayer =
                MediaPlayer.create(MainActivity.appContext, listOfMusic[currentTrack])
            musicService!!.mediaPlayer!!.start()
            MainActivity.buttonPlay.setImageResource(R.drawable.baseline_pause_24)
            musicService!!.showNotification(R.drawable.baseline_pause_mini)
            if (currentPosition != 0) {
                musicService?.mediaPlayer?.seekTo(currentPosition)
                MainActivity.isPlaying =true
            }
        } else {
            musicService!!.mediaPlayer!!.pause()
            currentPosition = musicService!!.mediaPlayer!!.currentPosition
            MainActivity.buttonPlay.setImageResource(R.drawable.baseline_play_arrow_24)
            musicService!!.showNotification(R.drawable.baseline_play_arrow_mini)
            MainActivity.isPlaying =false
        }
    }


    private fun nextSong() {
        musicService?.mediaPlayer?.stop()
        currentTrack = if (currentTrack == listOfMusic.size - 1) {
            0
        } else {
            currentTrack + 1
        }
        currentPosition = 0
        playMusic()
    }


    fun previousSong() {
        musicService?.mediaPlayer?.stop()
        currentTrack = if (currentTrack == 0) {
            listOfMusic.last()
        } else {
            currentTrack - 1
        }
        currentPosition = 0
        playMusic()
    }
}

