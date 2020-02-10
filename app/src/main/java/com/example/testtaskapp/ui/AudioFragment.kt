package com.example.testtaskapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        record.performClick()
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

                            viewModel.uploadDocument(
                                File(requireContext().cacheDir.absolutePath),
                                location!!.latitude,
                                location!!.longitude
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

                    viewModel.uploadDocument(
                        File(requireContext().cacheDir.absolutePath),
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

                            viewModel.uploadDocument(
                                File(requireContext().cacheDir.absolutePath),
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


}