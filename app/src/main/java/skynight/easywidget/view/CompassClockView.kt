package skynight.easywidget.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import skynight.easywidget.view.CompassClockView.ImgUtils.Companion.drawable2bitmap
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar
import java.util.Date

/**
 * @FILE:   CompassClockView
 * @AUTHOR: 1552980358
 * @DATE:   11 Jul 2019
 * @TIME:   2:38 PM
 **/

class CompassClockView : View {
    private var paint: Paint
    private var paintSelect: Paint
    //年
    private var year = 0
    //月
    private var month = 0
    //天
    private var day = 0
    //周
    private var week = 0
    //时
    private var hr = 0
    //分
    private var min = 0
    //秒
    private var sec = 0
    //当前有多少天
    private var dayCount = 0

    //秒针转动度数
    private var secDeg = 0f
    //分针转动度数
    private var minDeg = 0f
    //月份的转动度数
    private var monthDegrees: Float = 0f
    //天的转动度数
    private var dayDeg = 0f
    //小时的转动度数
    private var hrDeg = 0f
    //周的转动度数
    private var weekDeg = 0f

    private var yuan = 0f

    //中心点X
    private var centX: Float = 0.toFloat()
    //中心店Y
    private var centY: Float = 0.toFloat()

    //默认字体大小15centX
    private var textSize = 0f
    //默认字体间距5centX
    private var textMargin = 5f

    private var bgColor: Int = -1

    companion object {
        const val YEAR = "年"
        const val MONTH = "月"
        const val DAY = "日"
        const val WEEK = "星期"
        const val HOUR = "时"
        const val SECOND = "秒"
        const val MINUTE = "分"

        private val countTag = arrayListOf(
            "一", "二", "三", "四", "五", "六", "七", "八", "九", "十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
            "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八", "二十九", "三十",
            "三十一", "三十二", "三十三", "三十四", "三十五", "三十六", "三十七", "三十八", "三十九", "四十",
            "四十一", "四十二", "四十三", "四十四", "四十五", "四十六", "四十七", "四十八", "四十九", "五十",
            "五十一", "五十二", "五十三", "五十四", "五十五", "五十六", "五十七", "五十八", "五十九", "零"
        )
    }

    private var textColorSelected = Color.RED
    private var textColorNotSelected = Color.BLACK

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        paint = Paint().also {
            it.color = textColorNotSelected
            it.isAntiAlias = true
            it.textSize = textSize
            it.textAlign = Paint.Align.CENTER
        }

        paintSelect = Paint().also {
            it.color = textColorSelected
            it.isAntiAlias = true
            it.textSize = textSize
            it.textAlign = Paint.Align.CENTER
        }

        year = TimeUtils.getCurTimeString(SimpleDateFormat("yyyy", Locale.getDefault())).toInt()
        month = TimeUtils.getCurTimeString(SimpleDateFormat("MM", Locale.getDefault())).toInt()
        day = TimeUtils.getCurTimeString(SimpleDateFormat("dd", Locale.getDefault())).toInt()
        hr = TimeUtils.getCurTimeString(SimpleDateFormat("HH", Locale.getDefault())).toInt()
        min = TimeUtils.getCurTimeString(SimpleDateFormat("mm", Locale.getDefault())).toInt()
        sec = TimeUtils.getCurTimeString(SimpleDateFormat("ss", Locale.getDefault())).toInt()
        week = TimeUtils.getWeek()

        val cal = Calendar.getInstance()
        cal.set(year, month, 0)
        dayCount = cal.get(Calendar.DAY_OF_MONTH)

        Thread {
/*
            while (true) {
                try {
                    Thread.sleep(20)
                } catch (e: Exception) {
                    //
                }
                if (yuan != 360f) {
                    yuan++
                    postInvalidate()
                } else {
                    onTimeUpdate()
                }
            }
*/
            while (yuan != 360f) {
                try {
                    Thread.sleep(5)
                } catch (e: Exception) {
                    //
                }
                yuan++
                postInvalidate()
            }
            context.registerReceiver(TimeChangeReceiver(this), IntentFilter().also {
                it.addAction(Intent.ACTION_TIME_TICK)
                it.addAction(Intent.ACTION_DATE_CHANGED)
            })

            onTimeUpdate()
            while (sec <= 59) {
                try {
                    Thread.sleep(20)
                } catch (e: Exception) {
                    //
                }
                onSecondUpdate()
            }
            return@Thread
        }.start()
        setProp()
    }

    fun onStartThreadTimeUpdate() {
        Thread {
            onTimeUpdate()
            while (sec <= 59) {
                try {
                    Thread.sleep(20)
                } catch (e: Exception) {
                    //
                }
                onSecondUpdate()
            }
            try {
                Thread.currentThread().interrupt()
            } catch (e: Exception) {

            }
        }.start()
    }

    private fun onTimeUpdate() {
        hr = TimeUtils.getCurTimeString(SimpleDateFormat("HH", Locale.getDefault())).toInt()
        min = TimeUtils.getCurTimeString(SimpleDateFormat("mm", Locale.getDefault())).toInt()
        week = TimeUtils.getWeek()
        onSecondUpdate()
    }

    private fun onSecondUpdate() {
        sec = TimeUtils.getCurTimeString(SimpleDateFormat("ss", Locale.getDefault())).toInt()
        postInvalidate()
    }

    fun onDateUpdate() {
        year = TimeUtils.getCurTimeString(SimpleDateFormat("yyyy", Locale.getDefault())).toInt()
        month = TimeUtils.getCurTimeString(SimpleDateFormat("MM", Locale.getDefault())).toInt()
        day = TimeUtils.getCurTimeString(SimpleDateFormat("dd", Locale.getDefault())).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        try {
            if (bgColor != -1) {
                canvas.drawColor(bgColor)
            } else {
                canvas.drawBitmap(drawable2bitmap(background), 0f, 0f, paint)
            }
        } catch (e: Exception) {
            //
        }


        if (centX == 0f) centX = (width / 2).toFloat()
        if (centY == 0f) centY = (height / 2).toFloat()

        if (textSize == 0f) {
            setTextSize((if (centX > centY) centY else centX - textMargin * 6) / 25f)
        }

        //绘制年
        canvas.drawText(getYearAsString() + YEAR, centX, centY, paintSelect)

        val monthLen = paintSelect.measureText(getYearAsString() + YEAR) / 2 + centX + paintSelect.measureText(countTag[11] + MONTH) / 2 + textMargin

        //绘制月
        canvas.save()
        monthDegrees = getCircumference(12, month, monthDegrees)
        canvas.rotate(monthDegrees, centX, centY)
        onDrawContent(canvas, MONTH, 12, monthLen, centY, month - 1)
        canvas.restore()

        //绘制日
        val dayLen = monthLen + paintSelect.measureText(countTag[30] + DAY) / 2 + paintSelect.measureText(countTag[11] + MONTH) / 2 + textMargin
        canvas.save()
        dayDeg = getCircumference(dayCount, day, dayDeg)
        canvas.rotate(dayDeg, centX, centY)
        onDrawContent(canvas, DAY, dayCount, dayLen, centY, day - 1)
        canvas.restore()

        //绘制星期
        val weekLen = dayLen + paintSelect.measureText(countTag[30] + DAY) / 2 + paintSelect.measureText(WEEK + countTag[0]) / 2 + textMargin
        canvas.save()
        weekDeg = getCircumference(7, week, weekDeg)
        canvas.rotate(weekDeg, centX, centY)
        onDrawContent(canvas, WEEK, 7, weekLen, centY, week - 1)
        canvas.restore()

        //绘制时
        val hrLen = weekLen + paintSelect.measureText(countTag[23] + HOUR) / 2 + paintSelect.measureText(WEEK + countTag[0]) / 2 + textMargin
        canvas.save()
        hrDeg = getCircumference(24, hr, hrDeg)
        canvas.rotate(hrDeg, centX, centY)
        onDrawContent(canvas, HOUR, 24, hrLen, centY, hr - 1)
        canvas.restore()

        //绘制分
        val minLen = hrLen + paintSelect.measureText(countTag[23] + HOUR) / 2 + paintSelect.measureText(countTag[58] + MINUTE) / 2 + textMargin
        canvas.save()
        minDeg = getCircumference(60, min, minDeg)
        canvas.rotate(minDeg, centX, centY)
        onDrawContent(canvas, MINUTE, 60, minLen, centY, min - 1)
        canvas.restore()

        //绘制秒
        val secLen = minLen + paintSelect.measureText(countTag[58] + MINUTE) / 2 + paintSelect.measureText(countTag[58] + MINUTE) / 2 + textMargin
        canvas.save()
        secDeg = getCircumference(60, sec, secDeg)
        canvas.rotate(secDeg, centX, centY)
        onDrawContent(canvas, SECOND, 60, secLen, centY, sec - 1)
        canvas.restore()
    }

    private fun getYearAsString(): String {
        val stringBuilder = StringBuilder()
        year.toString().forEach {
            stringBuilder.append(
                when (it) {
                    '0' -> "零"
                    '1' -> "一"
                    '2' -> "二"
                    '3' -> "三"
                    '4' -> "四"
                    '5' -> "五"
                    '6' -> "六"
                    '7' -> "七"
                    '8' -> "八"
                    '9' -> "九"
                    else -> throw Exception("ERROR ON YEAR")
                }
            )
        }

        return stringBuilder.toString()
    }

    private fun getCircumference(number: Int, arrive: Int, currentCircumference: Float): Float {
        val cCir = -(360f / number) * (arrive - 1)
        return if (currentCircumference > cCir) currentCircumference - 1 else cCir
    }

    private fun onDrawContent(canvas: Canvas, company: String, count: Int, x: Float, y: Float, Selection: Int) {
        var selection = Selection
        canvas.save()
        if (selection == -1) selection = count - 1
        for (i in 0 until count) {
            var text = if (company.contains(WEEK)) company + countTag[i] else countTag[i] + company
            if (i == selection) {
                if (text == countTag[23] + HOUR) text = countTag[59] + HOUR
                canvas.drawText(text, x, y, paintSelect)
            } else {
                canvas.drawText(text, x, y, paint)
            }
            canvas.rotate(yuan / count, centX, centY)
        }
        canvas.restore()
    }

    //设置字体大小
    @Suppress("unused")
    fun setTextSize(textSize: Float) {
        this.textSize = textSize
        paint.textSize = textSize
        paintSelect.textSize = textSize
    }

    //设置不同单位之间的距离
    @Suppress("unused")
    fun setTextMargin(textMargin: Float) {
        this.textMargin = textMargin
    }

    // 设置选中颜色
    @Suppress("unused")
    fun setTextColorSelected(color: Int) {
        this.textColorSelected = color
        paintSelect.color = textColorSelected
        postInvalidate()
    }
    // 设置未选中颜色
    @Suppress("unused")
    fun setTextColorNotSelected(color: Int) {
        this.textColorNotSelected = color
        paint.color = color
        postInvalidate()
    }

    // 背景
    @Suppress("unused")
    fun setBackground(color: Int) {
        setBackgroundColor(color)
        postInvalidate()
    }
    @Suppress("unused")
    fun setBackground(bitmap: Bitmap) {
        background = BitmapDrawable(context.resources, bitmap)
        postInvalidate()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun setProp(unSelected: Int? = null, selected: Int? = null, bgColor: Int? = null, bgBitmap: Bitmap? = null, bgDrawable: Drawable? = null) {
        unSelected?.let {
            setTextColorNotSelected(it)
        }
        selected?.let {
            setTextColorSelected(it)
        }
        bgColor?.let {
            this.bgColor = it
            setBackground(it)
        }
        bgBitmap?.let {
            setBackground(it)
        }
        bgDrawable?.let {
            background = it
        }
    }

    internal class TimeChangeReceiver(private val compassClockView: CompassClockView): BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent!!.action == Intent.ACTION_TIME_TICK) {
                compassClockView.onDateUpdate()
                compassClockView.onStartThreadTimeUpdate()
            }
        }
    }

    internal class ImgUtils private constructor() {
        init {
            throw Exception("PrivateConstructorNotAllowedToInitialize")
        }

        companion object {
            fun drawable2bitmap(drawable: Drawable): Bitmap {
                return drawable.toBitmap()
            }
        }
    }

    internal class TimeUtils private constructor() {

        init {
            throw Exception("PrivateConstructorNotAllowedToInitialize")
        }

        companion object {
            @Volatile
            var DEFAULT_SDF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            private fun date2String(time: Date, format: SimpleDateFormat = DEFAULT_SDF): String {
                return format.format(time)
            }

            fun getCurTimeString(format: SimpleDateFormat): String {
                return date2String(Date(), format)
            }

            fun getWeek(): Int {
                return when (getCurTimeString(SimpleDateFormat("E", Locale.getDefault()))) {
                    "周一" -> 1
                    "周二" -> 2
                    "周三" -> 3
                    "周四" -> 4
                    "周五" -> 5
                    "周六" -> 6
                    "周日" -> 7
                    else -> 0
                }
            }
        }
    }
}