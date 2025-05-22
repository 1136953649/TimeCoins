package com.example.timecoins

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarLayout
import com.haibin.calendarview.CalendarView
import android.database.sqlite.SQLiteDatabase
import android.graphics.Paint


class MainActivity : AppCompatActivity() ,View.OnClickListener,CalendarView.OnCalendarSelectListener,CalendarView.OnYearChangeListener,CalendarView.OnViewChangeListener,CalendarView.OnCalendarRangeSelectListener{
    private lateinit var mCalendarView: CalendarView;
    private lateinit var mCalendarLayout: CalendarLayout;
    private lateinit var dbHelper: DatabaseHelper

    val labels = arrayOf("高效娱乐", "休息时间", "被动工作", "高效工作", "无效拖延")
    val colors = arrayOf("#10ebeb", "#1ae61a", "#ff7700", "#ffd900", "#FF0000")

    protected fun initWindow() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindow();
        setContentView(getLayoutId());
        
        dbHelper = DatabaseHelper(this)
        mCalendarView = findViewById(R.id.calendarView)
        mCalendarView.setOnCalendarSelectListener(this)
        mCalendarView.setOnViewChangeListener(this)
        
        // 初始化当前日期的格子数据
        val date = "" + mCalendarView.selectedCalendar.year + "-" + mCalendarView.selectedCalendar.month + "-" + mCalendarView.selectedCalendar.day;
        updateGridColors(date)
    }

    fun show(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }


    protected fun getLayoutId(): Int {
        return R.layout.activity_main
    }


    override fun onClick(v: View?) {
        v?.let { view ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("选择标记")
                .setItems(labels) { _, which ->
                    val color = colors[which]
                    val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
                    view.setBackgroundColor(Color.parseColor(color))
                    val date = "" + mCalendarView.selectedCalendar.year + "-" + mCalendarView.selectedCalendar.month + "-" + mCalendarView.selectedCalendar.day;
                    dbHelper.saveCellColor(date, gridLayout.indexOfChild(view), which)
                }
                .create()
                .show()
        }
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {
        TODO("Not yet implemented")
    }

    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        calendar?.let {
            val date = "" + it.year + "-" + it.month + "-" + it.day;
            updateGridColors(date)
        }
    }
    
    private fun updateGridColors(date: String) {
        println(date)
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)

        if (date.equals("0-0-0")) return

        val colorsMap = dbHelper.getAllCellColorsByDate(date)
        
        for (i in 0 until gridLayout.childCount) {
            // 恢复格子颜色状态
            if (i == 0){
                gridLayout.getChildAt(i).setBackgroundColor(Color.WHITE)
            }else if (i == 35){
                gridLayout.getChildAt(i).setBackgroundColor(Color.BLACK)
            }else {
                gridLayout.getChildAt(i).setOnClickListener(this)
                colorsMap[i]?.let { 
                    gridLayout.getChildAt(i).setBackgroundColor(Color.parseColor(colors[it])) 
                } ?: run {
                    gridLayout.getChildAt(i).setBackgroundColor(Color.WHITE)
                }
            }
        }
    }

    override fun onYearChange(year: Int) {
        TODO("Not yet implemented")
    }

    override fun onViewChange(isMonthView: Boolean) {
        println("is momth view : " + isMonthView)
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        gridLayout.visibility = if (isMonthView) View.GONE else View.VISIBLE
    }

    override fun onCalendarSelectOutOfRange(calendar: Calendar?) {
        TODO("Not yet implemented")
    }

    override fun onSelectOutOfRange(calendar: Calendar?, isOutOfMinRange: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onCalendarRangeSelect(calendar: Calendar?, isEnd: Boolean) {
        TODO("Not yet implemented")
    }
}