package com.forz.calculator.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.forz.calculator.calculator.AdditionalOperator
import com.forz.calculator.calculator.Bracket
import com.forz.calculator.calculator.DefaultOperator
import com.forz.calculator.calculator.ScientificFunction
import com.forz.calculator.calculator.TrigonometricFunction
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.pow

object NumberFormatter {

    fun formatExpression(expression: String, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): String {
        var exp = expression

        exp = inverseReplaceSeparators(exp,  groupingSeparatorSymbol, decimalSeparatorSymbol)
        exp = separateNumbers(exp)
        exp = replaceSeparators(exp, groupingSeparatorSymbol, decimalSeparatorSymbol)
        exp = replaceOperators(exp)

        return exp
    }

    fun formatResult(result: BigDecimal, numberPrecision: Int, maxScientificNotationDigits: Int, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): String {
        var res = bigDecimalToString(result, numberPrecision, maxScientificNotationDigits)
        res = replaceSeparators(separateNumbers(res), groupingSeparatorSymbol, decimalSeparatorSymbol)
        res = res.replace(DefaultOperator.Minus.value, DefaultOperator.Minus.text)

        return res
    }

    private fun bigDecimalToString(number: BigDecimal, decimalPlaces: Int, maxIntegerDigits: Int): String {
        val scientificFormatter = DecimalFormat("0.${"0".repeat(decimalPlaces)}E0", DecimalFormatSymbols(Locale.US))
        val standardFormatter = DecimalFormat("0.${"0".repeat(decimalPlaces)}", DecimalFormatSymbols(Locale.US))

        val absNumber = number.abs()

        var standardFormatted = standardFormatter.format(number)

        standardFormatted = standardFormatted.trimEnd('0')
        if (standardFormatted.endsWith('.')) {
            standardFormatted = standardFormatted.dropLast(1)
        }

        if (absNumber >= BigDecimal(10.0.pow(maxIntegerDigits.toDouble())) || absNumber < BigDecimal(10.0.pow(-decimalPlaces.toDouble()))) {
            var scientificFormatted = scientificFormatter.format(number)

            scientificFormatted = scientificFormatted.replace(Regex("0+E"), "E")
            scientificFormatted = scientificFormatted.replace(Regex("\\.E"), "E")
            return scientificFormatted
        }

        if (absNumber < BigDecimal(10.0.pow(-decimalPlaces.toDouble()))) {
            return "0"
        }

        return standardFormatted
    }

    fun clearExpression(expression: String, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): String {
        val replacementMap: MutableMap<String, String> = HashMap()

        DefaultOperator.entries.forEach {
            replacementMap[it.value] = it.text
        }
        ScientificFunction.entries.forEach {
            replacementMap[it.value] = it.text
        }
        TrigonometricFunction.entries.forEach {
            replacementMap[it.value] = it.text
        }

        var exp = expression

        exp = exp.replace(groupingSeparatorSymbol, "")
        exp = exp.replace(decimalSeparatorSymbol, ".")

        for ((key, value) in replacementMap) {
            exp = exp.replace(value, key)
        }

        val operatorEndExpression = (
                DefaultOperator.entries.map { it.value } +
                        ScientificFunction.entries.map { it.value } +
                        TrigonometricFunction.entries.map { it.value } +
                        Bracket.entries.map { it.value }
                ).sortedByDescending { it.length }

        while (exp.isNotEmpty()) {
            var matchFound = false
            for (operator in operatorEndExpression) {
                if (exp.endsWith(operator)) {
                    exp = exp.substring(0, exp.length - operator.length)
                    matchFound = true
                    break
                }
            }
            if (!matchFound) break
        }

        val operatorStartExpression = (
                DefaultOperator.entries.map { it.value } +
                        Bracket.entries.map { it.value }
                ).sortedByDescending { it.length }

        while (exp.isNotEmpty()) {
            var matchFound = false
            for (operator in operatorStartExpression) {
                if (exp.startsWith(operator)) {
                    if (operator != DefaultOperator.Minus.value){
                        exp = exp.substring(operator.length)
                        matchFound = true
                        break
                    }
                }
            }
            if (!matchFound) break
        }

        return exp
    }

    fun changeSeparatorsExpression(inputString: String,
                                     oldGroupingSeparatorSymbol: String, newGroupingSeparatorSymbol: String,
                                     oldDecimalSeparatorSymbol: String, newDecimalSeparatorSymbol: String
    ): String {
        var string = inputString
        string = string.replace(oldGroupingSeparatorSymbol, "#")
        string = string.replace(oldDecimalSeparatorSymbol, newDecimalSeparatorSymbol)
        string = string.replace("#", newGroupingSeparatorSymbol)
        return string
    }



    private fun separateNumbers(expression: String): String {
        var exp = expression

        exp = removeSeparators(exp, ",")

        val regex = Regex(pattern = "(?<!\\.)\\b[0-9]+\\b", options = setOf(RegexOption.IGNORE_CASE))
        val matches = regex.findAll(exp)

        matches.forEach { matchResult ->
            val oldNumber: String = matchResult.value
            val newNumber: String = addSeparators(oldNumber)

            exp = exp.replaceFirst(oldNumber, newNumber)
        }

        return exp
    }

    private fun addSeparators(number: String): String{
        val regex = "(\\d)(?=(\\d{3})+$)".toRegex()
        return number.replace(regex, "$1,")
    }

    fun removeSeparators(expression: String, groupingSeparatorSymbol: String): String {
        return expression.replace(groupingSeparatorSymbol, "")
    }

    private fun replaceSeparators(expression: String, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): String{
        var exp = expression

        exp = exp.replace(",", "#")
        exp = exp.replace(".", decimalSeparatorSymbol)
        exp = exp.replace("#", groupingSeparatorSymbol)

        return exp
    }
    private fun inverseReplaceSeparators(expression: String, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): String{
        var exp = expression

        exp = exp.replace(groupingSeparatorSymbol, "#")
        exp = exp.replace(decimalSeparatorSymbol, ".")
        exp = exp.replace("#", ",")

        return exp
    }

    private fun replaceOperators(expression: String): String{
        val replacementMap: MutableMap<String, String> = HashMap()

        DefaultOperator.entries.forEach {
            replacementMap[it.value] = it.text
        }
        ScientificFunction.entries.forEach {
            replacementMap[it.value] = it.text
        }
        TrigonometricFunction.entries.forEach {
            replacementMap[it.value] = it.text
        }

        var exp = expression

        for ((key, value) in replacementMap) {
            exp = exp.replace(key, value)
        }

        return exp
    }

    fun changeColorOperators(expression: String, context: Context): SpannableStringBuilder{
        val exp = SpannableStringBuilder(expression)

        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
        val color = ContextCompat.getColor(context, typedValue.resourceId)

        val operators = DefaultOperator.entries.map { it.text } + AdditionalOperator.entries.map { it.text }

        for (operator in operators) {
            var startIndex = exp.indexOf(operator)

            while (startIndex != -1) {
                val endIndex = startIndex + operator.length
                exp.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                startIndex = exp.indexOf(operator, endIndex)
            }
        }

        return exp
    }
}