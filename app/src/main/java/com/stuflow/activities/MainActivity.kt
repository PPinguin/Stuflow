package com.stuflow.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.stuflow.R
import com.stuflow.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.callback.observe(this){
            if (it != "ok")
                Snackbar
                    .make(findViewById(R.id.nav_host_fragment), it, Snackbar.LENGTH_SHORT).show()
        }
    }
}