package com.forz.calculator

import android.content.res.Configuration
import android.content.res.Resources
import android.util.TypedValue
import android.widget.EditText
//import android.widget.TextView

object AutoSizeText {
    private var expressionTextSize = 0f
//    private var resultTextSize = 0f
    fun expression(inputEditText: EditText, resources: Resources){
        var textSize: Float
        val step = 1f
        var high: Float
        val maxWidth = inputEditText.width.toFloat()

        when (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
            Configuration.SCREENLAYOUT_SIZE_SMALL -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        textSize = 60f
                        high = 90f
                    }
                    else -> {
                        textSize = 60f
                        high = 90f
                    }
                }
            }
            Configuration.SCREENLAYOUT_SIZE_XLARGE -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        textSize = 60f
                        high = 90f
                    }
                    else -> {
                        textSize = 60f
                        high = 90f
                    }
                }
            }
            Configuration.SCREENLAYOUT_SIZE_LARGE -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        textSize = 46f
                        high = 74f
                    }
                    else -> {
                        textSize = 46f
                        high = 74f
                    }
                }
            }
            else -> {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        textSize = 36f
                        high = 60f
                    }
                    else -> {
                        textSize = 40f
                        high = 64f
                    }
                }
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

//    fun result(inputTextView: TextView, resources: Resources){
//        var textSize: Float
//        val step = 1f
//        var high: Float
//        val maxWidth = inputTextView.width.toFloat()
//
//        when (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
//            Configuration.SCREENLAYOUT_SIZE_SMALL -> {
//                when (resources.configuration.orientation) {
//                    Configuration.ORIENTATION_LANDSCAPE -> {
//                        textSize = 60f
//                        high = 90f
//                    }
//                    else -> {
//                        textSize = 60f
//                        high = 90f
//                    }
//                }
//            }
//            Configuration.SCREENLAYOUT_SIZE_XLARGE -> {
//                when (resources.configuration.orientation) {
//                    Configuration.ORIENTATION_LANDSCAPE -> {
//                        textSize = 8f
//                        high = 58f
//                    }
//                    else -> {
//                        textSize = 8f
//                        high = 58f
//                    }
//                }
//            }
//            Configuration.SCREENLAYOUT_SIZE_LARGE -> {
//                when (resources.configuration.orientation) {
//                    Configuration.ORIENTATION_LANDSCAPE -> {
//                        textSize = 8f
//                        high = 46f
//                    }
//                    else -> {
//                        textSize = 8f
//                        high = 46f
//                    }
//                }
//            }
//            else -> {
//                when (resources.configuration.orientation) {
//                    Configuration.ORIENTATION_LANDSCAPE -> {
//                        textSize = 8f
//                        high = 38f
//                    }
//                    else -> {
//                        textSize = 8f
//                        high = 42f
//                    }
//                }
//            }
//        }
//
//
//        if (maxWidth == 0f){
//            if (resultTextSize == 0f){
//                resultTextSize = high
//            }
//            inputTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, resultTextSize)
//        } else {
//            var low = textSize
//
//            while (low <= high) {
//                val size = (low + high) / 2
//                inputTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
//
//                val paint = inputTextView.paint
//                val width = paint.measureText(inputTextView.text.toString())
//
//                if (width <= maxWidth) {
//                    textSize = size
//                    low = size + step
//                } else {
//                    high = size - step
//                }
//            }
//
//            inputTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
//            resultTextSize = textSize
//        }
//    }
}