package com.bakhus.sampleforbug

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).roundToInt()

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

fun View.showGone(show: Boolean): View {
    visibility = if (show) View.VISIBLE else View.GONE
    return this
}

fun View.showInvisible(show: Boolean): View {
    visibility = if (show) View.VISIBLE else View.INVISIBLE
    return this
}

fun View.gone(): View {
    visibility = View.GONE
    return this
}

fun View.invisible(): View {
    visibility = View.INVISIBLE
    return this
}

fun View.visible(): View {
    visibility = View.VISIBLE
    return this
}

fun View.enableDisable(enable: Boolean): View {
    return if (enable) this.enable() else this.disable()
}

fun View.enable(): View {
    isEnabled = true
    alpha = 1f
    isClickable = true
    return this
}

fun View.disable(): View {
    isEnabled = false
    alpha = 0.5f
    isClickable = false
    return this
}

fun View.blockClickable(time: Long = 2000) {
    this.isClickable = false
    Handler().postDelayed({ this.isClickable = true }, time)
}

internal fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(ContextCompat.getColor(context, color))

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)


fun dpToPx(activity: FragmentActivity, dp: Int): Int {
    val displayMetrics = activity.resources.displayMetrics
    return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToLong().toInt()
}

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}


