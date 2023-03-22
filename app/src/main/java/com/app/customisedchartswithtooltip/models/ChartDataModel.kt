package com.app.customisedchartswithtooltip.models

import androidx.annotation.Keep
import java.io.Serializable

data class ChartDataModel(
    val weekly: ArrayList<ChartData>,
    val monthly: ArrayList<ChartData>,
    val yearly: ArrayList<ChartData>,
) : Serializable

@Keep
data class ChartData(
    val day_Number: String,
    val day_Name: String,
    val companyA: String,
    val companyB: String,
    val companyC: String,
    val companyD: String,
    val year: String
) : Serializable