package com.forz.calculator.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates

object SettingsViewModel : ViewModel() {
    private val _theme = MutableLiveData<Int>()
    val theme: LiveData<Int> get() = _theme

    private val _color = MutableLiveData<String>()
    val color: LiveData<String> get() = _color

    private val _groupingSeparatorSymbol = MutableLiveData<String>()
    val groupingSeparatorSymbol: LiveData<String> get() = _groupingSeparatorSymbol
    var previousGroupingSeparatorSymbol: String by Delegates.notNull()

    private val _decimalSeparatorSymbol = MutableLiveData<String>()
    val decimalSeparatorSymbol: LiveData<String> get() = _decimalSeparatorSymbol
    var previousDecimalSeparatorSymbol: String by Delegates.notNull()

    private val _numberPrecision = MutableLiveData<Int>()
    val numberPrecision: LiveData<Int> get() = _numberPrecision

    private val _vibration = MutableLiveData<Boolean>()
    val vibration: LiveData<Boolean> get() = _vibration

    private val _sound = MutableLiveData<Boolean>()
    val sound: LiveData<Boolean> get() = _sound



    init {

    }

    fun init(theme: Int,
             color: String,
             groupingSeparatorSymbol: String,
             decimalSeparatorSymbol: String,
             numberPrecision: Int,
             vibration: Boolean,
             sound: Boolean){

        _theme.value = theme
        _color.value = color
        _groupingSeparatorSymbol.value = groupingSeparatorSymbol
        previousGroupingSeparatorSymbol = groupingSeparatorSymbol
        _decimalSeparatorSymbol.value = decimalSeparatorSymbol
        previousDecimalSeparatorSymbol = decimalSeparatorSymbol
        _numberPrecision.value = numberPrecision
        _vibration.value = vibration
        _sound.value = sound
    }


    fun setNumberPrecision(value: Int){
        _numberPrecision.value = value
    }

    fun setGroupingSeparatorSymbol(symbol: String){
        _groupingSeparatorSymbol.value = symbol
    }

    fun setDecimalSeparatorSymbol(symbol: String){
        _decimalSeparatorSymbol.value = symbol
    }

    fun setVibration(value: Boolean){
        _vibration.value = value
    }

    fun setSound(value: Boolean){
        _sound.value = value
    }
}