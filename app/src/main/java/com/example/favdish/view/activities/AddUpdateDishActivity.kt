package com.example.favdish.view.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    private lateinit var mBinding: ActivityAddUpdateDishBinding
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent> // for camera launch
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent> // for gallery launch


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setUpActionBar()
        mBinding.ivAddDishImage.setOnClickListener(this) // set on click listener for add dish image for popup the dialog with camera and gallery option

        // Register camera launcher
        //for below code Think of this as registering a "callback" that Android will trigger after the camera activity finishes and returns data.
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            //CALLBACK ==> “Hey Android! I’m placing a request to open the camera app. And here’s what to do when the result comes back!”
            //the below code is the call back func code like when the result return what needs to be done is written below
            //Callback = “Call me back when you’re done”
            //CALLBACK RULE ==>“When something takes time or involves another app or activity — use a callback to say what to do when it comes back.”
            //the block of code inside { result -> ... } is your callback function
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val thumbnail = data?.extras?.get("data") as? Bitmap
                thumbnail?.let {
                    mBinding.ivDishImage.setImageBitmap(it) // " it " refer to Bitmap
                    mBinding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_edit_24))
                }
            }
        }

        //Register for Gallery launcher
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val selectedImageUri: Uri? = result.data?.data
                selectedImageUri?.let {
                    mBinding.ivDishImage.setImageURI(it)
                    mBinding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_edit_24))
                }
            } else {
                Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setUpActionBar() {
        setSupportActionBar(mBinding.toolbarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_add_dish_image -> {
                    customImageSelectionDialog()
                }
            }
        }
    }

    private fun customImageSelectionDialog() {
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {
            Dexter.withContext(this)
                .withPermissions(
                    android.Manifest.permission.CAMERA,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    else
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    @SuppressLint("QueryPermissionsNeeded")
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (it.areAllPermissionsGranted()) {
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                if (intent.resolveActivity(packageManager) != null) {
                                    cameraLauncher.launch(intent)
                                } else {
                                    Toast.makeText(
                                        this@AddUpdateDishActivity,
                                        "No camera app found.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else if (it.isAnyPermissionPermanentlyDenied) {
                                showRationalDialog()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        showRationalDialog()
                    }
                })
                .onSameThread()
                .check()

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {
            Dexter.withContext(this)
                .withPermission(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    else
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        galleryLauncher.launch(galleryIntent)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        if (response != null && response.isPermanentlyDenied) {
                            showRationalDialog()
                        } else {
                            Toast.makeText(
                                this@AddUpdateDishActivity,
                                "Gallery permission denied",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        showRationalDialog()
                    }
                }).onSameThread().check()

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showRationalDialog() {
        AlertDialog.Builder(this)
            .setMessage("It looks like you have turned off permissions for this feature.")
            .setPositiveButton("GO TO SETTINGS") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

}
