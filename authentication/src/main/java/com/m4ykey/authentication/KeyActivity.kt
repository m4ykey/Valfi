package com.m4ykey.authentication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.m4ykey.authentication.data.model.KeyEntity
import com.m4ykey.authentication.data.repository.KeyRepository
import com.m4ykey.authentication.databinding.ActivityKeyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class KeyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityKeyBinding

    @Inject
    lateinit var repository: KeyRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityKeyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            val keys = repository.getKeys().firstOrNull()
            if (keys != null) {
                binding.etClientSecret.setText(keys.clientSecret)
                binding.etClientId.setText(keys.clientId)
            }
        }

        binding.toolbar.setOnClickListener { this.finish() }

        binding.btnSave.setOnClickListener {
            val clientId = binding.etClientId.text.toString()
            val clientSecret = binding.etClientSecret.text.toString()

            val keyEntity = KeyEntity(
                clientSecret = clientSecret,
                clientId = clientId
            )
            lifecycleScope.launch {
                repository.insertKeys(keyEntity)
            }
        }

        binding.btnDelete.setOnClickListener {
            lifecycleScope.launch {
                binding.etClientSecret.setText("")
                binding.etClientId.setText("")

                repository.deleteKeys()
            }
        }
    }
}