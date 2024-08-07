package com.forz.calculator.expression

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object ExpressionViewModel: ViewModel() {
    var expression: String = ""
    var cursorPositionStart: Int = 0
    var oldExpression: String = ""

    private val _isSelected = MutableLiveData<Boolean>()
    val isSelected: LiveData<Boolean> get() = _isSelected


    init {
        _isSelected.value = false
    }

    fun updateSelection(value: Boolean){
        _isSelected.value = value
    }
}
