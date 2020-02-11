package com.example.testtaskapp.ui

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.testtaskapp.R
import com.example.testtaskapp.ui.viewmodels.AudioViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File


class AudioFragment : Fragment() {


    private lateinit var progress_horizontal: ProgressBar

    companion object {
        fun newInstance() = AudioFragment()
    }

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val viewModel: AudioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.audio_fragment, container, false)
        val record = root.findViewById<Button>(R.id.record)
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        )
            requestPermissions(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 2048
            )
        record.setOnTouchListener { _, event ->

            if (event.action == MotionEvent.ACTION_DOWN) {
                viewModel.recordAudio()
            }

            if (event.action == MotionEvent.ACTION_UP) {
                viewModel.stopRecording()
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

                            viewModel.uploadDocument(
                                File(requireContext().cacheDir.absolutePath + "/fileZip.zip"),
                                location!!.latitude,
                                location.longitude
                            ).observe(this) {
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

            true

        }




        return root
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

                    viewModel.uploadDocument(
                        File(requireContext().cacheDir.absolutePath + "/fileZip.zip"),
                        null,
                        null
                    ).observe(this) {
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
                            viewModel.uploadDocument(
                                File(requireContext().cacheDir.absolutePath + "/fileZip.zip"),
                                location!!.latitude,
                                location.longitude
                            )
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
        progress_horizontal = dialog.findViewById(R.id.progress_horizontal) as ProgressBar
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