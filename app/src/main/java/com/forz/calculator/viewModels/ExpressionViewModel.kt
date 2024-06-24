package com.forz.calculator.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darkempire78.opencalculator.Calculator
import com.darkempire78.opencalculator.Expression
import com.darkempire78.opencalculator.division_by_0
import com.darkempire78.opencalculator.domain_error
import com.darkempire78.opencalculator.is_infinity
import com.darkempire78.opencalculator.require_real_number
import com.darkempire78.opencalculator.syntax_error
import com.forz.calculator.InsertInExpression
import com.forz.calculator.NumberFormatter
import com.forz.calculator.viewModels.CalculatorViewModel.isDegreeModActivated
import com.forz.calculator.settings.SettingsState.decimalSeparatorSymbol
import com.forz.calculator.settings.SettingsState.groupingSeparatorSymbol
import com.forz.calculator.settings.SettingsState.numberPrecision
import kotlin.properties.Delegates.notNull


object ExpressionViewModel : ViewModel() {

    private val _expression = MutableLiveData<String>()
    val expression: LiveData<String> get() = _expression

    private val _expressionCursorPositionStart = MutableLiveData<Int>()
    val expressionCursorPositionStart: LiveData<Int> get() = _expressionCursorPositionStart

    private val _expressionCursorPositionEnd = MutableLiveData<Int>()
    val expressionCursorPositionEnd: LiveData<Int> get() = _expressionCursorPositionEnd
    var symbolAfterCursorIsGroupingSeparatorSymbol: Boolean by notNull()

    private val _isSelection = MutableLiveData<Boolean>()
    val isSelection: LiveData<Boolean> get() = _isSelection
    var previousIsSelection: Boolean by notNull()

    var numberOfCharactersOfInsertedText: Int by notNull()
    var isExpressionInserted: Boolean by notNull()
    var saveExpression: String by notNull()

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> get() = _result





    init {
        _expression.value = ""
        _expressionCursorPositionStart.value = 0
        _expressionCursorPositionEnd.value = 0
        symbolAfterCursorIsGroupingSeparatorSymbol = false
        _isSelection.value = false
        previousIsSelection = false
        numberOfCharactersOfInsertedText = 0
        isExpressionInserted = false
        saveExpression = ""
        _result.value = ""
    }


    fun updateSymbolsInExpression(oldGroupingSeparatorSymbol: String, newGroupingSeparatorSymbol: String, oldDecimalSeparatorSymbol: String, newDecimalSeparatorSymbol: String){
        var string = expression.value!!
        string = string.replace(oldGroupingSeparatorSymbol, "#")
        string = string.replace(oldDecimalSeparatorSymbol, newDecimalSeparatorSymbol)
        string = string.replace("#", newGroupingSeparatorSymbol)
        _expression.value =  string
    }

    fun updateExpression(expression: String, cursorPositionStart: Int){
        formatExpression(cursorPositionStart, expression)
    }

    fun updateIsExpressionInserted(value: Boolean){
        isExpressionInserted = value
    }

    fun updateNumberOfCharactersOfInsertedText(value: Int){
        numberOfCharactersOfInsertedText = value
        isExpressionInserted = true
    }

    fun updateSaveExpression(expression: String){
        this.saveExpression = expression
    }
    fun updateResult(expression: String){
        _result.value = result(expression)
    }

    fun updateCursorPosition(start: Int, end: Int){
        _expressionCursorPositionStart.value = start
        _expressionCursorPositionEnd.value = end

        _isSelection.value = start != end
        previousIsSelection = start != end
    }

    fun enterDigit(digit: String) {
        val newValues = InsertInExpression.enterDigit(digit, expression.value!!, expressionCursorPositionStart.value!!, expressionCursorPositionEnd.value!!)
        formatExpression(newValues.second, newValues.first)
    }

    fun enterOperator(operator: String) {
        val newValues = InsertInExpression.enterOperator(operator, expression.value!!, expressionCursorPositionStart.value!!, expressionCursorPositionEnd.value!!, groupingSeparatorSymbol)
        formatExpression(newValues.second, newValues.first)
    }
    fun enterScienceFunction(operator: String) {
        val newValues = InsertInExpression.enterScienceFunction(operator, expression.value!!, expressionCursorPositionStart.value!!, expressionCursorPositionEnd.value!!, groupingSeparatorSymbol, decimalSeparatorSymbol)
        formatExpression(newValues.second, newValues.first)
    }
    fun enterOperator2(operator: String) {
        val newValues = InsertInExpression.enterOperator2(operator, expression.value!!, expressionCursorPositionStart.value!!, expressionCursorPositionEnd.value!!, groupingSeparatorSymbol)
        formatExpression(newValues.second, newValues.first)
    }
    fun enterBackspace() {
        val newValues = InsertInExpression.enterBackspace(_expression.value!!, expressionCursorPositionStart.value!!, expressionCursorPositionEnd.value!!, groupingSeparatorSymbol)
        formatExpression(newValues.second, newValues.first)
    }

    fun enterConstant(operator: String) {
        val newValues = InsertInExpression.enterConstant(operator, expression.value!!, expressionCursorPositionStart.value!!, expressionCursorPositionEnd.value!!, groupingSeparatorSymbol, decimalSeparatorSymbol)
        formatExpression(newValues.second, newValues.first)
    }

    fun enterBracket() {
        val newValues = InsertInExpression.enterBracket(_expression.value!!, expressionCursorPositionStart.value!!, expressionCursorPositionEnd.value!!, groupingSeparatorSymbol)
        formatExpression(newValues.second, newValues.first)
    }

    fun enterDot() {
        val newValues = InsertInExpression.enterDot(_expression.value!!, expressionCursorPositionStart.value!!, expressionCursorPositionEnd.value!!, groupingSeparatorSymbol, decimalSeparatorSymbol)
        formatExpression(newValues.second, newValues.first)
    }

    fun enterDoubleBrackets() {
        val newValues = InsertInExpression.enterDoubleBrackets(expression.value!!, expressionCursorPositionEnd.value!!, decimalSeparatorSymbol)
        formatExpression(newValues.second, newValues.first)
    }

    fun insertExpression(expression: String) {
        val newValues = InsertInExpression.insertExpression(expression, this.expression.value!!, expressionCursorPositionStart.value!!, expressionCursorPositionEnd.value!!, groupingSeparatorSymbol, decimalSeparatorSymbol)
        formatExpression(newValues.second, newValues.first)
    }

    fun insertResult(result: String){
        val newValues = InsertInExpression.insertResult(result, expression.value!!, expressionCursorPositionStart.value!!, expressionCursorPositionEnd.value!!, groupingSeparatorSymbol, decimalSeparatorSymbol)
        formatExpression(newValues.second, newValues.first)
    }

    fun clearExpression() {
        _expressionCursorPositionStart.value = 0
        _expressionCursorPositionEnd.value = 0
        _expression.value = ""
    }




    private fun formatExpression(selection: Int, inputString: String){
        val newText = NumberFormatter.formatExpression(
            inputString,
            groupingSeparatorSymbol,
            decimalSeparatorSymbol
        )

        val lengthDiff = newText.length - inputString.length
        val newSelection = if (selection + lengthDiff > 0) selection + lengthDiff else 0
        _expressionCursorPositionStart.value = newSelection
        _expressionCursorPositionEnd.value = newSelection

        _expression.value = newText
    }

    private fun result(inputString: String): String{
        division_by_0 = false
        domain_error = false
        syntax_error = false
        is_infinity = false
        require_real_number = false

        val calculationTmp = Expression().getCleanExpression(inputString, decimalSeparatorSymbol, groupingSeparatorSymbol)
        val calculationResult = Calculator(numberPrecision).evaluate(calculationTmp, isDegreeModActivated.value!!)

        return if (!(division_by_0 || domain_error || syntax_error || is_infinity || require_real_number) && calculationTmp.toDoubleOrNull() == null) {
            val result = NumberFormatter.formatResult(calculationResult.toString(), numberPrecision, groupingSeparatorSymbol, decimalSeparatorSymbol)
            result
        }else{
            ""
        }
    }
}
