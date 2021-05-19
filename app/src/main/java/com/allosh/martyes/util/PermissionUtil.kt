package com.allosh.martyes.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


object PermissionUtil {
    fun requestPermission(activity: Activity?, permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(activity!!, arrayOf(permission), requestCode)
    }

    fun requestPermissionOnFragment(fragment: Fragment, permission: String, requestCode: Int) {
        fragment.requestPermissions(arrayOf(permission), requestCode)
    }

    fun isPermissionGranted(permission: String?, context: Context?): Boolean {
        return (ContextCompat.checkSelfPermission(context!!, permission!!)
                == PackageManager.PERMISSION_GRANTED)
    }
}