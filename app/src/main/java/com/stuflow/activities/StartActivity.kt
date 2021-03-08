package com.stuflow.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.stuflow.R
import com.stuflow.viewmodels.AuthorizationViewModel

class StartActivity : AppCompatActivity() {

    lateinit var viewModel: AuthorizationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        viewModel  = ViewModelProvider(this).get(AuthorizationViewModel::class.java)
        viewModel.message.observe(this){
            Log.d("AUTH", it)
            if (it == "ok") toMain()
            else if (it != ""){
                Snackbar
                    .make(
                        findViewById<FragmentContainerView>(R.id.nav_host_fragment),
                        it,
                        Snackbar.LENGTH_SHORT
                    )
                    .show()
            }
        }

        if (viewModel.message.value != "ok"){
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.action_splashFragment_to_signInFragment)
        }
    }

    private fun toMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}