package com.m4ykey.settings.data

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.settings.R
import com.m4ykey.settings.databinding.FragmentDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataFragment : BaseFragment<FragmentDataBinding>(
    FragmentDataBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}