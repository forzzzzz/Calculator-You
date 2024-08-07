package com.forz.calculator.calculator

enum class DefaultOperator(val text: String, val value: String = text){
    Plus("+"),
    Minus("–", "-"),
    Multiply("×", "*"),
    Divide("÷", "/"),
    Power("^"),
    EPower("E", "*10^")
}

enum class AdditionalOperator(val text: String, val value: String = text){
    Factorial("!"),
    Percent("%")
}

enum class ScientificFunction(val text: String, val value: String = text){
    Ln("ln${Bracket.OpenBracket.text}"),
    Log("log${Bracket.OpenBracket.text}"),
    PowerE("exp${Bracket.OpenBracket.text}", "e^${Bracket.OpenBracket.value}"),
    SquareRoot("√${Bracket.OpenBracket.text}"),
    Absolute("abs${Bracket.OpenBracket.text}")
}

enum class Bracket(val text: String, val value: String = text){
    OpenBracket("("),
    ClosedBracket(")")
}

enum class Constant(val text: String, val value: String = text){
    E("e"),
    PI("π")
}

enum class TrigonometricFunction(val text: String, val value: String = text){
    Sin("sin${Bracket.OpenBracket.text}"),
    Cos("cos${Bracket.OpenBracket.text}"),
    Tan("tan${Bracket.OpenBracket.text}"),
    ASin("sin⁻¹${Bracket.OpenBracket.text}", "asin${Bracket.OpenBracket.value}"),
    ACos("cos⁻¹${Bracket.OpenBracket.text}", "acos${Bracket.OpenBracket.value}"),
    ATan("tan⁻¹${Bracket.OpenBracket.text}", "atan${Bracket.OpenBracket.value}")
}