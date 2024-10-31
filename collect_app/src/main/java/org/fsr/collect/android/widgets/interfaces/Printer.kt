package org.fsr.collect.android.widgets.interfaces

import android.content.Context
import org.fsr.collect.android.utilities.QuestionMediaManager
import org.fsr.collect.androidshared.livedata.NonNullLiveData

interface Printer {
    fun parseAndPrint(
        htmlDocument: String,
        questionMediaManager: QuestionMediaManager,
        context: Context
    )

    fun isLoading(): NonNullLiveData<Boolean>
}
