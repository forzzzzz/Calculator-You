package com.forz.calculator

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
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.forz.calculator.databinding.ActivitySettingsBinding
import com.forz.calculator.viewModels.ExpressionViewModel
import com.forz.calculator.viewModels.SettingsViewModel.decimalSeparatorSymbol
import com.forz.calculator.viewModels.SettingsViewModel.groupingSeparatorSymbol
import com.forz.calculator.viewModels.SettingsViewModel.numberPrecision
import com.forz.calculator.viewModels.SettingsViewModel.previousDecimalSeparatorSymbol
import com.forz.calculator.viewModels.SettingsViewModel.previousGroupingSeparatorSymbol
import com.forz.calculator.viewModels.SettingsViewModel.setDecimalSeparatorSymbol
import com.forz.calculator.viewModels.SettingsViewModel.setGroupingSeparatorSymbol
import com.forz.calculator.viewModels.SettingsViewModel.setNumberPrecision
import com.forz.calculator.viewModels.SettingsViewModel.setSound
import com.forz.calculator.viewModels.SettingsViewModel.setVibration
import com.forz.calculator.viewModels.SettingsViewModel.sound
import com.forz.calculator.viewModels.SettingsViewModel.vibration
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.materialswitch.MaterialSwitch
import kotlin.properties.Delegates.notNull


@Suppress("DEPRECATION")
class SettingsActivity : AppCompatActivity() {

    private var binding: ActivitySettingsBinding by notNull()
    private var preferences: Preferences by notNull()
    private var vibrator: Vibrator by notNull()


    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = Preferences(this)
        (this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).also { vibrator = it }

        if (!preferences.getDynamicColor()){
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





        loadButtonToggleGroupInt(binding.chooseThemeButtonToggleGroup, themeMap, preferences.getTheme())
        loadButtonToggleGroupString(binding.groupingSeparatorSymbolButtonToggleGroup, groupingSeparatorMap, preferences.getGroupingSeparatorSymbol())
        loadButtonToggleGroupString(binding.decimalSeparatorSymbolButtonToggleGroup, decimalSeparatorMap, preferences.getDecimalSeparatorSymbol())




        if (preferences.getDynamicColor()){
            binding.chooseColorLayout.visibility = View.GONE
        } else{
            binding.chooseColorLayout.visibility = View.VISIBLE

            val id = preferences.getColor()
            val idDone = id + "_selected"
            val resourceIdDone = resources.getIdentifier(idDone, "id", packageName)

            val viewDone = findViewById<View>(resourceIdDone)
            viewDone.visibility = View.VISIBLE

            val resourceId = resources.getIdentifier(id, "id", packageName)
            val view = findViewById<View>(resourceId)
            binding.chooseColorHorizontalScroll.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.post {
                        val targetLeft = view.left
                        val scrollX = targetLeft + view.width/2 - binding.chooseColorHorizontalScroll.width / 2
                        binding.chooseColorHorizontalScroll.smoothScrollTo(scrollX, 0)
                    }

                    binding.chooseColorHorizontalScroll.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }

        if (!vibrator.hasVibrator()){
            binding.vibrationLayout.visibility = View.GONE
        }else{
            binding.vibrationLayout.visibility = View.VISIBLE
        }



        loadSettings()




        binding.chooseThemeButtonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val newNightMode = when (checkedId) {
                    R.id.chooseThemeAutoButton -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    R.id.chooseThemeLightButton -> AppCompatDelegate.MODE_NIGHT_NO
                    R.id.chooseThemeDarkButton -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> return@addOnButtonCheckedListener
                }

                val currentNightMode = preferences.getTheme()
                if (currentNightMode != newNightMode) {
                    preferences.setTheme(newNightMode)
                    AppCompatDelegate.setDefaultNightMode(newNightMode)
                }
            }
        }

        binding.dynamicColorsLayout.setOnClickListener {
            preferences.setDynamicColor(switchSwitch(binding.dynamicColorsSwitch))
            recreate()
        }
        binding.dynamicColorsSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.setDynamicColor(isChecked)
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
                setNumberPrecision(binding.precisionSeekBar.progress)
            }
        })


        val invertedGroupingSeparatorMap = groupingSeparatorMap.entries.associateBy({ it.value }, { it.key })
        val invertedDecimalSeparatorMap = decimalSeparatorMap.entries.associateBy({ it.value }, { it.key })


        binding.groupingSeparatorSymbolButtonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val separator = invertedGroupingSeparatorMap[checkedId]
                setGroupingSeparatorSymbol(separator!!)
                binding.previewFormatText.text = updatePreviewText(getString(R.string.preview_format_text), numberPrecision.value!!, groupingSeparatorSymbol.value!!, decimalSeparatorSymbol.value!!)

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
                setDecimalSeparatorSymbol(separator!!)
                binding.previewFormatText.text = updatePreviewText(getString(R.string.preview_format_text), numberPrecision.value!!, groupingSeparatorSymbol.value!!, decimalSeparatorSymbol.value!!)

                if (binding.groupingSeparatorSymbolButtonToggleGroup.checkedButtonId != R.id.groupingSeparatorSymbolSpaceButton){
                    if (separator == ","){
                        binding.groupingSeparatorSymbolButtonToggleGroup.check(R.id.groupingSeparatorSymbolDotButton)
                    }else if (separator == "."){
                        binding.groupingSeparatorSymbolButtonToggleGroup.check(R.id.groupingSeparatorSymbolCommaButton)
                    }
                }
            }
        }










        binding.vibrationLayout.setOnClickListener {
            setVibration(switchSwitch(binding.vibrationSwitch))
        }
        binding.vibrationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setVibration(true)
            } else {
                setVibration(false)
            }
        }

        binding.soundEffectsLayout.setOnClickListener {
            setSound(switchSwitch(binding.soundEffectsSwitch))
        }
        binding.soundEffectsSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setSound(true)
            } else {
                setSound(false)
            }
        }





        binding.arrowBack.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()

        ExpressionViewModel.updateSymbolsInExpression(
            previousGroupingSeparatorSymbol,
            groupingSeparatorSymbol.value!!,
            previousDecimalSeparatorSymbol,
            decimalSeparatorSymbol.value!!
        )

//        preferences.setTheme(SettingsViewModel.theme.value!!)
//        preferences.setColor(color.value!!)
        preferences.setGroupingSeparatorSymbol(groupingSeparatorSymbol.value!!)
        preferences.setDecimalSeparatorSymbol(decimalSeparatorSymbol.value!!)
        preferences.setNumberPrecision(numberPrecision.value!!)
        preferences.setVibration(vibration.value!!)
        preferences.setSoundEffects(sound.value!!)
    }

    private fun loadSettings(){



//        loadChooseThemeButtonToggleGroup()
        loadChooseThemeImage()
        loadDynamicColorSwitch()
        loadVibrationSwitch()
        loadSoundEffectsSwitchSwitch()
        loadSeekBar(binding.precisionSeekBar, binding.precisionValueText, preferences.getNumberPrecision())
        binding.previewFormatText.text = updatePreviewText(getString(R.string.preview_format_text), preferences.getNumberPrecision(), preferences.getGroupingSeparatorSymbol(), preferences.getDecimalSeparatorSymbol())
    }



    private fun loadSpinner(spinner: Spinner, item: Int, arrayResId: Int, context: Context) {
        ArrayAdapter.createFromResource(
            context,
            arrayResId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.setSelection(item)
    }

    private fun loadChooseThemeButtonToggleGroup(){
        when (preferences.getTheme()){
            -1 -> binding.chooseThemeButtonToggleGroup.check(R.id.chooseThemeAutoButton)
            1 -> binding.chooseThemeButtonToggleGroup.check(R.id.chooseThemeLightButton)
            2 -> binding.chooseThemeButtonToggleGroup.check(R.id.chooseThemeDarkButton)
        }
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
            binding.dynamicColorsSwitch.isChecked = preferences.getDynamicColor()
        }else{
            binding.dynamicColorsLayout.visibility = View.GONE
        }
    }

    private fun loadVibrationSwitch(){
        binding.vibrationSwitch.isChecked = preferences.getVibration()
    }

    private fun loadSoundEffectsSwitchSwitch(){
        binding.soundEffectsSwitch.isChecked = preferences.getSoundEffects()
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





    fun selectColor(view: View){
        val resourceId = view.id
        val resourceName = view.resources.getResourceName(resourceId)
        val resourceNameWithoutPackage = resourceName.substringAfterLast("/")
        preferences.setColor(resourceNameWithoutPackage)
        recreate()
    }


    private fun updatePreviewText(inputString: String, numberPrecision: Int, groupingSeparatorSymbol: String, decimalSeparatorSymbol: String): String{
        var text = inputString
        text = NumberFormatter.removeSeparators(text, ",")
        text = NumberFormatter.formatResult(text, numberPrecision,  groupingSeparatorSymbol, decimalSeparatorSymbol)

        return text
    }
}