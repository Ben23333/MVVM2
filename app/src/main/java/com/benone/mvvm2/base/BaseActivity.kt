package com.benone.mvvm2.base

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.benone.mvvm2.Constants
import com.benone.mvvm2.R
import com.benone.mvvm2.ui.activity.ScanActivity
import com.benone.mvvm2.utils.Listeners
import com.classic.common.MultipleStatusView

abstract class BaseActivity : AppCompatActivity() {

    protected abstract var layoutId: Int

    protected var multipleStatusView: MultipleStatusView? = null

    protected abstract fun initData()

    protected abstract fun subscribeUi()

    private lateinit var permissionListener: Listeners.PermissionListener
    private lateinit var deniedPermissions: HashMap<String, Array<String>>
    private var showPermissionDialogOnDenied: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this !is BaseBindingActivity<*> && this !is ScanActivity) {
            setContentView(layoutId)
        }
        initData()
        multipleStatusView?.setOnClickListener {
            when (multipleStatusView?.viewStatus) {
                MultipleStatusView.STATUS_ERROR, MultipleStatusView.STATUS_EMPTY -> onRetry()
            }
        }
        subscribeUi()
    }

    open fun onRetry() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun <T> handleData(
        liveData: LiveData<RequestState<T>>, action: ((T?) -> Unit)
    ) =
        liveData.observe(this, Observer { result ->
            if (result.isLoading()) {
                multipleStatusView?.showLoading()
            } else if (result.isSuccess()) {
                if (result?.data != null) {
                    multipleStatusView?.showContent()
                    action(result.data)
                } else {
                    multipleStatusView?.showEmpty()
                }
            } else if (result.isError()) {
                multipleStatusView?.showError()
            }
        })

    protected fun checkPermissions(
        permissions: Array<String>,
        permissionsCN: Array<String>,
        reasons: Array<String>,
        listener: Listeners.PermissionListener
    ) {
        permissionListener = listener
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionListener.onGranted()
        } else {
            deniedPermissions = HashMap()
            permissions.forEachIndexed { index, s ->
                if (ContextCompat.checkSelfPermission(
                        this,
                        s
                    ) != PermissionChecker.PERMISSION_GRANTED
                ) {
                    deniedPermissions[s] = arrayOf(permissions[index], reasons[index])
                }
            }
            if (deniedPermissions.isEmpty()) {
                permissionListener.onGranted()
            } else {
                val permissionsSb = StringBuilder()
                val reasonsSb = StringBuilder()
                deniedPermissions.forEach {
                    if (shouldShowRequestPermissionRationale(it.key)) {
                        //??????????????????????????????????????????true???????????????????????????false
                        permissionsSb.append(it.value[0]).append(",")
                        reasonsSb.append(it.value[1]).append("\n")
                    }
                }
                if (permissionsSb.isNotBlank() && reasonsSb.isNotBlank()) {
                    showPermissionDialogOnDenied = false
                    permissionsSb.deleteCharAt(permissionsSb.length - 1)
                    reasonsSb.deleteCharAt(reasonsSb.length - 1)
                    showPermissionDeniedDialog(permissionsSb.toString(), reasonsSb.toString()) {
                        requestPermissions(
                            deniedPermissions.keys.toTypedArray(),
                            Constants.PERMISSION_CODE
                        )
                        permissionListener.onShowReason()
                    }
                } else {
                    requestPermissions(
                        deniedPermissions.keys.toTypedArray(),
                        Constants.PERMISSION_CODE
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                val deniedAgainPermissions = ArrayList<String>()
                for (i in grantResults.indices) {
                    val permission = permissions[i]
                    val grantResult = grantResults[i]
                    if (grantResult != PermissionChecker.PERMISSION_GRANTED) {
                        deniedAgainPermissions.add(permission)
                    }
                }
                if (deniedAgainPermissions.isEmpty()) {
                    permissionListener.onGranted()
                } else {
                    deniedAgainPermissions.forEach { now ->
                        deniedPermissions.forEach { old ->
                            if (now == old.key) {
                                if (showPermissionDialogOnDenied) {
                                    showPermissionDeniedDialog(old.value[0], old.value[1]) {
                                        permissionListener.onDenied(deniedAgainPermissions)
                                    }
                                } else {
                                    showPermissionDialogOnDenied = true
                                    permissionListener.onDenied((deniedAgainPermissions))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showPermissionDeniedDialog(p: String, reason: String, action: () -> Unit) {
        AlertDialog.Builder(this).setTitle("?????????$p?????????").setMessage(reason).setCancelable(false)
            .setPositiveButton(R.string.i_know) { _, _ -> action() }.show()
    }
}