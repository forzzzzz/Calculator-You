package com.forz.calculator.fragments.small

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.darkempire78.opencalculator.division_by_0
import com.darkempire78.opencalculator.is_infinity
import com.darkempire78.opencalculator.require_real_number
import com.forz.calculator.Anim
import com.forz.calculator.App
import com.forz.calculator.HapticAndSound
import com.forz.calculator.R
import com.forz.calculator.databinding.FragmentSmallCalculatorBinding
import com.forz.calculator.fragments.DigitFragment
import com.forz.calculator.fragments.ScientificFunctionFragment
import com.forz.calculator.fragments.adapters.ViewPageAdapter
import com.forz.calculator.history.HistoryService
import com.forz.calculator.settings.SettingsState
import com.forz.calculator.viewModels.CalculatorViewModel.isScienceModActivated
import com.forz.calculator.viewModels.CalculatorViewModel.previousScienceModActivated
import com.forz.calculator.viewModels.CalculatorViewModel.updateScienceModActivated
import com.forz.calculator.viewModels.ExpressionViewModel
import com.forz.calculator.viewModels.ExpressionViewModel.result
import com.forz.calculator.viewModels.ExpressionViewModel.saveExpression
import kotlin.properties.Delegates.notNull

class SmallCalculatorFragment : Fragment() {

    private var binding: FragmentSmallCalculatorBinding by notNull()
    private var hapticAndSound: HapticAndSound by notNull()

    private val historyService: HistoryService
        get() = (requireContext().applicationContext as App).historyService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSmallCalculatorBinding.inflate(inflater, container, false)

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
                    startVectorAnimation(binding.scienceModButton, R.drawable.anim_science_to_123)

                    binding.pager.setCurrentItem(0, true)
                } else{
                    startVectorAnimation(binding.scienceModButton, R.drawable.anim_123_to_science)

                    binding.pager.setCurrentItem(1, true)
                }
            } else{
                if (isScienceModActivated){
                    binding.scienceModButton.setImageResource(R.drawable.baseline_123)

                    binding.pager.setCurrentItem(0, false)
                } else{
                    binding.scienceModButton.setImageResource(R.drawable.baseline_science)

                    binding.pager.setCurrentItem(1, false)
                }
            }

        }





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

            if (result.value!!.isNotEmpty() && !is_infinity && !division_by_0 && !require_real_number){
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

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        hapticAndSound.setHapticFeedback()
        hapticAndSound.setSoundEffects()
        binding.pager.isUserInputEnabled = SettingsState.swipeDigitsAndScientificFunctions
    }


    private fun startVectorAnimation(imageButton: ImageButton, drawable: Int){
        imageButton.setImageResource(drawable)
        val anim = imageButton.drawable as AnimatedVectorDrawable
        anim.start()
    }
}