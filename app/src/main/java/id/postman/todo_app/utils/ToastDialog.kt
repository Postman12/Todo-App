package id.postman.todo_app.utils

import android.content.Context
import android.widget.Toast

class ToastDialog {

    companion object {
        fun setShortToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun setLongToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}