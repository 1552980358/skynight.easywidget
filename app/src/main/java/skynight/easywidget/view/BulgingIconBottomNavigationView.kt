package skynight.easywidget.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout

import kotlinx.android.synthetic.main.view_bulgingiconnavigation.view.*
import skynight.easywidget.R

/*
 * @File:   BulgingIconBottomNavigationView
 * @Author: 1552980358
 * @Time:   8:08 PM
 * @Date:   1 May 2019
 * 
 */

class BulgingIconBottomNavigationView : LinearLayout {

    // non-xml creating base, for xml-unwanted user
    @Suppress("unused", "SpellCheckingInspection")
    fun createBaseLayout(): View {
        /*
         * recommended as global variable for easy access
         *
         * private laterinit var view: RelativeLayout
         * private laterinit var radioGroup: RadioGroup
         * private laterinit var floatingActionButton: FloatingActionButton
         * private laterinit var divider:  View
         *
         * */

        // create root layout
        val view = RelativeLayout(context)
        view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        // set properties for RadioGroup
        val radioGroup = RadioGroup(context)
        radioGroup.orientation = HORIZONTAL
        val rgParams = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 50)
        rgParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        radioGroup.layoutParams = rgParams

        // set properties for FloatingActionButton
        val floatingActionButton = FloatingActionButton(context)
        val fabParams = RelativeLayout.LayoutParams(50, 50)
        fabParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        fabParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        fabParams.setMargins(0, 0, 0, 25) // float 25dp above bottom
        floatingActionButton.layoutParams = fabParams

        val divider = View(context)
        divider.background = ContextCompat.getDrawable(context, android.R.color.darker_gray)
        val divParams = RelativeLayout.LayoutParams(1, 50)
        divParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        divParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        divider.layoutParams = divParams

        // add to root
        view.addView(radioGroup)
        view.addView(floatingActionButton)
        view.addView(divider)

        return view
    }

    private var counter = 0
    private var idList = arrayListOf<Int>()

    constructor(context: Context) : this(context, attributeSet = null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        LayoutInflater.from(context).inflate(R.layout.view_bulgingiconnavigation, this)
        if (attributeSet != null) {
            val typedArray =
                context.obtainStyledAttributes(R.styleable.BulgingIconBottomNavigationView)
            if (typedArray.hasValue(R.styleable.BulgingIconBottomNavigationView_icon)) {
                floatingActionButton.setImageDrawable(typedArray.getDrawable(R.styleable.BulgingIconBottomNavigationView_icon))
            }
            if (typedArray.hasValue(R.styleable.BulgingIconBottomNavigationView_divide)) {
                divider.visibility = if (typedArray.getBoolean(
                        R.styleable.BulgingIconBottomNavigationView_divide, false
                    )
                ) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            if (typedArray.hasValue(R.styleable.BulgingIconBottomNavigationView_divideColor)) {
                divider.setBackgroundColor(
                    typedArray.getColor(
                        R.styleable.BulgingIconBottomNavigationView_divideColor,
                        ContextCompat.getColor(context, android.R.color.darker_gray)
                    )
                )
            }

            typedArray.recycle()
        }
    }

    @Suppress("unused")
    fun bindWithViewPager(viewPager: ViewPager) {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                findViewById<TabItem>(idList[p0]).isChecked = false
            }

            override fun onPageSelected(p0: Int) {
                findViewById<TabItem>(idList[p0]).isChecked = true
            }
        })
        setOnSelectTabItemListener(object : OnSelectTabItemListener {
            override fun onSelected(id: Int) {
                super.onSelected(id)
                viewPager.currentItem = idList.indexOf(id)
            }
        })
    }

    @Suppress("unused")
    fun setOnBulgingIconClickListener(l: OnClickListener) {
        floatingActionButton.setOnClickListener(l)
    }

    @Suppress("unused")
    fun setOnBulgingIconLongClickListener(l: OnLongClickListener) {
        floatingActionButton.setOnLongClickListener(l)
    }

    @Suppress("unused")
    fun setOnSelectTabItemListener(listener: OnSelectTabItemListener) {
        if (counter % 2 != 0) {
            throw Exception("NoOfTabItemShouldBeEvenBeforeCreateListener")
        }
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            listener.onSelected(checkedId)
        }
    }

    @Suppress("unused")
    fun addItem(tabItem: TabItem) {
        val id = View.generateViewId()
        tabItem.id = id
        idList.add(id)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        layoutParams.weight = 1f
        tabItem.layoutParams = layoutParams
        radioGroup.addView(tabItem)
        counter++
    }

    @Suppress("unused")
    fun setBulgingIcon(icon: Drawable): BulgingIconBottomNavigationView {
        floatingActionButton.setImageDrawable(icon)
        return this
    }

    @Suppress("unused")
    fun setBulgingIcon(icon: Bitmap): BulgingIconBottomNavigationView {
        floatingActionButton.setImageBitmap(icon)
        return this
    }

    @Suppress("unused")
    fun setBulgingIcon(uri: Uri): BulgingIconBottomNavigationView {
        floatingActionButton.setImageURI(uri)
        return this
    }

    @Suppress("unused")
    fun autoCreateRadioGroup(baseList: ArrayList<BaseInfo>) {
        for (i in baseList) {
            val tabItem = TabItem(context) // init

            i.getText()?.let {
                tabItem.text = it
            }
            i.getIconChecked()?.let {
                tabItem.setIconChecked(it)
            }
            i.getIconUnChecked()?.let {
                tabItem.setIconUnChecked(it)
            }
            // Customize
            if (i.getTextColorChecked() != -1) {
                tabItem.setTextCheckedColor(i.getTextColorChecked())
            }
            if (i.getTextColorUnChecked() != -1) {
                tabItem.setTextCheckedColor(i.getTextColorUnChecked())
            }

            addItem(tabItem)
        }
    }

    @Suppress("unused")
    class BaseInfo {
        private lateinit var iconChecked: Drawable
        private lateinit var iconUnChecked: Drawable
        private lateinit var text: String
        private var textColorChecked = -1
        private var textColorUnChecked = -1

        fun setVariables(
            text: String?,
            textColorChecked: Int?,
            textColorUnChecked: Int?,
            iconChecked: Drawable?,
            iconUnChecked: Drawable?
        ) {
            text?.let {
                this.text = it
            }
            textColorChecked?.let {
                this.textColorChecked = it
            }
            textColorUnChecked?.let {
                this.textColorUnChecked = it
            }
            iconChecked?.let {
                this.iconChecked = it
            }
            iconUnChecked?.let {
                this.iconUnChecked = it
            }
        }

        fun getText(): String? {
            return if (::text.isInitialized) {
                text
            } else {
                null
            }
        }

        fun getIconChecked(): Drawable? {
            return if (::iconChecked.isInitialized) {
                iconChecked
            } else {
                null
            }
        }

        fun getIconUnChecked(): Drawable? {
            return if (::iconUnChecked.isInitialized) {
                iconUnChecked
            } else {
                null
            }
        }

        fun getTextColorChecked(): Int {
            return textColorChecked
        }

        fun getTextColorUnChecked(): Int {
            return textColorUnChecked
        }
    }

    interface OnSelectTabItemListener {
        fun onSelected(id: Int) {
            // when select tab item
        }
    }

    class TabItem : RadioButton {
        private var textColorChecked = android.R.color.black            // default as black
        private var textColorUnChecked = android.R.color.darker_gray    // default as gray

        private lateinit var iconChecked: Drawable
        private lateinit var iconUnchecked: Drawable

        constructor(context: Context) : this(context, attributeSet = null)
        constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
            buttonDrawable = null // hide default button

            // applied in xml
            if (attributeSet != null) {
                val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TabItem)

                // text
                if (typedArray.hasValue(R.styleable.TabItem_title)) {
                    text = typedArray.getString(R.styleable.TabItem_title)
                }

                // text colors
                if (typedArray.hasValue(R.styleable.TabItem_textCheckedColor)) {
                    setTextCheckedColor(
                        typedArray.getColor(
                            R.styleable.TabItem_textCheckedColor,
                            ContextCompat.getColor(context, android.R.color.black)
                        )
                    )
                }
                if (typedArray.hasValue(R.styleable.TabItem_textUnCheckedColor)) {
                    setTextCheckedColor(
                        typedArray.getColor(
                            R.styleable.TabItem_textUnCheckedColor,
                            ContextCompat.getColor(context, android.R.color.darker_gray)
                        )
                    )
                }
                isChecked = false
                if (typedArray.hasValue(R.styleable.TabItem_isChecked)) {
                    isChecked = typedArray.getBoolean(R.styleable.TabItem_isChecked, false)
                }

                // icons
                if (typedArray.hasValue(R.styleable.TabItem_iconChecked)) {
                    setIconChecked(typedArray.getDrawable(R.styleable.TabItem_iconChecked)!!)
                }
                if (typedArray.hasValue(R.styleable.TabItem_iconUnChecked)) {
                    setIconUnChecked(typedArray.getDrawable(R.styleable.TabItem_iconUnChecked)!!)
                }

                typedArray.recycle() // recycle
            }

            // Listener for changing state
            setOnCheckedChangeListener { _, isChecked ->
                setTextColor(
                    if (isChecked) {
                        textColorChecked
                    } else {
                        textColorUnChecked
                    }
                )
                if (::iconChecked.isInitialized && ::iconChecked.isInitialized) {
                    setCompoundDrawables(
                        null, if (isChecked) {
                            iconChecked
                        } else {
                            iconUnchecked
                        }, null, null
                    )
                }
            }
        }

        @Suppress("unused")
        fun setTextColor(checked: Int?, unChecked: Int?): TabItem {
            if (checked != null) {
                textColorChecked = checked
            }
            if (unChecked != null) {
                textColorUnChecked = unChecked
            }
            return this
        }

        /*
         * Set color when checked
         *
         * @param Int
         *
         * @return TabItem
         *
         * */
        @Suppress("unused")
        fun setTextCheckedColor(color: Int): TabItem {
            textColorChecked = color
            return this
        }

        @Suppress("unused")
        fun setTextUnCheckedColor(color: Int): TabItem {
            textColorUnChecked = color
            return this
        }

        /*
         * Set color or text the same when both checked and unchecked
         * Color set to textColorUnchecked when color value is null by default
         *
         * @param Int?
         *
         * @return TabItem
         *
         * */
        @Suppress("unused")
        fun setTextColorTheSame(color: Int?): TabItem {
            if (color == null) {
                textColorChecked = textColorUnChecked
            } else {
                textColorChecked = color
                textColorUnChecked = color
            }
            return this
        }

        /*
         * Set current icon
         *
         * @param Drawable
         *
         * @param Bitmao
         *
         * @return TabItem
         *
         * */
        @Suppress("unused")
        fun setIcon(icon: Drawable): TabItem {
            setCompoundDrawables(null, icon, null, null)
            return this
        }

        @Suppress("unused")
        fun setIcon(bitmap: Bitmap): TabItem {
            return this.setIcon(BitmapDrawable(context.resources, bitmap))
        }

        /*
         * Set Icon when checked
         *
         * @param Drawable
         *
         * @param Bitmap
         *
         * @return TabItem
         *
         * */
        @Suppress("MemberVisibilityCanBePrivate")
        fun setIconChecked(icon: Drawable): TabItem {
            iconChecked = icon
            if (isChecked) {
                // auto apply when needed
                setCompoundDrawables(null, icon, null, null)
            }
            return this
        }

        @Suppress("unused")
        fun setIconChecked(bitmap: Bitmap): TabItem {
            return this.setIconChecked(BitmapDrawable(context.resources, bitmap))
        }

        /*
         * Set Icon when unchecked
         *
         * @param Drawable
         *
         * @param Bitmap
         *
         * @return TabItem
         *
         * */
        @Suppress("MemberVisibilityCanBePrivate")
        fun setIconUnChecked(icon: Drawable): TabItem {
            iconUnchecked = icon
            if (!isChecked) {
                // auto apply when needed
                setCompoundDrawables(null, icon, null, null)
            }
            return this
        }

        @Suppress("unused")
        fun setIconUnChecked(bitmap: Bitmap): TabItem {
            return this.setIconUnChecked(BitmapDrawable(context.resources, bitmap))
        }

        /*
         * Set icons for checked and unchecked
         *
         * @param Drawable
         * @param Drawable
         *
         * @return TabItem
         *
         * */
        @Suppress("unused")
        fun setIcons(checked: Drawable, unChecked: Drawable): TabItem {
            iconChecked = checked
            iconUnchecked = unChecked
            setCompoundDrawables(
                null, if (isChecked) {
                    checked
                } else {
                    unChecked
                }, null, null
            )
            return this
        }
    }

    @Suppress("unused")
    class FragmentViewPagerAdapter(
        fragmentManager: FragmentManager,
        private val fragmentList: ArrayList<Fragment>,
        private val titleList: ArrayList<String>
    ) : FragmentPagerAdapter(fragmentManager) {
        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(p0: Int): Fragment {
            return fragmentList[p0]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }

        override fun getItemPosition(`object`: Any): Int {
            return super.getItemPosition(`object`)
        }
    }
}