package mollah.yamin.androidImageGallery.app

import android.app.Application
import androidx.databinding.DataBindingUtil
import mollah.yamin.androidImageGallery.binding.AppDataBindingComponent

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DataBindingUtil.setDefaultComponent(AppDataBindingComponent())
    }
}