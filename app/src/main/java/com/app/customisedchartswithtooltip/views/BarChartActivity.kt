package com.app.customisedchartswithtooltip.views

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.app.customisedchartswithtooltip.R
import com.app.customisedchartswithtooltip.models.ChartData
import com.app.customisedchartswithtooltip.models.ChartDataModel
import com.app.customisedchartswithtooltip.models.ChartHoverModel
import com.app.customisedchartswithtooltip.utils.CommonUtils
import com.app.customisedchartswithtooltip.utils.CustomBarChartRender
import com.app.customisedchartswithtooltip.utils.CustomMarkerView
import com.app.customisedchartswithtooltip.utils.MyAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bar_chart.*
import kotlinx.android.synthetic.main.header_layout.*
import java.text.DecimalFormat

class BarChartActivity : BaseActivity(), OnChartValueSelectedListener {
    private var chartData: ChartDataModel?= null
    private var xAxisValues = ArrayList<String>()

    private var selectedTimeLine = CommonUtils.WEEKLY

    private var mChartDataArray = ArrayList<ChartData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonUtils.instance.changeStatusBarBackgroundColor(this)
        setContentView(R.layout.activity_bar_chart)
        CommonUtils.instance.setEmptyBarChart(chart, this)

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
        setBarChart()

        radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_week -> {
                    selectedTimeLine = CommonUtils.WEEKLY
                    mChartDataArray.clear()
                    chartData?.weekly?.let { mChartDataArray.addAll(it) }
                    setBarChart()
                }
                R.id.rb_month -> {
                   selectedTimeLine = CommonUtils.MONTHLY
                    mChartDataArray.clear()
                    chartData?.monthly?.let { mChartDataArray.addAll(it) }
                    setBarChart()
                }
                R.id.rb_year -> {
                    selectedTimeLine = CommonUtils.YEARLY
                    mChartDataArray.clear()
                    chartData?.yearly?.let { mChartDataArray.addAll(it) }
                    setBarChart()
                }
            }
        }

        tv_heading.text = resources.getString(R.string.bar_chart)

        iv_back.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
    }

    private fun setBarChart() {
        chart.highlightValues(null)
        CommonUtils.instance.resetBarChart(chart)

        val barDataSetArrayList = getDataSet()
        val data = BarData(barDataSetArrayList as List<IBarDataSet>?)

        data.setDrawValues(false)
        //chart.renderer = CustomBarRenderer(chart, chart.animator, chart.viewPortHandler, 55F)
        val typeface = ResourcesCompat.getFont(this, R.font.eina_02_regular)
        //typeface = Typeface.create(typeface, Typeface.BOLD)

        val xAxis = chart.xAxis
        xAxis.typeface = typeface
        xAxis?.textSize = 11f
        xAxis.textColor = ContextCompat.getColor(this, R.color.dark_grey)
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.yOffset = 20f
        //xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        val axisLeft = chart.axisLeft
        axisLeft.typeface = typeface
        axisLeft?.textSize = 11f
        axisLeft.textColor = ContextCompat.getColor(this, R.color.dark_grey)
        axisLeft.setDrawAxisLine(false)
        axisLeft.valueFormatter = MyAxisValueFormatter()
        axisLeft.setDrawTopYLabelEntry(true)
        axisLeft.xOffset = 20f
        axisLeft.gridColor = ContextCompat.getColor(this, R.color.divider_color)
        axisLeft.gridLineWidth = 1f
        axisLeft.axisMinimum = 0f
        val axisRight = chart.axisRight
        axisRight.setDrawAxisLine(false)
        axisRight.setDrawGridLines(false)
        axisRight.isEnabled = false

        val barChartRender = CustomBarChartRender(chart, chart.animator, chart.viewPortHandler)
        barChartRender.setRadius(5)
        chart.renderer = barChartRender
        chart.data = data
        chart.setDrawBorders(false)
        chart.setBackgroundColor(Color.TRANSPARENT) //set whatever color you prefer
        chart.legend.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.setPinchZoom(false)
        chart.isDoubleTapToZoomEnabled = false
        chart.description.isEnabled = false
        chart.animateY(1500)

        val groupSpace = 0.08f
        val barSpace = 0.12f / chart.data.dataSets.size.toFloat() // x4 DataSet

        val barWidth = 0.8f / chart.data.dataSets.size.toFloat() // x4 DataSet

        val groupCount = xAxisValues.size
        val startYear = 0

        if (chart.data.dataSets.size > 1) {
            chart.barData.barWidth = barWidth
            chart.xAxis.axisMinimum = startYear.toFloat()
            chart.xAxis.axisMaximum =
                startYear + chart.barData.getGroupWidth(groupSpace, barSpace) * groupCount
            //chart.xAxis.granularity = (chart.xAxis.axisMaximum / xAxisValues.size)
            chart.xAxis.granularity = (chart.xAxis.axisMaximum / xAxisValues.size)
            chart.groupBars(startYear.toFloat(), groupSpace, barSpace)
            chart.xAxis.setCenterAxisLabels(true)
        } else {
            chart.barData.barWidth = barWidth
            chart.xAxis.axisMinimum = -barWidth / 2
            chart.xAxis.axisMaximum =
                (chart.barData.getGroupWidth(groupSpace, barSpace) * groupCount) - (barWidth / 2)
            chart.xAxis.granularity = 1f
            chart.xAxis.setCenterAxisLabels(false)
        }

        if (groupCount == 12)
            chart.setVisibleXRangeMaximum(CommonUtils.BAR_VISIBLE_LIMIT)
        else
            chart.setVisibleXRangeMaximum(groupCount.toFloat())
        chart.isDragDecelerationEnabled = false
        //xAxis.setLabelCount(xAxisValues.size, true)
        chart.extraBottomOffset = 10f
        chart.notifyDataSetChanged()
        chart.invalidate()
        chart.setOnChartValueSelectedListener(this)
    }

    private fun getDataSet(): ArrayList<BarDataSet> {

        var companyASum = 0.0
        var companyBSum = 0.0
        var companyCSum = 0.0
        var companyDSum = 0.0

        val dataSets: ArrayList<BarDataSet> = ArrayList()
        xAxisValues.clear()
        val barEntryList1: ArrayList<BarEntry> = ArrayList()
        val barEntryList2: ArrayList<BarEntry> = ArrayList()
        val barEntryList3: ArrayList<BarEntry> = ArrayList()
        val barEntryList4: ArrayList<BarEntry> = ArrayList()
        for (j in 0 until mChartDataArray.size) {
            val barEntry1 = BarEntry(j.toFloat(), mChartDataArray[j].companyA.toFloat())
            barEntryList1.add(barEntry1)
            val barEntry2 = BarEntry(j.toFloat(), mChartDataArray[j].companyB.toFloat())
            barEntryList2.add(barEntry2)
            val barEntry3 = BarEntry(j.toFloat(), mChartDataArray[j].companyC.toFloat())
            barEntryList3.add(barEntry3)
            val barEntry4 = BarEntry(j.toFloat(), mChartDataArray[j].companyD.toFloat())
            barEntryList4.add(barEntry4)
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
        val barDataSet1 = BarDataSet(
            barEntryList1,
            resources.getString(R.string.company_a)
        )

        val barDataSet2 = BarDataSet(
            barEntryList2,
            resources.getString(R.string.company_b)
        )

        val barDataSet3 = BarDataSet(
            barEntryList3,
            resources.getString(R.string.company_c)
        )

        val barDataSet4 = BarDataSet(
            barEntryList4,
            resources.getString(R.string.company_d)
        )

        barDataSet1.highLightAlpha = 0
        barDataSet2.highLightAlpha = 0
        barDataSet3.highLightAlpha = 0
        barDataSet4.highLightAlpha = 0

        dataSets.add(barDataSet1)
        dataSets.add(barDataSet2)
        dataSets.add(barDataSet3)
        dataSets.add(barDataSet4)

        for (i in 0 until dataSets.size) {
            dataSets[i].color = CommonUtils.instance.getColorResourceForStatsPage(this, i)
        }

        tv_company_a_value.text = CommonUtils.instance.truncateNumberWithK(companyASum.toFloat())
        tv_company_b_value.text = CommonUtils.instance.truncateNumberWithK(companyBSum.toFloat())
        tv_company_c_value.text = CommonUtils.instance.truncateNumberWithK(companyCSum.toFloat())
        tv_company_d_value.text = CommonUtils.instance.truncateNumberWithK(companyDSum.toFloat())

        return dataSets
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onValueSelected(e: Entry?, h: Highlight?) {
        val barSets = chart.barData?.dataSets as ArrayList<BarDataSet>

        val data = ArrayList<ChartHoverModel>()
        for (i in 0 until barSets.size) {
            val chartHoverModel = ChartHoverModel()
            chartHoverModel.value =
                DecimalFormat("#0.00").format(barSets[i].getEntryForIndex(e?.x!!.toInt()).y)
            chartHoverModel.name = barSets[i].label
            when (selectedTimeLine) {
                CommonUtils.WEEKLY -> chartHoverModel.yAxisLabel =
                    CommonUtils.instance.getWeekDayWithDate(e.x.toInt())
               CommonUtils.YEARLY -> chartHoverModel.yAxisLabel =
                    xAxisValues[e.x.toInt()] + " " + mChartDataArray[e.x.toInt()].year
                else -> chartHoverModel.yAxisLabel = xAxisValues[e.x.toInt()]
            }
            val barColor = CommonUtils.instance.getColorResourceForStatsPage(this, i)
            val transparentColor =
                CommonUtils.instance.getColorResourceForStatsPageTransparent(this, i)

            chartHoverModel.color = barColor

            barSets[i].colors = getColors(
                barSets[i].entryCount,
                e.x.toInt(),
                barColor,
                transparentColor
            )
            data.add(chartHoverModel)
        }

        chart.invalidate()

        val mv = CustomMarkerView(this, R.layout.marker_view, data, "bar", "n", "f")
        mv.chartView = chart
        chart.markerView = mv
    }

    private fun getColors(
        entryCount: Int,
        selectedXIndex: Int,
        darkColor: Int,
        transparentColor: Int
    ): ArrayList<Int> {
        val colorList = ArrayList<Int>()
        for (i in 0 until entryCount) {
            if (i == selectedXIndex) {
                colorList.add(darkColor)
            } else {
                colorList.add(transparentColor)
            }
        }

        return colorList
    }

    override fun onNothingSelected() {
        val barSets = chart.barData?.dataSets as ArrayList<BarDataSet>
        for (i in 0 until barSets.size) {
            barSets[i].color = CommonUtils.instance.getColorResourceForStatsPage(this, i)
        }

        chart.invalidate()
    }

    override fun customBackPress() {
        finish()
    }
}