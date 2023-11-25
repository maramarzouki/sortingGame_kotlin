package tn.m1pdam.sortinggame

import android.app.Dialog
import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var i1: TextView;
    lateinit var i2: TextView;
    lateinit var i3: TextView;
    lateinit var i4: TextView;
    lateinit var i5: TextView;

    lateinit var valider: Button;

    lateinit var Layout1: LinearLayout;
    lateinit var Layout2: LinearLayout

    val n1 = (0..9999).random();
    val n2 = (0..9999).random();
    val n3 = (0..9999).random();
    val n4 = (0..9999).random();
    val n5 = (0..9999).random();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        i1 = findViewById(R.id.i1);
        i2 = findViewById(R.id.i2);
        i3 = findViewById(R.id.i3);
        i4 = findViewById(R.id.i4);
        i5 = findViewById(R.id.i5);

        valider = findViewById(R.id.validerBtn);

        Layout1 = findViewById(R.id.view1);
        Layout2 = findViewById(R.id.view2);

        i1.text = n1.toString()
        i2.text = n2.toString()
        i3.text = n3.toString()
        i4.text = n4.toString()
        i5.text = n5.toString()

        i1.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                moveNumber(i1);
            }
        })

        i2.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                moveNumber(i2);
            }
        })

        i3.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                moveNumber(i3);
            }
        })

        i4.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                moveNumber(i4);
            }
        })

        i5.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                moveNumber(i5);
            }
        })

        valider.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                val allTextViewsInLayout2 = areAllNumbersInLayout2()

                if (allTextViewsInLayout2) {
                    val orderedValues = getOrderedValuesFromLayout(Layout2)
                    val isAscending = isAscendingOrderTrue(orderedValues)
                    if (isAscending) {
                        showDial()
                    } else {
                        Toast.makeText(this@MainActivity, "Try again!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "You didn't move all of the numbers!", Toast.LENGTH_LONG).show()
                }
            }
        })

        setDragListeners()
    }

    abstract class DoubleClickListener : View.OnClickListener {
        var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(v)
            }
            lastClickTime = clickTime
        }

        abstract fun onDoubleClick(v: View?)

        companion object {
            private const val DOUBLE_CLICK_TIME_DELTA: Long = 300
        }
    }

    private fun moveNumber(number: TextView) {
        if (number.parent === Layout1) {
            Layout1.removeView(number)
            Layout2.addView(number)
        } else if (number.parent === Layout2) {
            Layout2.removeView(number)
            Layout1.addView(number)
        }
    }

    private fun getOrderedValuesFromLayout(layout: ViewGroup): List<Int> {
        val values = mutableListOf<Int>()

        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            if (child is TextView) {
                val value = child.text.toString().toIntOrNull()
                value?.let { values.add(it) }
            }
        }

        return values
    }

    private fun isAscendingOrderTrue(values: List<Int>): Boolean {
        return values == values.sorted()
    }

    private fun areAllNumbersInLayout2(): Boolean {
        return Layout1.childCount == 0
    }

    private fun showDial() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog)

        val dialBtn: Button = dialog.findViewById(R.id.dialogBTN)


        dialBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialog.dismiss()
                finish();
                startActivity(getIntent());
            }
        })

        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }


    private fun setDragListeners() {
        val dragListener = View.OnLongClickListener { v ->
            val item = ClipData.newPlainText("", "")
            val shadow = View.DragShadowBuilder(v)
            v.startDragAndDrop(item, shadow, v, 0)
        }

        val dropListener = View.OnDragListener { v, event ->
            val draggedView = event.localState as TextView

            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    val owner = draggedView.parent as ViewGroup
                    owner.removeView(draggedView)
                    val container = v as ViewGroup
                    container.addView(draggedView)
                    draggedView.visibility = View.VISIBLE
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    if (!event.result) {
                        draggedView.visibility = View.VISIBLE
                    }
                    true
                }
                else -> false
            }
        }

        setDragListenerForView(i1, dragListener)
        setDragListenerForView(i2, dragListener)
        setDragListenerForView(i3, dragListener)
        setDragListenerForView(i4, dragListener)
        setDragListenerForView(i5, dragListener)

        Layout1.setOnDragListener(dropListener)
        Layout2.setOnDragListener(dropListener)
    }

    private fun setDragListenerForView(view: View, listener: View.OnLongClickListener) {
        view.setOnLongClickListener(listener)
    }


}


