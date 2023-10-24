package ru.aston.musicapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import ru.aston.musicapplication.MainActivity.Companion.postionOfMusic
import kotlin.system.exitProcess

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {

            ApplicationClass.PREVIOUS -> previousSong()

            ApplicationClass.NEXT -> nextSong()

            ApplicationClass.PLAY -> playMusic(postionOfMusic)

            ApplicationClass.EXIT -> {
                MainActivity.musicService!!.stopForeground(true)
                MainActivity.musicService = null
                exitProcess(1)
            }
        }
    }

    private fun playMusic(position: Int) {

        if (!MainActivity.musicService!!.mediaPlayer!!.isPlaying) {
            MainActivity.musicService!!.mediaPlayer =
                MediaPlayer.create(MainActivity.appContext, MainActivity.listOfMusic[position])
            MainActivity.musicService!!.mediaPlayer!!.selectTrack(postionOfMusic)
            MainActivity.musicService!!.mediaPlayer!!.start()
            MainActivity.musicService!!.showNotification(R.drawable.baseline_pause_mini)
            MainActivity.buttonPlay.setImageResource(R.drawable.baseline_pause_24)
        } else {
            MainActivity.musicService!!.mediaPlayer!!.pause()
            MainActivity.musicService!!.showNotification(R.drawable.baseline_play_arrow_mini)
            MainActivity.buttonPlay.setImageResource(R.drawable.baseline_play_arrow_24)
        }
    }

    private fun nextSong() {
        if (MainActivity.musicService!!.mediaPlayer!!.isPlaying) MainActivity.musicService!!.mediaPlayer!!.stop()
        if (postionOfMusic < MainActivity.listOfMusic.size - 1) {
            postionOfMusic++
        } else {
            postionOfMusic = 0
        }
        playMusic(postionOfMusic)
    }


    fun previousSong() {
        if (MainActivity.musicService!!.mediaPlayer!!.isPlaying) MainActivity.musicService!!.mediaPlayer!!.stop()
        if (postionOfMusic > 0) {
            postionOfMusic--
        } else {
            postionOfMusic = MainActivity.listOfMusic.size - 1
        }
        playMusic(postionOfMusic)
    }
}