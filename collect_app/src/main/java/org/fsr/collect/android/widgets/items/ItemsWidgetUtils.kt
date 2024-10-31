package org.fsr.collect.android.widgets.items

import org.javarosa.core.model.SelectChoice
import org.javarosa.form.api.FormEntryPrompt
import org.javarosa.xpath.parser.XPathSyntaxException
import org.fsr.collect.android.R
import org.fsr.collect.android.exception.ExternalDataException
import org.fsr.collect.android.widgets.QuestionWidget
import org.fsr.collect.android.widgets.interfaces.SelectChoiceLoader
import java.io.FileNotFoundException

object ItemsWidgetUtils {

    @JvmStatic
    fun loadItemsAndHandleErrors(
        widget: QuestionWidget,
        prompt: FormEntryPrompt,
        selectChoiceLoader: SelectChoiceLoader
    ): List<SelectChoice> {
        return try {
            selectChoiceLoader.loadSelectChoices(prompt)
        } catch (e: FileNotFoundException) {
            widget.showWarning(widget.context.getString(org.fsr.collect.strings.R.string.file_missing, e.message))
            emptyList()
        } catch (e: XPathSyntaxException) {
            widget.showWarning(widget.context.getString(org.fsr.collect.strings.R.string.parser_exception, e.message))
            emptyList()
        } catch (e: ExternalDataException) {
            widget.showWarning(e.message)
            emptyList()
        }
    }
}
