package com.forz.calculator.utils

import com.forz.calculator.expression.ExpressionEditText
import com.forz.calculator.settings.Config.groupingSeparatorSymbol
import com.forz.calculator.settings.Config.decimalSeparatorSymbol
import com.forz.calculator.calculator.AdditionalOperator
import com.forz.calculator.calculator.Bracket
import com.forz.calculator.calculator.Constant
import com.forz.calculator.calculator.DefaultOperator
import com.forz.calculator.calculator.ScientificFunction
import com.forz.calculator.calculator.TrigonometricFunction

object InsertInExpression {
    private val afterDigitArray: Array<String> = (
            Constant.entries.map { it.text } +
                    Bracket.OpenBracket.text +
                    ScientificFunction.entries.map { it.text } +
                    TrigonometricFunction.entries.map { it.text }
            ).toTypedArray()

    private val beforeDigitArray: Array<String> = (
            Constant.entries.map { it.text } +
                    Bracket.ClosedBracket.text +
                    AdditionalOperator.entries.map { it.text }
            ).toTypedArray()

    private val scientificFunctionArray: Array<String> = (
            ScientificFunction.entries.map { it.text } +
                    TrigonometricFunction.entries.map { it.text }
            ).toTypedArray()



    fun clearExpression(inputEditText: ExpressionEditText){
        inputEditText.text?.clear()
    }

    fun setExpression(result: String, inputEditText: ExpressionEditText){
        inputEditText.setText(result)
        inputEditText.setSelection(inputEditText.text!!.length)
    }

    fun enterBackspace(inputEditText: ExpressionEditText) {
        val string = inputEditText.text.toString()
        val cursorPositionStart = inputEditText.selectionStart
        val cursorPositionEnd = inputEditText.selectionEnd

        if (cursorPositionStart != cursorPositionEnd) {
            deleteSelection(inputEditText, cursorPositionStart, cursorPositionEnd)
        } else if (cursorPositionStart > 0){
            val stringBeforeCursor = stringBeforePosition(string, cursorPositionStart)
            if (stringBeforeCursor.endsWith(groupingSeparatorSymbol)){
                deleteSelection(inputEditText, cursorPositionStart - 1 - groupingSeparatorSymbol.length, cursorPositionStart)
            } else if (scientificFunctionArray.any { func -> stringBeforeCursor.endsWith(func) }) {
                val matchingFunction = scientificFunctionArray.firstOrNull { func -> stringBeforeCursor.endsWith(func) }
                matchingFunction?.let { func ->
                    deleteSelection(inputEditText, cursorPositionStart - func.length, cursorPositionStart)
                }
            } else{
                deleteSelection(inputEditText, cursorPositionStart - 1, cursorPositionStart)
            }
        }
    }

    fun enterDigit(digit: String, inputEditText: ExpressionEditText) {
        val string = inputEditText.text.toString()

        deleteSelection(inputEditText, inputEditText.selectionStart, inputEditText.selectionEnd)

        val cursorPositionStart = inputEditText.selectionStart

        val stringAfterCursor: String = stringAfterPosition(string, cursorPositionStart)
        val operatorAfterDigit: String = if (afterDigitArray.any { stringAfterCursor.startsWith(it) }) DefaultOperator.Multiply.text else ""

        val stringBeforeCursor: String = stringBeforePosition(string, cursorPositionStart)
        val operatorBeforeDigit: String = if (beforeDigitArray.any { stringBeforeCursor.endsWith(it) }) DefaultOperator.Multiply.text else ""

        val textInsert = "$operatorBeforeDigit$digit$operatorAfterDigit"
        inputEditText.text?.insert(cursorPositionStart, textInsert)

        correctPositionIfOperatorAfterNumberIsSpace(operatorAfterDigit, inputEditText)
    }

    fun enterOperator(inputOperator: String, inputEditText: ExpressionEditText) {
        val string = inputEditText.text.toString()

        deleteSelection(inputEditText, inputEditText.selectionStart, inputEditText.selectionEnd)

        val cursorPositionStart = inputEditText.selectionStart

        val stringAfterCursor: String = stringAfterPosition(string, cursorPositionStart)
        val stringBeforeCursor: String = stringBeforePosition(string, cursorPositionStart)

        if (inputOperator != DefaultOperator.Minus.text){
            val lastCharIsDigit = stringBeforeCursor.isNotEmpty() && stringBeforeCursor.last().isDigit()
            val operator = if (
                (beforeDigitArray.any { stringBeforeCursor.endsWith(it) }
                        || lastCharIsDigit
                        || DefaultOperator.entries.any { stringBeforeCursor.endsWith(it.text) }
                        || stringBeforeCursor.endsWith(groupingSeparatorSymbol))
                && !stringAfterCursor.startsWith(inputOperator)
                && stringBeforeCursor.isNotEmpty()
            ) inputOperator else ""

            if (DefaultOperator.entries.any { stringBeforeCursor.endsWith(it.text) }){
                deleteSelection(inputEditText, cursorPositionStart - 1, cursorPositionStart)
                if (string.isNotEmpty() && !stringBeforePosition(string, cursorPositionStart - 1).endsWith(
                        Bracket.OpenBracket.text)){
                    inputEditText.text?.insert(cursorPositionStart - 1, operator)
                }
            }else{
                inputEditText.text?.insert(cursorPositionStart, operator)
            }
        }else{
            val lastCharIsDigit = stringBeforeCursor.isNotEmpty() && stringBeforeCursor.last().isDigit()
            val operator =
                if (beforeDigitArray.any { stringBeforeCursor.endsWith(it) }
                    || afterDigitArray.any { stringBeforeCursor.endsWith(it) }
                    || lastCharIsDigit
                    || stringBeforeCursor.endsWith(groupingSeparatorSymbol)
                    || stringBeforeCursor.isEmpty())
                {
                    DefaultOperator.Minus.text
                }else if (DefaultOperator.entries.any { stringBeforeCursor.endsWith(it.text) }){
                    "${Bracket.OpenBracket.text}${DefaultOperator.Minus.text}"
                }else{
                    ""
                }

            inputEditText.text?.insert(cursorPositionStart, operator)
        }
    }

    fun enterScienceFunction(inputScienceFunction: String, inputEditText: ExpressionEditText) {
        val string = inputEditText.text.toString()

        deleteSelection(inputEditText, inputEditText.selectionStart, inputEditText.selectionEnd)

        val cursorPositionStart = inputEditText.selectionStart

        val stringBeforeCursor: String = stringBeforePosition(string, cursorPositionStart)
        val lastCharIsDigit = stringBeforeCursor.isNotEmpty() && stringBeforeCursor.last().isDigit()
        val operatorBeforeDigit: String = if ((beforeDigitArray.any { stringBeforeCursor.endsWith(it) }
                    || lastCharIsDigit
                    || stringBeforeCursor.endsWith(groupingSeparatorSymbol))
            && stringBeforeCursor.isNotEmpty()
            ) DefaultOperator.Multiply.text else ""

        if (!stringBeforeCursor.endsWith(decimalSeparatorSymbol)){
            val textInsert = "$operatorBeforeDigit$inputScienceFunction"
            inputEditText.text?.insert(cursorPositionStart, textInsert)
        }
    }

    fun enterAdditionalOperator(inputOperator: String, inputEditText: ExpressionEditText) {
        val string = inputEditText.text.toString()

        deleteSelection(inputEditText, inputEditText.selectionStart, inputEditText.selectionEnd)

        val cursorPositionStart = inputEditText.selectionStart

        val stringAfterCursor: String = stringAfterPosition(string, cursorPositionStart)
        val stringBeforeCursor: String = stringBeforePosition(string, cursorPositionStart)

        val lastCharIsDigit = stringBeforeCursor.isNotEmpty() && stringBeforeCursor.last().isDigit()
        if (stringBeforeCursor.isNotEmpty()
            && (lastCharIsDigit
                    || beforeDigitArray.any { stringBeforeCursor.endsWith(it) }
                    || stringBeforeCursor.endsWith(groupingSeparatorSymbol)))
        {
            val firstCharIsDigit = stringAfterCursor.isNotEmpty() && stringAfterCursor.first().isDigit()
            val operatorAfterDigit: String = if (afterDigitArray.any { stringAfterCursor.startsWith(it) }
                || firstCharIsDigit
                && stringAfterCursor.isNotEmpty()
                ) DefaultOperator.Multiply.text else ""

            val textInsert = "$inputOperator$operatorAfterDigit"
            inputEditText.text?.insert(cursorPositionStart, textInsert)

            correctPositionIfOperatorAfterNumberIsSpace(operatorAfterDigit, inputEditText)
        }
    }

    fun enterConstant(inputOperator: String, inputEditText: ExpressionEditText) {
        val string = inputEditText.text.toString()

        deleteSelection(inputEditText, inputEditText.selectionStart, inputEditText.selectionEnd)

        val cursorPositionStart = inputEditText.selectionStart

        val stringAfterCursor: String = stringAfterPosition(string, cursorPositionStart)
        val firstCharIsDigit = stringAfterCursor.isNotEmpty() && stringAfterCursor.first().isDigit()
        val operatorAfterDigit: String = if ((afterDigitArray.any { stringAfterCursor.startsWith(it) }
                    || firstCharIsDigit)
            && stringAfterCursor.isNotEmpty()
            ) DefaultOperator.Multiply.text else ""


        val stringBeforeCursor: String = stringBeforePosition(string, cursorPositionStart)
        val lastCharIsDigit = stringBeforeCursor.isNotEmpty() && stringBeforeCursor.last().isDigit()
        val operatorBeforeDigit: String = if ((beforeDigitArray.any { stringBeforeCursor.endsWith(it) }
                    || lastCharIsDigit
                    || stringBeforeCursor.endsWith(groupingSeparatorSymbol))
            && string.isNotEmpty()
            && stringBeforeCursor.isNotEmpty()
            ) DefaultOperator.Multiply.text else ""

        if (!stringBeforeCursor.endsWith(decimalSeparatorSymbol)){
            val textInsert = "$operatorBeforeDigit$inputOperator$operatorAfterDigit"
            inputEditText.text?.insert(cursorPositionStart, textInsert)

            correctPositionIfOperatorAfterNumberIsSpace(operatorAfterDigit, inputEditText)
        }
    }

    fun enterBracket(inputEditText: ExpressionEditText) {
        val string = inputEditText.text.toString()

        deleteSelection(inputEditText, inputEditText.selectionStart, inputEditText.selectionEnd)

        val cursorPositionStart = inputEditText.selectionStart

        val stringBeforeCursor: String = stringBeforePosition(string, cursorPositionStart)
        val lastCharIsDigit = stringBeforeCursor.isNotEmpty() && stringBeforeCursor.last().isDigit()

        if ((beforeDigitArray.any { stringBeforeCursor.endsWith(it) }
                    || lastCharIsDigit
                    || stringBeforeCursor.endsWith(groupingSeparatorSymbol))
            && stringBeforeCursor.isNotEmpty())
        {
            if (checkBrackets(string, cursorPositionStart)){
                inputEditText.text?.insert(cursorPositionStart, Bracket.ClosedBracket.text)
            }else{
                inputEditText.text?.insert(cursorPositionStart, "${DefaultOperator.Multiply.text}${Bracket.OpenBracket.text}")
            }
        } else if (DefaultOperator.entries.any { stringBeforeCursor.endsWith(it.text) }
            || afterDigitArray.any { stringBeforeCursor.endsWith(it) }
            || stringBeforeCursor.isEmpty())
        {
            inputEditText.text?.insert(cursorPositionStart, Bracket.OpenBracket.text)
        }
    }

    fun enterDot(inputEditText: ExpressionEditText) {
        val string = inputEditText.text.toString()

        deleteSelection(inputEditText, inputEditText.selectionStart, inputEditText.selectionEnd)

        val cursorPositionStart = inputEditText.selectionStart

        val stringBeforeCursor: String = stringBeforePosition(string, cursorPositionStart)
        val lastCharIsDigit = stringBeforeCursor.isNotEmpty() && stringBeforeCursor.last().isDigit()

        val operatorTexts = DefaultOperator.entries.map { it.text } +
                Bracket.OpenBracket.text
        val lastCharIsOperator = stringBeforeCursor.isNotEmpty() && operatorTexts.contains(stringBeforeCursor.last().toString())

        val additionalOperatorTexts = AdditionalOperator.entries.map { it.text } +
                Constant.entries.map { it.text } +
                Bracket.ClosedBracket.text
        val lastCharIsAdditionalOperator = stringBeforeCursor.isNotEmpty() && additionalOperatorTexts.contains(stringBeforeCursor.last().toString())


        if (lastCharIsDigit && !checkPoint(string, cursorPositionStart) && string.isNotEmpty()) {
            inputEditText.text?.insert(cursorPositionStart, decimalSeparatorSymbol)
        } else if (string.isEmpty() || lastCharIsOperator){
            inputEditText.text?.insert(cursorPositionStart, "0$decimalSeparatorSymbol")
        } else if (lastCharIsAdditionalOperator){
            inputEditText.text?.insert(cursorPositionStart, "${DefaultOperator.Multiply.text}0$decimalSeparatorSymbol")
        }
    }

    fun enterDoubleBrackets(inputEditText: ExpressionEditText) {
        val string = inputEditText.text.toString()

        val cursorPositionEnd = inputEditText.selectionEnd

        if (string.isNotEmpty()){
            inputEditText.text?.insert(0, Bracket.OpenBracket.text)

            val stringAfterCursor: String = stringAfterPosition(string, cursorPositionEnd + 1)
            val firstCharIsDigit = stringAfterCursor.isNotEmpty() && stringAfterCursor.first().isDigit()

            val stringBeforeCursor: String = stringBeforePosition(string, cursorPositionEnd + 1)

            if ((firstCharIsDigit
                        || afterDigitArray.any { stringAfterCursor.startsWith(it) })
                && stringAfterCursor.isNotEmpty())
            {
                inputEditText.text?.insert(cursorPositionEnd + 1, "${Bracket.ClosedBracket.text}${DefaultOperator.Multiply.text}")
            }

            if (!stringBeforeCursor.endsWith(decimalSeparatorSymbol)){
                inputEditText.text?.insert(cursorPositionEnd + 1, Bracket.ClosedBracket.text)
            }
        }
    }

    fun insertHistoryExpression(inputExpression: String, inputEditText: ExpressionEditText) {
        insertHistoryResult(inputExpression, inputEditText)
    }

    fun insertHistoryResult(inputResult: String, inputEditText: ExpressionEditText) {
        deleteSelection(inputEditText, inputEditText.selectionStart, inputEditText.selectionEnd)

        val cursorPositionStart = inputEditText.selectionStart

        inputEditText.text?.insert(cursorPositionStart, inputResult)
    }




    private fun deleteSelection(inputEditText: ExpressionEditText, cursorPositionStart: Int, cursorPositionEnd: Int){
        if (cursorPositionStart != cursorPositionEnd) {
            inputEditText.text?.delete(cursorPositionStart, cursorPositionEnd)
        }
    }

    fun stringAfterPosition(inputString: String, positionStart: Int): String {
        return if (positionStart < inputString.length && positionStart >= 0) inputString.substring(positionStart) else ""
    }

    private fun stringBeforePosition(inputString: String, positionStart: Int): String {
        return if (positionStart <= inputString.length && positionStart > 0) inputString.substring(0, positionStart) else ""
    }

    private fun correctPositionIfOperatorAfterNumberIsSpace(operatorAfterDigit: String, inputEditText: ExpressionEditText){
        if (operatorAfterDigit != ""){
            inputEditText.setSelection(inputEditText.selectionStart - 1)
        }
    }

    private fun checkBrackets(text: String, cursorPosition: Int): Boolean {
        var openBrackets = 0
        var closedBrackets = 0

        for (i in 0 until cursorPosition) {
            if (text[i].toString() == Bracket.OpenBracket.text) {
                openBrackets++
            } else if (text[i].toString() == Bracket.ClosedBracket.text) {
                closedBrackets++
            }
        }

        return openBrackets > closedBrackets
    }

    private fun checkPoint(inputText: String, cursorPosition: Int): Boolean {

        val newText = NumberFormatter.removeSeparators(inputText, groupingSeparatorSymbol)

        val lengthDiff = newText.length - inputText.length
        val newSelection = if (cursorPosition + lengthDiff > 0) cursorPosition + lengthDiff else 0

        val subTextBefore = newText.substring(0, newSelection)
        val subTextAfter = newText.substring(newSelection)
        var foundDigitBefore = false
        var foundDigitAfter = false

        outerLoop@ for (i in subTextBefore.length - 1 downTo 0) {
            if (subTextBefore[i].toString() == decimalSeparatorSymbol) {
                foundDigitBefore = true
                break@outerLoop
            } else if (!subTextBefore[i].isDigit() && subTextBefore[i].toString() != decimalSeparatorSymbol) {
                break@outerLoop
            }
        }

        outerLoop@ for (i in subTextAfter.indices) {
            if (subTextAfter[i].toString() == decimalSeparatorSymbol) {
                foundDigitAfter = true
                break@outerLoop
            } else if (!subTextAfter[i].isDigit() && subTextAfter[i].toString() != decimalSeparatorSymbol) {
                break@outerLoop
            }
        }

        return foundDigitBefore || foundDigitAfter
    }
}