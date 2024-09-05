package com.forz.calculator.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.forz.calculator.App
import com.forz.calculator.utils.NumberFormatter
import com.forz.calculator.R
import com.forz.calculator.colorThemes.Color
import com.forz.calculator.colorThemes.ColorActionListener
import com.forz.calculator.colorThemes.ColorAdapter
import com.forz.calculator.databinding.ActivitySettingsBinding
import com.forz.calculator.history.HistoryService
import com.forz.calculator.settings.Config.color
import com.forz.calculator.settings.Config.decimalSeparatorSymbol
import com.forz.calculator.settings.Config.groupingSeparatorSymbol
import com.forz.calculator.settings.Config.isDynamicColor
import com.forz.calculator.settings.Config.numberPrecision
import com.forz.calculator.settings.Config.oldDecimalSeparatorSymbol
import com.forz.calculator.settings.Config.oldGroupingSeparatorSymbol
import com.forz.calculator.settings.Config.sound
import com.forz.calculator.settings.Config.swipeDigitsAndScientificFunctions
import com.forz.calculator.settings.Config.swipeMain
import com.forz.calculator.settings.Config.vibration
import com.forz.calculator.expression.ExpressionViewModel
import com.forz.calculator.settings.Config.maxScientificNotationDigits
import com.forz.calculator.settings.Config.autoSavingResults
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.slider.Slider
import java.math.BigDecimal
import kotlin.properties.Delegates.notNull


@Suppress("DEPRECATION")
class SettingsActivity : AppCompatActivity() {

    companion object{
        var firstCreatedSettingsActivity = true
    }

    private var binding: ActivitySettingsBinding by notNull()
    private var preferences: Preferences by notNull()
    private var vibrator: Vibrator by notNull()
    private var adapter: ColorAdapter by notNull()

    private val historyService: HistoryService
        get() = (this.applicationContext as App).historyService


    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = Preferences(this)
        vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (!isDynamicColor){
            setTheme(resources.getIdentifier(preferences.getColor(), "style", packageName))
        }else{
            setTheme(R.style.dynamicColors)
        }

        val typedValue = TypedValue()
        this.theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
        window.statusBarColor = ContextCompat.getColor(this, typedValue.resourceId)


        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater).also { setContentView(it.root) }


        val themeMap = mapOf(-1 to R.id.chooseThemeAutoButton, 1 to R.id.chooseThemeLightButton, 2 to R.id.chooseThemeDarkButton)
        val groupingSeparatorMap = mapOf("," to R.id.groupingSeparatorSymbolCommaButton, "." to R.id.groupingSeparatorSymbolDotButton, " " to R.id.groupingSeparatorSymbolSpaceButton)
        val decimalSeparatorMap = mapOf("." to R.id.decimalSeparatorSymbolDotButton, "," to R.id.decimalSeparatorSymbolCommaButton)





        loadChooseThemeImage()
        loadButtonToggleGroupInt(binding.chooseThemeButtonToggleGroup, themeMap, Config.theme)
        loadDynamicColorSwitch()

        loadSlider(binding.precisionSlider, numberPrecision)
        loadSlider(binding.maxScientificNotationDigitsSlider, maxScientificNotationDigits)
        binding.previewFormatText.text = updatePreviewText(getString(R.string.preview_format_text), numberPrecision, maxScientificNotationDigits, groupingSeparatorSymbol, decimalSeparatorSymbol)
        loadButtonToggleGroupString(binding.groupingSeparatorSymbolButtonToggleGroup, groupingSeparatorMap, groupingSeparatorSymbol)
        loadButtonToggleGroupString(binding.decimalSeparatorSymbolButtonToggleGroup, decimalSeparatorMap, decimalSeparatorSymbol)

        loadSwitch(binding.savingIntermediateResultsSwitch, autoSavingResults)
        loadSwitch(binding.vibrationSwitch, vibration)
        loadSwitch(binding.soundEffectsSwitch, sound)





        if (isDynamicColor){
            binding.chooseColorLayout.visibility = View.GONE
        } else{
            binding.chooseColorLayout.visibility = View.VISIBLE

            adapter = ColorAdapter(this, object : ColorActionListener{
                override fun onSelectColor(color: Color) {
                    adapter.selectColor(color)
                    recreate()
                }
            })

            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.chooseColorRecyclerView.layoutManager = layoutManager
            binding.chooseColorRecyclerView.adapter = adapter

            if (firstCreatedSettingsActivity){
                firstCreatedSettingsActivity = false

                val selectedPosition = adapter.getIdSelectedColor()

                val displayMetrics = resources.displayMetrics
                val itemWidthInPixels = (72 * displayMetrics.density).toInt()
                val paddingInPixels = (8 * displayMetrics.density).toInt()

                binding.chooseColorRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        binding.chooseColorRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        val recyclerViewWidth = binding.chooseColorRecyclerView.width

                        val offset = (recyclerViewWidth / 2) - (itemWidthInPixels / 2) - paddingInPixels

                        layoutManager.scrollToPositionWithOffset(selectedPosition, offset)
                    }
                })
            }
        }

        if (!vibrator.hasVibrator()){
            binding.vibrationLayout.visibility = View.GONE
        }else{
            binding.vibrationLayout.visibility = View.VISIBLE
        }




        binding.chooseThemeButtonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val newNightMode = when (checkedId) {
                    R.id.chooseThemeAutoButton -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    R.id.chooseThemeLightButton -> AppCompatDelegate.MODE_NIGHT_NO
                    R.id.chooseThemeDarkButton -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> return@addOnButtonCheckedListener
                }

                val currentNightMode = preferences.getTheme()
                Config.theme = currentNightMode
                if (currentNightMode != newNightMode) {
                    Config.theme = newNightMode
                    preferences.setTheme(Config.theme)
                    AppCompatDelegate.setDefaultNightMode(Config.theme)
                }
            }
        }

        binding.dynamicColorsLayout.setOnClickListener {
            isDynamicColor = switchSwitch(binding.dynamicColorsSwitch)
            recreate()
        }
        binding.dynamicColorsSwitch.setOnCheckedChangeListener { _, isChecked ->
            isDynamicColor = isChecked
            recreate()
        }






        binding.precisionSlider.addOnChangeListener { _, value, _ ->
            val progress = value.toInt()
            binding.previewFormatText.text = updatePreviewText(
                getString(R.string.preview_format_text),
                progress,
                maxScientificNotationDigits,
                groupingSeparatorSymbol,
                decimalSeparatorSymbol
            )
        }

        binding.precisionSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }
            override fun onStopTrackingTouch(slider: Slider) {
                numberPrecision = slider.value.toInt()
            }
        })


        binding.maxScientificNotationDigitsSlider.addOnChangeListener { _, value, _ ->
            val progress = value.toInt()
            binding.previewFormatText.text = updatePreviewText(
                getString(R.string.preview_format_text),
                numberPrecision,
                progress,
                groupingSeparatorSymbol,
                decimalSeparatorSymbol
            )
        }

        binding.maxScientificNotationDigitsSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }
            override fun onStopTrackingTouch(slider: Slider) {
                maxScientificNotationDigits = slider.value.toInt()
            }
        })


        val invertedGroupingSeparatorMap = groupingSeparatorMap.entries.associateBy({ it.value }, { it.key })
        val invertedDecimalSeparatorMap = decimalSeparatorMap.entries.associateBy({ it.value }, { it.key })


        binding.groupingSeparatorSymbolButtonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val separator = invertedGroupingSeparatorMap[checkedId]
                groupingSeparatorSymbol = separator!!
                binding.previewFormatText.text = updatePreviewText(getString(R.string.preview_format_text), numberPrecision, maxScientificNotationDigits, groupingSeparatorSymbol, decimalSeparatorSymbol)

                if (separator == "."){
                    binding.decimalSeparatorSymbolButtonToggleGroup.check(R.id.decimalSeparatorSymbolCommaButton)
                }else if (separator == ","){
                    binding.decimalSeparatorSymbolButtonToggleGroup.check(R.id.decimalSeparatorSymbolDotButton)
                }
            }
        }



        binding.decimalSeparatorSymbolButtonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val separator = invertedDecimalSeparatorMap[checkedId]
                decimalSeparatorSymbol = separator!!
                binding.previewFormatText.text = updatePreviewText(getString(R.string.preview_format_text), numberPrecision, maxScientificNotationDigits, groupingSeparatorSymbol, decimalSeparatorSymbol)

                if (binding.groupingSeparatorSymbolButtonToggleGroup.checkedButtonId != R.id.groupingSeparatorSymbolSpaceButton){
                    if (separator == ","){
                        binding.groupingSeparatorSymbolButtonToggleGroup.check(R.id.groupingSeparatorSymbolDotButton)
                    }else if (separator == "."){
                        binding.groupingSeparatorSymbolButtonToggleGroup.check(R.id.groupingSeparatorSymbolCommaButton)
                    }
                }
            }
        }







        binding.swipesLayout.setOnClickListener {
            val selectedItems = booleanArrayOf(swipeMain, swipeDigitsAndScientificFunctions)
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder
                .setTitle(getString(R.string.title_swipes))
                .setPositiveButton(getString(R.string.swipes_ok)) { _, _ ->
                    swipeMain = selectedItems[0]
                    swipeDigitsAndScientificFunctions = selectedItems[1]
                }
                .setNegativeButton(getString(R.string.swipes_cancel)) { _, _ ->

                }
                .setMultiChoiceItems(
                    arrayOf(getString(R.string.description_swipes2), getString(R.string.description_swipes3)),
                    selectedItems
                ) { _, which, isChecked ->
                    selectedItems[which] = isChecked
                }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        binding.savingIntermediateResultsLayout.setOnClickListener {
            autoSavingResults = switchSwitch(binding.savingIntermediateResultsSwitch)
        }
        binding.savingIntermediateResultsSwitch.setOnCheckedChangeListener { _, isChecked ->
            autoSavingResults = isChecked
        }

        binding.vibrationLayout.setOnClickListener {
            vibration = switchSwitch(binding.vibrationSwitch)
        }
        binding.vibrationSwitch.setOnCheckedChangeListener { _, isChecked ->
            vibration = isChecked
        }

        binding.soundEffectsLayout.setOnClickListener {
            sound = switchSwitch(binding.soundEffectsSwitch)
        }
        binding.soundEffectsSwitch.setOnCheckedChangeListener { _, isChecked ->
            sound = isChecked
        }



        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()

        if (oldGroupingSeparatorSymbol != groupingSeparatorSymbol || oldDecimalSeparatorSymbol != decimalSeparatorSymbol){
            ExpressionViewModel.expression = NumberFormatter.changeSeparatorsExpression(
                ExpressionViewModel.expression,
                oldGroupingSeparatorSymbol,
                groupingSeparatorSymbol,
                oldDecimalSeparatorSymbol,
                decimalSeparatorSymbol
            )

            historyService.modifyHistoryData(
                oldGroupingSeparatorSymbol,
                groupingSeparatorSymbol,
                oldDecimalSeparatorSymbol,
                decimalSeparatorSymbol
            )

            oldGroupingSeparatorSymbol = groupingSeparatorSymbol
            oldDecimalSeparatorSymbol = decimalSeparatorSymbol
        }


        preferences.setDynamicColor(isDynamicColor)
        preferences.setColor(color)
        preferences.setGroupingSeparatorSymbol(groupingSeparatorSymbol)
        preferences.setDecimalSeparatorSymbol(decimalSeparatorSymbol)
        preferences.setNumberPrecision(numberPrecision)
        preferences.setMaxScientificNotationDigits(maxScientificNotationDigits)
        preferences.setSwipeHistoryAndCalculator(swipeMain)
        preferences.setSwipeDigitsAndScientificFunctions(swipeDigitsAndScientificFunctions)
        preferences.setAutoSavingResults(autoSavingResults)
        preferences.setVibration(vibration)
        preferences.setSoundEffects(sound)
    }



    private fun loadChooseThemeImage(){
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                when (this@SettingsActivity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        binding.chooseThemeImage.setImageResource(R.drawable.baseline_dark_mode)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        binding.chooseThemeImage.setImageResource(R.drawable.baseline_light_mode)
                    }
                }
            }
            AppCompatDelegate.MODE_NIGHT_NO -> {
                binding.chooseThemeImage.setImageResource(R.drawable.baseline_light_mode)
            }
            else -> {
                binding.chooseThemeImage.setImageResource(R.drawable.baseline_dark_mode)
            }
        }
    }

    private fun loadDynamicColorSwitch(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            binding.dynamicColorsSwitch.isChecked = isDynamicColor
        }else{
            binding.dynamicColorsLayout.visibility = View.GONE
        }
    }



    private fun loadSwitch(switch: MaterialSwitch, value: Boolean){
        switch.isChecked = value
    }

    private fun switchSwitch(switch: MaterialSwitch): Boolean{
        return when (switch.isChecked){
            true -> {
                switch.isChecked = false
                false
            }

            else -> {
                switch.isChecked = true
                true
            }
        }
    }

    private fun loadSlider(slider: Slider, value: Int) {
        slider.value = value.toFloat()
    }

    private fun loadButtonToggleGroupInt(buttonToggleGroup: MaterialButtonToggleGroup, map: Map<Int, Int>, value: Int) {
        map[value]?.let { buttonToggleGroup.check(it) }
    }
    private fun loadButtonToggleGroupString(buttonToggleGroup: MaterialButtonToggleGroup, map: Map<String, Int>, value: String) {
        map[value]?.let { buttonToggleGroup.check(it) }
    }

    private fun updatePreviewText(inputString: String, numberPrecision: Int, maxIntegerDigits: Int, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): String{
        var text = inputString
        text = NumberFormatter.removeSeparators(text, ",")
        text = NumberFormatter.formatResult(
            BigDecimal(text),
            numberPrecision,
            maxIntegerDigits,
            groupingSeparatorSymbol,
            decimalSeparatorSymbol
        )

        return text
    }
}