package com.app.customisedchartswithtooltip.utils

import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.renderer.*
import com.github.mikephil.charting.utils.ViewPortHandler

class CustomCombinedChartRenderer(
    chart: CombinedChart?,
    animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?
) : CombinedChartRenderer(chart, animator, viewPortHandler) {
    override fun createRenderers() {
        mRenderers.clear()
        val chart = mChart.get() as CombinedChart?
        if (chart != null) {
            val orders = chart.drawOrder
            val var4 = orders.size
            for (var5 in 0 until var4) {
                val order = orders[var5]
                when (order) {
                    DrawOrder.BAR -> if (chart.barData != null) {
                        mRenderers.add(
                            CustomBarChartRendererFromTop(
                                chart,
                                mAnimator,
                                mViewPortHandler
                            )
                        )
                    }
                    DrawOrder.BUBBLE -> if (chart.bubbleData != null) {
                        mRenderers.add(
                            BubbleChartRenderer(
                                chart,
                                mAnimator,
                                mViewPortHandler
                            )
                        )
                    }
                    DrawOrder.LINE -> if (chart.lineData != null) {
                        mRenderers.add(
                            LineChartRenderer(
                                chart,
                                mAnimator,
                                mViewPortHandler
                            )
                        )
                    }
                    DrawOrder.CANDLE -> if (chart.candleData != null) {
                        mRenderers.add(
                            CandleStickChartRenderer(
                                chart,
                                mAnimator,
                                mViewPortHandler
                            )
                        )
                    }
                    DrawOrder.SCATTER -> if (chart.scatterData != null) {
                        mRenderers.add(
                            ScatterChartRenderer(
                                chart,
                                mAnimator,
                                mViewPortHandler
                            )
                        )
                    }
                }
            }
        }
    }
}