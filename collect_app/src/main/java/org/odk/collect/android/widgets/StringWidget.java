/*
 * Copyright (C) 2009 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.odk.collect.android.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Selection;
import android.widget.EditText;

import androidx.annotation.NonNull;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.StringData;
import org.odk.collect.android.activities.FormEntryActivity;
import org.odk.collect.android.formentry.questions.QuestionDetails;
import org.odk.collect.android.utilities.SoftKeyboardUtils;

/**
 * The most basic widget that allows for entry of any text.
 */
@SuppressLint("ViewConstructor")
public class StringWidget extends QuestionWidget {
    boolean readOnly;
    protected final EditText answerText;

    protected StringWidget(Context context, QuestionDetails questionDetails, boolean readOnlyOverride) {
        super(context, questionDetails);

        readOnly = questionDetails.getPrompt().isReadOnly() || readOnlyOverride;
        answerText = getAnswerEditText(readOnly, getFormEntryPrompt());
        setUpLayout();
    }

    protected void setUpLayout() {
        setDisplayValueFromModel();
        addAnswerView(answerText);
    }

    @Override
    public void clearAnswer() {
        answerText.setText(null);
    }

    @Override
    public IAnswerData getAnswer() {
        String s = getAnswerText();
        return !s.equals("") ? new StringData(s) : null;
    }

    @NonNull
    public String getAnswerText() {
        return answerText.getText().toString();
    }

    @Override
    public void setFocus(Context context) {
        if (!readOnly) {
            SoftKeyboardUtils.showSoftKeyboard(answerText);
            /*
             * If you do a multi-question screen after a "add another group" dialog, this won't
             * automatically pop up. It's an Android issue.
             *
             * That is, if I have an edit text in an activity, and pop a dialog, and in that
             * dialog's button's OnClick() I call edittext.requestFocus() and
             * showSoftInput(edittext, 0), showSoftinput() returns false. However, if the edittext
             * is focused before the dialog pops up, everything works fine. great.
             */
        } else {
            SoftKeyboardUtils.hideSoftKeyboard(answerText);
        }
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        answerText.setOnLongClickListener(l);
    }

    @Override
    public void cancelLongPress() {
        super.cancelLongPress();
        answerText.cancelLongPress();
    }

    /**
     * Registers all subviews except for the EditText to clear on long press. This makes it possible
     * to long-press to paste or perform other text editing functions.
     */
    @Override
    protected void registerToClearAnswerOnLongPress(FormEntryActivity activity) {
        for (int i = 0; i < getChildCount(); i++) {
            if (!(getChildAt(i) instanceof EditText)) {
                activity.registerForContextMenu(getChildAt(i));
            }
        }
    }

    public void setDisplayValueFromModel() {
        String currentAnswer = getFormEntryPrompt().getAnswerText();

        if (currentAnswer != null) {
            answerText.setText(currentAnswer);
            Selection.setSelection(answerText.getText(), answerText.getText().toString().length());
        }
    }
}
