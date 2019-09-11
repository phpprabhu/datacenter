package com.ar.businesscard.anchornode.scenes.loading.nodes

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.MotionEvent
import android.widget.Toast
import com.ar.bankar.R
import com.ar.businesscard.activity.data.ARData
import com.ar.businesscard.utils.ar.ArResources
import com.ar.businesscard.utils.ar.AugmentedImageNode
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.ar.sceneform.HitTestResult
import java.util.*


class ChartAugmentedImageNode(private val context: Context) : AugmentedImageNode(ArResources.chartRenderable) {

    private lateinit var barChart: BarChart
    private var dataSets = ArrayList<IBarDataSet>()
    private var defaultBarWidth = -1f
    private var xAxisValues: List<String> = ARData.getMonthList()


    override fun initLayout() {
        super.initLayout()

        // make it under
        offsetZ = -(anchorNode.arHeight  + anchorNode.arHeight * 0.11f)
    }

    override fun modifyLayout() {
        super.modifyLayout()

        localRotation = ArResources.viewRenderableRotation

        barChart = getChart(R.id.chart)
        setChart(3)
    }

    private fun getChart(btnId: Int) = ArResources.chartRenderable.get().view.findViewById<BarChart>(btnId)

    override fun onTouchEvent(p0: HitTestResult?, p1: MotionEvent?): Boolean {
        isEnabled = false
        return false
    }


    private fun setChart(size: Int) {

        val incomeEntries = getIncomeEntries(size)
        val expenseEntries = getExpenseEntries()
        dataSets = ArrayList<IBarDataSet>()
        val set1: BarDataSet
        val set2: BarDataSet

        set1 = BarDataSet(incomeEntries, "Income")
        set1.color = Color.rgb(65, 168, 121)
        set1.valueTextColor = context.getColor(R.color.black)
        set1.valueTextSize = 10f

        set2 = BarDataSet(expenseEntries, "Expense")
        set2.setColors(Color.rgb(241, 107, 72))
        set2.valueTextColor = context.getColor(R.color.black)
        set2.valueTextSize = 10f

        dataSets.add(set1)
        dataSets.add(set2)

        val data = BarData(dataSets)
        barChart.data = data
        barChart.axisLeft.axisMinimum = 0f

        barChart.description.isEnabled = false
        barChart.axisRight.axisMinimum = 0f
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.setMaxVisibleValueCount(10)
        barChart.setPinchZoom(false)
        barChart.setDrawGridBackground(false)


        val l = barChart.legend
        l.isWordWrapEnabled = true
        l.textSize = 14f
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = Legend.LegendForm.CIRCLE

        val xAxis = barChart.xAxis
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.labelRotationAngle = -45f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisMaximum = getExpenseEntries().size.toFloat()
        xAxis.mLabelWidth = 20

        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
        barChart.xAxis.textColor = context.getColor(R.color.black)


        val leftAxis = barChart.axisLeft
        leftAxis.removeAllLimitLines()
        leftAxis.typeface = Typeface.DEFAULT
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.textColor = Color.TRANSPARENT
        leftAxis.setDrawGridLines(false)
        barChart.axisRight.isEnabled = false

        setBarWidth(data, size)
        barChart.animateY(3000, Easing.EaseOutBack);
        barChart.invalidate()

    }

    private fun setBarWidth(barData: BarData, size: Int) {
        if (dataSets.size > 1) {
            val barSpace = 0.02f
            val groupSpace = 0.3f
            defaultBarWidth = (1 - groupSpace) / dataSets.size - barSpace
            if (defaultBarWidth >= 0) {
                barData.barWidth = defaultBarWidth
            } else {
                Toast.makeText(context, "Default Barwdith $defaultBarWidth", Toast.LENGTH_SHORT).show()
            }
            val groupCount = getExpenseEntries().size
            if (groupCount != -1) {
                barChart.xAxis.axisMinimum = 0f
                barChart.xAxis.axisMaximum = 0 + barChart.barData.getGroupWidth(groupSpace, barSpace) * groupCount
                barChart.xAxis.setCenterAxisLabels(true)
            } else {
                Toast.makeText(context, "no of bar groups is $groupCount", Toast.LENGTH_SHORT).show()
            }

            barChart.groupBars(0f, groupSpace, barSpace) // perform the "explicit" grouping
            barChart.invalidate()
        }
    }

    private fun getExpenseEntries(): List<BarEntry> {
        return ARData.getExpenseList()
    }

    private fun getIncomeEntries(size: Int): List<BarEntry> {
        return ARData.getIncomeList()
    }
}
