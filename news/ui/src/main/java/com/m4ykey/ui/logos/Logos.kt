package com.m4ykey.ui.logos

import com.m4ykey.ui.logos.Logos.BILLBOARD_LOGO
import com.m4ykey.ui.logos.Logos.CONSEQUENCE_LOGO
import com.m4ykey.ui.logos.Logos.NME_LOGO
import com.m4ykey.ui.logos.Logos.PITCHFORK_LOGO
import com.m4ykey.ui.logos.Logos.ROLLING_STONE_LOGO
import com.m4ykey.ui.logos.Logos.STEREOGUM_LOGO
import com.m4ykey.ui.logos.Logos.THE_FADER_LOGO

object Logos {

    const val ROLLING_STONE_LOGO = "https://play-lh.googleusercontent.com/ToohFdJJ016UxyObeFjWkB6wtp3_M-DiSaPvCJqV19kU_k0mGZ7SJPKrodbNJw4KGT4=w240-h480-rw"
    const val PITCHFORK_LOGO = "https://yt3.googleusercontent.com/ytc/AIf8zZS9edaTMwek-1vVJgY6yP5mMXfzQo0nyvnmD4arHQ=s900-c-k-c0x00ffffff-no-rj"
    const val BILLBOARD_LOGO = "https://upload.wikimedia.org/wikipedia/commons/0/0d/Billboard-logo-web.png"
    const val STEREOGUM_LOGO = "https://pbs.twimg.com/profile_images/1325696974578913280/NRN47Aw-_400x400.jpg"
    const val NME_LOGO = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ef/NME_logo_free.svg/2560px-NME_logo_free.svg.png"
    const val CONSEQUENCE_LOGO = "https://upload.wikimedia.org/wikipedia/en/9/95/Consequence-main-logo.jpg"
    const val THE_FADER_LOGO = "https://www.thefader.com/assets/Fader-New-Black-FB-d2aaa59a0df7666f2ffae62bf70a70896fa5d60656f2c33d97f9ce70d98a2209.png"

}

val logos = mapOf(
    "Rolling Stone" to ROLLING_STONE_LOGO,
    "Pitchfork" to PITCHFORK_LOGO,
    "Billboard" to BILLBOARD_LOGO,
    "Stereogum" to STEREOGUM_LOGO,
    "NME" to NME_LOGO,
    "Consequence.net" to CONSEQUENCE_LOGO,
    "The FADER" to THE_FADER_LOGO
)