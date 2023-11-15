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

            // ApplicationClass.PREVIOUS -> previousSong()

            //  ApplicationClass.NEXT -> nextSong()

            ApplicationClass.PLAY -> playMusic()

            ApplicationClass.EXIT -> {
                //musicService!!.stopForeground(true)
                // musicService = null
                exitProcess(1)
            }
        }
    }


    private fun playMusic() {

        if (!musicService!!.mediaPlayer!!.isPlaying) {
            musicService!!.mediaPlayer =
                MediaPlayer.create(MainActivity.appContext, listOfMusic[currentTrack])
            musicService!!.mediaPlayer!!.start()
            musicService!!.showNotification(R.drawable.baseline_pause_mini)
            musicService!!.mediaPlayer!!.seekTo(currentPosition)
            //buttonPlay.setImageResource(R.drawable.baseline_pause_24)
            /*musicService!!.mediaPlayer =
                MediaPlayer.create(MainActivity.appContext, MainActivity.listOfMusic[position])
            MainActivity.musicService!!.mediaPlayer!!.selectTrack(postionOfMusic)
            MainActivity.musicService!!.mediaPlayer!!.start()
            MainActivity.musicService!!.showNotification(R.drawable.baseline_pause_mini)
            MainActivity.buttonPlay.setImageResource(R.drawable.baseline_pause_24)*/
        } else {
            /* MainActivity.musicService!!.mediaPlayer!!.pause()
             MainActivity.musicService!!.showNotification(R.drawable.baseline_play_arrow_mini)
             MainActivity.buttonPlay.setImageResource(R.drawable.baseline_play_arrow_24)*/
            musicService!!.mediaPlayer!!.pause()
            musicService!!.showNotification(R.drawable.baseline_play_arrow_mini)
            currentPosition = musicService!!.mediaPlayer!!.currentPosition
            //buttonPlay.setImageResource(R.drawable.baseline_play_arrow_24)*/
        }

        /*
    private fun nextSong() {
        /*  if (musicService!!.mediaPlayer!!.isPlaying) musicService!!.mediaPlayer!!.stop()
        if (currentTrack < listOfMusic.size - 1) {
            currentTrack++
        } else {
            currentTrack = 0
        }
        currentPosition = musicService!!.mediaPlayer!!.currentPosition
        playMusic()*/
        /*if (MainActivity.musicService!!.mediaPlayer!!.isPlaying) MainActivity.musicService!!.mediaPlayer!!.stop()
        if (postionOfMusic < MainActivity.listOfMusic.size - 1) {
            postionOfMusic++
        } else {
            postionOfMusic = 0
        }
        playMusic(postionOfMusic)*/
    }


    fun previousSong() {
        /* if (musicService!!.mediaPlayer!!.isPlaying) musicService!!.mediaPlayer!!.stop()
        if (currentTrack > 0) {
            currentTrack--
        } else {
            currentTrack = listOfMusic.size - 1
        }
        currentPosition = musicService!!.mediaPlayer!!.currentPosition
        playMusic()*/
        /* if (MainActivity.musicService!!.mediaPlayer!!.isPlaying) MainActivity.musicService!!.mediaPlayer!!.stop()
         if (postionOfMusic > 0) {
             postionOfMusic--
         } else {
             postionOfMusic = MainActivity.listOfMusic.size - 1
         }
         playMusic(postionOfMusic)*/
    }
*/
    }
}
