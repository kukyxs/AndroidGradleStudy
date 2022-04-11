package com.kuky.androidgradlestudy

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kuky.androidgradlestudy.databinding.ActivityMainBinding
import com.kuky.modulelib.ModuleTest

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.packageName.text = packageName
        mBinding.version.text = BuildConfig.type
        mBinding.moduleVerify.text = ModuleTest.testFunc()
        mBinding.moduleVerify.startAnimation(AnimationUtils.loadAnimation(this, com.kuky.modulelib.R.anim.scale))
    }
}