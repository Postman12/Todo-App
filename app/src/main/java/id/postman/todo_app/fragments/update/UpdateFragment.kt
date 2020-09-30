package id.postman.todo_app.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import id.postman.todo_app.R
import id.postman.todo_app.data.model.TodoData
import id.postman.todo_app.data.viewmodel.SharedViewModel
import id.postman.todo_app.data.viewmodel.TodoViewModel
import id.postman.todo_app.databinding.FragmentUpdateBinding
import id.postman.todo_app.utils.ToastDialog
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment() {

    private val mSharedViewmModel: SharedViewModel by viewModels()
    private val mTodoViewModel: TodoViewModel by viewModels()
    private val args by navArgs<UpdateFragmentArgs>()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args

        binding.currentSpinnerPriority.onItemSelectedListener = mSharedViewmModel.listener

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_update_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateITem()
            R.id.menu_delete -> confirmItemRemove()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateITem() {
        val getTitle = current_et_title.text.toString()
        val getDescription = current_et_description.text.toString()
        val getPriority = current_spinner_priority.selectedItem.toString()

        val validation = mSharedViewmModel.validateData(getTitle, getDescription)
        if (validation) {
            val updateData = TodoData(
                id = args.currentItem.id,
                title = getTitle,
                description = getDescription,
                priority = mSharedViewmModel.parsePriority(getPriority)
            )
            mTodoViewModel.updateData(updateData)
            ToastDialog.setShortToast(requireContext(), getString(R.string.update_success))
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            et_title.error = getString(R.string.error_title_empty_fields)
            et_description.error = getString(R.string.error_title_empty_fields)
            ToastDialog.setShortToast(
                requireContext(),
                getString(R.string.error_title_empty_fields)
            )
        }
    }

    private fun confirmItemRemove() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setPositiveButton(getString(R.string.yes_text)) { _, _ ->
            mTodoViewModel.deleteData(args.currentItem)
            ToastDialog.setShortToast(requireContext(), "${args.currentItem.title} deleted")
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        alertDialogBuilder.setNegativeButton(getString(R.string.no_text)) { _, _ -> }
        alertDialogBuilder.setTitle("Delete ${args.currentItem.title} ?")
        alertDialogBuilder.setMessage("Are sure you want to remove it ?")
        alertDialogBuilder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}