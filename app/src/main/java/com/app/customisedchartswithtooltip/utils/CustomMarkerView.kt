@file:Suppress("DEPRECATION")

package com.app.customisedchartswithtooltip.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.customisedchartswithtooltip.MarkerViewAdapter
import com.app.customisedchartswithtooltip.R
import com.app.customisedchartswithtooltip.models.ChartHoverModel
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

@SuppressLint("ViewConstructor")
class CustomMarkerView(
    context: Context,
    layoutResource: Int,
    chartHoverModelList: ArrayList<ChartHoverModel>,
    from: String,
    selectedType: String,
    where: String
) :
    MarkerView(context, layoutResource), IMarker {
    private val tvXAxisLabel: TextView = findViewById<View>(R.id.tv_x_axis_label) as TextView
    private val rvData: RecyclerView = findViewById<View>(R.id.rv_data) as RecyclerView
    private val mChartHoverModelList: ArrayList<ChartHoverModel>
    private val mContext: Context
    private var mFrom: String
    private var mSelectedType: String
    private var mWhere: String

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry, highlight: Highlight) {
        if (mChartHoverModelList.size > 0) {
            tvXAxisLabel.text =
                mChartHoverModelList[0].yAxisLabel // set the entry-value as the display text
            val markerViewAdapter = MarkerViewAdapter(mChartHoverModelList, mSelectedType, mWhere)
            if (mChartHoverModelList.size > 1) {
                rvData.layoutManager =
                    GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false)
                rvData.adapter = markerViewAdapter
            } else {
                rvData.layoutManager = GridLayoutManager(mContext, 1, RecyclerView.VERTICAL, false)
                rvData.adapter = markerViewAdapter
            }
        }
        super.refreshContent(e, highlight)

    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    init {
        mChartHoverModelList = chartHoverModelList
        mContext = context
        mFrom = from
        mSelectedType = selectedType
        mWhere = where
    }
}