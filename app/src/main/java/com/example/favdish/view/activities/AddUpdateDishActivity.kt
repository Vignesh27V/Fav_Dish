package com.example.favdish.view.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.favdish.R
import com.example.favdish.databinding.ActivityAddUpdateDishBinding

class AddUpdateDishActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityAddUpdateDishBinding //Using view Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpActionBar()

    }
    private fun setUpActionBar(){
        setSupportActionBar(mBinding.toolbarAddDishActivity) //set up the custom tool bar for us to customize with our icons
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //this line is responsible for showing the arrow at the top
        mBinding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}