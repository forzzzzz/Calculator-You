package com.forz.calculator

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import androidx.core.content.ContextCompat
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

object NumberFormatter {

    fun formatExpression(inputString: String, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): String{
        var text = inverseReplaceSeparators(inputString,  groupingSeparatorSymbol, decimalSeparatorSymbol)
        text = removeSeparators(text, ",")
        text = separateNumbers(text)
        text = replaceSeparators(text, groupingSeparatorSymbol, decimalSeparatorSymbol)
        text = replaceOperators(text)
        return text
    }


    fun formatResult(inputString: String, numberPrecision: Int, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): String{
        val text = limitFractionalPart(inputString.toBigDecimal(), numberPrecision)
        return replaceSeparators(separateNumbers(text.toString().replace("-", "–")),
                groupingSeparatorSymbol, decimalSeparatorSymbol)
    }






    private fun separateNumbers(inputString: String): String {
        var text = removeSeparators(inputString, ",")
        val regex = Regex(pattern = "(?<!\\.)\\b[0-9]+\\b", options = setOf(RegexOption.IGNORE_CASE))
        val matches = regex.findAll(text)

        matches.forEach { matchResult ->
            val oldNumber: String = matchResult.value
            val newNumber: String = addSeparators(oldNumber)

            text = text.replaceFirst(oldNumber, newNumber)
        }

        return text
    }

    private fun addSeparators(inputString: String): String{
        val regex = "(\\d)(?=(\\d{3})+$)".toRegex()
        return inputString.replace(regex, "$1,")
    }

    fun removeSeparators(inputString: String, groupingSeparatorSymbol: String): String {
        return inputString.replace(groupingSeparatorSymbol, "")
    }

    private fun limitFractionalPart(inputDecimal: BigDecimal, numberPrecision: Int): BigDecimal{
        if (inputDecimal.toDouble() != 0.0){
            var newResult = inputDecimal.setScale(numberPrecision, RoundingMode.HALF_EVEN)

            if (newResult >= BigDecimal(999999999999999) || newResult <= BigDecimal(0.1)) {
                val scientificString = String.format(Locale.US, "%.15g", inputDecimal)
                newResult = BigDecimal(scientificString)
            }

            if (!newResult.toString().contains('E')){
                newResult = newResult.toString().trimEnd('0').trimEnd('.').toBigDecimal()
            }

            return newResult
        }else{
            return BigDecimal(0)
        }
    }

    private fun replaceSeparators(inputString: String, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): String{
        var text = inputString.replace(",", "#")
        text = text.replace(".", decimalSeparatorSymbol)
        text = text.replace("#", groupingSeparatorSymbol)
        return text
    }
    private fun inverseReplaceSeparators(inputString: String, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): String{
        var text = inputString.replace(groupingSeparatorSymbol, "#")
        text = text.replace(decimalSeparatorSymbol, ".")
        text = text.replace("#", ",")
        return text
    }

    private fun replaceOperators(inputString: String): String{
        var text = inputString

        text = text.replace("*", "×")
        text = text.replace("/", "÷")
        text = text.replace("-", "–")

        return text
    }

    fun changeColorOperators(inputString: String, context: Context): SpannableStringBuilder{
        val spannableString = SpannableStringBuilder(inputString)

        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
        val color = ContextCompat.getColor(context, typedValue.resourceId)

        for (operator in arrayOf("+", "–", "×", "÷", "^", "%", "!")) {
            var startIndex = spannableString.indexOf(operator)

            while (startIndex != -1) {
                val endIndex = startIndex + operator.length
                spannableString.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                startIndex = spannableString.indexOf(operator, endIndex)
            }
        }

        return spannableString
    }
}