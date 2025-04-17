package com.example.favdish.view.activities

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.favdish.R
import com.example.favdish.databinding.ActivityAddUpdateDishBinding
import com.example.favdish.databinding.DialogCustomImageSelectionBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener

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

        // Camera Click Listener
        binding.tvCamera.setOnClickListener {
            // Request CAMERA + READ + WRITE permissions using Dexter
            Dexter.withContext(this)
                .withPermissions(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        // If all permissions are granted
                        if (report != null && report.areAllPermissionsGranted()) {
                            Toast.makeText(this@AddUpdateDishActivity, "Camera permission granted", Toast.LENGTH_SHORT).show()

                            // Create an intent to open the device camera
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                            // Start the camera activity (no result handling here)
                            startActivity(intent)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        // If user denied permission earlier, show rationale dialog
                        showRationalDialog()
                    }
                })
                .onSameThread()
                .check()

            // Dismiss the custom dialog after clicking camera option
            dialog.dismiss()
        }

// Gallery Click Listener
        binding.tvGallery.setOnClickListener {
            // Request READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE permissions using Dexter
            Dexter.withContext(this)
                .withPermissions(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        // Check if all permissions are granted
                        if (report != null && report.areAllPermissionsGranted()) {
                            Toast.makeText(this@AddUpdateDishActivity, "Storage permissions granted", Toast.LENGTH_SHORT).show()

                            // Create an intent to open the gallery for picking images
                            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                            // Start the gallery activity (no result handling here)
                            startActivity(galleryIntent)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        // Show rationale if permission was denied before
                        showRationalDialog()
                    }
                })
                .onSameThread()
                .check()

            // Dismiss the custom dialog after clicking gallery option
            dialog.dismiss()
        }

        dialog.show() // To show the dialog
    }

    private fun showRationalDialog() {
        // Create an AlertDialog to inform the user
        AlertDialog.Builder(this)
            // Set the message shown in the dialog
            .setMessage("It looks like you have tuned off the permissions for this feature.")

            // Set the positive button "GO TO SETTINGS"
            .setPositiveButton("GO TO SETTINGS") { _, _ ->
                try {
                    // Create an intent to open the app's settings screen
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    // Pass the URI of your app package to take user directly to app settings
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    // Start the activity to open settings
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    // If the activity is not found, print the error
                    e.printStackTrace()
                }
            }

            // Set the negative button "Cancel" to dismiss the dialog
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss() // Just close the dialog
            }.show()
    }


}