package com.example.kotlin.totoapp

class ToDoItemListModel {
    private val items: MutableList<ToDoItem> = mutableListOf()
    private var onUpdateListener: Runnable? = null

    fun add(item: ToDoItem) {
        val maxID = items.map { n -> n.id }.max()
        if (maxID != null) {
            item.id = maxID + 1
        } else {
            item.id = 0
        }
        items.add(item)
        notifyUpdated()
    }

    fun change(itemID: Long, completed: Boolean) {
        items.filter { n -> (n.id == itemID) }.forEach { n -> n.completed = completed }
        notifyUpdated()
    }

    fun delete(itemID: Long) {
        items.removeAll{ n -> (n.id == itemID)}
        notifyUpdated()
    }

    fun clearCompleted() {
        items.removeAll { n -> n.completed }
        notifyUpdated()
    }

    fun getItems() : List<ToDoItem> {
        return items.toList()
    }

    fun setOnUpdateListener(listener: Runnable) {
        onUpdateListener = listener
    }

    private fun notifyUpdated() {
        onUpdateListener?.run()
    }
}
