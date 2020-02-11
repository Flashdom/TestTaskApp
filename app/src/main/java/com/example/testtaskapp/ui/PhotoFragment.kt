package com.example.testtaskapp.ui

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.esafirm.imagepicker.features.ImagePicker
import com.example.testtaskapp.R
import com.example.testtaskapp.ui.viewmodels.PhotoViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class PhotoFragment : Fragment() {

    companion object {
        fun newInstance() = PhotoFragment()
    }

    private lateinit var file: File


    private val viewModel: PhotoViewModel by viewModels()
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.photo_fragment, container, false)
        ImagePicker.cameraOnly().start(this)
        return root
    }


    private fun compressFile(file: File): File {
        val bitmapImage = BitmapFactory.decodeFile(file.path)
        val bos = ByteArrayOutputStream()
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 1, bos)
        val bitMapData: ByteArray = bos.toByteArray()
        val tempFile = File(requireContext().cacheDir.path, "photo.jpg")
        val fos: FileOutputStream?
        try {
            fos = FileOutputStream(tempFile)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return tempFile
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        super.onCreate(savedInstanceState)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            val image = ImagePicker.getFirstImageOrNull(data)
            file = compressFile(File(image.path))
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            )
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2048)
            else {
                mFusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        showLoadingIndicator()
                        viewModel.uploadPhoto(file, location!!.latitude, location.longitude)
                            .observe(this) {
                                if (it.success)
                                    parentFragmentManager.beginTransaction().replace(
                                        R.id.MainLayout,
                                        MainMenuFragment.newInstance(),
                                        "mainMenu"
                                    )
                                else
                                    Toast.makeText(
                                        context,
                                        "Что-то пошло не так",
                                        Toast.LENGTH_LONG
                                    ).show()


                            }
                    }


            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            2048 -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showLoadingIndicator()
                    viewModel.uploadPhoto(file, null, null).observe(this) {
                        if (it.success)
                            parentFragmentManager.beginTransaction().replace(
                                R.id.MainLayout,
                                MainMenuFragment.newInstance(),
                                "mainMenu"
                            )
                        else
                            Toast.makeText(
                                context,
                                "Что-то пошло не так",
                                Toast.LENGTH_LONG
                            ).show()


                    }
                } else {
                    mFusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            showLoadingIndicator()
                            viewModel.uploadPhoto(file, location!!.latitude, location.longitude)
                                .observe(this) {
                                    if (it.success)
                                        parentFragmentManager.beginTransaction().replace(
                                            R.id.MainLayout,
                                            MainMenuFragment.newInstance(),
                                            "mainMenu"
                                        )
                                    else
                                        Toast.makeText(
                                            context,
                                            "Что-то пошло не так",
                                            Toast.LENGTH_LONG
                                        ).show()


                                }
                        }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun showLoadingIndicator() {
        var status = 0
        val handler = Handler()
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loadingindicator)
        val progress_horizontal = dialog.findViewById(R.id.progress_horizontal) as ProgressBar
        val value123 = dialog.findViewById<TextView>(R.id.value123)
        Thread(Runnable {
            while (status < 100) {
                status += 1
                Thread.sleep(200)
                handler.post {
                    progress_horizontal.progress = status
                    value123.text = status.toString()
                    if (status == 100) {
                        dialog.dismiss()
                    }
                }

            }
        }).start()

        dialog.show()

        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )


    }

}



