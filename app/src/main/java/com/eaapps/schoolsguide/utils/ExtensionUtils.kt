package com.eaapps.schoolsguide.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.view.*
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.eaapps.schoolsguide.R
import com.github.ybq.android.spinkit.style.FadingCircle
import com.github.ybq.android.spinkit.style.FoldingCube
import com.google.android.material.snackbar.Snackbar
 import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


@ExperimentalCoroutinesApi
fun EditText.flowTextChange(): Flow<String> {
    return callbackFlow {
        addTextChangedListener {
            trySend(it.toString())
        }
        awaitClose()
    }
}

fun Fragment.launchFragment(navDirections: NavDirections) {
    val navDestination = NavHostFragment.findNavController(this).currentDestination
    if (navDestination?.getAction(navDirections.actionId) != null && navDestination.id != navDirections.actionId)
        NavHostFragment.findNavController(this).navigate(navDirections)
}

fun Fragment.launchFragmentById(id: Int) {
    val navDestination = NavHostFragment.findNavController(this).currentDestination
    if (navDestination?.getAction(id) != null && navDestination.id != id)
        NavHostFragment.findNavController(this).navigate(id)
}

fun Activity.launchFragmentById(view: View, id: Int) {
    val navDestination = Navigation.findNavController(view).currentDestination
    if (navDestination?.id != id)
        Navigation.findNavController(view).navigate(id)
}

fun Context.hasPermission(permission: String): Boolean {

    // Background permissions didn't exit prior to Q, so it's approved by default
    if (permission == android.Manifest.permission.ACCESS_BACKGROUND_LOCATION &&
        android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q
    )
        return true

    return ActivityCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.hiddenKeyboard(v: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(v.windowToken, 0)
}

fun Context.showKeyboard(v: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
}


fun ProgressBar.foldingCube(color: Int = Color.WHITE): FoldingCube {
    val foldingCube = FoldingCube()
    foldingCube.setDrawBounds(0, 0, 100, 100)
    foldingCube.color = color
    indeterminateDrawable = foldingCube
    foldingCube.start()
    return foldingCube
}

fun ProgressBar.fadingCircle(color: Int = Color.WHITE): FadingCircle {
    val fadingCircle = FadingCircle()
    fadingCircle.setDrawBounds(0, 0, 100, 100)
    fadingCircle.color = color
    indeterminateDrawable = fadingCircle
    fadingCircle.start()
    return fadingCircle
}

@SuppressLint("InflateParams")
fun Context.progressDialog(color: Int = Color.WHITE): Dialog {
    val dialog = AlertDialog.Builder(this)
    val view = LayoutInflater.from(this).inflate(R.layout.progressing_dialog, null)
    dialog.setView(view)
    dialog.setCancelable(false)
    val progress = view.findViewById<ProgressBar>(R.id.progress)
    progress.fadingCircle(color)
    return dialog.create()
}

fun Dialog.showWithCustomSize(): Dialog {
    show()
    val layoutParams = WindowManager.LayoutParams()
    layoutParams.copyFrom(window?.attributes)
    layoutParams.width = 800
    layoutParams.gravity = Gravity.CENTER_VERTICAL
    window?.attributes = layoutParams
    window?.setBackgroundDrawableResource(android.R.color.transparent)
    return this
}

@SuppressLint("InflateParams")
fun Dialog.loadDialog(context: Context): FoldingCube {
    val view = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null)
    setContentView(view)
    setCancelable(false)
    val progress = view.findViewById<ProgressBar>(R.id.progress)
    return progress.foldingCube()
}

fun Activity.colorStatusBar(
    @ColorRes colorStatus: Int,
    @ColorRes colorNavigationBar: Int,
    lightBar: Boolean
) {

    if (lightBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0,
                0
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = 0
        }
    }

    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(this, colorStatus)
    window.navigationBarColor = ContextCompat.getColor(this, colorNavigationBar)

}

fun View.visibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visibleOrInvisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

fun Fragment.request() {
    lifecycle.addObserver(object : DefaultLifecycleObserver {

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)

        }
    })
}

@RequiresApi(Build.VERSION_CODES.M)
fun AppCompatActivity.requestPermissionActivityRationale(
    permission: String,
    snackbar: Snackbar,
    permissionApplied: () -> Unit
): ActivityResultLauncher<String>? {
    var resultRequestPermission: ActivityResultLauncher<String>? = null
    val provideRational = shouldShowRequestPermissionRationale(permission)
    if (provideRational)
        snackbar.show()
    else {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it)
                permissionApplied()
            else
                snackbar.show()
        }.also { resultRequestPermission = it }
    }
    return resultRequestPermission
}

fun Fragment.requestPermissionWithRationale(
    permission: String,
    snackbar: Snackbar,
    permissionApplied: () -> Unit
): ActivityResultLauncher<String> {
    var resultRequestPermission: ActivityResultLauncher<String>? = null
    val provideRational = shouldShowRequestPermissionRationale(permission)
    if (provideRational)
        snackbar.show()
    else {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    if (it)
                        permissionApplied()
                    else
                        snackbar.show()
                }.also { resultRequestPermission = it }
            }
        })
    }
    return resultRequestPermission!!
}

fun ImageView.loadImage(uri: Uri) {
    Glide.with(this).load(uri).placeholder(R.drawable.user__).into(this)
}

fun View.enable() {
    isEnabled = true
    isFocusable = true
    isFocusableInTouchMode = true
}

fun View.enableNot() {
    isEnabled = false
    isFocusable = false
    isFocusableInTouchMode = false
}

fun Fragment.createDialog(
    @StyleRes styleRes: Int,
    colorStatusBar: Int? = null,
    lightBar: Boolean = false
): Dialog {
    val relativeLayoutRoot = RelativeLayout(requireActivity())
    relativeLayoutRoot.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    val dialog = Dialog(requireContext(), styleRes)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(relativeLayoutRoot)
    dialog.window?.setBackgroundDrawable(ColorDrawable())
    if (colorStatusBar != null)
        dialog.window?.statusBarColor = colorStatusBar

    if (lightBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dialog.window?.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dialog.window?.insetsController?.setSystemBarsAppearance(
                0,
                0
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog.window?.decorView?.systemUiVisibility = 0
        }
    }

    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

    dialog.setCanceledOnTouchOutside(false)
    return dialog
}