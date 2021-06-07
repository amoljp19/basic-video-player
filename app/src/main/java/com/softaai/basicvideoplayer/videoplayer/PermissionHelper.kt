package com.softaai.basicvideoplayer.videoplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PermissionsHelper(private val activity: AppCompatActivity) {
    fun hasCameraPermission(): Boolean {
        val permissionCheckResult = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.CAMERA
        )
        return permissionCheckResult == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestCameraPermission() {
        activity.requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun resultGranted(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ): Boolean {
        if (requestCode != REQUEST_CODE) {
            return false
        }
        if (grantResults.size < 1) {
            return false
        }
        if (permissions[0] != Manifest.permission.CAMERA) {
            return false
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        requestCameraPermission()
        return false
    }

    companion object {
        private const val REQUEST_CODE = 10
    }

}