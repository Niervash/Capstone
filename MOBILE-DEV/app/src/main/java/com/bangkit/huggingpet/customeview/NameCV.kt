package com.bangkit.huggingpet.customeview

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.bangkit.huggingpet.R

class NameCV : AppCompatEditText, View.OnFocusChangeListener {

    var isNameValid = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {

        inputType = InputType.TYPE_CLASS_TEXT

        onFocusChangeListener = this
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            validateName()
        }
    }

    private fun validateName() {
        isNameValid = text.toString().trim().isNotEmpty()
        error = if (!isNameValid) {
            resources.getString(R.string.nameNone)
        } else {
            null

        }
    }
}
