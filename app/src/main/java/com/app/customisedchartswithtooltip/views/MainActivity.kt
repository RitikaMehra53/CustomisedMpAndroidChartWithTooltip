package com.app.customisedchartswithtooltip.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.customisedchartswithtooltip.R
import com.app.customisedchartswithtooltip.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonUtils.instance.changeStatusBarBackgroundColor(this)
        setContentView(R.layout.activity_main)

        btn_bar_chart.setOnClickListener {
            startActivity(Intent(this, BarChartActivity::class.java))
        }

        btn_combined_chart.setOnClickListener {
            startActivity(Intent(this, CombinedChartActivity::class.java))
        }

        btn_line_chart.setOnClickListener {
            startActivity(Intent(this, LineChartActivity::class.java))
        }
    }

}