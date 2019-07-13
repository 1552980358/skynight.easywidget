package skynight.easywidget

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_compassclockview.*

/**
 * @FILE:   CompassClockActivity
 * @AUTHOR: 1552980358
 * @DATE:   11 Jul 2019
 * @TIME:   5:40 PM
 **/

class CompassClockActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compassclockview)
        setSupportActionBar(toolbar)
        Color.BLACK
        compassClockView.setProp(bgColor = Color.RED)
        //compassClockView.setTextSize(14f)
    }
}