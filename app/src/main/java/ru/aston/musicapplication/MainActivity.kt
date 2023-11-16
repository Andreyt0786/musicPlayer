package ru.aston.musicapplication

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
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
import ru.aston.musicapplication.MainViewModel.Companion.musicService

class MainActivity : AppCompatActivity(), ServiceConnection {

    private val viewModel: MainViewModel by viewModels()

    companion object {
        lateinit var appContext: Context

        @SuppressLint("StaticFieldLeak")
        lateinit var buttonPlay: ImageButton
        var isPlaying: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext
        setContentView(R.layout.activity_main)
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)
        buttonPlay = findViewById<ImageButton>(R.id.button_play_pause)

        if (!isPlaying) {
            buttonPlay.setImageResource(R.drawable.baseline_play_arrow_24)
        } else {
            buttonPlay.setImageResource(R.drawable.baseline_pause_24)
        }


        findViewById<ImageButton>(R.id.button_play_pause).setOnClickListener {
            viewModel.play()
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
        musicService = binder.currentService()
        if (!isPlaying) {
            musicService!!.showNotification(R.drawable.baseline_play_arrow_mini)
        } else {
            musicService!!.showNotification(R.drawable.baseline_pause_mini)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("KEY", isPlaying)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState != null) {
            super.onRestoreInstanceState(savedInstanceState)
        }
        savedInstanceState?.let {
            isPlaying = savedInstanceState.getBoolean("KEY")
        }
    }
}
