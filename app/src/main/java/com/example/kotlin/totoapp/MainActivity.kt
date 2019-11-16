package com.example.kotlin.totoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*

class MainActivity : AppCompatActivity() {
    val model: ToDoItemListModel = ToDoItemListModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model.setOnUpdateListener(Runnable {
            updateList()
        })

        val textEditText: EditText = findViewById(R.id.text_item)
        textEditText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    add()
                    return@setOnEditorActionListener true
                }
                else -> false
            }
        }

        val filtersRadioGroup: RadioGroup = findViewById(R.id.radio_group_filters)
        filtersRadioGroup.setOnCheckedChangeListener { _, _ ->
            updateList()
        }

        val clearCompletedButton: Button = findViewById(R.id.button_clear_completed)
        clearCompletedButton.setOnClickListener {
            clearCompleted()
        }

        updateList()
    }

    private fun add() {
        val textEditText: EditText = findViewById(R.id.text_item)
        val text = textEditText.text.toString()
        if (text.isEmpty()) {
            return
        }

        val item = ToDoItem()
        item.text = text
        model.add(item)
    }

    fun clearCompleted() {
        model.clearCompleted()
    }

    fun updateList() {
        val items = model.getItems()

        // 項目数
        val numberOfItemsTextView: TextView = findViewById(R.id.text_number_of_items)
        if (items.isEmpty()) {
            numberOfItemsTextView.visibility = View.INVISIBLE
        } else {
            numberOfItemsTextView.visibility = View.VISIBLE
            numberOfItemsTextView.text = String.format(resources.getString(R.string.number_of_items), items.size)
        }

        // 完了済み
        val clearCompletedButton: Button = findViewById(R.id.button_clear_completed)
        if (items.filter { it.completed }.count() > 0) {
            clearCompletedButton.visibility = View.VISIBLE
        } else {
            clearCompletedButton.visibility = View.INVISIBLE
        }

        // フィルター
        val filtersRadioGroup: RadioGroup = findViewById(R.id.radio_group_filters)
        if (items.isEmpty()) {
            filtersRadioGroup.visibility = View.INVISIBLE
        } else {
            filtersRadioGroup.visibility = View.VISIBLE
        }

        // リスト
        val itemsToList = when (filtersRadioGroup.checkedRadioButtonId) {
            R.id.filter_active -> items.filter { !it.completed }
            R.id.filter_completed -> items.filter { it.completed }
            else -> items
        }
        val listView: ListView = findViewById(R.id.list_item)
        listView.adapter = ToDoItemAdapter(applicationContext, R.layout.item, itemsToList, model)
    }
}
