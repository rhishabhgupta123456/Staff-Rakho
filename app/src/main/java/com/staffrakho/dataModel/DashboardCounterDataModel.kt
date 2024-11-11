package com.staffrakho.dataModel

data class DashboardCounterDataModel(
    val success: Boolean,
    val message: String,
    val data: ArrayList<Counters>,
)

data class Counters(
    val label: String,
    val count: String,
    val key: String,
)


