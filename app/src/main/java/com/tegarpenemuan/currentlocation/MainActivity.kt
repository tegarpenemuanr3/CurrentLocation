package com.tegarpenemuan.currentlocation

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tegarpenemuan.currentlocation.databinding.ActivityMainBinding
import com.vmadalin.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), LocationListener, EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding
    private var locationProviderClient: FusedLocationProviderClient? = null

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** Current Location*/
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        /** Easy Permission*/
        if (!hasLocationPermission()) {
            requestLocationPermission()
        } else {
            ShowLatLong()
        }
    }

    /** Current Location
     * ===============================================
     * */
    override fun onLocationChanged(p0: Location) {}


    @SuppressLint("MissingPermission")
    private fun ShowLatLong() {
        locationProviderClient!!.lastLocation.addOnSuccessListener { location ->
            binding.tvInfo.text = "Latitude: ${location.latitude}\n" +
                    "Longtitude: ${location.longitude}"
        }
    }

    /** Easy Permission
     * =================================================
     * */
    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "Aplikasi ini tidak dapat bekerja tanpa Location Permission.",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //SettingsDialog.Builder(this).build().show()
            Toast.makeText(
                this,
                "Mohon Izinkan Permission",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            this,
            "Permission Diizinkan!",
            Toast.LENGTH_SHORT
        ).show()
        ShowLatLong()
    }

}