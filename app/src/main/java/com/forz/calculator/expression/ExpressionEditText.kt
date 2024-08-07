package com.forz.calculator.expression

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.forz.calculator.utils.InsertInExpression
import com.forz.calculator.utils.NumberFormatter
import com.forz.calculator.settings.Config.decimalSeparatorSymbol
import com.forz.calculator.settings.Config.groupingSeparatorSymbol

class ExpressionEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    private var isUpdating = false

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)

        if (selStart < selEnd) {
            ExpressionViewModel.updateSelection(true)
        }else{
            if (InsertInExpression.stringAfterPosition(text.toString(), selStart).startsWith(groupingSeparatorSymbol)){
                setSelection(selStart + groupingSeparatorSymbol.length)
            }

            if (ExpressionViewModel.isSelected.value == true){
                ExpressionViewModel.updateSelection(false)
            }
        }
    }

    fun autoSizeTextExpressionEditText(exampleTextView: TextView){
        exampleTextView.text = text
        setTextSize(TypedValue.COMPLEX_UNIT_PX, exampleTextView.textSize)
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (!isUpdating) {
            isUpdating = true
            formatExpression(this, context)
        }
        isUpdating = false
    }

    private fun formatExpression(inputEditText: ExpressionEditText, context: Context){
        val string = inputEditText.text.toString()
        val cursorPositionStart = inputEditText.selectionStart

        val newText = NumberFormatter.formatExpression(
            string,
            groupingSeparatorSymbol,
            decimalSeparatorSymbol
        )

        val lengthDiff = newText.length - string.length
        val newSelection = if (cursorPositionStart + lengthDiff > 0) cursorPositionStart + lengthDiff else 0

        val colorText = NumberFormatter.changeColorOperators(newText, context)

        inputEditText.text = colorText
        inputEditText.setSelection(newSelection)
    }
}