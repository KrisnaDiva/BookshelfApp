package com.krisna.diva.bookshelfapi.utils

import android.widget.EditText

object FormValidator {
    fun validateEditTexts(vararg editTexts: EditText): Boolean {
        var allFieldsFilled = true
        for (editText in editTexts) {
            if (editText.text.toString().isEmpty()) {
                editText.error = "This field cannot be empty"
                allFieldsFilled = false
            }
        }
        return allFieldsFilled
    }
}