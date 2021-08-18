package com.eaapps.schoolsguide.features.resetPassword


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.ActivityRestPasswordBinding
import com.eaapps.schoolsguide.features.MainActivity
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.getColorResource
import com.eaapps.schoolsguide.utils.progressSmallDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import www.sanju.motiontoast.MotionToast


@InternalCoroutinesApi
@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {

    private var binding: ActivityRestPasswordBinding? = null

    private val viewModel: ResetViewModel by viewModels()

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestPasswordBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.resetViewModel = viewModel
        intent.data?.apply {
            viewModel.resetPasswordModel.token = this.lastPathSegment!!
        }

        collectResetFlow()

        binding!!.back.setOnClickListener { navigationToMainActivity() }

        dialog = progressSmallDialog(this.getColorResource(R.color.colorApp1Dark))

    }

    private fun collectResetFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel.resetStateFlow.collect(FlowEvent(onError = {
                dialog.dismiss()
                MotionToast.createColorToast(
                    this@ResetPasswordActivity,
                    "Failed ☹",
                    it,
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this@ResetPasswordActivity, R.font.rpt_bold)

                )
            },
                onLoading = {
                    dialog.show()
                },
                onSuccess = {
                    dialog.dismiss()
                    navigationToMainActivity()
                },
                onNothing = { dialog.dismiss() }
            ))
        }
    }

    private fun navigationToMainActivity() {
        startActivity(
            Intent(
                this,
                MainActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        finish()
    }

    override fun onDestroy() {
        dialog.dismiss()
        super.onDestroy()
    }
}
