package com.ervalsa.storyapp.views

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText

class CustomEmailEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleSet: Int) : super(context, attrs, defStyleSet) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun init() {
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do Nothing
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (sequence != null) {
                    if (sequence.length > 0 && !Patterns.EMAIL_ADDRESS.matcher(sequence).matches()) {
                        error = "Format email tidak sesuai"
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // Do Nothing
            }

        })
    }
}