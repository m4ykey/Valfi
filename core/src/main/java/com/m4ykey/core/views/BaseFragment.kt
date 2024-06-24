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
    private val layoutInflater : (LayoutInflater) -> T
) : Fragment() {

    private var _binding : T? = null
    protected val binding get() = _binding!!

    protected var bottomNavigationVisibility : BottomNavigationVisibility? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = layoutInflater(inflater)
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationVisibility) {
            bottomNavigationVisibility = context
        } else {
            throw RuntimeException("$context ${getString(R.string.must_implement_bottom_navigation)}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}