package com.benone.mvvm2.ui.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.benone.mvvm2.R
import com.benone.mvvm2.base.BaseActivity
import com.benone.mvvm2.utils.Listeners
import com.benone.mvvm2.utils.ToastUtils
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class ScanActivity : BaseActivity(), ZBarScannerView.ResultHandler {

    private lateinit var context: Context

    override var layoutId = 0

    override fun initData() {

    }

    override fun subscribeUi() {

    }

    private lateinit var mScannerView: ZBarScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView = ZBarScannerView(this)
        setContentView(mScannerView)
        context = this
        checkPermissions(
            arrayOf(Manifest.permission.CAMERA),
            arrayOf("摄像头"),
            arrayOf(getString(R.string.camera_permission_reason)),
            object: Listeners.PermissionListener{
                override fun onGranted() {
                    mScannerView.setResultHandler(context as ScanActivity)
                    mScannerView.startCamera()
                }

                override fun onDenied(permissions: List<String>) {
                    ToastUtils.show(getString(R.string.no_permission))
                    finish()
                }

                override fun onShowReason() {
                }

            }
        )

    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun handleResult(p0: Result) {
        AlertDialog.Builder(this).setTitle(R.string.scan_result)
            .setMessage(p0.contents)
            .setCancelable(false)
            .setPositiveButton(R.string.i_know,null)
            .show()
    }


}