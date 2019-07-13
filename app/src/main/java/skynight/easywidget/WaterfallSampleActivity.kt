package skynight.easywidget

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_waterfallsample.*
import skynight.easywidget.view.WaterfallScrollView


/*
 * @File:   WaterfallSampleActivity
 * @Author: 1552980358
 * @Time:   12:47 PM
 * @Date:   1 May 2019
 * 
 */

class WaterfallSampleActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waterfallsample)
        setSupportActionBar(toolbar)

        Thread {
            // init waterfall
            val waterfallScrollView = WaterfallScrollView(this)


            for (i: Int in 0 until 7) {
                // add item to waterfall
                val item = WaterfallScrollView.WaterfallCardViewItem(this)
                item.setTitle("Title $i") // title
                    .setSubTitle("SubTitle $i") // subtitle
                    .setBanner(ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)!!) // banner
                    .setOnClickListener { runOnUiThread { Toast.makeText(this, "$i", LENGTH_SHORT).show() } }
                waterfallScrollView.addView(item)

                // no title & subtitle item
                val item2 = WaterfallScrollView.WaterfallCardViewItem(this)
                item2.hideTitle()
                    .hideSubTitle()
                    .setBanner(ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)!!)
                    .setOnClickListener { runOnUiThread { Toast.makeText(this, "NoTitle&SubTitle", LENGTH_SHORT).show() } }
                waterfallScrollView.addView(item2)
            }

            runOnUiThread { nestedScrollView.addView(waterfallScrollView) } // add to ui
        }.start()
    }
}
