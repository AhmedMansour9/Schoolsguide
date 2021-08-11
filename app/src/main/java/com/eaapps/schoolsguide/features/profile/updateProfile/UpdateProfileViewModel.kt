package com.eaapps.schoolsguide.features.profile.updateProfile

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.domain.model.UpdateProfileModel
import com.eaapps.schoolsguide.domain.usecase.UpdateFatherProfileUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(private val updateFatherProfileUseCase: UpdateFatherProfileUseCase) :
    ViewModel() {

    private val _updateProfileStateFlow: MutableStateFlow<Resource<ResponseEntity>> =
        MutableStateFlow(Resource.Nothing())
    val updateProfileStateFlow: StateFlow<Resource<ResponseEntity>> = _updateProfileStateFlow

    lateinit var updateProfileModel: UpdateProfileModel
    val inputEditError = ObservableField<HashMap<String, String>>(HashMap())

    private var helperValid = HashMap<String, String>().apply {
        put("name", "")
        put("email", "")
        put("phone", "")
        put("city", "")
    }

    fun updateProfileBtn() {
        inputEditError.set(helperValid)
        inputEditError.notifyChange()
        if (updateFatherProfileUseCase.isValid(updateProfileModel)) {
            viewModelScope.launch {
                try {
                    _updateProfileStateFlow.emit(Resource.Loading())
                    val result = updateFatherProfileUseCase.execute(updateProfileModel)
                    _updateProfileStateFlow.emit(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            inputEditError.set(updateFatherProfileUseCase.validMessage(updateProfileModel))
            inputEditError.notifyChange()
        }
    }


}