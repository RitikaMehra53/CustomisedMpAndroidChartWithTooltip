package com.app.customisedchartswithtooltip.views

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.app.customisedchartswithtooltip.R
import com.app.customisedchartswithtooltip.models.ChartData
import com.app.customisedchartswithtooltip.models.ChartDataModel
import com.app.customisedchartswithtooltip.models.ChartHoverModel
import com.app.customisedchartswithtooltip.utils.CommonUtils
import com.app.customisedchartswithtooltip.utils.CustomMarkerViewForLineChart
import com.app.customisedchartswithtooltip.utils.MyAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bar_chart.*
import kotlinx.android.synthetic.main.activity_line_chart.*
import kotlinx.android.synthetic.main.activity_line_chart.chart
import kotlinx.android.synthetic.main.activity_line_chart.radioGroup
import kotlinx.android.synthetic.main.activity_line_chart.tv_company_a_value
import kotlinx.android.synthetic.main.activity_line_chart.tv_company_b_value
import kotlinx.android.synthetic.main.activity_line_chart.tv_company_c_value
import kotlinx.android.synthetic.main.activity_line_chart.tv_company_d_value
import kotlinx.android.synthetic.main.header_layout.*
import java.text.DecimalFormat

class LineChartActivity : BaseActivity(), OnChartValueSelectedListener {
    private var chartData: ChartDataModel?= null
    private var xAxisValues = ArrayList<String>()

    private var selectedTimeLine = CommonUtils.WEEKLY

    private var mChartDataArray = ArrayList<ChartData>()

    private var selectedIndex = -1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonUtils.instance.changeStatusBarBackgroundColor(this)
        setContentView(R.layout.activity_line_chart)
        CommonUtils.instance.setEmptyLineChart(chart, this)

        val jsonForProfile = CommonUtils.instance.getJsonFromAssets(this, "chartData.json")
        chartData = Gson().fromJson(jsonForProfile, ChartDataModel::class.java)


        when (selectedTimeLine) {
            CommonUtils.WEEKLY -> radioGroup.check(R.id.rb_week)
            CommonUtils.MONTHLY -> radioGroup.check(R.id.rb_month)
            CommonUtils.YEARLY -> radioGroup.check(R.id.rb_year)
        }

        selectedTimeLine = CommonUtils.WEEKLY
        mChartDataArray.clear()
        chartData?.weekly?.let { mChartDataArray.addAll(it) }
        setLineChart()

        radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_week -> {
                    selectedTimeLine = CommonUtils.WEEKLY
                    mChartDataArray.clear()
                    chartData?.weekly?.let { mChartDataArray.addAll(it) }
                    setLineChart()
                }
                R.id.rb_month -> {
                    selectedTimeLine = CommonUtils.MONTHLY
                    mChartDataArray.clear()
                    chartData?.monthly?.let { mChartDataArray.addAll(it) }
                    setLineChart()
                }
                R.id.rb_year -> {
                    selectedTimeLine = CommonUtils.YEARLY
                    mChartDataArray.clear()
                    chartData?.yearly?.let { mChartDataArray.addAll(it) }
                    setLineChart()
                }
            }
        }

        tv_heading.text = resources.getString(R.string.line_chart)

        iv_back.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
    }

    private fun setLineChart() {
        chart.highlightValues(null)
        CommonUtils.instance.resetLineChart(chart)
        val dataSets = getDataSet()
        val typeface = ResourcesCompat.getFont(this, R.font.eina_02_regular)

        val xAxis = chart.xAxis
        xAxis.typeface = typeface
        xAxis.textColor = ContextCompat.getColor(this, R.color.dark_grey)
        xAxis.textSize = 11f
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.yOffset = 20f
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        val axisLeft = chart.axisLeft
        axisLeft.typeface = typeface
        axisLeft.textColor = ContextCompat.getColor(this, R.color.dark_grey)
        axisLeft.setDrawAxisLine(false)
        axisLeft.textSize = 11f
        axisLeft.valueFormatter = MyAxisValueFormatter()
        axisLeft.setDrawTopYLabelEntry(true)
        axisLeft.xOffset = 20f
        axisLeft.gridColor = ContextCompat.getColor(this, R.color.divider_color)
        axisLeft.gridLineWidth = 1f

        val axisRight = chart.axisRight
        axisRight?.setDrawAxisLine(false)
        axisRight?.setDrawGridLines(false)
        axisRight?.isEnabled = false



        chart.description?.isEnabled = false
        chart.setDrawBorders(false)
        chart.setBackgroundColor(Color.TRANSPARENT) //set whatever color you prefer
        chart.legend?.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.setPinchZoom(false)

        chart.isDoubleTapToZoomEnabled = false
        val data = LineData(dataSets as List<ILineDataSet>)
        chart.data = data

        chart.xAxis.granularity = 1f
        chart.extraBottomOffset = 10f
        chart.extraTopOffset = 30f
        chart.extraRightOffset = 30f
        chart.axisLeft.axisMinimum = 0f
        if (xAxisValues.size == 12)
            chart.setVisibleXRangeMaximum(CommonUtils.BAR_VISIBLE_LIMIT)
        else
            chart.setVisibleXRangeMaximum(xAxisValues.size.toFloat())
        chart.isDragDecelerationEnabled = false
        chart.invalidate()
        chart.setOnChartValueSelectedListener(this)
    }


    var companyASum = 0.0
    var companyBSum = 0.0
    var companyCSum = 0.0
    var companyDSum = 0.0

    private fun getDataSet(): ArrayList<LineDataSet> {
        var xAxisLabelsAdded = false
        companyASum = 0.0
        companyBSum = 0.0
        companyCSum = 0.0
        companyDSum = 0.0

        val dataSets = ArrayList<LineDataSet>()

        xAxisValues.clear()
        for (i in 0 until 4) {
            val valueSet = ArrayList<Entry>()
            for (j in 0 until mChartDataArray.size) {
                when (i) {
                    0 -> {
                        valueSet.add(
                            Entry(
                                j.toFloat(),
                                mChartDataArray[j].companyA.toFloat()
                            )
                        )
                    }
                    1 -> {
                        valueSet.add(
                            Entry(
                                j.toFloat(),
                                mChartDataArray[j].companyB.toFloat()
                            )
                        )
                    }
                    2 -> {
                        valueSet.add(
                            Entry(
                                j.toFloat(),
                                mChartDataArray[j].companyC.toFloat()
                            )
                        )
                    }
                    else -> {
                        valueSet.add(
                            Entry(
                                j.toFloat(),
                                mChartDataArray[j].companyD.toFloat()
                            )
                        )
                    }
                }

                if(!xAxisLabelsAdded) {
                    if (selectedTimeLine == CommonUtils.MONTHLY) {
                        xAxisValues.add("${mChartDataArray[j].day_Name} ${mChartDataArray[j].day_Number}")
                    } else {
                        xAxisValues.add(mChartDataArray[j].day_Name)
                    }
                    companyASum += mChartDataArray[j].companyA.toFloat()
                    companyBSum += mChartDataArray[j].companyB.toFloat()
                    companyCSum += mChartDataArray[j].companyC.toFloat()
                    companyDSum += mChartDataArray[j].companyD.toFloat()
                }

            }

            xAxisLabelsAdded = true

            val dataSet = LineDataSet(valueSet, "")
            dataSet.lineWidth = 2.5f
            dataSet.circleSize = 5f
            dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            val color = CommonUtils.instance.getColorResource(this, i)
            dataSet.color = color
            dataSet.setCircleColor(color)
            dataSet.setDrawValues(false)
            dataSet.setDrawCircles(false)
            dataSet.highLightColor = ContextCompat.getColor(this, R.color.dark_grey)
            dataSet.enableDashedHighlightLine(8f, 8f, 100f)
            dataSet.highlightLineWidth = 2f
            dataSet.setDrawHorizontalHighlightIndicator(false)
            dataSets.add(dataSet)
        }

        tv_company_a_value.text = CommonUtils.instance.truncateNumberWithK(companyASum.toFloat())
        tv_company_b_value.text = CommonUtils.instance.truncateNumberWithK(companyBSum.toFloat())
        tv_company_c_value.text = CommonUtils.instance.truncateNumberWithK(companyCSum.toFloat())
        tv_company_d_value.text = CommonUtils.instance.truncateNumberWithK(companyDSum.toFloat())

        return dataSets
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onValueSelected(e: Entry?, h: Highlight?) {

        val lineSets = chart.lineData?.dataSets

        for (i in 0 until lineSets?.size!!) {
            for (j in 0 until lineSets[i].entryCount) {
                lineSets[i].getEntryForIndex(j).icon = null
            }
        }


        val data = ArrayList<ChartHoverModel>()
        for (i in 0 until lineSets.size) {
            val chartHoverModel = ChartHoverModel()
            chartHoverModel.value =
                DecimalFormat("#0.00").format(lineSets[i].getEntryForIndex(e?.x!!.toInt()).y)
            chartHoverModel.name = lineSets[i].label
            when (selectedTimeLine) {
                CommonUtils.WEEKLY -> chartHoverModel.yAxisLabel =
                    CommonUtils.instance.getWeekDayWithDateA(e.x.toInt())
                CommonUtils.YEARLY -> chartHoverModel.yAxisLabel =
                    CommonUtils.instance.getFirstDateAndLastDateUsingMonthYear(
                        xAxisValues[e.x.toInt()],
                        mChartDataArray[0].year
                    )
                else -> chartHoverModel.yAxisLabel =
                    CommonUtils.instance.getWeekStartAndEndDate(e.x.toInt() + 1, false)
            }
            chartHoverModel.color = lineSets[i].color
            data.add(chartHoverModel)

            val barColor = lineSets[i].color
            chartHoverModel.color = barColor

            for (j in 0 until lineSets[i].entryCount) {
                if (e.x.toInt() == j) {
                    lineSets[i].getEntryForIndex(j).icon =
                        CommonUtils.instance.getDrawableUsingPosition(this, i)
                    Log.e("fkdlkfd",i.toString())
                }
            }

        }
        window.decorView.performHapticFeedback(
            HapticFeedbackConstants.VIRTUAL_KEY,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
        chart.invalidate()

        val mv = CustomMarkerViewForLineChart(
            this,
            R.layout.marker_view_line_chart,
            chart.height,
            data
        )

        // Set the marker to the chart
        mv.chartView = chart
        chart.markerView = mv

        selectedIndex = e?.x?.toInt() ?: -1

        setFinancialsAccordingToSelection()
    }

    override fun onNothingSelected() {
        val lineSets = chart.lineData?.dataSets

        for (i in 0 until lineSets?.size!!) {
            for (j in 0 until lineSets[i].entryCount) {
                lineSets[i].getEntryForIndex(j).icon = null
            }

        }

        selectedIndex = -1

        setFinancialsAccordingToSelection()
    }

    private fun setFinancialsAccordingToSelection() {
        if(selectedIndex == -1){
            tv_company_a_value.text = CommonUtils.instance.truncateNumberWithK(companyASum.toFloat())
            tv_company_b_value.text = CommonUtils.instance.truncateNumberWithK(companyBSum.toFloat())
            tv_company_c_value.text = CommonUtils.instance.truncateNumberWithK(companyCSum.toFloat())
            tv_company_d_value.text = CommonUtils.instance.truncateNumberWithK(companyDSum.toFloat())
        }
        else{
            for (i in 0 until mChartDataArray.size){
                if(i == selectedIndex){
                    tv_company_a_value.text = CommonUtils.instance.truncateNumberWithK(mChartDataArray[i].companyA.toFloat())
                    tv_company_b_value.text = CommonUtils.instance.truncateNumberWithK(mChartDataArray[i].companyB.toFloat())
                    tv_company_c_value.text = CommonUtils.instance.truncateNumberWithK(mChartDataArray[i].companyC.toFloat())
                    tv_company_d_value.text = CommonUtils.instance.truncateNumberWithK(mChartDataArray[i].companyD.toFloat())
                    break
                }
            }
        }
    }
    
    override fun customBackPress() {
        finish()
    }
}