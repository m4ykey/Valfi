package com.m4ykey.core.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.m4ykey.core.R

abstract class BaseFragment<T : ViewBinding>(
    private val layoutInflater : (LayoutInflater, ViewGroup?, Boolean) -> T
) : Fragment() {

    private var _binding : T? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = layoutInflater(inflater, container, false)
        }
        return binding.root
    }
}