package com.app.customisedchartswithtooltip.utils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat

class MyAxisValueFormatter() : IValueFormatter, IAxisValueFormatter {
    private var mSuffix = arrayOf(
        "", "k", "m", "b", "t"
    )
    private var mMaxLength = 5
    private val mFormat: DecimalFormat
    private var mText = ""

    /**
     * Creates a formatter that appends a specified text to the result string
     *
     * @param appendix a text that will be appended
     */
    constructor(appendix: String) : this() {
        mText = appendix
    }

    // IValueFormatter
    override fun getFormattedValue(
        value: Float,
        entry: Entry,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler
    ): String {
        return CommonUtils.instance.makePretty(value.toDouble()) + mText
    }

    // IAxisValueFormatter
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        return CommonUtils.instance.makePretty(value.toDouble()) + mText
    }

    /**
     * Set an appendix text to be added at the end of the formatted value.
     *
     * @param appendix
     */
    fun setAppendix(appendix: String) {
        mText = appendix
    }

    /**
     * Set custom suffix to be appended after the values.
     * Default suffix: ["", "k", "m", "b", "t"]
     *
     * @param suffix new suffix
     */
    fun setSuffix(suffix: Array<String>) {
        mSuffix = suffix
    }

    fun setMaxLength(maxLength: Int) {
        mMaxLength = maxLength
    }


    val decimalDigits: Int
        get() = 0

    init {
        mFormat = DecimalFormat("###E00")
    }
}