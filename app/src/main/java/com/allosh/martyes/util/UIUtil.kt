package com.allosh.martyes.util


import android.app.Activity
import android.app.Service
import android.content.Context
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import com.allosh.martyes.R


object UIUtil {
    fun closeKeyboard(activity: Activity) {
        val inputMethodManager = activity
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showShortToast(@StringRes stringId: Int, context: Context) {
        showToast(stringId, Toast.LENGTH_SHORT, context)
    }

    fun showLongToast(@StringRes stringId: Int, context: Context) {
        showToast(stringId, Toast.LENGTH_LONG, context)
    }

    private fun showToast(@StringRes stringId: Int, duration: Int, context: Context) {
        Toast.makeText(context, stringId, duration).show()
    }

    fun showShortToast(text: String, context: Context) {
        showToast(text, Toast.LENGTH_SHORT, context)
    }

    fun showLongToast(text: String, context: Context) {
        showToast(text, Toast.LENGTH_LONG, context)
    }

    private fun showToast(text: String, duration: Int, context: Context) {
        Toast.makeText(context, text, duration).show()
    }

    fun EditTextsFilled(editTexts: Array<EditText>, context: Context): Boolean {
        for (editText in editTexts) {
            if (editText.text.toString().trim { it <= ' ' } == "") {
                editText.error = context.getString(R.string.field_required)
                return false
            }
        }
        return true
    }

    fun setOnClickListeners(views: Array<View>, onClickListener: View.OnClickListener?) {
        for (view in views) {
            view.setOnClickListener(onClickListener)
        }
    }
}
