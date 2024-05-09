package com.forz.calculator

object InsertInExpression {
    private val operators: Array<String> = arrayOf("×", "÷", "–", "+", "^")
    private val afterDigitArray: Array<String> = arrayOf("π", "e", "√", "(", "ln", "log", "sin", "cos", "tan", "sin⁻¹", "cos⁻¹", "tan⁻¹", "exp")
    private val beforeDigitArray: Array<String> = arrayOf("π", "e", "%", ")", "!")
    private val scientificFunctionArray: Array<String> = arrayOf("√(", "ln(", "log(", "sin(", "cos(", "tan(", "sin⁻¹(", "cos⁻¹(", "tan⁻¹(", "exp(")

    val triggersIsDegreeModActivatedShowArray = arrayOf("sin", "cos", "tan", "sin⁻¹", "cos⁻¹", "tan⁻¹")


    fun enterBackspace(inputString: String, cursorPositionStart: Int, cursorPositionEnd: Int, groupingSeparatorSymbol: String): Pair<String, Int>{
        var string = inputString
        var cursorPosition = cursorPositionStart

        if (cursorPositionStart != cursorPositionEnd) {
            string = deleteSelection(cursorPositionStart, cursorPositionEnd, string)
        } else if (cursorPositionStart > 0){
            val stringBeforeCursor = stringBeforeCursor(cursorPositionStart, string)
            if (stringBeforeCursor.endsWith(groupingSeparatorSymbol)){
                string = deleteSelection(cursorPositionStart - 2, cursorPositionEnd, string)
                cursorPosition -= 2
            }else if (scientificFunctionArray.any { func -> stringBeforeCursor.endsWith(func) }) {
                val matchingFunction = scientificFunctionArray.firstOrNull { func -> stringBeforeCursor.endsWith(func) }
                matchingFunction?.let { func ->
                    string = deleteSelection(cursorPositionStart - func.length, cursorPositionEnd, string)
                    cursorPosition -= func.length
                }
            } else{
                string = deleteSelection(cursorPositionStart - 1, cursorPositionEnd, string)
                cursorPosition -= 1
            }
        }

        return Pair(string, cursorPosition)
    }


    fun enterDigit(digit: String, inputString: String, cursorPositionStart: Int, cursorPositionEnd: Int): Pair<String, Int> {
        var string = inputString

        string = deleteSelection(cursorPositionStart, cursorPositionEnd, string)

        val stringAfterCursor: String = stringAfterCursor(cursorPositionStart, string)
        val operatorAfterDigit: String = if (afterDigitArray.any { stringAfterCursor.startsWith(it) }) "×" else ""

        val stringBeforeCursor: String = stringBeforeCursor(cursorPositionStart, string)
        val operatorBeforeDigit: String = if (beforeDigitArray.any { stringBeforeCursor.endsWith(it) }) "×" else ""

        val textInsert = "$operatorBeforeDigit$digit$operatorAfterDigit"
        string = string.substring(0, cursorPositionStart) + textInsert + string.substring(
            cursorPositionStart
        )

        val newPositionStart = cursorPositionStart + textInsert.length

        return if(isOperatorAfterNumberSpace(operatorAfterDigit)){
            Pair(string, newPositionStart - 1)
        }else{
            Pair(string, newPositionStart)
        }
    }

    fun enterOperator(inputOperator: String, inputString: String, cursorPositionStart: Int, cursorPositionEnd: Int, groupingSeparatorSymbol: String): Pair<String, Int>{
        var string = inputString

        string = deleteSelection(cursorPositionStart, cursorPositionEnd, string)

        val stringAfterCursor: String = stringAfterCursor(cursorPositionStart, string)
        val stringBeforeCursor: String = stringBeforeCursor(cursorPositionStart, string)

        if (inputOperator != "–"){
            val lastCharIsDigit = stringBeforeCursor.isNotEmpty() && stringBeforeCursor.last().isDigit()
            val operator = if (
                (beforeDigitArray.any { stringBeforeCursor.endsWith(it) }
                        || lastCharIsDigit
                        || operators.any { stringBeforeCursor.endsWith(it) }
                        || stringBeforeCursor.endsWith(groupingSeparatorSymbol))
                && !stringAfterCursor.startsWith(inputOperator)
                && stringBeforeCursor.isNotEmpty()
            ) inputOperator else ""

            if (operators.any { stringBeforeCursor.endsWith(it) }){
                string = deleteSelection(cursorPositionStart - 1, cursorPositionStart, string)
                if (string.isNotEmpty() && !stringBeforeCursor(cursorPositionStart - 1, string).endsWith("(")){
                    string = insert(string, operator, cursorPositionStart - 1)
                    val newPositionStart = cursorPositionStart - 1 + operator.length
                    return Pair(string, newPositionStart)
                }
            }else{
                string = insert(string, operator, cursorPositionStart)
                val newPositionStart = cursorPositionStart + operator.length
                return Pair(string, newPositionStart)
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
                    "–"
                }else if (operators.any { stringBeforeCursor.endsWith(it) }){
                    "(–"
                }else{
                    ""
                }

            string = insert(string, operator, cursorPositionStart)
            val newPositionStart = cursorPositionStart + operator.length
            return Pair(string, newPositionStart)
        }
        return Pair(string, cursorPositionStart - 1)
    }

    fun enterScienceFunction(inputOperator: String, inputString: String, cursorPositionStart: Int, cursorPositionEnd: Int, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): Pair<String, Int>{
        var string = inputString

        string = deleteSelection(cursorPositionStart, cursorPositionEnd, string)

        val stringBeforeCursor: String = stringBeforeCursor(cursorPositionStart, string)
        val lastCharIsDigit = stringBeforeCursor.isNotEmpty() && stringBeforeCursor.last().isDigit()
        val operatorBeforeDigit: String = if ((beforeDigitArray.any { stringBeforeCursor.endsWith(it) }
                    || lastCharIsDigit
                    || stringBeforeCursor.endsWith(groupingSeparatorSymbol))
            && stringBeforeCursor.isNotEmpty()
            ) "×" else ""

        if (!stringBeforeCursor.endsWith(decimalSeparatorSymbol)){
            val textInsert = "$operatorBeforeDigit$inputOperator"
            string = insert(string, textInsert, cursorPositionStart)
            val newPositionStart = cursorPositionStart + textInsert.length
            return Pair(string, newPositionStart)
        }
        return Pair(string, cursorPositionStart)
    }

    fun enterOperator2(inputOperator: String, inputString: String, cursorPositionStart: Int, cursorPositionEnd: Int, groupingSeparatorSymbol: String): Pair<String, Int>{
        var string = inputString

        string = deleteSelection(cursorPositionStart, cursorPositionEnd, string)

        val stringAfterCursor: String = stringAfterCursor(cursorPositionStart, string)
        val stringBeforeCursor: String = stringBeforeCursor(cursorPositionStart, string)

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
                ) "×" else ""

            val textInsert = "$inputOperator$operatorAfterDigit"
            string = insert(string, textInsert, cursorPositionStart)
            val newPositionStart = cursorPositionStart + textInsert.length

            return if(isOperatorAfterNumberSpace(operatorAfterDigit)){
                Pair(string, newPositionStart - 1)
            }else{
                Pair(string, newPositionStart)
            }
        }
        return Pair(string, cursorPositionStart)
    }

    fun enterConstant(inputOperator: String, inputString: String, cursorPositionStart: Int, cursorPositionEnd: Int, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): Pair<String, Int>{
        var string = inputString

        string = deleteSelection(cursorPositionStart, cursorPositionEnd, string)

        val stringAfterCursor: String = stringAfterCursor(cursorPositionStart, string)
        val firstCharIsDigit = stringAfterCursor.isNotEmpty() && stringAfterCursor.first().isDigit()
        val operatorAfterDigit: String = if ((afterDigitArray.any { stringAfterCursor.startsWith(it) }
                    || firstCharIsDigit)
            && stringAfterCursor.isNotEmpty()
            ) "×" else ""


        val stringBeforeCursor: String = stringBeforeCursor(cursorPositionStart, string)
        val lastCharIsDigit = stringBeforeCursor.isNotEmpty() && stringBeforeCursor.last().isDigit()
        val operatorBeforeDigit: String = if ((beforeDigitArray.any { stringBeforeCursor.endsWith(it) }
                    || lastCharIsDigit
                    || stringBeforeCursor.endsWith(groupingSeparatorSymbol))
            && string.isNotEmpty()
            && stringBeforeCursor.isNotEmpty()
            ) "×" else ""

        if (!stringBeforeCursor.endsWith(decimalSeparatorSymbol)){
            val textInsert = "$operatorBeforeDigit$inputOperator$operatorAfterDigit"
            string = insert(string, textInsert, cursorPositionStart)
            val newPositionStart = cursorPositionStart + textInsert.length

            return if(isOperatorAfterNumberSpace(operatorAfterDigit)){
                Pair(string, newPositionStart - 1)
            }else{
                Pair(string, newPositionStart)
            }
        }

        return Pair(string, cursorPositionStart)
    }

    fun enterBracket(inputString: String, cursorPositionStart: Int, cursorPositionEnd: Int, groupingSeparatorSymbol: String): Pair<String, Int>{
        var string = inputString

        string = deleteSelection(cursorPositionStart, cursorPositionEnd, string)

        val stringBeforeCursor: String = stringBeforeCursor(cursorPositionStart, string)
        val lastCharIsDigit = stringBeforeCursor.isNotEmpty() && stringBeforeCursor.last().isDigit()

        if ((beforeDigitArray.any { stringBeforeCursor.endsWith(it) }
                    || lastCharIsDigit
                    || stringBeforeCursor.endsWith(groupingSeparatorSymbol))
            && stringBeforeCursor.isNotEmpty())
        {
            return if (checkBrackets(string, cursorPositionStart)){
                string = insert(string, ")", cursorPositionStart)
                val newPositionStart = cursorPositionStart + ")".length
                Pair(string, newPositionStart)
            }else{
                string = insert(string, "×(", cursorPositionStart)
                val newPositionStart = cursorPositionStart + "×(".length
                Pair(string, newPositionStart)
            }
        } else if (operators.any { stringBeforeCursor.endsWith(it) }
            || afterDigitArray.any { stringBeforeCursor.endsWith(it) }
            || stringBeforeCursor.isEmpty())
        {
            string = insert(string, "(", cursorPositionStart)
            val newPositionStart = cursorPositionStart + "(".length
            return Pair(string, newPositionStart)
        }

        return Pair(string, cursorPositionStart)
    }

    fun enterDot(inputString: String, cursorPositionStart: Int, cursorPositionEnd: Int, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): Pair<String, Int>{
        var string = inputString

        string = deleteSelection(cursorPositionStart, cursorPositionEnd, string)

        val stringBeforeCursor: String = stringBeforeCursor(cursorPositionStart, string)
        val lastCharIsDigit = stringBeforeCursor.isNotEmpty() && stringBeforeCursor.last().isDigit()

        if (lastCharIsDigit
            && !checkPoint(
                string,
                cursorPositionStart,
                groupingSeparatorSymbol,
                decimalSeparatorSymbol
            ) && string.isNotEmpty())
        {
            string = insert(string, decimalSeparatorSymbol, cursorPositionStart)
            val newPositionStart = cursorPositionStart + decimalSeparatorSymbol.length
            return Pair(string, newPositionStart)
        } else if (string.isEmpty()){
            string = insert(string, "0$decimalSeparatorSymbol", cursorPositionStart)
            val newPositionStart = cursorPositionStart + "0$decimalSeparatorSymbol".length
            return Pair(string, newPositionStart)
        }

        return Pair(string, cursorPositionStart)
    }

    fun enterDoubleBrackets(inputString: String, positionEnd: Int, decimalSeparatorSymbol: String): Pair<String, Int>{
        var string = inputString
        if (string.isNotEmpty()){
            string = insert(string, "(", 0)

            val stringAfterCursor: String = stringAfterCursor(positionEnd + 1, string)
            val firstCharIsDigit = stringAfterCursor.isNotEmpty() && stringAfterCursor.first().isDigit()

            val stringBeforeCursor: String = stringBeforeCursor(positionEnd + 1, string)

            if ((firstCharIsDigit
                        || afterDigitArray.any { stringAfterCursor.startsWith(it) })
                && stringAfterCursor.isNotEmpty())
            {
                string = insert(string, ")×", positionEnd + 1)
                return Pair(string, positionEnd + 2)
            }

            if (!stringBeforeCursor.endsWith(decimalSeparatorSymbol)){
                string = insert(string, ")", positionEnd + 1)
                return Pair(string, positionEnd + 2)
            }
        }
        return Pair(string, positionEnd)
    }

    fun insertExpression(inputExpression: String, inputString: String, cursorPositionStart: Int, cursorPositionEnd: Int, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): Pair<String, Int>{
        var expression = inputExpression

        if (expression.first() != '('){
            expression = "($expression"
        }
        if (expression.last() != ')'){
            expression = "$expression)"
        }

        return insertResult(expression, inputString, cursorPositionStart, cursorPositionEnd, groupingSeparatorSymbol, decimalSeparatorSymbol)
    }

    fun insertResult(expression: String, inputString: String, cursorPositionStart: Int, cursorPositionEnd: Int, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): Pair<String, Int>{
        var string = inputString

        string = deleteSelection(cursorPositionStart, cursorPositionEnd, string)

        val stringAfterCursor: String = stringAfterCursor(cursorPositionStart, string)
        val firstCharIsDigit = stringAfterCursor.isNotEmpty() && stringAfterCursor.first().isDigit()
        val operatorAfterDigit: String = if ((afterDigitArray.any { stringAfterCursor.startsWith(it) }
                    || firstCharIsDigit)
            && stringAfterCursor.isNotEmpty()
        ) "×" else ""


        val stringBeforeCursor: String = stringBeforeCursor(cursorPositionStart, string)
        val lastCharIsDigit = stringBeforeCursor.isNotEmpty() && stringBeforeCursor.last().isDigit()
        val operatorBeforeDigit: String = if ((beforeDigitArray.any { stringBeforeCursor.endsWith(it) }
                    || lastCharIsDigit
                    || stringBeforeCursor.endsWith(groupingSeparatorSymbol))
            && string.isNotEmpty()
            && stringBeforeCursor.isNotEmpty()
        ) "×" else ""

        if (!stringBeforeCursor.endsWith(decimalSeparatorSymbol)){
            val textInsert = "$operatorBeforeDigit$expression$operatorAfterDigit"
            string = insert(string, textInsert, cursorPositionStart)
            val newPositionStart = cursorPositionStart + textInsert.length

            return if(isOperatorAfterNumberSpace(operatorAfterDigit)){
                Pair(string, newPositionStart - 1)
            }else{
                Pair(string, newPositionStart)
            }
        }

        return Pair(string, cursorPositionStart)
    }




    private fun deleteSelection(positionStart: Int, positionEnd: Int, inputString: String): String {
        if (positionStart != positionEnd) {
            return inputString.substring(0, positionStart) + inputString.substring(positionEnd)
        }
        return inputString
    }

    fun stringAfterCursor(positionStart: Int, inputString: String): String {
        return if (positionStart < inputString.length && positionStart >= 0) inputString.substring(positionStart) else ""
    }

    private fun stringBeforeCursor(positionStart: Int, inputString: String): String {
        return if (positionStart > 0 && positionStart <= inputString.length) inputString.substring(0, positionStart) else ""
    }

    private fun insert(inputString: String, text: String, cursorPositionStart: Int): String{
        return inputString.substring(0, cursorPositionStart) + text + inputString.substring(cursorPositionStart)
    }

    private fun isOperatorAfterNumberSpace(inputChar: String): Boolean{
        return inputChar != ""
    }

    private fun checkBrackets(text: String, cursorPosition: Int): Boolean {
        var openBrackets = 0
        var closedBrackets = 0

        for (i in 0 until cursorPosition) {
            if (text[i] == '(') {
                openBrackets++
            } else if (text[i] == ')') {
                closedBrackets++
            }
        }

        return openBrackets > closedBrackets
    }

    private fun checkPoint(inputText: String, cursorPosition: Int, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): Boolean {

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