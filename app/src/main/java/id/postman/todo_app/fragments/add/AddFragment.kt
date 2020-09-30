package id.postman.todo_app.fragments.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import id.postman.todo_app.R
import id.postman.todo_app.data.model.TodoData
import id.postman.todo_app.data.viewmodel.SharedViewModel
import id.postman.todo_app.data.viewmodel.TodoViewModel
import id.postman.todo_app.utils.ToastDialog
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment : Fragment() {

    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add, container, false)

        setHasOptionsMenu(true)

        view.spinner_priority.onItemSelectedListener = mSharedViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDB()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDB() {
        val title = et_title.text.toString()
        val description = et_description.text.toString()
        val priority = spinner_priority.selectedItem.toString()

        val validation = mSharedViewModel.validateData(title, description)
        if (validation) {
            val todoData = TodoData(
                0,
                title,
                description,
                mSharedViewModel.parsePriority(priority)
            )
            mTodoViewModel.insertData(todoData)
            ToastDialog.setShortToast(requireContext(), getString(R.string.add_success))
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            et_title.error = getString(R.string.error_title_empty_fields)
            et_description.error = getString(R.string.error_title_empty_fields)
            ToastDialog.setShortToast(
                requireContext(),
                getString(R.string.error_title_empty_fields)
            )
        }
    }
}