@file:Suppress("DEPRECATION")

package com.forz.calculator

import android.content.res.Configuration
import android.content.res.Resources
import android.util.TypedValue
import android.widget.EditText
import android.widget.TextView

object AutoSizeText {
    private var expressionTextSize = 0f
    private var resultTextSize = 0f
    fun expression(inputEditText: EditText, resources: Resources){
        var textSize = 34f
        val step = 1f
        var high = 64f
        val maxWidth = inputEditText.width.toFloat()

        when (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
            Configuration.SCREENLAYOUT_SIZE_SMALL -> {

            }
            Configuration.SCREENLAYOUT_SIZE_NORMAL -> {
                textSize = 34f
                high = 64f
            }
            Configuration.SCREENLAYOUT_SIZE_XLARGE -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        textSize = 60f
                        high = 90f
                    }
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        textSize = 46f
                        high = 72f
                    }

                    Configuration.ORIENTATION_SQUARE -> {
                        TODO()
                    }

                    Configuration.ORIENTATION_UNDEFINED -> {
                        TODO()
                    }
                }
            }
            Configuration.SCREENLAYOUT_SIZE_LARGE -> {
                textSize = 46f
                high = 74f
            }
        }


        if (maxWidth == 0f){
            if (expressionTextSize == 0f){
                expressionTextSize = high
            }
            inputEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, expressionTextSize)
        } else {
            var low = textSize

            while (low <= high) {
                val size = (low + high) / 2
                inputEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)

                val paint = inputEditText.paint
                val width = paint.measureText(inputEditText.text.toString())

                if (width <= maxWidth) {
                    textSize = size
                    low = size + step
                } else {
                    high = size - step
                }
            }

            inputEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
            expressionTextSize = textSize
        }
    }

    fun result(inputTextView: TextView, resources: Resources){
        var textSize = 8f
        val step = 1f
        var high = 42f
        val maxWidth = inputTextView.width.toFloat()

        when (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
            Configuration.SCREENLAYOUT_SIZE_NORMAL -> {
                textSize = 8f
                high = 42f
            }

            Configuration.SCREENLAYOUT_SIZE_XLARGE -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        textSize = 8f
                        high = 58f
                    }

                    Configuration.ORIENTATION_LANDSCAPE -> {
                        textSize = 8f
                        high = 58f
                    }

                    Configuration.ORIENTATION_SQUARE -> {
                        TODO()
                    }

                    Configuration.ORIENTATION_UNDEFINED -> {
                        TODO()
                    }
                }
            }

            Configuration.SCREENLAYOUT_SIZE_LARGE -> {
                textSize = 8f
                high = 46f
            }
        }


        if (maxWidth == 0f){
            if (resultTextSize == 0f){
                resultTextSize = high
            }
            inputTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, resultTextSize)
        } else {
            var low = textSize

            while (low <= high) {
                val size = (low + high) / 2
                inputTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)

                val paint = inputTextView.paint
                val width = paint.measureText(inputTextView.text.toString())

                if (width <= maxWidth) {
                    textSize = size
                    low = size + step
                } else {
                    high = size - step
                }
            }

            inputTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
            resultTextSize = textSize
        }
    }
}