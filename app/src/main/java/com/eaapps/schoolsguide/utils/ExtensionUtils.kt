package com.eaapps.schoolsguide.utils

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.ViewDataBinding
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentNoInternetBinding
import com.github.ybq.android.spinkit.style.FadingCircle
import com.github.ybq.android.spinkit.style.FoldingCube
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.ktx.addCircle
import com.squareup.picasso.Picasso
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import www.sanju.motiontoast.MotionToast
import java.io.IOException
import java.io.Reader


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
//    if (permission == android.Manifest.permission.ACCESS_BACKGROUND_LOCATION && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
//        return true
    return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
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

@SuppressLint("InflateParams")
fun Context.progressSmallDialog(
    color: Int = Color.WHITE,
    colorBackground: Int = Color.WHITE
): Dialog {
    val dialog = AlertDialog.Builder(this)
    dialog.create().window
    val view = LayoutInflater.from(this).inflate(R.layout.progressing_dialog_small, null)
    dialog.setView(view)
    dialog.setCancelable(false)
    val progress = view.findViewById<ProgressBar>(R.id.progress)
    val background = view.findViewById<MaterialCardView>(R.id.smallCard)
    background.setCardBackgroundColor(colorBackground)
    progress.fadingCircle(color)
    val alertDialog = dialog.create()
    alertDialog.window?.apply {
        setBackgroundDrawableResource(android.R.color.transparent)
        attributes.gravity = Gravity.CENTER
    }
    return alertDialog
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

fun Window.colorStatusBar(
    context: Context,
    @ColorRes colorStatus: Int,
    @ColorRes colorNavigationBar: Int,
    lightBar: Boolean
) {

    if (lightBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            insetsController?.setSystemBarsAppearance(
                0,
                0
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decorView.systemUiVisibility = 0
        }
    }

    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    statusBarColor = ContextCompat.getColor(context, colorStatus)
    navigationBarColor = ContextCompat.getColor(context, colorNavigationBar)

}

fun Activity.fullScreenEnable(fullScreen: Boolean, mainContainer: View) {
    if (fullScreen) {
        window?.apply {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, mainContainer).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    } else {
        window?.apply {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(
                window,
                mainContainer
            ).let { controller ->
                controller.show(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
            }
        }
    }
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

fun ImageView.loadImage(uri: String) {
    Picasso.get()
        .load(uri)
        .placeholder(R.drawable.user__)
        .into(this)
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
    lightBar: Boolean = false,
    shouldInterceptBackPress: Boolean = false,
    onSpecialBackPressed: () -> Unit = {}
): Dialog {
    val relativeLayoutRoot = RelativeLayout(requireActivity())
    relativeLayoutRoot.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    val dialog = object : Dialog(requireContext(), styleRes) {
        override fun onBackPressed() {
            if (shouldInterceptBackPress)
                onSpecialBackPressed()
            else
                super.onBackPressed()
        }
    }

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

fun <T> Gson.fromReaderToObject(json: Reader): T? {
    val type = object : TypeToken<T>() {}.type
    return fromJson(json, type) as T?
}

fun Context.getColorResource(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun <T> Gson.fromStringToObject(json: String): T {
    val type = object : TypeToken<T>() {}.type
    return fromJson(json, type)
}


// Data store jetPack

fun <T> DataStore<Preferences>.getValueFlow(
    key: Preferences.Key<T>,
    defaultValue: T
): Flow<T> {
    return this.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else
            throw  exception
    }.map { preferences ->
        preferences[key] ?: defaultValue
    }
}

suspend fun <T> DataStore<Preferences>.setValue(key: Preferences.Key<T>, value: T) {
    this.edit {
        it[key] = value
    }
}

fun View.expand() {
    if (visibility == View.VISIBLE) return
    val durations: Long
    val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        (parent as View).width,
        View.MeasureSpec.EXACTLY
    )
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        0,
        View.MeasureSpec.UNSPECIFIED
    )
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    layoutParams.height = 1
    visibility = View.VISIBLE
    durations = ((targetHeight / context.resources
        .displayMetrics.density)).toLong()

    alpha = 0.0F
    visibility = View.VISIBLE
    animate().alpha(1.0F).setDuration(durations).setListener(null)

    val a: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            layoutParams.height =
                if (interpolatedTime == 1f) LinearLayout.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Expansion speed of 1dp/ms
    a.duration = durations
    startAnimation(a)
}

fun View.collapse() {
    if (visibility == View.GONE) return
    val durations: Long
    val initialHeight = measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    durations = (initialHeight / context.resources
        .displayMetrics.density).toLong()

    alpha = 1.0F
    animate().alpha(0.0F).setDuration(durations)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
                alpha = 1.0F
            }
        })

    // Collapse speed of 1dp/ms
    a.duration = durations
    startAnimation(a)
}

fun Activity.transparentStatusBar() {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    else
        window.setDecorFitsSystemWindows(false)
}

fun ViewDataBinding.showBottomSheet(
    window: WindowManager?,
    @StyleRes style: Int = R.style.ThemeOverlay_MaterialComponents_BottomSheetDialog,
    fullScreen: Boolean = false
): BottomSheetDialog {
    val bottomSheetDialog = BottomSheetDialog(this.root.context, style)
    bottomSheetDialog.setContentView(this.root)
    val view = bottomSheetDialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
    val behavior = BottomSheetBehavior.from<View>(view!!)
    bottomSheetDialog.setCancelable(false)
    val displayMetrics = DisplayMetrics()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.root.context.display?.getRealMetrics(displayMetrics)
    } else {
        window?.defaultDisplay?.getMetrics(displayMetrics)
    }
    val size = fullScreen.let {
        if (it)
            displayMetrics.heightPixels
        else
            displayMetrics.heightPixels * 90 / 100
    }
    behavior.setPeekHeight(size, true)
    behavior.isDraggable = false
    bottomSheetDialog.show()
    return bottomSheetDialog
}

fun Dialog.createDialog(resources: Resources, peekHeightOffset: Float = 1.0f): Dialog {
    this.setCancelable(false)
    val displayMetrics = resources.displayMetrics
    val size = displayMetrics.heightPixels * peekHeightOffset
    (this as? BottomSheetDialog)
        ?.behavior?.peekHeight = size.toInt()
    val behavior = (this as? BottomSheetDialog)?.behavior
    behavior?.isHideable = false
    behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    //behavior?.isDraggable = false
    return this
}

fun BottomSheetDialog.dialogShow(
    resources: Resources,
    peekHeightOffset: Float = 1.0f,
    draggable: Boolean = false,
    hidden: Boolean = false,
    state: Int = BottomSheetBehavior.STATE_COLLAPSED,

    ) {
    setOnShowListener {
        val d = it as BottomSheetDialog
        val sheet = d.findViewById<View>(R.id.design_bottom_sheet)
        val displayMetrics = resources.displayMetrics
        val size = displayMetrics.heightPixels * peekHeightOffset
        val behavior = BottomSheetBehavior.from(sheet!!)
        behavior.isHideable = hidden
        behavior.isDraggable = draggable
        behavior.peekHeight = size.toInt()
        behavior.state = state
    }
}


fun Activity.toastingError(msg: String) {
    MotionToast.createColorToast(
        this,
        "Failed ☹",
        msg,
        MotionToast.TOAST_ERROR,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.SHORT_DURATION,
        ResourcesCompat.getFont(this, R.font.rpt_bold)

    )
}

fun Activity.toastingInfo(msg: String) {
    MotionToast.createColorToast(
        this,
        "Info",
        msg,
        MotionToast.TOAST_INFO,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.SHORT_DURATION,
        ResourcesCompat.getFont(this, R.font.rpt_bold)

    )
}


fun Context.currentLocation(
    location: (location: Location) -> Unit,
    onLocationPermissionRequired: () -> Unit
) {
    val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        onLocationPermissionRequired()
        return
    }
    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.let { location(it) }

}

fun GoogleMap.newLocationWithZoom(
    lat: Double,
    lng: Double,
    animation: Boolean = true,
    zoom: Float = 12f
) {
    if (animation) animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), zoom))
    else moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), zoom))
}

fun GoogleMap.circle(point: LatLng, radius: Double, colorFill: Int = Color.WHITE): Circle {
    return addCircle {
        center(point)
        radius(radius)
        strokeWidth(0f)
        strokeColor(colorFill)
        fillColor(colorFill)
    }
}

fun Location.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}

fun View.snack(message: String, action: (() -> Unit)? = null, error: Boolean = true) {
    context.errorDialog(error, message, action)
}

@InternalCoroutinesApi
fun Fragment.handleApiError(
    failure: ErrorEntity,
    retry: (() -> Unit)? = null
) {
    failure.apply {
        when (this) {
            is ErrorEntity.HttpError -> {
                when (this.httpError) {
                    HttpErrorEntity.BadGateway502 -> requireView().snack(
                        resources.getString(R.string.error502),
                        retry
                    )
                    HttpErrorEntity.BadRequest400 -> requireView().snack(
                        resources.getString(R.string.error400),
                        retry
                    )
                    HttpErrorEntity.Forbidden403 -> requireView().snack(
                        resources.getString(R.string.error403),
                        retry
                    )
                    HttpErrorEntity.NotAcceptable406 -> requireView().snack(
                        resources.getString(R.string.error400),
                        retry
                    )
                    HttpErrorEntity.NotFound404 -> requireView().snack(
                        resources.getString(R.string.error404),
                        retry
                    )
                    HttpErrorEntity.NotImplemented501 -> requireView().snack(
                        resources.getString(R.string.error501),
                        retry
                    )
                    is HttpErrorEntity.Nothing -> {
                        requireView().snack(
                            (this.httpError as HttpErrorEntity.Nothing).msg ?: "",
                            retry
                        )
                    }


                    HttpErrorEntity.ServerError500 -> requireView().snack(
                        resources.getString(R.string.error500),
                        retry
                    )
                    HttpErrorEntity.ServiceUnavailable503 -> requireView().snack(
                        resources.getString(R.string.error503),
                        retry
                    )
                    HttpErrorEntity.TimeoutGateway504 -> requireView().snack(
                        resources.getString(R.string.error504),
                        retry
                    )
                    HttpErrorEntity.Unauthorized401 -> requireView().snack(
                        resources.getString(R.string.error401),
                        retry
                    )
                    else -> Unit
                }
            }

            ErrorEntity.NoInternet -> {
                requireView().snack(
                    resources.getString(R.string.no_internet_),
                    retry
                )
            }

            ErrorEntity.SSLError -> {
                requireView().snack(
                    resources.getString(R.string.no_internet_),
                    retry
                )
            }

            is ErrorEntity.IOError -> {
                requireView().snack(this.msg ?: "", retry)
            }

            ErrorEntity.NothingError -> requireView().snack(
                resources.getString(R.string.connect_timeout),
                retry
            )
            ErrorEntity.ParseError -> requireView().snack(
                resources.getString(R.string.parse_error),
                retry
            )
            ErrorEntity.ServerNotResponse -> requireView().snack(
                resources.getString(R.string.server_not_responding),
                retry
            )
            ErrorEntity.TimeOut -> requireView().snack(
                resources.getString(R.string.connect_timeout),
                retry
            )
        }
    }
}

fun Context.errorDialog(
    error: Boolean = true,
    descMsg: String,
    onButtonClicked: (() -> Unit)? = null
): Dialog {
    val dialog = AlertDialog.Builder(this)
    dialog.create().window
    val view = LayoutInflater.from(this).inflate(R.layout.dialog_error_1, null)
    dialog.setView(view)
    dialog.setCancelable(false)
    val title = view.findViewById<TextView>(R.id.title)
    if (error)
        title.text = getString(R.string.oh_snap)
    else
        title.text = getString(R.string.yay)

    val msg = view.findViewById<TextView>(R.id.msg)
    msg.text = descMsg
    val icon = view.findViewById<ImageView>(R.id.icon)
    if (error)
        icon.setImageResource(R.drawable.sad)
    else
        icon.setImageResource(R.drawable.smile)

    val errorBtn = view.findViewById<Button>(R.id.errorBtn)
    if (error)
        errorBtn.text = getString(R.string.try_again)
    else
        errorBtn.text = getString(R.string.ok_cool)

    val alertDialog = dialog.create()
    onButtonClicked?.apply {
        errorBtn.visibleOrGone(true)
        errorBtn.setOnClickListener {
            this()
            alertDialog.dismiss()
        }
    } ?: run {
        errorBtn.visibleOrGone(false)
        Handler(Looper.getMainLooper()).postDelayed({
            alertDialog.dismiss()
        }, 5000L)
    }

    alertDialog.window?.apply {
        setBackgroundDrawableResource(android.R.color.transparent)
        attributes.gravity = Gravity.CENTER
    }
    alertDialog.show()
    return alertDialog
}

@InternalCoroutinesApi
fun FragmentActivity.noInternetDialog(
    onSuccessConnection: (() -> Unit)
) {
    val dialogBottomSheet = BottomSheetDialog(this)
    val binding = FragmentNoInternetBinding.inflate(layoutInflater)
    dialogBottomSheet.setContentView(binding.root)
    dialogBottomSheet.setCancelable(false)
    dialogBottomSheet.setOnShowListener {
        val bottomSheetDialog = it as BottomSheetDialog
        val parentLayout = bottomSheetDialog.findViewById<View>(
            com.google.android.material.R.id.design_bottom_sheet
        )
        parentLayout?.let { bottomSheet ->
            val behaviour = BottomSheetBehavior.from(bottomSheet)
            val layoutParams = bottomSheet.layoutParams
            val displayMetrics = resources.displayMetrics
            val size = displayMetrics.heightPixels
            layoutParams.height = size
            bottomSheet.layoutParams = layoutParams
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            behaviour.isDraggable = false
            behaviour.isHideable = false
        }
    }
    binding.run {
        retryButton.setOnClickListener {
            progressBar.visibleOrGone(true)
            retryButton.visibleOrGone(false)
            if (NetworkChecker.isNetworkConnected(this@noInternetDialog)) {
                msg.text = getString(R.string.no_internet)
                lifecycleScope.launchWhenStarted {
                    NetworkChecker.isOnline().collect(FlowEvent(onError = {
                        progressBar.visibleOrGone(false)
                        retryButton.visibleOrGone(true)
                    }, onSuccess = {
                        if (it) {
                            progressBar.visibleOrGone(false)
                            retryButton.visibleOrGone(false)
                            msg.text = getString(R.string.online)
                            onSuccessConnection()
                            dialogBottomSheet.dismiss()
                        }
                    }))
                }
            } else {
                progressBar.visibleOrGone(false)
                retryButton.visibleOrGone(true)
                msg.text = getString(R.string.any_network)
            }
        }
    }
    dialogBottomSheet.show()
}

fun Fragment.shortLink(id: Int, onLinkShare: (String) -> Unit) {
    Firebase.dynamicLinks.shortLinkAsync {
        link = Uri.parse("https://saudischoolsguide.com/ar/schoolDetails/$id")
        domainUriPrefix = "https://schoolsguide.page.link"
    }.addOnSuccessListener {
        onLinkShare(it.shortLink.toString())
    }
}