package ru.aston.musicapplication

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener, SeekBar.OnSeekBarChangeListener,
    ServiceConnection {

    companion object {
        var musicService: MusicService? = null
        var postionOfMusic = 0
        val listOfMusic = listOf(R.raw.music1, R.raw.music2, R.raw.music3)
        var position = 0

        @SuppressLint("StaticFieldLeak")
        lateinit var buttonPlay: ImageButton
        lateinit var appContext: Context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            val position = savedInstanceState.getInt("position")
            val isPlaying = savedInstanceState.getBoolean("playing")
            Log.d("Main", "get position")

            if (isPlaying) {
                musicService!!.mediaPlayer = MediaPlayer.create(this, listOfMusic[postionOfMusic])
                musicService!!.mediaPlayer!!.start()
                musicService!!.mediaPlayer!!.seekTo(position)
                buttonPlay.setImageResource(R.drawable.baseline_pause_24)
                musicService!!.showNotification(R.drawable.baseline_pause_mini)
            } else {
                buttonPlay.setImageResource(R.drawable.baseline_play_arrow_24)
                musicService!!.showNotification(R.drawable.baseline_play_arrow_mini)
            }
        }


    appContext = applicationContext
    setContentView(R.layout.activity_main)
    buttonPlay = findViewById(R.id.button_play_pause)
    buttonPlay.setOnClickListener(this)
    buttonPlay.setImageResource(R.drawable.baseline_play_arrow_24)
    val buttonPrevious: ImageButton = findViewById(R.id.button_previous)
    buttonPrevious.setOnClickListener(this)
    val buttonNext: ImageButton = findViewById(R.id.button_next)
    buttonNext.setOnClickListener(this)
    createMediaPlayer(postionOfMusic)
    val intent = Intent(this, MusicService::class.java)
    bindService(intent, this, BIND_AUTO_CREATE)
    startService(intent)
    val seekSong: SeekBar = findViewById(R.id.seekSong)
    seekSong.max = 100
    seekSong.progress = 0
    seekSong.setOnSeekBarChangeListener(this)

}

private fun createMediaPlayer(position: Int) {
    val seekSong: SeekBar = findViewById(R.id.seekSong)
    try {
        if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer =
            MediaPlayer.create(this, listOfMusic[position])
        musicService!!.mediaPlayer!!.reset()
        musicService!!.mediaPlayer!!.prepare()
        musicService!!.mediaPlayer!!.start()
        buttonPlay.setImageResource(R.drawable.baseline_pause_24)
        musicService!!.showNotification(R.drawable.baseline_pause_mini)
        seekSong.progress = 0
        seekSong.max = musicService!!.mediaPlayer!!.duration
    } catch (e: Exception) {
        return
    }
}

private fun audioPlay(position: Int) {
    val buttonPlay: ImageButton = findViewById(R.id.button_play_pause)
    if (!musicService!!.mediaPlayer!!.isPlaying) {
        musicService!!.mediaPlayer = MediaPlayer.create(this, listOfMusic[position])
        musicService!!.mediaPlayer!!.start()
        buttonPlay.setImageResource(R.drawable.baseline_pause_24)
        musicService!!.showNotification(R.drawable.baseline_pause_mini)
    } else {
        musicService!!.mediaPlayer!!.pause()
        buttonPlay.setImageResource(R.drawable.baseline_play_arrow_24)
        musicService!!.showNotification(R.drawable.baseline_play_arrow_mini)
    }
}

private fun audioNext() {
    if (musicService!!.mediaPlayer!!.isPlaying) musicService!!.mediaPlayer!!.stop()
    if (postionOfMusic < listOfMusic.size - 1) {
        postionOfMusic++
    } else {
        postionOfMusic = 0
    }
    audioPlay(postionOfMusic)
}

private fun audioPrevious() {
    if (musicService!!.mediaPlayer!!.isPlaying) musicService!!.mediaPlayer!!.stop()
    if (postionOfMusic > 0) {
        postionOfMusic--
    } else {
        postionOfMusic = listOfMusic.size - 1
    }
    audioPlay(postionOfMusic)
}


override fun onClick(v: View?) {
    when (v?.id) {
        R.id.button_play_pause -> {
            audioPlay(postionOfMusic)
        }

        R.id.button_previous -> {
            audioPrevious()
        }

        R.id.button_next -> {
            audioNext()
        }

    }
}

override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    if (fromUser) musicService!!.mediaPlayer!!.seekTo(progress)
}

override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
    val binder = service as MusicService.MyBinder
    musicService = binder.currentService()
    createMediaPlayer(postionOfMusic)
    musicService!!.showNotification(R.drawable.baseline_play_arrow_mini)
}

override fun onServiceDisconnected(name: ComponentName?) {
    musicService = null
}

override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt("position", musicService!!.mediaPlayer!!.currentPosition)
    outState.putBoolean("playing", musicService!!.mediaPlayer!!.isPlaying)

    Log.d("Main", "position")
}
/* fun onConfigurationChanged(newConfig: Configuration) {
     super.onConfigurationChanged(newConfig)
     if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
         //if you are using mediaplayer than u need to mp.getCurrentPosition();
         val currentPosition: Int = vv.getCurrentPosition()
         vv.seekTo(currentPosition)
     } else if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
         val currentPosition: Int = vv.getCurrentPosition()
         vv.seekTo(currentPosition)
     }
 }*/
}