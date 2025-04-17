package com.example.favdish.view.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.favdish.R
import com.example.favdish.databinding.ActivityAddUpdateDishBinding
import com.example.favdish.databinding.DialogCustomImageSelectionBinding

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mBinding: ActivityAddUpdateDishBinding //Using view Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpActionBar()
        mBinding.ivAddDishImage.setOnClickListener(this)
    }

    private fun setUpActionBar(){
        setSupportActionBar(mBinding.toolbarAddDishActivity) //set up the custom tool bar for us to customize with our icons
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //this line is responsible for showing the arrow at the top
        mBinding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.iv_add_dish_image -> {
                    customImageSelectionDialog()
                    return
                }
            }
        }
    }

    private fun customImageSelectionDialog(){
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding = DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {
            Toast.makeText(this, "Camera is clicked", Toast.LENGTH_SHORT).show()
        }

        binding.tvGallery.setOnClickListener {
            Toast.makeText(this, "Gallery is clicked", Toast.LENGTH_SHORT).show()
        }

        dialog.show() // To show the dialog
    }

}