package org.fsr.collect.androidshared.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import org.fsr.collect.shared.strings.StringUtils

class OneSignTextWatcher(private val editText: EditText) : TextWatcher {
    lateinit var oldTextString: String

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        oldTextString = s.toString()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(editable: Editable?) {
        editable.toString().let {
            if (it != oldTextString) {
                val trimmedString = StringUtils.firstCharacterOrEmoji(it)
                editText.setText(trimmedString)
                editText.setSelection(trimmedString.length)
            }
        }
    }
}
