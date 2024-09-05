package com.forz.calculator.fragments.smallLand

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.forz.calculator.App
import com.forz.calculator.OnMainActivityListener
import com.forz.calculator.databinding.FragmentSmallLandBinding
import com.forz.calculator.fragments.HistoryFragment
import com.forz.calculator.fragments.adapters.ViewPageAdapter
import com.forz.calculator.settings.Config
import com.forz.calculator.calculator.CalculatorViewModel
import com.forz.calculator.calculator.Evaluator
import com.forz.calculator.expression.ExpressionViewModel
import com.forz.calculator.expression.ExpressionViewModel.cursorPositionStart
import com.forz.calculator.expression.ExpressionViewModel.expression
import com.forz.calculator.expression.ExpressionViewModel.oldExpression
import com.forz.calculator.fragments.CalculatorFragment
import com.forz.calculator.fragments.Fragments.CALCULATOR_FRAGMENT
import com.forz.calculator.fragments.Fragments.currentItemMainPager
import com.forz.calculator.fragments.UnitConverterFragment
import com.forz.calculator.history.HistoryService
import com.forz.calculator.settings.Config.autoSavingResults
import com.forz.calculator.utils.InsertInExpression
import kotlin.properties.Delegates.notNull

class SmallLandFragment : Fragment(),
    OnMainActivityListener,
    CalculatorFragment.OnButtonClickListener,
    HistoryFragment.OnButtonClickListener,
    UnitConverterFragment.OnButtonClickListener
{

    private var result: String by notNull()
    private var binding: FragmentSmallLandBinding by notNull()


    private val historyService: HistoryService
        get() = (requireContext().applicationContext as App).historyService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSmallLandBinding.inflate(inflater, container, false)

        binding.expressionEditText.showSoftInputOnFocus = false
        binding.expressionEditText.requestFocus()


        val adapter = ViewPageAdapter(childFragmentManager, lifecycle)

        adapter.addFragment(UnitConverterFragment())
        adapter.addFragment(CalculatorFragment())
        adapter.addFragment(HistoryFragment())

        binding.pager.adapter = adapter
        binding.pager.setCurrentItem(currentItemMainPager, false)
        binding.pager.offscreenPageLimit = 3

        binding.pager.apply {
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentItemMainPager = position
            }
        })

        binding.expressionEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                view.requestFocus()
            }
        }

        binding.expressionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                result = Evaluator.getResult(binding.expressionEditText, ExpressionViewModel.isSelected.value ?: false, requireContext())
                binding.expressionEditText.autoSizeTextExpressionEditText(binding.expressionTextView)
            }
        })

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.expressionEditText.autoSizeTextExpressionEditText(binding.expressionTextView)
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        ExpressionViewModel.isSelected.observe(requireActivity()){ isSelected ->
            result = Evaluator.getResult(binding.expressionEditText, isSelected, requireContext())
        }

        CalculatorViewModel.isDegreeModActivated.observe(requireActivity()) { _ ->
            result = Evaluator.getResult(binding.expressionEditText, ExpressionViewModel.isSelected.value ?: false, requireContext())
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.pager.isUserInputEnabled = Config.swipeMain
        binding.expressionEditText.setText(expression)
        binding.expressionEditText.setSelection(cursorPositionStart)
    }

    override fun onStop() {
        super.onStop()

        expression = binding.expressionEditText.text.toString()
        cursorPositionStart = binding.expressionEditText.selectionStart

        if (Evaluator.isCalculated && autoSavingResults){
            val result = result

            val expression: String = if (ExpressionViewModel.isSelected.value == true){
                binding.expressionEditText.text
                    .toString()
                    .substring(
                        binding.expressionEditText.selectionStart, binding.expressionEditText.selectionEnd
                    )
            } else {
                binding.expressionEditText.text.toString()
            }

            historyService.addHistoryData(expression, result)
        }
    }

    override fun onBackPressed(): Boolean {
        return if (currentItemMainPager != CALCULATOR_FRAGMENT) {
            binding.pager.setCurrentItem(CALCULATOR_FRAGMENT, true)
            true
        } else {
            false
        }
    }

    override fun onDigitButtonClick(digit: String) {
        InsertInExpression.enterDigit(digit, binding.expressionEditText)
    }

    override fun onDotButtonClick() {
        InsertInExpression.enterDot(binding.expressionEditText)
    }

    override fun onBackspaceButtonClick() {
        InsertInExpression.enterBackspace(binding.expressionEditText)
    }

    override fun onClearExpressionButtonClick() {
        InsertInExpression.clearExpression(binding.expressionEditText)
    }

    override fun onOperatorButtonClick(operator: String) {
        InsertInExpression.enterOperator(operator, binding.expressionEditText)
    }

    override fun onScienceFunctionButtonClick(scienceFunction: String) {
        InsertInExpression.enterScienceFunction(scienceFunction, binding.expressionEditText)
    }

    override fun onAdditionalOperatorButtonClick(operator: String) {
        InsertInExpression.enterAdditionalOperator(operator, binding.expressionEditText)
    }

    override fun onConstantButtonClick(constant: String) {
        InsertInExpression.enterConstant(constant, binding.expressionEditText)
    }

    override fun onBracketButtonClick() {
        InsertInExpression.enterBracket(binding.expressionEditText)
    }

    override fun onDoubleBracketsButtonClick() {
        InsertInExpression.enterDoubleBrackets(binding.expressionEditText)
    }

    override fun onEqualsButtonClick() {
        if (Evaluator.isCalculated){
            val result = result

            val expression: String = if (ExpressionViewModel.isSelected.value == true){
                binding.expressionEditText.text
                    .toString()
                    .substring(
                        binding.expressionEditText.selectionStart, binding.expressionEditText.selectionEnd
                    )
            } else {
                binding.expressionEditText.text.toString()
            }

            oldExpression = expression
            InsertInExpression.setExpression(result, binding.expressionEditText)
            historyService.addHistoryData(expression, result)
        }
    }

    override fun onEqualsButtonLongClick() {
        if (oldExpression.isNotEmpty()){
            InsertInExpression.setExpression(oldExpression, binding.expressionEditText)
        }
    }

    override fun onExpressionTextClick(expression: String) {
        InsertInExpression.insertHistoryExpression(expression, binding.expressionEditText)
    }

    override fun onResultTextClick(result: String) {
        InsertInExpression.insertHistoryResult(result, binding.expressionEditText)
    }

    override fun onUnitResultTextClick(result: String) {
        InsertInExpression.setExpression(result, binding.expressionEditText)
    }
}