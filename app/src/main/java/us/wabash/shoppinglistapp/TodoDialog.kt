package us.wabash.shoppinglistapp



import android.app.Dialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import us.wabash.shoppinglistapp.data.Todo
import kotlinx.android.synthetic.main.new_todo_dialog.view.*
import android.content.Context as AndroidContentContext


class TodoDialog : DialogFragment() {

    interface TodoHandler {
        fun todoCreated(item: Todo)
        fun todoUpdated(item: Todo)
    }




    private lateinit var todoHandler: TodoHandler
    private lateinit var itemPrice: EditText
    private lateinit var itemName: EditText
    private lateinit var itemDesc: EditText
    private lateinit var itemIsPurchased: Switch
    private lateinit var spinner : Spinner





    var itemCategory = 1

    var isEditMode = false



    override fun onAttach(context: AndroidContentContext?) {
        super.onAttach(context)

        if (context is TodoHandler) {
            todoHandler = context
        } else {
            throw RuntimeException("The activity does not implement the TodoHandlerInterface")
        }
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        createDialogBuilder(builder)

        createArrayAdapter()

        createDialogBuilderEditMode(builder)

        builder.setPositiveButton(getString(R.string.add)) {
                dialog, witch -> // empty

        }



        return builder.create()
    }

    private fun createDialogBuilderEditMode(builder: AlertDialog.Builder) {



        isEditMode = ((arguments != null) && arguments!!.containsKey(
            ScrollingActivity.KEY_TODO
        ))

        if (isEditMode) {

            builder.setTitle("Edit")



            var todo: Todo = (arguments?.getSerializable(ScrollingActivity.KEY_TODO) as Todo)

            itemName.setText(todo.todoText)
            itemPrice.setText(todo.createDate)
            itemIsPurchased.isChecked = todo.done
            spinner.setSelection(todo.category-1)
            itemDesc.setText(todo.txtDesc)

        }
    }


    private fun createArrayAdapter() {
        activity?.let {
            ArrayAdapter.createFromResource(it, R.array.items, android.R.layout.simple_spinner_item)
                .also {
                        arrayAdapter ->
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner!!.setAdapter(arrayAdapter)

                    spinner.onItemSelectedListener
                }
        }
    }



    private fun createDialogBuilder(builder: AlertDialog.Builder) {

        builder.setTitle(getString(R.string.add_new_item))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_todo_dialog, null
        )

        itemPrice = rootView.editTextPrice
        itemName = rootView.editTextName
        itemIsPurchased = rootView.switch1
        spinner = rootView.spinnerCategory
        itemDesc = rootView.etDescriptionDialog

        builder.setView(rootView)
    }



    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (itemName.text.isNotEmpty() && itemPrice.text.isNotEmpty()) {


                if (isEditMode) {
                    handleTodoEdit()
                } else {
                    handleShoppingListCreate()
                }
                dialog.dismiss()

            } else {
                if (itemName.text.isEmpty()){ itemName.error = getString(R.string.this_field_can_not_be_empty)}
                if (itemPrice.text.isEmpty()){ itemPrice.error = getString(R.string.this_field_can_not_be_empty)}
            }

        }

    }


    private fun handleShoppingListCreate() {

        getCategoryPositionNumber()


        todoHandler.todoCreated(
            Todo(
                null,
                "$"+itemPrice.text.toString(),
                itemName.text.toString(),
                itemIsPurchased.isChecked,
                itemCategory,
                itemDesc.text.toString()
            )
        )
    }


    private fun getCategoryPositionNumber() {
        itemCategory = spinner.getSelectedItemPosition() + 1
    }




    private fun handleTodoEdit() {

        getCategoryPositionNumber()

        val todoToEdit = arguments?.getSerializable(ScrollingActivity.KEY_TODO) as Todo

        todoToEdit.todoText = itemName.text.toString()
        todoToEdit.createDate = itemPrice.text.toString()
        todoToEdit.txtDesc = itemDesc.text.toString()
        todoToEdit.category = itemCategory
        todoToEdit.done = itemIsPurchased.isChecked

        todoHandler.todoUpdated(todoToEdit)
    }
}



