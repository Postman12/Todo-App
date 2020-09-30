package id.postman.todo_app.fragments

import android.os.Build
import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.postman.todo_app.R
import id.postman.todo_app.data.model.Priority
import id.postman.todo_app.data.model.TodoData
import id.postman.todo_app.fragments.list.ListFragmentDirections

class BindingAdapters {

    companion object {
        @BindingAdapter("android:navigationToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:parsePriorityToInt")
        @JvmStatic
        fun parsePriorityToInt(view: Spinner, priority: Priority) {
            when (priority) {
                Priority.LOW -> {
                    view.setSelection(0)
                }
                Priority.MEDIUM -> {
                    view.setSelection(1)
                }
                Priority.HIGH -> {
                    view.setSelection(2)
                }
            }
        }

        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(view: CardView, priority: Priority) {
            when (priority) {
                Priority.LOW -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        view.setCardBackgroundColor(view.context.getColor(R.color.green))
                    } else {
                        view.setCardBackgroundColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }

                Priority.MEDIUM -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        view.setCardBackgroundColor(view.context.getColor(R.color.yellow))
                    } else {
                        view.setCardBackgroundColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.yellow
                            )
                        )
                    }
                }

                Priority.HIGH -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        view.setCardBackgroundColor(view.context.getColor(R.color.red))
                    } else {
                        view.setCardBackgroundColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.red
                            )
                        )
                    }
                }
            }
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view: ConstraintLayout, item: TodoData) {
            view.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(item)
                view.findNavController().navigate(action)
            }
        }

    }
}