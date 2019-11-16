package com.example.kotlin.totoapp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG

class ToDoItemAdapter : BaseAdapter {
    private var items: List<ToDoItem>? = null
    private var model: ToDoItemListModel? = null

    private var inflater: LayoutInflater? = null
    private var resourcedId: Int = 0

    constructor(context: Context, resourcedId: Int, items: List<ToDoItem>, model: ToDoItemListModel) {
        this.inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.resourcedId = resourcedId

        this.items = items
        this.model = model
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = null
        if (convertView == null) {
            view = inflater!!.inflate(resourcedId, parent, false)
        } else {
            view = convertView
        }

        val item = items!![position]

        // テキスト
        val textTextView: TextView = view!!.findViewById(R.id.text)
        textTextView.text = item.text
        if (item.completed) {
            val paint = textTextView.paint
            paint.flags = textTextView.paintFlags or STRIKE_THRU_TEXT_FLAG
        }

        // チェック
        val checkBox: CheckBox = view.findViewById(R.id.button_check)
        checkBox.tag = item.id
        checkBox.isChecked = item.completed
        checkBox.setOnCheckedChangeListener { v, checked ->
            model!!.change(v.tag as Long, checked)
        }

        // 削除
        val deleteButton: Button = view.findViewById(R.id.button_delete)
        deleteButton.tag = item.id
        deleteButton.setOnClickListener {
            model!!.delete(it.tag as Long)
        }

        return view
    }

    override fun getCount(): Int {
        return items!!.size
    }

    override fun getItem(position: Int): Any {
        return items!![position]
    }

    override fun getItemId(position: Int): Long {
        return items!![position].id
    }
}
