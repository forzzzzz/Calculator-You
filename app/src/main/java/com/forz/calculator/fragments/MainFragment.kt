package com.forz.calculator.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.forz.calculator.AboutActivity
import com.forz.calculator.App
import com.forz.calculator.HapticAndSound
import com.forz.calculator.InsertInExpression
import com.forz.calculator.InsertInExpression.triggersIsDegreeModActivatedShowArray
import com.forz.calculator.MainActivity
import com.forz.calculator.NumberFormatter
import com.forz.calculator.Preferences
import com.forz.calculator.R
import com.forz.calculator.StateViews
import com.forz.calculator.StateViews.pagerIsRecreated
import com.forz.calculator.databinding.FragmentDefaultBinding
import com.forz.calculator.fragments.adapters.ViewPageAdapter
import com.forz.calculator.history.HistoryService
import com.forz.calculator.settings.SettingsActivity
import com.forz.calculator.settings.SettingsState
import com.forz.calculator.viewModels.CalculatorViewModel
import com.forz.calculator.viewModels.ExpressionViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.properties.Delegates

@Suppress("DEPRECATION")
class MainFragment : Fragment() {

    private var binding: FragmentDefaultBinding by Delegates.notNull()
    private var preferences: Preferences by Delegates.notNull()
    private var hapticAndSound: HapticAndSound by Delegates.notNull()

    private val historyService: HistoryService
        get() = (requireContext().applicationContext as App).historyService

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDefaultBinding.inflate(inflater, container, false)
        preferences = Preferences(requireContext())

        val views: Array<View> = arrayOf(
            binding.degreeTitleText
        )
        hapticAndSound = HapticAndSound(requireContext(), views)
        binding.expressionEditText.showSoftInputOnFocus = false


        val fadeOutAnimation200: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out_200)
        val fadeInAnimation200: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_200)


        val adapter = ViewPageAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(HistoryFragment())
        adapter.addFragment(CalculatorFragment())
        binding.pager.adapter = adapter
        binding.pager.setCurrentItem(StateViews.currentItemPager, false)
        binding.pager.offscreenPageLimit = 2
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.setIcon(
                when (position) {
                    0 -> R.drawable.baseline_history
                    1 -> R.drawable.baseline_calculate
                    else -> throw IllegalArgumentException("Invalid position")
                }
            )
        }.attach()
        binding.pager.apply {
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        if (pagerIsRecreated && binding.pager.currentItem == 1){
            pagerIsRecreated = false
        }

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                StateViews.currentItemPager = position

                when (position) {
                    0 -> {
                        if (pagerIsRecreated){
                            binding.degreeTitleText.visibility = ImageView.GONE
                            binding.historyTitleText.visibility = ImageView.VISIBLE

                            pagerIsRecreated = false
                        }else if (triggersIsDegreeModActivatedShowArray.any { ExpressionViewModel.expression.value!!.contains(it) }){
                            binding.degreeTitleText.startAnimation(fadeOutAnimation200.apply {
                                setAnimationListener(object : Animation.AnimationListener {
                                    override fun onAnimationStart(animation: Animation?) {
                                    }
                                    override fun onAnimationEnd(animation: Animation?) {
                                        binding.degreeTitleText.visibility = ImageView.GONE
                                        binding.historyTitleText.visibility = ImageView.VISIBLE
                                        binding.historyTitleText.startAnimation(fadeInAnimation200)
                                    }
                                    override fun onAnimationRepeat(animation: Animation?) {
                                    }
                                })
                            })
                        } else{
                            binding.historyTitleText.visibility = ImageView.VISIBLE
                            binding.historyTitleText.startAnimation(fadeInAnimation200)
                        }
                    }
                    1 -> {
                        if (triggersIsDegreeModActivatedShowArray.any { ExpressionViewModel.expression.value!!.contains(it) }){
                            binding.historyTitleText.startAnimation(fadeOutAnimation200.apply {
                                setAnimationListener(object : Animation.AnimationListener {
                                    override fun onAnimationStart(animation: Animation?) {
                                    }
                                    override fun onAnimationEnd(animation: Animation?) {
                                        binding.historyTitleText.visibility = ImageView.GONE
                                        binding.degreeTitleText.visibility = ImageView.VISIBLE
                                        binding.degreeTitleText.startAnimation(fadeInAnimation200)
                                    }
                                    override fun onAnimationRepeat(animation: Animation?) {
                                    }
                                })
                            })
                        } else{
                            binding.historyTitleText.startAnimation(fadeOutAnimation200.apply {
                                setAnimationListener(object : Animation.AnimationListener {
                                    override fun onAnimationStart(animation: Animation?) {
                                    }
                                    override fun onAnimationEnd(animation: Animation?) {
                                        binding.historyTitleText.visibility = ImageView.GONE
                                    }
                                    override fun onAnimationRepeat(animation: Animation?) {
                                    }
                                })
                            })
                        }
                    }
                }
            }
        })




        binding.optionsMenuButton.setOnClickListener { view ->
            val popupMenu = PopupMenu(requireContext(), view)
            if (binding.pager.currentItem == 1){
                popupMenu.inflate(R.menu.options_menu)
            } else{
                popupMenu.inflate(R.menu.history_options_menu)
            }
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.history -> {
                        binding.pager.setCurrentItem(0, true)
                        true
                    }
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
                                binding.pager.setCurrentItem(1, true)
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

            if (triggersIsDegreeModActivatedShowArray.any { expression.contains(it) } && binding.pager.currentItem == 1){
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
        binding.pager.isUserInputEnabled = SettingsState.swipeHistoryAndCalculator
    }

    override fun onStop() {
        super.onStop()

        preferences.setDegreeMod(CalculatorViewModel.isDegreeModActivated.value!!)
    }


    private fun autoSizeTextExpressionEditText(){
        binding.expressionTextView.text = binding.expressionEditText.text
        binding.expressionEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.expressionTextView.textSize)
    }
}