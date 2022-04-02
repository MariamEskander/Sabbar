package com.example.sabbartask.ui_not_compose.main

import android.os.Bundle
import androidx.activity.viewModels
import com.example.sabbartask.base.BaseActivity
import com.example.sabbartask.base.BaseViewModel
import com.example.sabbartask.databinding.ActivityMainBinding
import com.example.sabbartask.ui_not_compose.identity.DriverUserActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel:MainViewModel by viewModels()
    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {
    }

    override fun initClickListeners() {
        binding.btnDriver.setOnClickListener {
            DriverUserActivity.start(this,isUser = false,false)
        }
        binding.btnUser.setOnClickListener {
            DriverUserActivity.start(this,isUser = true, false)
        }
    }

    override fun setObservers() {

    }

}