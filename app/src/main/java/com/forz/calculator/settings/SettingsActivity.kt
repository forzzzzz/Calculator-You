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
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.forz.calculator.NumberFormatter
import com.forz.calculator.Preferences
import com.forz.calculator.R
import com.forz.calculator.StateViews.chooseColorRecyclerViewIsRecreated
import com.forz.calculator.StateViews.colorIsSelected
import com.forz.calculator.colorThemes.Color
import com.forz.calculator.colorThemes.ColorActionListener
import com.forz.calculator.colorThemes.ColorAdapter
import com.forz.calculator.databinding.ActivitySettingsBinding
import com.forz.calculator.viewModels.ExpressionViewModel
import com.forz.calculator.settings.SettingsState.color
import com.forz.calculator.settings.SettingsState.decimalSeparatorSymbol
import com.forz.calculator.settings.SettingsState.groupingSeparatorSymbol
import com.forz.calculator.settings.SettingsState.isDynamicColor
import com.forz.calculator.settings.SettingsState.numberPrecision
import com.forz.calculator.settings.SettingsState.previousDecimalSeparatorSymbol
import com.forz.calculator.settings.SettingsState.previousGroupingSeparatorSymbol
import com.forz.calculator.settings.SettingsState.sound
import com.forz.calculator.settings.SettingsState.swipeDigitsAndScientificFunctions
import com.forz.calculator.settings.SettingsState.swipeHistoryAndCalculator
import com.forz.calculator.settings.SettingsState.vibration
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.materialswitch.MaterialSwitch
import kotlin.properties.Delegates.notNull


@Suppress("DEPRECATION")
class SettingsActivity : AppCompatActivity() {

    private var binding: ActivitySettingsBinding by notNull()
    private var preferences: Preferences by notNull()
    private var vibrator: Vibrator by notNull()
    private var adapter: ColorAdapter by notNull()


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
        loadButtonToggleGroupInt(binding.chooseThemeButtonToggleGroup, themeMap, SettingsState.theme)
        loadDynamicColorSwitch()

        loadSeekBar(binding.precisionSeekBar, binding.precisionValueText, numberPrecision)
        binding.previewFormatText.text = updatePreviewText(getString(R.string.preview_format_text), numberPrecision, groupingSeparatorSymbol, decimalSeparatorSymbol)
        loadButtonToggleGroupString(binding.groupingSeparatorSymbolButtonToggleGroup, groupingSeparatorMap, groupingSeparatorSymbol)
        loadButtonToggleGroupString(binding.decimalSeparatorSymbolButtonToggleGroup, decimalSeparatorMap, decimalSeparatorSymbol)

        loadSwitch(binding.vibrationSwitch, vibration)
        loadSwitch(binding.soundEffectsSwitch, sound)







        if (isDynamicColor){
            binding.chooseColorLayout.visibility = View.GONE
        } else{
            binding.chooseColorLayout.visibility = View.VISIBLE

            adapter = ColorAdapter(this, object : ColorActionListener{
                override fun onSelectColor(color: Color) {
                    colorIsSelected = true
                    adapter.selectColor(color)
                    recreate()
                }
            })

            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.chooseColorRecyclerView.layoutManager = layoutManager
            binding.chooseColorRecyclerView.adapter = adapter

            if (!colorIsSelected || !chooseColorRecyclerViewIsRecreated){
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
                SettingsState.theme = currentNightMode
                if (currentNightMode != newNightMode) {
                    SettingsState.theme = newNightMode
                    preferences.setTheme(SettingsState.theme)
                    AppCompatDelegate.setDefaultNightMode(SettingsState.theme)
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










        binding.precisionSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.precisionValueText.text = binding.precisionSeekBar.progress.toString()
                binding.previewFormatText.text = updatePreviewText(getString(R.string.preview_format_text), binding.precisionSeekBar.progress, preferences.getGroupingSeparatorSymbol(), preferences.getDecimalSeparatorSymbol())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                numberPrecision = binding.precisionSeekBar.progress
            }
        })


        val invertedGroupingSeparatorMap = groupingSeparatorMap.entries.associateBy({ it.value }, { it.key })
        val invertedDecimalSeparatorMap = decimalSeparatorMap.entries.associateBy({ it.value }, { it.key })


        binding.groupingSeparatorSymbolButtonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val separator = invertedGroupingSeparatorMap[checkedId]
                groupingSeparatorSymbol = separator!!
                binding.previewFormatText.text = updatePreviewText(getString(R.string.preview_format_text), numberPrecision, groupingSeparatorSymbol, decimalSeparatorSymbol)

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
                binding.previewFormatText.text = updatePreviewText(getString(R.string.preview_format_text), numberPrecision, groupingSeparatorSymbol, decimalSeparatorSymbol)

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
            val selectedItems = booleanArrayOf(swipeHistoryAndCalculator, swipeDigitsAndScientificFunctions)
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder
                .setTitle(getString(R.string.title_swipes))
                .setPositiveButton(getString(R.string.swipes_ok)) { _, _ ->
                    swipeHistoryAndCalculator = selectedItems[0]
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





        binding.arrowBack.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()

        ExpressionViewModel.updateSymbolsInExpression(
            previousGroupingSeparatorSymbol,
            groupingSeparatorSymbol,
            previousDecimalSeparatorSymbol,
            decimalSeparatorSymbol
        )

        preferences.setDynamicColor(isDynamicColor)
        preferences.setColor(color)
        preferences.setGroupingSeparatorSymbol(groupingSeparatorSymbol)
        preferences.setDecimalSeparatorSymbol(decimalSeparatorSymbol)
        preferences.setNumberPrecision(numberPrecision)
        preferences.setSwipeHistoryAndCalculator(swipeHistoryAndCalculator)
        preferences.setSwipeDigitsAndScientificFunctions(swipeDigitsAndScientificFunctions)
        preferences.setVibration(vibration)
        preferences.setSoundEffects(sound)
    }



    private fun loadChooseThemeImage(){
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                when (this@SettingsActivity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        binding.chooseThemeImage.setImageResource(R.drawable.dark_mode)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        binding.chooseThemeImage.setImageResource(R.drawable.light_mode)
                    }
                }
            }
            AppCompatDelegate.MODE_NIGHT_NO -> {
                binding.chooseThemeImage.setImageResource(R.drawable.light_mode)
            }
            else -> {
                binding.chooseThemeImage.setImageResource(R.drawable.dark_mode)
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

    private fun loadSeekBar(seekBar: SeekBar, textView: TextView, value: Int){
        seekBar.progress = value
        textView.text = value.toString()
    }

    private fun loadButtonToggleGroupInt(buttonToggleGroup: MaterialButtonToggleGroup, map: Map<Int, Int>, value: Int) {
        map[value]?.let { buttonToggleGroup.check(it) }
    }
    private fun loadButtonToggleGroupString(buttonToggleGroup: MaterialButtonToggleGroup, map: Map<String, Int>, value: String) {
        map[value]?.let { buttonToggleGroup.check(it) }
    }

    private fun updatePreviewText(inputString: String, numberPrecision: Int, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): String{
        var text = inputString
        text = NumberFormatter.removeSeparators(text, ",")
        text = NumberFormatter.formatResult(
            text,
            numberPrecision,
            groupingSeparatorSymbol,
            decimalSeparatorSymbol
        )

        return text
    }

    override fun recreate() {
        super.recreate()

        chooseColorRecyclerViewIsRecreated = true
    }
}