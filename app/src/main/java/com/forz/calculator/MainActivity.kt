package com.forz.calculator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.forz.calculator.databinding.ActivityMainBinding
import com.forz.calculator.fragments.CalculatorFragment
import com.forz.calculator.fragments.HistoryFragment
import com.forz.calculator.fragments.adapters.ViewPageAdapter
import com.forz.calculator.history.HistoryService
import com.forz.calculator.settings.SettingsActivity
import com.forz.calculator.viewModels.CalculatorViewModel
import com.forz.calculator.viewModels.CalculatorViewModel.isDegreeModActivated
import com.forz.calculator.viewModels.CalculatorViewModel.updateDegreeModActivated
import com.forz.calculator.viewModels.ExpressionViewModel
import com.forz.calculator.settings.SettingsState
import com.forz.calculator.settings.SettingsState.groupingSeparatorSymbol
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.properties.Delegates.notNull

@Suppress("DEPRECATION")
@SuppressLint("DiscouragedApi")
class MainActivity : AppCompatActivity() {

    private val triggersIsDegreeModActivatedShowArray = arrayOf("sin", "cos", "tan", "sin⁻¹", "cos⁻¹", "tan⁻¹")

    private var binding: ActivityMainBinding by notNull()
    private var preferences: Preferences by notNull()
    private var hapticAndSound: HapticAndSound by notNull()

    private val historyService: HistoryService
        get() = (applicationContext as App).historyService


    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = Preferences(this)



        if (!preferences.getDynamicColor()){
            setTheme(resources.getIdentifier(preferences.getColor(), "style", packageName))
        }else{
            setTheme(R.style.dynamicColors)
        }


        when (preferences.getTheme()) {
            -1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val views: Array<View> = arrayOf(
            binding.degreeTitleText
        )

        hapticAndSound = HapticAndSound(this, views)
        binding.expressionEditText.showSoftInputOnFocus = false



        val fadeOutAnimation200: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_out_200)
        val fadeInAnimation200: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_200)




        val adapter = ViewPageAdapter(supportFragmentManager, lifecycle)
        adapter.addFragment(HistoryFragment())
        adapter.addFragment(CalculatorFragment())
        binding.pager.adapter = adapter
        binding.pager.setCurrentItem(1, false)
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



        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                hapticAndSound.vibrateEffectClick()

                when (position) {
                    0 -> {
                        if (binding.degreeTitleText.visibility == View.VISIBLE){
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
                        if (triggersIsDegreeModActivatedShowArray.any {ExpressionViewModel.expression.value!!.contains(it) }){
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
            val popupMenu = PopupMenu(this, view)
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
                        val intent = Intent(this, SettingsActivity::class.java)
                        startActivityForResult(intent, REQUEST_CODE_CHILD)
                        true
                    }
                    R.id.about -> {
                        val intent = Intent(this, AboutActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.clearHistory -> {
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
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
            updateDegreeModActivated()
            hapticAndSound.vibrateEffectClick()
        }

        isDegreeModActivated.observe(this) { isDegreeModActivated ->
            if (isDegreeModActivated) {
                binding.degreeTitleText.text = getString(R.string.deg)
            } else {
                binding.degreeTitleText.text = getString(R.string.rad)
            }

            ExpressionViewModel.updateResult(ExpressionViewModel.expression.value!!)
        }

        ExpressionViewModel.expression.observe(this){ expression ->
            val expressionCursorPositionStart = ExpressionViewModel.expressionCursorPositionStart.value!!
            val expressionCursorPositionEnd = ExpressionViewModel.expressionCursorPositionEnd.value!!
            binding.expressionEditText.text = NumberFormatter.changeColorOperators(expression, this)
            binding.expressionEditText.setSelection(expressionCursorPositionStart, expressionCursorPositionEnd)

            if (triggersIsDegreeModActivatedShowArray.any { expression.contains(it) }){
                binding.degreeTitleText.visibility = ImageView.VISIBLE
            } else{
                binding.degreeTitleText.visibility = ImageView.GONE
            }

            ExpressionViewModel.updateResult(expression)
        }

        ExpressionViewModel.result.observe(this){ result ->
            if (result != ExpressionViewModel.expression.value!!){
                binding.resultText.text = result
            }else{
                binding.resultText.text = ""
            }

            AutoSizeText.result(binding.resultText, binding.resultText.resources)
        }

        binding.expressionEditText.requestFocus()
        binding.expressionEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                view.requestFocus()
            }
        }



        binding.expressionEditText.addTextChangedListener {
            if (ExpressionViewModel.expression.value!! != binding.expressionEditText.text.toString()){
                ExpressionViewModel.updateNumberOfCharactersOfInsertedText(binding.expressionEditText.text!!.length)
                ExpressionViewModel.updateExpression(binding.expressionEditText.text.toString(), binding.expressionEditText.selectionStart)
            }

            AutoSizeText.expression(binding.expressionEditText, binding.expressionEditText.resources)
        }

        ExpressionViewModel.isSelection.observe(this){ isSelection ->
            if (isSelection){
                ExpressionViewModel.updateResult(ExpressionViewModel.expression.value!!.substring(ExpressionViewModel.expressionCursorPositionStart.value!!, ExpressionViewModel.expressionCursorPositionEnd.value!!))
            }else if (!isSelection && ExpressionViewModel.previousIsSelection){
                ExpressionViewModel.updateResult(ExpressionViewModel.expression.value!!)
            } else if (InsertInExpression.stringAfterCursor(ExpressionViewModel.expressionCursorPositionStart.value!!, ExpressionViewModel.expression.value!!).startsWith(groupingSeparatorSymbol)){
                binding.expressionEditText.setSelection(ExpressionViewModel.expressionCursorPositionStart.value!! + 1)
            }
        }
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
            preferences.getVibration(),
            preferences.getSoundEffects()
        )
        CalculatorViewModel.init(preferences.getDegreeMod())

        hapticAndSound.setHapticFeedback()
        hapticAndSound.setSoundEffects()
    }

    override fun onStop() {
        super.onStop()

        preferences.setDegreeMod(isDegreeModActivated.value!!)
    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHILD) {
            recreate()
        }
    }

    companion object {
        private const val REQUEST_CODE_CHILD = 1
    }
}