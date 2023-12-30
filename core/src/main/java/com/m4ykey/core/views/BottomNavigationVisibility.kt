package com.m4ykey.core.views

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.showBottomNavigation(@IdRes id : Int) {
    val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(id)
    bottomNavigation.show()
}

fun Fragment.hideBottomNavigation(@IdRes id : Int) {
    val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(id)
    bottomNavigation.hide()
}