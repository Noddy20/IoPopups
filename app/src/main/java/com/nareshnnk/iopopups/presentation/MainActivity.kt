package com.nareshnnk.iopopups.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nareshnnk.iopopups.databinding.ActivityMainBinding
import com.nareshnnk.iopopups.presentation.customviews.SnackbarIo

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var snackbar: SnackbarIo
    private lateinit var stickySnackbar: SnackbarIo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSnackbar()
        setupStickySnackbar()
    }

    private fun setupSnackbar() {
        snackbar = SnackbarIo(this, this)
        binding.snackbarButton.setOnClickListener {
            snackbar.show(binding.root, "Hello from SnackbarIo!")
        }
    }

    private fun setupStickySnackbar() {
        stickySnackbar = SnackbarIo(this, this)
        binding.snackbarStickyButton.setOnClickListener {
            stickySnackbar.showSticky(
                binding.root,
                "This is a sticky SnackbarIo! Tap close to dismiss.",
                "Close"
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        snackbar.dismiss(0)
        stickySnackbar.dismiss(0)
    }
}
