package com.forz.calculator.fragments.land

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.forz.calculator.AboutActivity
import com.forz.calculator.App
import com.forz.calculator.utils.HapticAndSound
import com.forz.calculator.MainActivity
import com.forz.calculator.OnMainActivityListener
import com.forz.calculator.R
import com.forz.calculator.databinding.FragmentLandBinding
import com.forz.calculator.history.HistoryService
import com.forz.calculator.settings.SettingsActivity
import com.forz.calculator.settings.Config
import com.forz.calculator.calculator.CalculatorViewModel
import com.forz.calculator.calculator.Evaluator
import com.forz.calculator.calculator.TrigonometricFunction
import com.forz.calculator.expression.ExpressionViewModel
import com.forz.calculator.expression.ExpressionViewModel.cursorPositionStart
import com.forz.calculator.expression.ExpressionViewModel.expression
import com.forz.calculator.expression.ExpressionViewModel.oldExpression
import com.forz.calculator.fragments.CalculatorFragment
import com.forz.calculator.fragments.Fragments.CALCULATOR_FRAGMENT
import com.forz.calculator.fragments.Fragments.UNIT_CONVERTER_FRAGMENT
import com.forz.calculator.fragments.Fragments.currentItemMainPager
import com.forz.calculator.fragments.HistoryFragment
import com.forz.calculator.fragments.UnitConverterFragment
import com.forz.calculator.fragments.adapters.ViewPageAdapter
import com.forz.calculator.settings.Config.autoSavingResults
import com.forz.calculator.utils.InsertInExpression
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.properties.Delegates.notNull

@Suppress("DEPRECATION")
class MainLandFragment : Fragment(),
    OnMainActivityListener,
    CalculatorFragment.OnButtonClickListener,
    HistoryFragment.OnButtonClickListener,
    UnitConverterFragment.OnButtonClickListener
{

    private var result: String by notNull()
    private var binding: FragmentLandBinding by notNull()
    private var hapticAndSound: HapticAndSound by notNull()


    private val historyService: HistoryService
        get() = (requireContext().applicationContext as App).historyService

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLandBinding.inflate(inflater, container, false)

        val views: Array<View> = arrayOf(
            binding.degreeTitleText
        )
        hapticAndSound = HapticAndSound(requireContext(), views)

        binding.expressionEditText.showSoftInputOnFocus = false
        binding.expressionEditText.requestFocus()


        val adapter = ViewPageAdapter(childFragmentManager, lifecycle)

        adapter.addFragment(UnitConverterFragment())
        adapter.addFragment(CalculatorFragment())

        binding.calculatorViewPager.adapter = adapter
        binding.calculatorViewPager.setCurrentItem(currentItemMainPager, false)
        binding.calculatorViewPager.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabLayout, binding.calculatorViewPager) { tab, position ->
            tab.setIcon(
                when (position) {
                    UNIT_CONVERTER_FRAGMENT -> R.drawable.baseline_autorenew
                    CALCULATOR_FRAGMENT -> R.drawable.baseline_calculate
                    else -> throw IllegalArgumentException("Invalid position")
                }
            )
        }.attach()

        binding.calculatorViewPager.apply {
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        binding.toolbar.let { toolbar ->
            toolbar.menu.clear()
            toolbar.inflateMenu(R.menu.history_options_menu)
        }

        binding.calculatorViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentItemMainPager = position
            }
        })



        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.settings -> {
                    val intent = Intent(requireActivity(), SettingsActivity::class.java)
                    startActivityForResult(intent, MainActivity.REQUEST_CODE_CHILD)
                    true
                }
                R.id.about -> {
                    val intent = Intent(requireContext(), AboutActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.clearHistory -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder
                        .setMessage(getString(R.string.clear_history_title))
                        .setPositiveButton(getString(R.string.clear_history_clear)) { _, _ ->
                            historyService.clearHistoryData()
                        }.setNegativeButton(getString(R.string.clear_history_dismiss)) { _, _ ->
                        }

                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                    true
                }
                else -> false
            }
        }


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

                if (TrigonometricFunction.entries.any { binding.expressionEditText.text!!.contains(it.text) }){
                    binding.degreeTitleText.visibility = ImageView.VISIBLE
                } else{
                    binding.degreeTitleText.visibility = ImageView.GONE
                }
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

        binding.degreeTitleText.setOnClickListener {
            CalculatorViewModel.updateDegreeModActivated()
            hapticAndSound.vibrateEffectClick()
        }

        CalculatorViewModel.isDegreeModActivated.observe(requireActivity()) { isDegreeModActivated ->
            if (isDegreeModActivated) {
                binding.degreeTitleText.text = getString(R.string.deg)
            } else {
                binding.degreeTitleText.text = getString(R.string.rad)
            }

            result = Evaluator.getResult(binding.expressionEditText, ExpressionViewModel.isSelected.value ?: false, requireContext())
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        hapticAndSound.setHapticFeedback()
        hapticAndSound.setSoundEffects()
        binding.calculatorViewPager.isUserInputEnabled = Config.swipeMain
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
            binding.calculatorViewPager.setCurrentItem(CALCULATOR_FRAGMENT, true)
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