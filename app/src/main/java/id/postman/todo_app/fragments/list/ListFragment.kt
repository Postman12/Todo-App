package id.postman.todo_app.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import id.postman.todo_app.R
import id.postman.todo_app.data.model.TodoData
import id.postman.todo_app.data.viewmodel.SharedViewModel
import id.postman.todo_app.data.viewmodel.TodoViewModel
import id.postman.todo_app.databinding.FragmentListBinding
import id.postman.todo_app.fragments.list.adapter.ListAdapter
import id.postman.todo_app.fragments.list.adapter.SwipeToDelete
import id.postman.todo_app.utils.ToastDialog
import id.postman.todo_app.utils.hideKeyboard
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val listAdapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        setUpRecyclerView()

        mTodoViewModel.getAllData.observe(viewLifecycleOwner, { data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            listAdapter.setData(data)
        })

        //set menu
        setHasOptionsMenu(true)

        hideKeyboard(requireActivity())
        return binding.root
    }

    private fun setUpRecyclerView() {
        val recycleView = binding.rvList
        recycleView.adapter = listAdapter
        recycleView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recycleView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
        swipeToDelete(recycleView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = listAdapter.dataList[viewHolder.adapterPosition]
                mTodoViewModel.deleteData(deletedItem)
                listAdapter.notifyItemRemoved(viewHolder.adapterPosition)

                restoreToDelete(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreToDelete(view: View, deletedItem: TodoData) {
        val snackBar = Snackbar.make(view, "Deleted ${deletedItem.title}", Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo") {
            mTodoViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_fragment, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isQueryRefinementEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> mTodoViewModel.sortByHighPriority.observe(
                this, { listAdapter.setData(it) })
            R.id.menu_priority_low -> mTodoViewModel.sortByLowPriority.observe(
                this,
                { listAdapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchDatabase(newText)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        mTodoViewModel.searchData(searchQuery).observe(this, { listData ->
            listData?.let {
                listAdapter.setData(it)
            }
        })
    }

    private fun confirmRemoval() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setPositiveButton(getString(R.string.yes_text)) { _, _ ->
            mTodoViewModel.deleteAll()
            ToastDialog.setShortToast(requireContext(), "All deleted")
        }
        alertDialogBuilder.setNegativeButton(getString(R.string.no_text)) { _, _ -> }
        alertDialogBuilder.setTitle("Delete everything ?")
        alertDialogBuilder.setMessage("Are sure you want to remove it ?")
        alertDialogBuilder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}