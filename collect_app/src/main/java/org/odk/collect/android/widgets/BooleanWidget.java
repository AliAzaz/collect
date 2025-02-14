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

package org.odk.collect.android.widgets;

import android.content.Context;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatCheckBox;

import org.javarosa.core.model.data.BooleanData;
import org.javarosa.core.model.data.IAnswerData;
import org.odk.collect.android.R;
import org.odk.collect.android.formentry.questions.QuestionDetails;
import org.odk.collect.android.utilities.ViewIds;

public class BooleanWidget extends QuestionWidget {

    private AppCompatCheckBox booleanButton;
    private final QuestionDetails questionDetails;

    public BooleanWidget(Context context, QuestionDetails questionDetails) {
        super(context, questionDetails);
        this.questionDetails = questionDetails;

        setupBooleanButton();
        readSavedAnswer();

        booleanButton.setOnClickListener(view -> {
                widgetValueChanged();
        });
    }

    @Override
    public void clearAnswer() {
        booleanButton.setChecked(false);
        widgetValueChanged();
    }

    @Override
    public IAnswerData getAnswer() {
        return new BooleanData(booleanButton.isChecked());
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        booleanButton.setOnLongClickListener(l);
    }

    @Override
    public void cancelLongPress() {
        super.cancelLongPress();
        booleanButton.cancelLongPress();
    }

    private void readSavedAnswer() {
        Boolean value = (Boolean) questionDetails.getPrompt().getAnswerValue().getValue();
        if (value != null) {
            booleanButton.setChecked(value);
        }
    }

    private void setupBooleanButton() {
        booleanButton = new AppCompatCheckBox(getContext());
        booleanButton.setId(ViewIds.generateViewId());
        booleanButton.setText(getContext().getString(R.string.trigger));
        booleanButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getAnswerFontSize());
        booleanButton.setEnabled(!questionDetails.getPrompt().isReadOnly());
        addAnswerView(booleanButton);
    }

    boolean isChecked() {
        return booleanButton.isChecked();
    }

    void isChecked(boolean isChecked) {
        booleanButton.setChecked(isChecked);
    }
}
