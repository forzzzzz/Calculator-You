package com.forz.calculator.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.forz.calculator.utils.Anim
import com.forz.calculator.utils.HapticAndSound
import com.forz.calculator.R
import com.forz.calculator.databinding.FragmentCalculatorBinding
import com.forz.calculator.fragments.Fragments.DIGIT_FRAGMENT
import com.forz.calculator.fragments.Fragments.SCIENTIFIC_FUNCTION_FRAGMENT
import com.forz.calculator.fragments.adapters.ViewPageAdapter
import com.forz.calculator.settings.Config
import com.forz.calculator.calculator.CalculatorViewModel.isScienceModActivated
import com.forz.calculator.calculator.CalculatorViewModel.previousScienceModActivated
import com.forz.calculator.calculator.CalculatorViewModel.updateScienceModActivated
import com.forz.calculator.calculator.AdditionalOperator
import com.forz.calculator.calculator.Constant
import com.forz.calculator.calculator.DefaultOperator
import com.forz.calculator.calculator.ScientificFunction
import kotlin.properties.Delegates.notNull

class CalculatorFragment : Fragment(), DigitFragment.OnButtonClickListener, ScientificFunctionFragment.OnButtonClickListener {

    private var binding: FragmentCalculatorBinding by notNull()
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
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)

        val views: Array<View> = arrayOf(
            binding.scienceModButton,
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
            binding.backspaceButton
        )

        hapticAndSound = HapticAndSound(requireContext(), views)


        val adapter = ViewPageAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(ScientificFunctionFragment())
        adapter.addFragment(DigitFragment())
        binding.pager.adapter = adapter
        binding.pager.offscreenPageLimit = 2
        binding.pager.apply {
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        val userScroll = arrayListOf<Int>()
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (userScroll.contains(1)) {
                    updateScienceModActivated()
                }
                userScroll.clear()
            }
            override fun onPageScrollStateChanged(state: Int) {
                userScroll.add(state)
                if ((userScroll.contains(0) && userScroll.contains(1) && userScroll.contains(2)) || userScroll.contains(0)){
                    userScroll.clear()
                }
            }
        })

        binding.scienceModButton.setOnClickListener {
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }


        isScienceModActivated.observe(viewLifecycleOwner) { isScienceModActivated ->
            if (isScienceModActivated != previousScienceModActivated){
                if (isScienceModActivated){
                    Anim.startVectorAnimation(binding.scienceModButton, R.drawable.anim_science_to_123)

                    binding.pager.setCurrentItem(SCIENTIFIC_FUNCTION_FRAGMENT, true)
                } else{
                    Anim.startVectorAnimation(binding.scienceModButton, R.drawable.anim_123_to_science)

                    binding.pager.setCurrentItem(DIGIT_FRAGMENT, true)
                }
            } else{
                if (isScienceModActivated){
                    binding.scienceModButton.setImageResource(R.drawable.baseline_123)

                    binding.pager.setCurrentItem(SCIENTIFIC_FUNCTION_FRAGMENT, false)
                } else{
                    binding.scienceModButton.setImageResource(R.drawable.baseline_science)

                    binding.pager.setCurrentItem(DIGIT_FRAGMENT, false)
                }
            }

        }





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
            hapticAndSound.vibrateEffectHeavyClick()
            Anim.buttonAnim(binding.equalsButton, requireContext())
            true
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        hapticAndSound.setHapticFeedback()
        hapticAndSound.setSoundEffects()
        binding.pager.isUserInputEnabled = Config.swipeDigitsAndScientificFunctions
    }


    override fun onDigitButtonClick(digit: String) {
        callback?.onDigitButtonClick(digit)
    }

    override fun onDotButtonClick() {
        callback?.onDotButtonClick()
    }

    override fun onScienceFunctionButtonClick(scienceFunction: String) {
        callback?.onScienceFunctionButtonClick(scienceFunction)
    }

    override fun onConstantButtonClick(constant: String) {
        callback?.onConstantButtonClick(constant)
    }
}