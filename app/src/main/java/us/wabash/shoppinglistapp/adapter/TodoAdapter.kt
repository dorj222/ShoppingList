package us.wabash.shoppinglistapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import us.wabash.shoppinglistapp.R
import us.wabash.shoppinglistapp.ScrollingActivity
import us.wabash.shoppinglistapp.data.AppDatabase
import us.wabash.shoppinglistapp.data.Todo
import us.wabash.shoppinglistapp.touch.TodoTouchHelperCallback
import kotlinx.android.synthetic.main.todo_row.view.*
import kotlinx.android.synthetic.main.todo_row.view.editTextName
import kotlinx.android.synthetic.main.todo_row.view.editTextPrice
import kotlinx.android.synthetic.main.todo_row.view.btnTextDesc
import java.util.*




class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>, TodoTouchHelperCallback {

    var todoList = mutableListOf<Todo>()

    val context: Context
    constructor(context: Context, todos: List<Todo>){
        this.context = context

        todoList.addAll(todos)

        //for (i in 0..20){
        //    todoList.add(Todo("2019", "Todo $i", false))
        //}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val todoRow = LayoutInflater.from(context).inflate(
            R.layout.todo_row, parent, false
        )
        return ViewHolder(todoRow)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var todo = todoList.get(holder.adapterPosition)

        holder.editName.text = todo.todoText
        holder.editPrice.text = todo.createDate
        holder.editDesc.text = todo.txtDesc

        holder.switchBool.isChecked = todo.done


        if (todo.category == 1) {
            holder.ivCategory.setImageResource(R.drawable.other)
        } else if (todo.category == 2) {
            holder.ivCategory.setImageResource(R.drawable.cloth)
        } else if (todo.category == 3) {
            holder.ivCategory.setImageResource(R.drawable.electronics)
        } else if (todo.category == 4) {
            holder.ivCategory.setImageResource(R.drawable.schoolsupply)
        } else if (todo.category == 5) {
            holder.ivCategory.setImageResource(R.drawable.food)
        } else if (todo.category == 6) {
            holder.ivCategory.setImageResource(R.drawable.videogames)
        } else if (todo.category == 7) {
            holder.ivCategory.setImageResource(R.drawable.cosmetics)
        }



        holder.btnDelete.setOnClickListener {
            deleteTodo(holder.adapterPosition)
        }

        holder.switchBool.setOnClickListener {
            todo.done = holder.switchBool.isChecked
            updateTodo(todo)
        }

        holder.btnEdit.setOnClickListener {
            (context as ScrollingActivity).showEditTodoDialog(
                todo, holder.adapterPosition
            )
        }

        holder.btnDescription.setOnClickListener{

            
            if(!(holder.editDesc.VISIBLE())){
                holder.editDesc.setVisible(true)
            }

            else if((holder.editDesc.VISIBLE())){
                holder.editDesc.setVisible(false)
            }

        }



    }

    fun updateTodo(todo: Todo) {
        Thread {
            AppDatabase.getInstance(context).todoDao().updateTodo(todo)
        }.start()
    }

    fun updateTodoOnPosition(todo: Todo, index: Int) {
        todoList.set(index, todo)
        notifyItemChanged(index)
    }


    fun deleteTodo(index: Int){
        Thread{
            AppDatabase.getInstance(context).todoDao().deleteTodo(todoList[index])

            (context as ScrollingActivity).runOnUiThread {
                todoList.removeAt(index)
                notifyItemRemoved(index)
            }
        }.start()
    }

    fun deleteAllTodos() {
        Thread {
            AppDatabase.getInstance(context).todoDao().deleteAllTodo()

            (context as ScrollingActivity).runOnUiThread {
                todoList.clear()
                notifyDataSetChanged()
            }
        }.start()
    }

    fun addTodo(todo: Todo) {
        todoList.add(todo)
        notifyItemInserted(todoList.lastIndex)
    }


    override fun onDismissed(position: Int) {
        deleteTodo(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(todoList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val editPrice = itemView.editTextPrice
        val editName = itemView.editTextName
        val editDesc = itemView.btnTextDesc
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
        val ivCategory = itemView.ivIcon
        val switchBool = itemView.switchPurchased
        val btnDescription = itemView.btnDescrption
    }

    private fun View.VISIBLE(): Boolean{
        return visibility == View.VISIBLE
    }

    fun View.setVisible(visible: Boolean) {
        visibility = if (visible) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

}





