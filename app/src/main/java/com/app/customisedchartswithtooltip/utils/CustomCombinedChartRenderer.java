package com.app.customisedchartswithtooltip.utils;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.renderer.BubbleChartRenderer;
import com.github.mikephil.charting.renderer.CandleStickChartRenderer;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.renderer.ScatterChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class CustomCombinedChartRenderer extends CombinedChartRenderer {

    public CustomCombinedChartRenderer(CombinedChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    public void createRenderers() {
        this.mRenderers.clear();
        CombinedChart chart = (CombinedChart)this.mChart.get();
        if (chart != null) {
            CombinedChart.DrawOrder[] orders = chart.getDrawOrder();
            CombinedChart.DrawOrder[] var3 = orders;
            int var4 = orders.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                CombinedChart.DrawOrder order = var3[var5];
                switch(order) {
                    case BAR:
                        if (chart.getBarData() != null) {
                            this.mRenderers.add(new CustomBarChartRendererFromTop(chart, this.mAnimator, this.mViewPortHandler));
                        }
                        break;
                    case BUBBLE:
                        if (chart.getBubbleData() != null) {
                            this.mRenderers.add(new BubbleChartRenderer(chart, this.mAnimator, this.mViewPortHandler));
                        }
                        break;
                    case LINE:
                        if (chart.getLineData() != null) {
                            this.mRenderers.add(new LineChartRenderer(chart, this.mAnimator, this.mViewPortHandler));
                        }
                        break;
                    case CANDLE:
                        if (chart.getCandleData() != null) {
                            this.mRenderers.add(new CandleStickChartRenderer(chart, this.mAnimator, this.mViewPortHandler));
                        }
                        break;
                    case SCATTER:
                        if (chart.getScatterData() != null) {
                            this.mRenderers.add(new ScatterChartRenderer(chart, this.mAnimator, this.mViewPortHandler));
                        }
                }
            }    
        }
    }
}