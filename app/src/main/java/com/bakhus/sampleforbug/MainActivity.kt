package com.bakhus.sampleforbug

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bakhus.sampleforbug.databinding.ActivityMainBinding
import com.bakhus.sampleforbug.databinding.ItemCalendarBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.Size
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedDate = LocalDate.now()
    private val selectedDates = mutableSetOf<LocalDate>()
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initCalendar()

    }

    private fun initCalendar() {
        binding.apply {
            weekCalendar.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    return rv.scrollState == RecyclerView.SCROLL_STATE_DRAGGING
                }
            })
            weekCalendar.setOnTouchListener { _, event ->
                event.action == MotionEvent.ACTION_MOVE
            }
        }
        val dm = DisplayMetrics()
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        binding.weekCalendar.apply {
            val dayWidth = (dm.widthPixels - 20.dp) / 7
            val dayHeight = (dayWidth * 1)
            daySize = Size(dayWidth, dayHeight)
        }

        class DayViewContainer(view: View) : ViewContainer(view) {
            val bind = ItemCalendarBinding.bind(view)
            lateinit var day: CalendarDay

            val parentView = ItemCalendarBinding.bind(view).parentView
            val dateText = ItemCalendarBinding.bind(view).dateText
            val dateNumber = ItemCalendarBinding.bind(view).dateNumber

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDates.contains(day.date)) {
                            selectedDates.remove(day.date)
                        } else {
                            selectedDates.add(day.date)
                        }
                        binding.weekCalendar.notifyDayChanged(day)
                    }
                }
            }

            fun bind(day: CalendarDay) {
                this.day = day
                bind.dateNumber.text = dateFormatter.format(day.date)
                bind.dateText.text = dayFormatter.format(day.date)
            }
        }

        binding.weekCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.bind(day)
                container.day = day
                val parentView = container.parentView
                val dateText = container.dateText
                val dateNumber = container.dateNumber
                if (day.owner == DayOwner.THIS_MONTH) {
                    when {
                        selectedDates.contains(day.date) -> {
                            dateText.setTextColorRes(R.color.purple50)
                            dateNumber.setTextColorRes(R.color.purple)
                            parentView.setBackgroundResource(R.drawable.selected_day)
                        }
                        else -> {
                            dateText.setTextColorRes(R.color.white50)
                            dateNumber.setTextColorRes(R.color.white)
                            parentView.setBackgroundResource(R.drawable.calendar_rounded_corners)
                        }
                    }
                } else {
                    dateText.setTextColorRes(R.color.white50)
                    dateNumber.setTextColorRes(R.color.white)
                    parentView.setBackgroundResource(R.drawable.calendar_rounded_corners)
                }
            }
        }
        val currentMonth = YearMonth.now()

        binding.weekCalendar.setup(
            currentMonth,
            currentMonth.plusMonths(3),
            DayOfWeek.values().random()
        )
        binding.weekCalendar.scrollToDate(LocalDate.now())
    }

}