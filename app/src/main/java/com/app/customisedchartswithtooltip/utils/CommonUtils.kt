package com.app.customisedchartswithtooltip.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.app.customisedchartswithtooltip.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.LineChart
import java.io.IOException
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.floor
import kotlin.math.log10

class CommonUtils {
    companion object {
        val instance: CommonUtils = CommonUtils()
        var BAR_VISIBLE_LIMIT = 7f
        var WEEKLY = 1
        var MONTHLY = 2
        var YEARLY = 3
    }

    private var toolTipDayFormat = "EEEE, MMM dd"
    private var df1 = DecimalFormat("0.00")

    @SuppressLint("SimpleDateFormat")
    var lineChartToolTipFormat = SimpleDateFormat("MMM dd, yyyy")

    @RequiresApi(Build.VERSION_CODES.O)
    val formatterFullToolTip = DateTimeFormatter.ofPattern("MMM dd, yyyy")!!

    @RequiresApi(Build.VERSION_CODES.M)
    fun changeStatusBarBackgroundColor(mContext: Activity) {
        val window: Window = mContext.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(mContext, R.color.white)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark
    }

    fun getJsonFromAssets(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun setEmptyLineChart(lineChart: LineChart, context: Context) {
        val typeface = ResourcesCompat.getFont(context, R.font.eina_02_regular)
        //lineChart.setNoDataText("Description that you want");
        val paint = lineChart.getPaint(Chart.PAINT_INFO)
        paint.textSize = convertDpToPixel(11f, context)
        paint.color = ContextCompat.getColor(context, R.color.black)
        paint.typeface = typeface
        lineChart.invalidate()
    }

    fun setEmptyBarChart(barChart: BarChart, context: Context) {
        val typeface = ResourcesCompat.getFont(context, R.font.eina_02_regular)
        //barChart.setNoDataText("Description that you want");
        val paint = barChart.getPaint(Chart.PAINT_INFO)
        paint.textSize = convertDpToPixel(11f, context)
        paint.color = ContextCompat.getColor(context, R.color.black)
        paint.typeface = typeface
        barChart.invalidate()
    }

    fun setEmptyCombineChart(combinedChart: CombinedChart, context: Context) {
        val typeface = ResourcesCompat.getFont(context, R.font.eina_02_regular)
        //barChart.setNoDataText("Description that you want");
        val paint = combinedChart.getPaint(Chart.PAINT_INFO)
        paint.textSize = convertDpToPixel(11f, context)
        paint.color = ContextCompat.getColor(context, R.color.black)
        paint.typeface = typeface
        combinedChart.invalidate()
    }

    fun resetBarChart(chart: BarChart) {
        chart.fitScreen()
        chart.data?.clearValues()
        chart.xAxis.valueFormatter = null
        chart.notifyDataSetChanged()
        chart.clear()
        chart.invalidate()
    }

    fun resetLineChart(chart: LineChart) {
        chart.fitScreen()
        chart.data?.clearValues()
        chart.xAxis.valueFormatter = null
        chart.notifyDataSetChanged()
        chart.clear()
        chart.invalidate()
    }

    fun resetCombineChart(chart: CombinedChart) {
        chart.fitScreen()
        chart.data?.clearValues()
        chart.xAxis.valueFormatter = null
        chart.notifyDataSetChanged()
        chart.clear()
        chart.invalidate()
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun makePretty(number: Double): String {
        /*var r: String = mFormat.format(number)
        r = r.replace(
            "E[0-9]".toRegex(), suffix[Character.getNumericValue(
                r[r.length - 1]
            ) / 3]
        )
        while (r.length > maxLength || r.matches(Regex("[0-9]+\\.[a-z]"))) {
            r = r.substring(0, r.length - 2) + r.substring(r.length - 1)
        }
        return r*/
        var numberString = ""
        numberString = if (Math.abs(number / 1000000) > 1) {
            Math.round(number / 1000000).toString() + "M"
        } else if (Math.abs(number / 1000) > 1) {
            Math.round(number / 1000).toString() + "K"
        } else {
            Math.round(number).toString()
        }
        numberString = if (numberString.contains("-")) {
            "-$${numberString.removePrefix("-")}"
        } else {
            "$${numberString}"
        }
        return numberString
    }

    fun getColorResourceForStatsPage(mContext: Activity, position: Int): Int {
        when (position) {
            0 -> return ContextCompat.getColor(mContext, R.color.sky_blue)
            1 -> return ContextCompat.getColor(mContext, R.color.navy_blue)
            2 -> return ContextCompat.getColor(mContext, R.color.green)
            3 -> return ContextCompat.getColor(mContext, R.color.purple)
            else -> (position == 4)
        }
        return ContextCompat.getColor(mContext, R.color.purple)
    }

    fun getColorResourceForStatsPageTransparent(mContext: Activity, position: Int): Int {
        when (position) {
            0 -> return ContextCompat.getColor(mContext, R.color.app_color_transparent)
            1 -> return ContextCompat.getColor(mContext, R.color.splash_color_transparent)
            2 -> return ContextCompat.getColor(mContext, R.color.green_transparent)
            3 -> return ContextCompat.getColor(mContext, R.color.purple_transparent)
            else -> (position == 4)
        }
        return ContextCompat.getColor(mContext, R.color.purple_transparent)
    }


    fun getWeekDayWithDate(position: Int): String? {
        val c = Calendar.getInstance()
        c.firstDayOfWeek = Calendar.MONDAY
        when (position) {
            0 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            }
            1 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.TUESDAY
            }
            2 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.WEDNESDAY
            }
            3 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.THURSDAY
            }
            4 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.FRIDAY
            }
            5 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.SATURDAY
            }
            6 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
            }
        }
        val df: DateFormat = SimpleDateFormat(toolTipDayFormat)
        return df.format(c.time)
    }

    fun truncateNumberWithK(floatNumber: Float): String {
        val thousand = 1000L
        val million = 1000000L
        val billion = 1000000000L
        val trillion = 1000000000000L
        val roundUpValue = Math.round(floatNumber).toLong()
        var returnValue = ""
        if (roundUpValue in 99999 until million) {
            val fraction = calculateFraction(floatNumber, thousand)
            returnValue = Math.round(df1.format(fraction - 0.05).toDouble()).toString() + "K"
        } else if (roundUpValue in million until billion) {
            val fraction = calculateFraction(floatNumber, million)
            returnValue = df1.format(fraction - 0.05) + "M"
        } else if (roundUpValue in billion until trillion) {
            val fraction = calculateFraction(floatNumber, billion)
            returnValue = df1.format(fraction - 0.05) + "B"
        } else {
            returnValue = thousandSeparator(roundUpValue.toInt())
        }

        returnValue = if (returnValue.contains("-")) {
            "-$${returnValue.removePrefix("-")}"
        } else {
            "$${returnValue}"
        }

        return returnValue
    }

    fun thousandSeparator(n: Int): String {
        // Counting number of digits
        var number = n
        var isMinusAvailable = false
        if(number < 0){
            isMinusAvailable = true
            number = Math.abs(number)
        }

        val l = floor(log10(number.toDouble())).toInt() + 1
        val str = StringBuffer("")
        var count = 0
        var r: Int

        // Checking if number of digits is greater than 3
        if (l > 3) {
            for (i in l - 1 downTo 0) {
                r = number % 10
                number /= 10
                count++
                if (count % 3 == 0 && i != 0) {

                    // Parsing String value of Integer
                    str.append(r.toString())

                    // Appending the separator
                    str.append(",")
                } else str.append(r.toString())
            }
            str.reverse()
        } else str.append(number.toString())

        return if(isMinusAvailable)
            "-$str"
        else
            str.toString()
    }

    private fun calculateFraction(number: Float, divisor: Long): Float {
        val truncate = (number * 10L + divisor / 2L) / divisor
        return truncate * 0.10f
    }

    fun getColorResource(mContext: Activity, position: Int): Int {
        when (position) {
            0 -> return ContextCompat.getColor(mContext, R.color.sky_blue)
            1 -> return ContextCompat.getColor(mContext, R.color.pink)
            2 -> return ContextCompat.getColor(mContext, R.color.orange)
            3 -> return ContextCompat.getColor(mContext, R.color.purple)
            else -> (position == 4)
        }
        return ContextCompat.getColor(mContext, R.color.purple)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawableUsingPosition(context: Context, j: Int): Drawable? {
        return when (j) {
            0 ->
                ContextCompat.getDrawable(context, R.drawable.ic_1_circle)
            1 ->
                ContextCompat.getDrawable(context, R.drawable.ic_2_circle)
            2 ->
                ContextCompat.getDrawable(context, R.drawable.ic_3_circle)
            3 ->
                ContextCompat.getDrawable(context, R.drawable.ic_circle_purple)
            else ->
                ContextCompat.getDrawable(context, R.drawable.ic_eclipse_common)
        }
    }

    fun getWeekDayWithDateA(position: Int): String? {
        val c = Calendar.getInstance()
        c.firstDayOfWeek = Calendar.MONDAY
        when (position) {
            0 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            }
            1 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.TUESDAY
            }
            2 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.WEDNESDAY
            }
            3 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.THURSDAY
            }
            4 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.FRIDAY
            }
            5 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.SATURDAY
            }
            6 -> {
                c[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
            }
        }
        val df: DateFormat = lineChartToolTipFormat
        return df.format(c.time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFirstDateAndLastDateUsingMonthYear(month: String, year: String): String {
        val yearMonth = YearMonth.parse("$month/$year", DateTimeFormatter.ofPattern("MMM/yyyy"))

        val firstOfMonth = yearMonth.atDay(1)
        val last = yearMonth.atEndOfMonth()

        return "${formatterFullToolTip.format(firstOfMonth)} - ${formatterFullToolTip.format(last)}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeekStartAndEndDate(week: Int, isLastMonth: Boolean): String {
        val currentDate = LocalDate.now()
        var month = currentDate.month
        var currentYear = currentDate.year
        if (isLastMonth) {
            month = currentDate.month - 1
            if (month.toString() == "DECEMBER")
                currentYear = currentDate.year - 1
        }

        val requireMonth = convertDateInSpecificFormat("MMMM", "MMM", month.toString())
        return when (week) {
            1 -> {
                "$requireMonth 01, $currentYear - $requireMonth 07, $currentYear"
            }
            2 -> {
                "$requireMonth 08, $currentYear - $requireMonth 14, $currentYear"
            }
            3 -> {
                "$requireMonth 15, $currentYear - $requireMonth 21, $currentYear"
            }
            4 -> {
                "$requireMonth 22, $currentYear - $requireMonth 28, $currentYear"
            }
            else -> {
                "$requireMonth 22, $currentYear - $requireMonth 28, $currentYear"
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateInSpecificFormat(
        readFormat: String,
        writeFormat: String,
        input: String
    ): String {

        try {
            val originalFormat: DateFormat = SimpleDateFormat(readFormat, Locale.ENGLISH)
            val targetFormat: DateFormat = SimpleDateFormat(writeFormat)
            val date: Date = originalFormat.parse(input)
            return targetFormat.format(date) // 20120821
        } catch (e: Exception) {
            return ""
        }

    }

    fun getColorResourceForLineChart(mContext: Activity, position: Int): Int {
        when (position) {
            0 -> return ContextCompat.getColor(mContext, R.color.sky_blue)
            1 -> return ContextCompat.getColor(mContext, R.color.purple)
            2 -> return ContextCompat.getColor(mContext, R.color.orange)
            else -> (position == 4)
        }
        return ContextCompat.getColor(mContext, R.color.purple)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawableUsingPositionLineChart(context: Context, j: Int): Drawable? {
        return when (j) {
            0 ->
                ContextCompat.getDrawable(context, R.drawable.ic_1_circle)
            1 ->
                ContextCompat.getDrawable(context, R.drawable.ic_circle_purple)
            2 ->
                ContextCompat.getDrawable(context, R.drawable.ic_3_circle)
            3 ->
                ContextCompat.getDrawable(context, R.drawable.ic_4_circle)
            else ->
                ContextCompat.getDrawable(context, R.drawable.ic_eclipse_common)
        }
    }
}