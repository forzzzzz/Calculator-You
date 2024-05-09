package com.forz.calculator.fragments.land

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.forz.calculator.AboutActivity
import com.forz.calculator.App
import com.forz.calculator.AutoSizeText
import com.forz.calculator.HapticAndSound
import com.forz.calculator.InsertInExpression
import com.forz.calculator.InsertInExpression.triggersIsDegreeModActivatedShowArray
import com.forz.calculator.MainActivity
import com.forz.calculator.NumberFormatter
import com.forz.calculator.Preferences
import com.forz.calculator.R
import com.forz.calculator.databinding.FragmentLandBinding
import com.forz.calculator.history.HistoryService
import com.forz.calculator.settings.SettingsActivity
import com.forz.calculator.settings.SettingsState
import com.forz.calculator.viewModels.CalculatorViewModel
import com.forz.calculator.viewModels.ExpressionViewModel
import kotlin.properties.Delegates

@Suppress("DEPRECATION")
class MainLandFragment : Fragment() {

    private var binding: FragmentLandBinding by Delegates.notNull()
    private var preferences: Preferences by Delegates.notNull()
    private var hapticAndSound: HapticAndSound by Delegates.notNull()

    private val historyService: HistoryService
        get() = (requireContext().applicationContext as App).historyService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLandBinding.inflate(inflater, container, false)
        preferences = Preferences(requireContext())

        val views: Array<View> = arrayOf(
            binding.degreeTitleText
        )
        hapticAndSound = HapticAndSound(requireContext(), views)
        binding.expressionEditText.showSoftInputOnFocus = false


        binding.optionsMenuButton.setOnClickListener { view ->
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.inflate(R.menu.history_options_menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
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
                            }
                            .setNegativeButton(getString(R.string.clear_history_dismiss)) { _, _ ->
                            }

                        val dialog: AlertDialog = builder.create()
                        dialog.show()

                        true
                    }
                    else ->  false
                }
            }
            popupMenu.show()
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

            ExpressionViewModel.updateResult(ExpressionViewModel.expression.value!!)
        }

        ExpressionViewModel.expression.observe(requireActivity()){ expression ->
            val expressionCursorPositionStart = ExpressionViewModel.expressionCursorPositionStart.value!!
            val expressionCursorPositionEnd = ExpressionViewModel.expressionCursorPositionEnd.value!!
            binding.expressionEditText.text = NumberFormatter.changeColorOperators(expression, requireContext())
            binding.expressionEditText.setSelection(expressionCursorPositionStart, expressionCursorPositionEnd)

            if (triggersIsDegreeModActivatedShowArray.any { expression.contains(it) }){
                binding.degreeTitleText.visibility = ImageView.VISIBLE
            } else{
                binding.degreeTitleText.visibility = ImageView.GONE
            }

            ExpressionViewModel.updateResult(expression)
        }

        ExpressionViewModel.result.observe(requireActivity()){ result ->
            if (result != ExpressionViewModel.expression.value!!){
                binding.resultText.text = result
            }else{
                binding.resultText.text = ""
            }
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

            AutoSizeText.expression(binding.expressionEditText, binding.expressionEditText.resources)
        }


        ExpressionViewModel.isSelection.observe(requireActivity()){ isSelection ->
            if (isSelection){
                ExpressionViewModel.updateResult(
                    ExpressionViewModel.expression.value!!.substring(
                        ExpressionViewModel.expressionCursorPositionStart.value!!, ExpressionViewModel.expressionCursorPositionEnd.value!!))
            }else if (!isSelection && ExpressionViewModel.previousIsSelection){
                ExpressionViewModel.updateResult(ExpressionViewModel.expression.value!!)
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

        hapticAndSound.setHapticFeedback()
        hapticAndSound.setSoundEffects()
    }

    override fun onStop() {
        super.onStop()

        preferences.setDegreeMod(CalculatorViewModel.isDegreeModActivated.value!!)
    }
}