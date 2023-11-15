package ru.aston.musicapplication
/*
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.aston.musicapplication.ViewModel.Companion.musicService

class MainActivity : AppCompatActivity(), View.OnClickListener,
    ServiceConnection {
    private val viewModel: ViewModel by viewModels()
    var isPlaying: Boolean = false
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var buttonPlay: ImageButton
        lateinit var appContext: Context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContext = applicationContext
        setContentView(R.layout.activity_main)
        buttonPlay = findViewById(R.id.button_play_pause)
        buttonPlay.setOnClickListener(this)
        val buttonPrevious: ImageButton = findViewById(R.id.button_previous)
        buttonPrevious.setOnClickListener(this)
        val buttonNext: ImageButton = findViewById(R.id.button_next)
        buttonNext.setOnClickListener(this)



        viewModel.createMediaPlayer()
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)
        viewModel.playData
            .flowWithLifecycle(lifecycle)
            .onEach {
                isPlaying = it.isPlaying
            }
            .launchIn(lifecycleScope)



    }

 /*   private fun createMediaPlayer(position: Int) {
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer =
                MediaPlayer.create(this, listOfMusic[position])
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
        } catch (e: Exception) {
            return
        }
    }*/

   /* private fun audioPlay(position: Int) {
        val buttonPlay: ImageButton = findViewById(R.id.button_play_pause)
        if (!musicService!!.mediaPlayer!!.isPlaying) {
            musicService!!.mediaPlayer = MediaPlayer.create(this, listOfMusic[position])
            musicService!!.mediaPlayer!!.start()
            buttonPlay.setImageResource(R.drawable.baseline_pause_24)
            musicService!!.showNotification(R.drawable.baseline_pause_mini)
            musicService!!.mediaPlayer!!.seekTo(musicPosition)
        } else {
            musicService!!.mediaPlayer!!.pause()
            buttonPlay.setImageResource(R.drawable.baseline_play_arrow_24)
            musicService!!.showNotification(R.drawable.baseline_play_arrow_mini)
            musicPosition = musicService!!.mediaPlayer!!.currentPosition
        }
    }
*/
    /*private fun audioNext() {
        if (musicService!!.mediaPlayer!!.isPlaying) musicService!!.mediaPlayer!!.stop()
        if (postionOfMusic < listOfMusic.size - 1) {
            postionOfMusic++
        } else {
            postionOfMusic = 0
        }
        musicPosition = 0
        audioPlay(postionOfMusic)
    }*/

   /* private fun audioPrevious() {
        if (musicService!!.mediaPlayer!!.isPlaying) musicService!!.mediaPlayer!!.stop()
        if (postionOfMusic > 0) {
            postionOfMusic--
        } else {
            postionOfMusic = listOfMusic.size - 1
        }
        musicPosition = 0
        audioPlay(postionOfMusic)
    }*/


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_play_pause -> {
                viewModel.audioPlay()
                if(viewModel.playData.value.isPlaying){
                    buttonPlay.setImageResource(R.drawable.baseline_pause_24)
                } else{
                    buttonPlay.setImageResource(R.drawable.baseline_play_arrow_24)
                }
            }

            R.id.button_previous -> {
                viewModel.audioPrevious()
            }

            R.id.button_next -> {
                viewModel.audioNext()
            }

        }
    }



    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        viewModel.createMediaPlayer()
        musicService!!.showNotification(R.drawable.baseline_play_arrow_mini)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }
}
*/

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity(), ServiceConnection {

    private val viewModel: MainViewModel by viewModels()

    private var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageButton>(R.id.button_play_pause).setOnClickListener {
            if (isPlaying) {
                viewModel.pause()
                findViewById<ImageButton>(R.id.button_play_pause).setImageResource(R.drawable.baseline_pause_24)
            } else {
                viewModel.play()
                findViewById<ImageButton>(R.id.button_play_pause).setImageResource(R.drawable.baseline_play_arrow_24)
            }
        }

        viewModel.setContext(this)

        viewModel.playData
            .flowWithLifecycle(lifecycle)
            .onEach {
                isPlaying = it.isPlaying
            }
            .launchIn(lifecycleScope)

        findViewById<ImageButton>(R.id.button_next).setOnClickListener { viewModel.next() }
        findViewById<ImageButton>(R.id.button_previous).setOnClickListener { viewModel.previous() }


    }
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        viewModel.musicService = binder.currentService()
        //viewModel.createMediaPlayer()
        viewModel.musicService!!.showNotification(R.drawable.baseline_play_arrow_mini)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        viewModel.musicService = null
    }

}
