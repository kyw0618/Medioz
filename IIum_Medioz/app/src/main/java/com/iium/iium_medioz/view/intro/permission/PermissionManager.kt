package com.iium.iium_medioz.view.intro.permission

import android.app.Activity
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.content.Context
import com.iium.iium_medioz.util.`object`.Constant

object PermissionManager {

    fun getPermissionGranted(context: Context?, permissionCode: String?): Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            permissionCode!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getAllPermissionGranted(context: Context?): Boolean {
        var allGranted = true
        for (permissionCode in Constant.MUTILE_PERMISSION) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    permissionCode!!
                ) == PackageManager.PERMISSION_DENIED
            ) {
                allGranted = false
            }
        }
        return allGranted
    }

    fun requestPermission(context: Context?, permissionCode: String) {
        ActivityCompat.requestPermissions(
            (context as Activity?)!!,
            arrayOf(permissionCode),
            Constant.ONE_PERMISSION_REQUEST_CODE
        )
    }

    fun requestPermissions(context: Context?, permissionCode: Array<String?>?) {
        ActivityCompat.requestPermissions(
            (context as Activity?)!!,
            permissionCode!!,
            Constant.ONE_PERMISSION_REQUEST_CODE
        )
    }

    fun requestMultiPermission(context: Context?, permissionCodes: Array<String?>?) {
        ActivityCompat.requestPermissions(
            (context as Activity?)!!,
            permissionCodes!!,
            Constant.ONE_PERMISSION_REQUEST_CODE
        )
    }

    fun requestAllPermissions(context: Context?) {
        val arraySize: Int = Constant.MUTILE_PERMISSION.size
        ActivityCompat.requestPermissions(
            (context as Activity?)!!,
            Constant.MUTILE_PERMISSION.toArray(arrayOfNulls<String>(arraySize)),
            Constant.ALL_PERMISSION_REQUEST_CODE
        )
    }
}