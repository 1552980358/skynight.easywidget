@file:Suppress("unused")

package skynight.easywidget.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.ScrollView
import kotlinx.android.synthetic.main.item_waterfallcardview.view.*

import kotlinx.android.synthetic.main.view_waterfallscroll.view.*
import skynight.easywidget.R

import java.lang.Exception

/*
 * @File:   WaterfallScrollView
 * @Author: 1552980358
 * @Time:   4:26 PM
 * @Date:   29 Apr 2019
 * 
 */

class WaterfallScrollView : LinearLayout {

    private var linearLayoutMAX = 2 // default 2 LinearLayouts
    private var linearLayoutList = mutableListOf<LinearLayout>() // LinearLayout list

    private var viewCount = 0  // count added View

    private lateinit var bottomView: View

    constructor(context: Context) : this(context, attributeSet = null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        LayoutInflater.from(context).inflate(R.layout.view_waterfallscroll, this)

        if (attributeSet != null) {
            // When attributeSet isn't null
            // refers to /res/layouts/view/values/attrs.xml
            val typedArray =
                context.obtainStyledAttributes(attributeSet, R.styleable.WaterfallScrollView)
            linearLayoutMAX =
                typedArray.getInt(R.styleable.WaterfallScrollView_linearMax, 2) // Default 2
            setLinearLayoutMax(linearLayoutMAX)

            if (typedArray.hasValue(R.styleable.WaterfallScrollView_bottomView)) {
                bottomActionView.visibility = View.VISIBLE
                bottomView = LayoutInflater.from(context).inflate(
                        typedArray.getResourceId(
                            R.styleable.WaterfallScrollView_bottomView,
                            0 // whatever you want, that is reachless
                        ), null
                    )
                bottomActionView.addView(bottomView)
            }

            typedArray.recycle() // recycle
        } else {
            // When attributeSet is null
            // Create default layout
            setLinearLayoutMax(linearLayoutMAX)
        }
    }

    /*
     * Set and create specific number of LinearLayout
     *
     * @param Int
     * @return void
     *
     * */
    @Suppress("unused")
    fun setLinearLayoutMax(max: Int) {
        if (linearLayoutMAX != max) {
            linearLayoutMAX = max
        }
        // set to 0
        linearLayoutList = mutableListOf()
        root.removeAllViews()

        if (max > 0) {
            for (i: Int in 0 until max) {
                val linearLayout = LinearLayout(context)
                linearLayout.orientation = VERTICAL  // 直立
                val layoutParams = LayoutParams(0, WRAP_CONTENT)
                layoutParams.weight = 1f
                linearLayout.layoutParams = layoutParams
                linearLayoutList.add(linearLayout) // 添加
                root.addView(linearLayout)
            }
        }
    }

    /*
     * Get specific LinearLayout added
     *
     * @param Int
     * @return LinearLayout
     *
     * */
    @Suppress("unused")
    fun getLinearLayout(index: Int): LinearLayout {
        return linearLayoutList[index]
    }

    /*
     * Get list which contains all LinearLayout added
     *
     * @return MutableList<LinearLayout>
     *
     * */
    @Suppress("unused")
    fun getLinearLayoutList(): MutableList<LinearLayout> {
        return linearLayoutList
    }

    /*
     * Remove all views added to specific LinearLayout
     *
     * @param Int
     * @return void
     *
     * */
    @Suppress("unused")
    fun removeAllViewsInLayout(layoutIndex: Int) {
        linearLayoutList[layoutIndex].removeAllViews()
    }

    /*
     * Remove all views added to every LinearLayout
     *
     * @return void
     *
     * */
    override fun removeAllViews() {
        if (linearLayoutMAX > 0) {
            for (i: Int in 0 until linearLayoutMAX) {
                linearLayoutList[i].removeAllViews()
            }
        }
    }

    /*
     * Set support Bottom View, return this class for faster set up
     *
     * @return WaterfallScrollView
     *
     * */
    @Suppress("unused")
    fun setSupportBottomView(): WaterfallScrollView {
        bottomActionView.visibility = View.VISIBLE
        return this
    }

    /*
     * Set custom Bottom View
     *
     * @param View
     * @return void
     *
     * */
    @Suppress("unused")
    fun setBottomView(view: View) {
        bottomView = view
        bottomActionView.addView(bottomView)
    }

    /*
     * Get Bottom View
     *
     * @return View
     *
     * */
    @Suppress("unused")
    fun getBottomView(): View {
        if (::bottomView.isInitialized) {
            return bottomView
        } else {
            throw Exception("BottomViewNullException")
        }
    }

    /*
     * Move to top of the page
     *
     * @return void
     *
     * */
    @Suppress("unused")
    fun moveToTop() {
        scrollView.fullScroll(ScrollView.FOCUS_UP)
    }

    /*
     * Move to bottom of the page
     *
     * @return void
     *
     * */
    @Suppress("unused")
    fun moveToBottom() {
        scrollView.fullScroll(ScrollView.FOCUS_DOWN)
    }

    /*
     * Remove all LinearLayout added
     *
     * @return void
     *
     * */

    @Suppress("unused")
    fun removeAllLinearLayout() {
        setLinearLayoutMax(0)
    }

    /*
     * Add views to next LinearLayout automatically
     *
     * @param view
     * @return void
     *
     * */
    override fun addView(view: View) {
        linearLayoutList[viewCount % linearLayoutMAX].addView(view)
        viewCount++
    }

    class WaterfallCardViewItem : LinearLayout {
        constructor(context: Context) : this(context, attributeSet = null)
        constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
            LayoutInflater.from(context).inflate(R.layout.item_waterfallcardview, this)
            if (attributeSet != null) {

            }
        }

        /*
         * Set Title
         *
         * @param Int
         *
         * @param CharSequence
         *
         * return WaterfallCardViewItem
         *
         * */
        @Suppress("unused")
        fun setTitle(resId: Int): WaterfallCardViewItem {
            return this.setTitle(context.getString(resId))
        }
        @Suppress("MemberVisibilityCanBePrivate")
        fun setTitle(t: CharSequence): WaterfallCardViewItem {
            title.text = t
            return this
        }

        /*
         * Set Sub Title
         *
         * @param Int
         *
         * @param CharSequence
         *
         * return WaterfallCardViewItem
         *
         * */
        @Suppress("unused")
        fun setSubTitle(resId: Int): WaterfallCardViewItem {
            return this.setSubTitle(context.getString(resId))
        }
        @Suppress("MemberVisibilityCanBePrivate")
        fun setSubTitle(s: CharSequence): WaterfallCardViewItem {
            subTitle.text = s
            return this
        }

        /*
         * Hide title / sub title
         *
         * @return WaterfallCardViewItem
         * */
        fun hideTitle(): WaterfallCardViewItem {
            title.visibility = View.GONE
            return this
        }
        fun hideSubTitle(): WaterfallCardViewItem {
            subTitle.visibility = View.GONE
            return this
        }

        /*
         * Set image over text
         *
         * @param Drawable
         *
         * @param Int
         *
         * @param Bitmap
         *
         * @param String
         *
         * @param Icon (for SDK23+)
         *
         * @return WaterfallCardViewItem
         *
         * */
        fun setBanner(drawable: Drawable): WaterfallCardViewItem {
            banner.setImageDrawable(drawable)
            return this
        }
        fun setBanner(resId: Int): WaterfallCardViewItem {
            banner.setImageResource(resId)
            return this
        }
        fun setBanner(bitmap: Bitmap): WaterfallCardViewItem {
            banner.setImageBitmap(bitmap)
            return this
        }
        fun setBanner(uri: String): WaterfallCardViewItem {
            return this.setBanner(Uri.parse(uri))
        }
        @Suppress("MemberVisibilityCanBePrivate")
        fun setBanner(uri: Uri): WaterfallCardViewItem {
            banner.setImageURI(uri)
            return this
        }
        fun setBanner(icon: Icon): WaterfallCardViewItem {
            if (Build.VERSION.SDK_INT >= 23) {
                banner.setImageIcon(icon)
            } else {
                throw Exception("API23+ Required")
            }
            return this
        }

        /*
         * Set OnClickListener on item
         *
         * @param OnClickListener
         *
         * @return void
         *
         * */
        override fun setOnClickListener(l: OnClickListener) {
            itemRoot.setOnClickListener(l)
        }
    }
}