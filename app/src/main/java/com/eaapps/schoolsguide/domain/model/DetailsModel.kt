package com.eaapps.schoolsguide.domain.model

import androidx.annotation.DrawableRes


data class NavigationPropertiesModel(
    var id:Int,
    var title: String,
    @DrawableRes var icon: Int
)

data class SubModel(var title:String,var subTitle:String,var lineBottom:Boolean = true)