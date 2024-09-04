package com.forz.calculator.converter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.forz.calculator.R
import com.forz.calculator.converter.data.ConverterUnit
import com.forz.calculator.databinding.ItemUnitBinding
import com.forz.calculator.settings.Config.decimalSeparatorSymbol
import com.forz.calculator.settings.Config.groupingSeparatorSymbol
import com.forz.calculator.settings.Config.maxScientificNotationDigits
import com.forz.calculator.settings.Config.numberPrecision
import com.forz.calculator.utils.HapticAndSound
import com.forz.calculator.utils.NumberFormatter
import java.math.BigDecimal
import kotlin.properties.Delegates.notNull

interface UnitActionListener {
    fun onUnitCopyButtonClick(result: String)
    fun onUnitResultTextClick(result: String)
    fun onUnitResultTextLongClick(result: String)
}

class UnitAdapter(
    private val context: Context,
    private val actionListener: UnitActionListener
    ) : RecyclerView.Adapter<UnitAdapter.UnitViewHolder>(), View.OnClickListener, View.OnLongClickListener {

    private var hapticAndSound: HapticAndSound by notNull()

    private var units: MutableList<Unit> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateUnits(results: List<Pair<ConverterUnit, Double>>?, sourceUnit: ConverterUnit?) {
        val updatedUnits = results?.mapIndexed { _, pair ->
            Unit(
                id = pair.first.id,
                unit = context.getString(pair.first.name),
                result = NumberFormatter.formatResult(
                    BigDecimal(pair.second),
                    numberPrecision,
                    maxScientificNotationDigits,
                    groupingSeparatorSymbol,
                    decimalSeparatorSymbol
                )
            )
        }?.filter {
            it.id != sourceUnit?.id
        }

        units.clear()
        updatedUnits?.let { units.addAll(it) }

        notifyDataSetChanged()
    }

    fun clearUnits() {
        val size = units.size
        units.clear()
        notifyItemRangeRemoved(0, size)
    }

    override fun onClick(v: View) {
        val unit = v.tag as Unit

        when (v.id){
            R.id.resultText -> {
                actionListener.onUnitResultTextClick(unit.result)
                hapticAndSound.vibrateEffectClick()
            }
        }
    }

    override fun onLongClick(v: View): Boolean {
        val unit = v.tag as Unit
        when (v.id){
            R.id.resultText -> {
                actionListener.onUnitResultTextLongClick(unit.result)
            }
        }
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUnitBinding.inflate(inflater, parent, false)

        val views: Array<View> = arrayOf(
            binding.resultText
        )

        hapticAndSound = HapticAndSound(parent.context, views)
        hapticAndSound.setSoundEffects()

        binding.resultText.setOnClickListener(this)
        binding.resultText.setOnLongClickListener(this)

        return UnitViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return units.size
    }

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) {
        val unit = units[position]
        with(holder) {
            binding.resultText.tag = unit

            binding.toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.copy -> {
                        actionListener.onUnitCopyButtonClick(unit.result)
                        true
                    }
                    else -> false
                }

            }

            binding.unitText.text = unit.unit
            binding.resultText.text = unit.result
        }
    }


    class UnitViewHolder(
        val binding: ItemUnitBinding
    ) : RecyclerView.ViewHolder(binding.root)
}