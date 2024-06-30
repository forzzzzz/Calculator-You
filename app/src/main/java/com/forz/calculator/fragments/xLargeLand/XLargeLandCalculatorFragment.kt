package com.forz.calculator.fragments.xLargeLand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darkempire78.opencalculator.division_by_0
import com.darkempire78.opencalculator.domain_error
import com.darkempire78.opencalculator.is_infinity
import com.darkempire78.opencalculator.require_real_number
import com.forz.calculator.Anim
import com.forz.calculator.App
import com.forz.calculator.HapticAndSound
import com.forz.calculator.R
import com.forz.calculator.databinding.FragmentXLargeLandCalculatorBinding
import com.forz.calculator.history.HistoryService
import com.forz.calculator.settings.SettingsState.decimalSeparatorSymbol
import com.forz.calculator.viewModels.CalculatorViewModel.isDegreeModActivated
import com.forz.calculator.viewModels.CalculatorViewModel.updateDegreeModActivated
import com.forz.calculator.viewModels.ExpressionViewModel
import com.forz.calculator.viewModels.ExpressionViewModel.result
import com.forz.calculator.viewModels.ExpressionViewModel.saveExpression
import kotlin.properties.Delegates.notNull

class XLargeLandCalculatorFragment : Fragment() {

    private var binding: FragmentXLargeLandCalculatorBinding by notNull()
    private var hapticAndSound: HapticAndSound by notNull()

    private val historyService: HistoryService
        get() = (requireContext().applicationContext as App).historyService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentXLargeLandCalculatorBinding.inflate(inflater, container, false)

        val views: Array<View> = arrayOf(
            binding.sqrtButton,
            binding.piButton,
            binding.powerButton,
            binding.factorialButton,
            binding.clearButton,
            binding.bracketsButton,
            binding.percentageButton,
            binding.divisionButton,
            binding.multiplicationButton,
            binding.minusButton,
            binding.plusButton,
            binding.equalsButton,
            binding.backspaceButton,
            binding.digit0Button,
            binding.digit1Button,
            binding.digit2Button,
            binding.digit3Button,
            binding.digit4Button,
            binding.digit5Button,
            binding.digit6Button,
            binding.digit7Button,
            binding.digit8Button,
            binding.digit9Button,
            binding.dotButton,
            binding.degreeButton,
            binding.lnButton,
            binding.logButton,
            binding.sinButton,
            binding.sin1Button,
            binding.cosButton,
            binding.cos1Button,
            binding.tanButton,
            binding.tan1Button,
            binding.eButton,
            binding.expButton
        )

        hapticAndSound = HapticAndSound(requireContext(), views)


        binding.backspaceButton.setOnClickListener {
            ExpressionViewModel.enterBackspace()
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.backspaceButton, requireContext())
        }
        binding.backspaceButton.setOnLongClickListener {
            ExpressionViewModel.clearExpression()
            hapticAndSound.vibrateEffectHeavyClick()
            Anim.buttonAnim(binding.backspaceButton, requireContext())
            true
        }
        binding.powerButton.setOnClickListener {
            ExpressionViewModel.enterOperator("^(")
            hapticAndSound.vibrateEffectClick()
        }
        binding.divisionButton.setOnClickListener {
            ExpressionViewModel.enterOperator("÷")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.divisionButton, requireContext())
        }
        binding.multiplicationButton.setOnClickListener {
            ExpressionViewModel.enterOperator("×")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.multiplicationButton, requireContext())
        }
        binding.minusButton.setOnClickListener {
            ExpressionViewModel.enterOperator("–")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.minusButton, requireContext())
        }
        binding.plusButton.setOnClickListener {
            ExpressionViewModel.enterOperator("+")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.plusButton, requireContext())
        }
        binding.sqrtButton.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("√(")
            hapticAndSound.vibrateEffectClick()
        }
        binding.percentageButton.setOnClickListener {
            ExpressionViewModel.enterOperator2("%")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.percentageButton, requireContext())
        }
        binding.factorialButton.setOnClickListener {
            ExpressionViewModel.enterOperator2("!")
            hapticAndSound.vibrateEffectClick()
        }
        binding.piButton.setOnClickListener {
            ExpressionViewModel.enterConstant("π")
            hapticAndSound.vibrateEffectClick()
        }
        binding.bracketsButton.setOnClickListener {
            ExpressionViewModel.enterBracket()
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.bracketsButton, requireContext())
        }
        binding.bracketsButton.setOnLongClickListener {
            ExpressionViewModel.enterDoubleBrackets()
            hapticAndSound.vibrateEffectHeavyClick()
            Anim.buttonAnim(binding.bracketsButton, requireContext())
            true
        }
        binding.clearButton.setOnClickListener {
            ExpressionViewModel.clearExpression()
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.clearButton, requireContext())
        }
        binding.equalsButton.setOnClickListener {
            val expression: String

            if (result.value!!.isNotEmpty() && !is_infinity && !division_by_0 && !require_real_number && !domain_error){
                if (ExpressionViewModel.isSelection.value!!){
                    expression = ExpressionViewModel.expression.value!!.substring(ExpressionViewModel.expressionCursorPositionStart.value!!, ExpressionViewModel.expressionCursorPositionEnd.value!!)
                    ExpressionViewModel.updateSaveExpression(expression)
                }else{
                    expression = ExpressionViewModel.expression.value!!
                    ExpressionViewModel.updateSaveExpression(expression)
                }

                historyService.addHistoryData(expression, result.value!!)
                ExpressionViewModel.updateExpression(result.value!!, result.value!!.length)
            }

            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.equalsButton, requireContext())
        }
        binding.equalsButton.setOnLongClickListener {
            if (saveExpression.isNotEmpty()){
                ExpressionViewModel.updateExpression(saveExpression, saveExpression.length)
            }
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.equalsButton, requireContext())
            true
        }

        binding.digit0Button.setOnClickListener {
            ExpressionViewModel.enterDigit("0")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit0Button, requireContext())
        }
        binding.digit1Button.setOnClickListener {
            ExpressionViewModel.enterDigit("1")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit1Button, requireContext())
        }
        binding.digit2Button.setOnClickListener {
            ExpressionViewModel.enterDigit("2")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit2Button, requireContext())
        }
        binding.digit3Button.setOnClickListener {
            ExpressionViewModel.enterDigit("3")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit3Button, requireContext())
        }
        binding.digit4Button.setOnClickListener {
            ExpressionViewModel.enterDigit("4")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit4Button, requireContext())
        }
        binding.digit5Button.setOnClickListener {
            ExpressionViewModel.enterDigit("5")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit5Button, requireContext())
        }
        binding.digit6Button.setOnClickListener {
            ExpressionViewModel.enterDigit("6")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit6Button, requireContext())
        }
        binding.digit7Button.setOnClickListener {
            ExpressionViewModel.enterDigit("7")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit7Button, requireContext())
        }
        binding.digit8Button.setOnClickListener {
            ExpressionViewModel.enterDigit("8")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit8Button, requireContext())
        }
        binding.digit9Button.setOnClickListener {
            ExpressionViewModel.enterDigit("9")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit9Button, requireContext())
        }
        binding.dotButton.setOnClickListener {
            ExpressionViewModel.enterDot()
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.dotButton, requireContext())
        }

        binding.degreeButton.setOnClickListener {
            updateDegreeModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.lnButton.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("ln(")
            hapticAndSound.vibrateEffectClick()
        }
        binding.logButton.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("log(")
            hapticAndSound.vibrateEffectClick()
        }
        binding.sinButton.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("sin(")
            hapticAndSound.vibrateEffectClick()
        }
        binding.cosButton.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("cos(")
            hapticAndSound.vibrateEffectClick()
        }
        binding.tanButton.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("tan(")
            hapticAndSound.vibrateEffectClick()
        }
        binding.sin1Button.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("sin⁻¹(")
            hapticAndSound.vibrateEffectClick()
        }
        binding.cos1Button.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("cos⁻¹(")
            hapticAndSound.vibrateEffectClick()
        }
        binding.tan1Button.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("tan⁻¹(")
            hapticAndSound.vibrateEffectClick()
        }
        binding.eButton.setOnClickListener {
            ExpressionViewModel.enterConstant("e")
            hapticAndSound.vibrateEffectClick()
        }
        binding.expButton.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("exp(")
            hapticAndSound.vibrateEffectClick()
        }


        isDegreeModActivated.observe(viewLifecycleOwner){ isDegreeModActivated ->
            if (isDegreeModActivated) {
                binding.degreeButton.text = getString(R.string.deg)
            } else {
                binding.degreeButton.text = getString(R.string.rad)
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.dotButton.text = decimalSeparatorSymbol

        hapticAndSound.setHapticFeedback()
        hapticAndSound.setSoundEffects()
    }
}