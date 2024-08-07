package com.forz.calculator.fragments.xLargeLand

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.forz.calculator.utils.Anim
import com.forz.calculator.calculator.AdditionalOperator
import com.forz.calculator.utils.HapticAndSound
import com.forz.calculator.databinding.FragmentXLargeLandCalculatorBinding
import com.forz.calculator.settings.Config.decimalSeparatorSymbol
import com.forz.calculator.calculator.Constant
import com.forz.calculator.calculator.DefaultOperator
import com.forz.calculator.calculator.ScientificFunction
import com.forz.calculator.calculator.TrigonometricFunction
import kotlin.properties.Delegates.notNull

class XLargeLandCalculatorFragment : Fragment() {

    private var binding: FragmentXLargeLandCalculatorBinding by notNull()
    private var hapticAndSound: HapticAndSound by notNull()
    private var callback: OnButtonClickListener? = null

    interface OnButtonClickListener {
        fun onDigitButtonClick(digit: String)
        fun onDotButtonClick()
        fun onBackspaceButtonClick()
        fun onClearExpressionButtonClick()
        fun onOperatorButtonClick(operator: String)
        fun onScienceFunctionButtonClick(scienceFunction: String)
        fun onAdditionalOperatorButtonClick(operator: String)
        fun onConstantButtonClick(constant: String)
        fun onBracketButtonClick()
        fun onDoubleBracketsButtonClick()
        fun onEqualsButtonClick()
        fun onEqualsButtonLongClick()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = parentFragment as OnButtonClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnButtonClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentXLargeLandCalculatorBinding.inflate(inflater, container, false)

        binding.dotButton.text = decimalSeparatorSymbol

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
            binding.absButton,
            binding.lnButton,
            binding.logButton,
            binding.sinButton,
            binding.asinButton,
            binding.cosButton,
            binding.acosButton,
            binding.tanButton,
            binding.atanButton,
            binding.eButton,
            binding.expButton
        )

        hapticAndSound = HapticAndSound(requireContext(), views)


        binding.backspaceButton.setOnClickListener {
            callback?.onBackspaceButtonClick()
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.backspaceButton, requireContext())
        }
        binding.backspaceButton.setOnLongClickListener {
            callback?.onClearExpressionButtonClick()
            hapticAndSound.vibrateEffectHeavyClick()
            Anim.buttonAnim(binding.backspaceButton, requireContext())
            true
        }
        binding.powerButton.setOnClickListener {
            callback?.onOperatorButtonClick(DefaultOperator.Power.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.divisionButton.setOnClickListener {
            callback?.onOperatorButtonClick(DefaultOperator.Divide.text)
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.divisionButton, requireContext())
        }
        binding.multiplicationButton.setOnClickListener {
            callback?.onOperatorButtonClick(DefaultOperator.Multiply.text)
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.multiplicationButton, requireContext())
        }
        binding.minusButton.setOnClickListener {
            callback?.onOperatorButtonClick(DefaultOperator.Minus.text)
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.minusButton, requireContext())
        }
        binding.plusButton.setOnClickListener {
            callback?.onOperatorButtonClick(DefaultOperator.Plus.text)
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.plusButton, requireContext())
        }
        binding.sqrtButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(ScientificFunction.SquareRoot.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.percentageButton.setOnClickListener {
            callback?.onAdditionalOperatorButtonClick(AdditionalOperator.Percent.text)
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.percentageButton, requireContext())
        }
        binding.factorialButton.setOnClickListener {
            callback?.onAdditionalOperatorButtonClick(AdditionalOperator.Factorial.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.piButton.setOnClickListener {
            callback?.onConstantButtonClick(Constant.PI.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.bracketsButton.setOnClickListener {
            callback?.onBracketButtonClick()
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.bracketsButton, requireContext())
        }
        binding.bracketsButton.setOnLongClickListener {
            callback?.onDoubleBracketsButtonClick()
            hapticAndSound.vibrateEffectHeavyClick()
            Anim.buttonAnim(binding.bracketsButton, requireContext())
            true
        }
        binding.clearButton.setOnClickListener {
            callback?.onClearExpressionButtonClick()
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.clearButton, requireContext())
        }
        binding.equalsButton.setOnClickListener {
            callback?.onEqualsButtonClick()
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.equalsButton, requireContext())
        }
        binding.equalsButton.setOnLongClickListener {
            callback?.onEqualsButtonLongClick()
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.equalsButton, requireContext())
            true
        }
        binding.digit0Button.setOnClickListener {
            callback?.onDigitButtonClick("0")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit0Button, requireContext())
        }
        binding.digit1Button.setOnClickListener {
            callback?.onDigitButtonClick("1")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit1Button, requireContext())
        }
        binding.digit2Button.setOnClickListener {
            callback?.onDigitButtonClick("2")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit2Button, requireContext())
        }
        binding.digit3Button.setOnClickListener {
            callback?.onDigitButtonClick("3")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit3Button, requireContext())
        }
        binding.digit4Button.setOnClickListener {
            callback?.onDigitButtonClick("4")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit4Button, requireContext())
        }
        binding.digit5Button.setOnClickListener {
            callback?.onDigitButtonClick("5")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit5Button, requireContext())
        }
        binding.digit6Button.setOnClickListener {
            callback?.onDigitButtonClick("6")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit6Button, requireContext())
        }
        binding.digit7Button.setOnClickListener {
            callback?.onDigitButtonClick("7")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit7Button, requireContext())
        }
        binding.digit8Button.setOnClickListener {
            callback?.onDigitButtonClick("8")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit8Button, requireContext())
        }
        binding.digit9Button.setOnClickListener {
            callback?.onDigitButtonClick("9")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit9Button, requireContext())
        }
        binding.dotButton.setOnClickListener {
            callback?.onDotButtonClick()
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.dotButton, requireContext())
        }

        binding.absButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(ScientificFunction.Absolute.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.lnButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(ScientificFunction.Ln.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.logButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(ScientificFunction.Log.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.sinButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(TrigonometricFunction.Sin.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.cosButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(TrigonometricFunction.Cos.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.tanButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(TrigonometricFunction.Tan.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.asinButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(TrigonometricFunction.ASin.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.acosButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(TrigonometricFunction.ACos.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.atanButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(TrigonometricFunction.ATan.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.eButton.setOnClickListener {
            callback?.onConstantButtonClick(Constant.E.text)
            hapticAndSound.vibrateEffectClick()
        }
        binding.expButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(ScientificFunction.PowerE.text)
            hapticAndSound.vibrateEffectClick()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        hapticAndSound.setHapticFeedback()
        hapticAndSound.setSoundEffects()
    }
}