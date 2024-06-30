package com.forz.calculator.fragments.smallLand

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.forz.calculator.InsertInExpression
import com.forz.calculator.NumberFormatter
import com.forz.calculator.OnBackPressedListener
import com.forz.calculator.Preferences
import com.forz.calculator.StateViews
import com.forz.calculator.databinding.FragmentSmallLandBinding
import com.forz.calculator.fragments.HistoryFragment
import com.forz.calculator.fragments.adapters.ViewPageAdapter
import com.forz.calculator.fragments.small.SmallCalculatorFragment
import com.forz.calculator.settings.SettingsState
import com.forz.calculator.viewModels.CalculatorViewModel
import com.forz.calculator.viewModels.ExpressionViewModel
import kotlin.properties.Delegates

class SmallLandFragment : Fragment(), OnBackPressedListener {


    private var binding: FragmentSmallLandBinding by Delegates.notNull()
    private var preferences: Preferences by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSmallLandBinding.inflate(inflater, container, false)
        preferences = Preferences(requireContext())

        binding.expressionEditText.showSoftInputOnFocus = false


        val adapter = ViewPageAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(SmallCalculatorFragment())
        adapter.addFragment(HistoryFragment())
        binding.pager.adapter = adapter
        binding.pager.setCurrentItem(StateViews.currentItemPager, false)
        binding.pager.offscreenPageLimit = 2

        binding.pager.apply {
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }


        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                StateViews.currentItemPager = position
            }
        })

        CalculatorViewModel.isDegreeModActivated.observe(requireActivity()) { _ ->
            ExpressionViewModel.updateResult(ExpressionViewModel.expression.value!!, requireContext())
        }

        ExpressionViewModel.expression.observe(requireActivity()){ expression ->
            val expressionCursorPositionStart = ExpressionViewModel.expressionCursorPositionStart.value!!
            val expressionCursorPositionEnd = ExpressionViewModel.expressionCursorPositionEnd.value!!
            binding.expressionEditText.text = NumberFormatter.changeColorOperators(expression, requireContext())
            binding.expressionEditText.setSelection(expressionCursorPositionStart, expressionCursorPositionEnd)

            ExpressionViewModel.updateResult(expression, requireContext())
        }

        binding.expressionEditText.requestFocus()
        binding.expressionEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                view.requestFocus()
            }
        }


        binding.expressionEditText.addTextChangedListener{
            if (ExpressionViewModel.expression.value!! != binding.expressionEditText.text.toString()){
                ExpressionViewModel.updateNumberOfCharactersOfInsertedText(binding.expressionEditText.text!!.length)
                ExpressionViewModel.updateExpression(binding.expressionEditText.text.toString(), binding.expressionEditText.selectionStart)
            }

            autoSizeTextExpressionEditText()
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                autoSizeTextExpressionEditText()
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })


        ExpressionViewModel.isSelection.observe(requireActivity()){ isSelection ->
            if (isSelection){
                ExpressionViewModel.updateResult(
                    ExpressionViewModel.expression.value!!.substring(
                        ExpressionViewModel.expressionCursorPositionStart.value!!, ExpressionViewModel.expressionCursorPositionEnd.value!!), requireContext())
            }else if (!isSelection && ExpressionViewModel.previousIsSelection){
                ExpressionViewModel.updateResult(ExpressionViewModel.expression.value!!, requireContext())
            } else if (InsertInExpression.stringAfterCursor(ExpressionViewModel.expressionCursorPositionStart.value!!, ExpressionViewModel.expression.value!!).startsWith(
                    SettingsState.groupingSeparatorSymbol
                )){
                binding.expressionEditText.setSelection(ExpressionViewModel.expressionCursorPositionStart.value!! + 1)
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        SettingsState.init(
            preferences.getTheme(),
            preferences.getColor(),
            preferences.getDynamicColor(),
            preferences.getGroupingSeparatorSymbol(),
            preferences.getDecimalSeparatorSymbol(),
            preferences.getNumberPrecision(),
            preferences.getSwipeHistoryAndCalculator(),
            preferences.getSwipeDigitsAndScientificFunctions(),
            preferences.getVibration(),
            preferences.getSoundEffects()
        )
        CalculatorViewModel.init(preferences.getDegreeMod())

        binding.pager.isUserInputEnabled = SettingsState.swipeHistoryAndCalculator
    }

    override fun onStop() {
        super.onStop()

        preferences.setDegreeMod(CalculatorViewModel.isDegreeModActivated.value!!)
    }


    override fun onBackPressed(): Boolean {
        return if (binding.pager.currentItem == 1) {
            binding.pager.setCurrentItem(0, true)
            true
        } else {
            false
        }
    }


    private fun autoSizeTextExpressionEditText(){
        binding.expressionTextView.text = binding.expressionEditText.text
        binding.expressionEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.expressionTextView.textSize)
    }
}