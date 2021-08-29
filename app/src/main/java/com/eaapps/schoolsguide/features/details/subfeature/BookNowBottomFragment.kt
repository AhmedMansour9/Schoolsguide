package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.BookNowBottomSheetBinding
import com.eaapps.schoolsguide.features.details.DetailsViewModel
import com.eaapps.schoolsguide.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import www.sanju.motiontoast.MotionToast

@InternalCoroutinesApi
@AndroidEntryPoint
class BookNowBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BookNowBottomSheetBinding
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }
    private var pairGrades: Pair<List<SchoolResponse.SchoolData.DataSchool.Grade>, ArrayList<String>>? =
        null
    private lateinit var dialogProcess: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BookNowBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheet.dialogShow(resources, 0.95f, draggable = true)
        return bottomSheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailsViewModel = viewModel
        dialogProcess =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))
        buildArgs()
        collectResultBookingNow()
        binding.bindListsDown()
        binding.bindClicks()
    }

    private fun buildArgs() {
        BookNowBottomFragmentArgs.fromBundle(requireArguments()).dataSchool.apply {
            viewModel.bookSchoolModel.school_id = id
            val list: ArrayList<String> = ArrayList()
            grades.forEach { typeData ->
                list.add(typeData.name)
            }
            pairGrades = Pair(grades, list)

        }
    }

    private fun BookNowBottomSheetBinding.bindListsDown() {
        val arrayItem = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val adapterNumber = ArrayAdapter(
            requireContext(),
            R.layout.city_list_item,
            arrayItem
        )
        (studentNumberEdit as? AutoCompleteTextView)?.setAdapter(adapterNumber)
        (studentNumberEdit as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
            viewModel.bookSchoolModel.number_of_students = "${arrayItem[position]}"
        }
    }

    private fun BookNowBottomSheetBinding.bindClicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
        studentGradeChoose.setOnClickListener {
            bindGrades()
        }
    }

    private fun BookNowBottomSheetBinding.bindGrades() {
        val gradeList = ArrayList<Int>()
        pairGrades?.second?.apply {
            val dialogChoose =
                MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogStyle)
            dialogChoose.setTitle(getString(R.string.select_grade))
            val selectGrade = BooleanArray(this.size)
            dialogChoose.setMultiChoiceItems(
                this.toTypedArray(), selectGrade
            ) { _, which, isChecked ->
                if (isChecked) {
                    gradeList.add(which)
                    gradeList.sort()
                } else {
                    gradeList.remove(which)
                }

            }

            dialogChoose.setPositiveButton(getString(R.string._done)) { dialog, _ ->
                pairGrades?.first?.apply {
                    val arrayGradeSelected = ArrayList<Int>()
                    val strBuilder = StringBuilder()
                    gradeList.forEach {
                        arrayGradeSelected.add(this[it].id)
                        strBuilder.append(this[it].name).append(",")
                    }
                    studentGradeChoose.text = strBuilder.toString()
                    viewModel.bookSchoolModel.grades = arrayGradeSelected.toTypedArray()
                    dialog.dismiss()
                }
            }

            dialogChoose.setNegativeButton(getString(R.string._cancel)) { dialog, _ ->
                dialog.dismiss()
            }

            dialogChoose.setNeutralButton(getString(R.string._clear_all)) { _, _ ->
                repeat((selectGrade.indices).count()) {
                    selectGrade[it] = false
                    gradeList.clear()
                    studentGradeChoose.text = getString(R.string.student_grades)
                }
            }

            dialogChoose.create().show()
        }
    }

    private fun collectResultBookingNow() {
        lifecycleScope.launchWhenStarted {
            viewModel.bookSchoolFlow.stateFlow.collect(FlowEvent(onError = {
                dialogProcess.dismiss()
                requireActivity().toastingError(it)
            },
                onLoading = {
                    dialogProcess.show()
                },
                onSuccess = {
                    MotionToast.createColorToast(
                        requireActivity(),
                        getString(R.string.booking_school),
                        getString(R.string.success_booking),
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        8000L,
                        ResourcesCompat.getFont(requireContext(), R.font.rpt_bold)
                    )
                    viewModel.bookSchoolFlow.setValue(Resource.Nothing())
                    dialogProcess.dismiss()
                    dismiss()
                },
                onNothing = { dialogProcess.dismiss() }
            ))
        }
    }
}