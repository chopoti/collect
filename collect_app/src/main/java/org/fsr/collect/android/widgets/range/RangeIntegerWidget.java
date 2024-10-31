/*
 * Copyright 2017 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fsr.collect.android.widgets.range;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.slider.Slider;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.IntegerData;
import org.javarosa.form.api.FormEntryPrompt;
import org.fsr.collect.android.formentry.questions.QuestionDetails;
import org.fsr.collect.android.views.TrackingTouchSlider;
import org.fsr.collect.android.widgets.QuestionWidget;
import org.fsr.collect.android.widgets.utilities.RangeWidgetUtils;

import java.math.BigDecimal;

@SuppressLint("ViewConstructor")
public class RangeIntegerWidget extends QuestionWidget implements Slider.OnChangeListener {
    TrackingTouchSlider slider;
    TextView currentValue;

    private ColorStateList defaultTickActiveTintList;
    private int defaultThumbWidth;
    private int defaultThumbTrackGapSize;

    public RangeIntegerWidget(Context context, QuestionDetails prompt) {
        super(context, prompt);
        render();
    }

    @Override
    protected View onCreateAnswerView(Context context, FormEntryPrompt prompt, int answerFontSize) {
        RangeWidgetUtils.RangeWidgetLayoutElements layoutElements = RangeWidgetUtils.setUpLayoutElements(context, prompt);
        slider = layoutElements.getSlider();
        currentValue = layoutElements.getCurrentValue();

        defaultTickActiveTintList = slider.getTickActiveTintList();
        defaultThumbWidth = slider.getThumbWidth();
        defaultThumbTrackGapSize = slider.getThumbTrackGapSize();

        setUpActualValueLabel(RangeWidgetUtils.setUpSlider(prompt, slider, true));

        if (slider.isEnabled()) {
            slider.addOnChangeListener(this);
        }
        return layoutElements.getAnswerView();
    }

    @Override
    public IAnswerData getAnswer() {
        String stringAnswer = currentValue.getText().toString();
        return stringAnswer.isEmpty() ? null : new IntegerData(Integer.parseInt(stringAnswer));
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
    }

    @Override
    public boolean shouldSuppressFlingGesture() {
        return slider.isTrackingTouch();
    }

    @Override
    public void clearAnswer() {
        setUpActualValueLabel(null);
        widgetValueChanged();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
        if (fromUser) {
            BigDecimal actualValue = RangeWidgetUtils.getActualValue(getFormEntryPrompt(), value);
            setUpActualValueLabel(actualValue);
            widgetValueChanged();
        }
    }

    private void setUpActualValueLabel(BigDecimal actualValue) {
        if (actualValue != null) {
            currentValue.setText(String.valueOf(actualValue.intValue()));
            slider.setTickActiveTintList(defaultTickActiveTintList);
            slider.setThumbWidth(defaultThumbWidth);
            slider.setThumbTrackGapSize(defaultThumbTrackGapSize);
        } else {
            slider.setValue(slider.getValueFrom());
            slider.setTickActiveTintList(ColorStateList.valueOf(MaterialColors.getColor(this, com.google.android.material.R.attr.colorPrimary)));
            slider.setThumbWidth(0);
            slider.setThumbTrackGapSize(0);
            currentValue.setText("");
        }
    }
}