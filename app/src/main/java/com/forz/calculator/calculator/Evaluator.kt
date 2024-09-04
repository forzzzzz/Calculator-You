package com.forz.calculator.calculator

import android.content.Context
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.forz.calculator.utils.NumberFormatter
import com.forz.calculator.R
import com.forz.calculator.expression.ExpressionEditText
import com.forz.calculator.settings.Config.decimalSeparatorSymbol
import com.forz.calculator.settings.Config.groupingSeparatorSymbol
import com.forz.calculator.settings.Config.maxScientificNotationDigits
import com.forz.calculator.settings.Config.numberPrecision
import org.javia.arity.Symbols
import java.math.BigDecimal


object Evaluator: ViewModel() {
    var isCalculated = false

    private val _converterResult = MutableLiveData<Double?>()
    val converterResult: LiveData<Double?> get() = _converterResult

    private val symbols: Symbols = Symbols()

    private fun evaluate(expr: String, context: Context): String {
        var exp = expr

        if (exp == "Anastasia" || exp == "Анастасия"){
            _converterResult.value = null
            isCalculated = false
            return "I love you❤\uFE0F"
        }

        exp = NumberFormatter.clearExpression(exp, groupingSeparatorSymbol, decimalSeparatorSymbol)
        if (exp.isEmpty()) {
            _converterResult.value = null
            isCalculated = false
            return ""
        }else if (exp.toDoubleOrNull() != null) {
            _converterResult.value = exp.toDouble()
            isCalculated = false
            return ""
        }

        try {
            val result: Double = symbols.eval(exp)
            if (java.lang.Double.isNaN(result)) {
                _converterResult.value = null
                isCalculated = false
                return context.getString(R.string.expression_error)
            } else if (result.isInfinite()){
                _converterResult.value = null
                isCalculated = false
                return context.getString(R.string.expression_infinity)
            }else{
                _converterResult.value = result
                isCalculated = true
                return NumberFormatter.formatResult(BigDecimal(result), numberPrecision, maxScientificNotationDigits, groupingSeparatorSymbol, decimalSeparatorSymbol)
            }
        } catch (e: Exception) {
            _converterResult.value = null
            isCalculated = false
            return context.getString(R.string.expression_error)
        }
    }

    fun getResult(expressionEditText: ExpressionEditText, isSelected: Boolean, context: Context): String{
        if (isSelected){
            val result = evaluate(
                expressionEditText.text
                    .toString()
                    .substring(
                        expressionEditText.selectionStart, expressionEditText.selectionEnd),
                context)
            return result
        }else{
            val result = evaluate(expressionEditText.text.toString(), context)
            return result
        }
    }

    fun setResultTextView(expressionEditText: ExpressionEditText, resultTextView: TextView, isSelected: Boolean, context: Context){
        if (isSelected){
            val result = evaluate(
                expressionEditText.text
                    .toString()
                    .substring(
                        expressionEditText.selectionStart, expressionEditText.selectionEnd),
                context)
            resultTextView.text = result
        }else{
            val result = evaluate(expressionEditText.text.toString(), context)
            resultTextView.text = result
        }
    }
}