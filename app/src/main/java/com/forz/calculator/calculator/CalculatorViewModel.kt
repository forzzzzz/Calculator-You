package com.forz.calculator.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates

object CalculatorViewModel : ViewModel() {
    private val _isDegreeModActivated = MutableLiveData<Boolean>()
    val isDegreeModActivated: LiveData<Boolean> get() = _isDegreeModActivated

    private val _isScienceModActivated = MutableLiveData<Boolean>()
    val isScienceModActivated: LiveData<Boolean> get() = _isScienceModActivated
    var previousScienceModActivated: Boolean by Delegates.notNull()



    init {
        _isScienceModActivated.value = false
        previousScienceModActivated = false
    }

    fun init(isDegreeModActivated: Boolean){
        _isDegreeModActivated.value = isDegreeModActivated
    }

    fun updateDegreeModActivated(){
        _isDegreeModActivated.value = !isDegreeModActivated.value!!
    }

    fun updateScienceModActivated(){
        val oldScienceModActivated = _isScienceModActivated.value!!
        val newScienceModActivated = !oldScienceModActivated

        _isScienceModActivated.value = newScienceModActivated
        previousScienceModActivated = newScienceModActivated
    }
}