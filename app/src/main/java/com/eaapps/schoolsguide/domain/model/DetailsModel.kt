package com.eaapps.schoolsguide.domain.model

import androidx.annotation.DrawableRes


data class NavigationPropertiesModel(
    var title: String,
    @DrawableRes var icon: Int
)