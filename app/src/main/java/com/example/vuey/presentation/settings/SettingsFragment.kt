package com.example.vuey.presentation.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vuey.R
import com.example.vuey.databinding.FragmentSettingsBinding
import com.m4ykey.common.utils.showBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            showBottomNavigation(R.id.bottomNavigation)
            btnSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_signUpFragment)
            }
            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
