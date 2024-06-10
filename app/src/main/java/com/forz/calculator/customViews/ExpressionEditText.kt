package com.forz.calculator.customViews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.forz.calculator.StateViews.expressionEditTextIsRecreated
import com.forz.calculator.viewModels.ExpressionViewModel

class ExpressionEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    private var counter = 0

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (expressionEditTextIsRecreated){
            counter++
        }

        if (selEnd >= selStart && (counter >= 6 || !expressionEditTextIsRecreated)) {
            counter = 0
            expressionEditTextIsRecreated = false

            if ((selStart != ExpressionViewModel.expressionCursorPositionStart.value!! || selEnd != ExpressionViewModel.expressionCursorPositionEnd.value!!) && !ExpressionViewModel.isExpressionInserted) {
                ExpressionViewModel.updateCursorPosition(selStart, selEnd)
            }
            if (selStart == ExpressionViewModel.numberOfCharactersOfInsertedText && ExpressionViewModel.isExpressionInserted) {
                ExpressionViewModel.updateIsExpressionInserted(false)
            }
        }
    }
}