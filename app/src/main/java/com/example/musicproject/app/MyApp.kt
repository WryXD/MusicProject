package com.example.musicproject.app

import android.app.Application
import com.bumptech.glide.annotation.GlideModule
import dagger.hilt.android.HiltAndroidApp

@GlideModule
@HiltAndroidApp
class MyApp : Application()