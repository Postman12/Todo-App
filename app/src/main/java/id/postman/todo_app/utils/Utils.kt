package id.postman.todo_app.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun hideKeyboard(activity: Activity) {
    val inputKeyboardManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val currentFocussedView = activity.currentFocus
    currentFocussedView?.let {
        inputKeyboardManager.hideSoftInputFromWindow(
            currentFocussedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}