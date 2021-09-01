package com.eaapps.schoolsguide.domain.model

import java.io.File


data class InquiryModel(
    var message_type: String = "",
    var reply_type: String = "",
    var reply_time: String = "",
    var message: String = "",
    var school_id: Int? = null
)

data class JoinDiscountModel(
    var full_name: String = "",
    var phone: String = "",
    var email: String = "",
    var number_of_students: Int? = null,
    var school_id: Int? = null
)

data class BookSchoolModel(
    var full_name: String = "",
    var email: String = "",
    var phone: String = "",
    var address: String = "",
    var number_of_students: String ="",
    var school_id: Int? = null,
    var grades: Array<Int>? = null
){
    fun clear(){
        full_name = ""
        email = ""
        phone= ""
        address=""
        number_of_students=""
        school_id=null
        grades=null

    }
}

data class UploadCvModel(
    var school_id: Int? = null,
    var job_id: Int? = null,
    var attachment: File? = null
)