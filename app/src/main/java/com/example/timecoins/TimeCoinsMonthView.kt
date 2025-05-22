package com.example.timecoins

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.haibin.calendarview.BaseView
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.RangeMonthView


class TimeCoinsMonthView(context: Context) : RangeMonthView(context) {

    private lateinit var dbHelper: DatabaseHelper

    init {
        dbHelper = DatabaseHelper(context)
    }

    /**
     * 绘制选中的日子
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 返回true 则绘制onDrawScheme，因为这里背景色不是是互斥的，所以返回true
     */
    override fun onDrawSelected(canvas: Canvas, calendar: Calendar, x: Int, y: Int, hasScheme: Boolean,isSelectedPre: Boolean, isSelectedNext: Boolean): Boolean {
        val totalDegrees : Int = 360; // 总角度
        val step : Int = 10; // 每10度一个颜色
        val colorsCount : Int = totalDegrees / step; // 需要绘制的颜色数量
        val sweepAngle : Float = step.toFloat(); // 每次绘制的角度
        var startAngle : Float = -90f; // 开始角度，使得圆环从顶部开始绘制

        mSelectedPaint.setStyle(Paint.Style.STROKE);
        val lineWidth : Float =  mItemWidth.toFloat()/10;
        mSelectedPaint.setStrokeWidth(lineWidth);

        val date = "" + calendar.year + "-" + calendar.month + "-" +calendar.day;
        val colorsMap = dbHelper.getAllCellColorsByDate(date)

        for (i in 1 .. colorsCount) {
            mSelectedPaint.setColor(TimeCoinsWeekView.generateColor(i,colorsMap)); // 生成颜色

            canvas.drawArc(x + lineWidth, y + lineWidth, (x + mItemWidth) - lineWidth, (y + mItemHeight) - lineWidth, startAngle, sweepAngle, false, mSelectedPaint);
            startAngle += sweepAngle; // 更新开始角度
        }
        return true
    }


    /**
     * 绘制标记的事件日子
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    override fun onDrawScheme(canvas: Canvas, calendar: Calendar, x: Int, y: Int, isSelected: Boolean) {
        //这里绘制标记的日期样式，想怎么操作就怎么操作
    }


    /**
     * 绘制文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    override fun onDrawText(canvas: Canvas, calendar: Calendar, x: Int, y: Int, hasScheme: Boolean, isSelected: Boolean) {
        var cx : Int = x + mItemWidth / 2;

        var top : Int = y - mItemHeight / 6;

        if (isSelected) {//优先绘制选择的
            canvas.drawText(calendar.getDay().toString(), cx.toFloat(), mTextBaseLine + top,
                mSelectTextPaint);
            canvas.drawText(calendar.getLunar(), cx.toFloat(), mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
        } else if (hasScheme) {//否则绘制具有标记的
            canvas.drawText(calendar.getDay().toString(), cx.toFloat(), mTextBaseLine + top,
                if(calendar.isCurrentMonth()) mSchemeTextPaint else mOtherMonthTextPaint);

            canvas.drawText(calendar.getLunar(), cx.toFloat(), mTextBaseLine + y + mItemHeight / 10, mCurMonthLunarTextPaint);
        } else {//最好绘制普通文本
            canvas.drawText(calendar.getDay().toString(), cx.toFloat(), mTextBaseLine + top,
                if(calendar.isCurrentDay()) mCurDayTextPaint else
                    (if(calendar.isCurrentMonth()) mCurMonthTextPaint else mOtherMonthTextPaint));
            canvas.drawText(calendar.getLunar(), cx.toFloat(), mTextBaseLine + y + mItemHeight / 10,
                if(calendar.isCurrentDay()) mCurDayLunarTextPaint else
                    (if(calendar.isCurrentMonth()) mCurMonthLunarTextPaint else mOtherMonthLunarTextPaint));
        }

    }

}