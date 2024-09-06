package com.forz.calculator.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.forz.calculator.R
import com.forz.calculator.calculator.Evaluator
import com.forz.calculator.converter.UnitActionListener
import com.forz.calculator.converter.UnitAdapter
import com.forz.calculator.converter.data.AngleConverter
import com.forz.calculator.converter.data.AreaConverter
import com.forz.calculator.converter.data.DensityConverter
import com.forz.calculator.converter.data.DigitalStorageConverter
import com.forz.calculator.converter.data.EnergyConverter
import com.forz.calculator.converter.data.ForceConverter
import com.forz.calculator.converter.data.FrequencyConverter
import com.forz.calculator.converter.data.FuelConverter
import com.forz.calculator.converter.data.LengthConverter
import com.forz.calculator.converter.data.LightConverter
import com.forz.calculator.converter.data.MassConverter
import com.forz.calculator.converter.data.PowerConverter
import com.forz.calculator.converter.data.PressureConverter
import com.forz.calculator.converter.data.SpeedConverter
import com.forz.calculator.converter.data.TemperatureConverter
import com.forz.calculator.converter.data.TimeConverter
import com.forz.calculator.converter.data.TorqueConverter
import com.forz.calculator.converter.data.UnitConverter
import com.forz.calculator.converter.data.ViscosityConverter
import com.forz.calculator.converter.data.VolumeConverter
import com.forz.calculator.databinding.FragmentUnitConverterBinding
import com.forz.calculator.utils.InteractionAndroid
import kotlin.properties.Delegates.notNull

class UnitConverterFragment : Fragment() {

    companion object {
        var physicalQuantity = 10
        var unit = 1010
    }

    private val lengthConverter = LengthConverter()
    private val timeConverter = TimeConverter()
    private val massConverter = MassConverter()
    private val temperatureConverter = TemperatureConverter()
    private val speedConverter = SpeedConverter()
    private val forceConverter = ForceConverter()
    private val energyConverter = EnergyConverter()
    private val powerConverter = PowerConverter()
    private val pressureConverter = PressureConverter()
    private val volumeConverter = VolumeConverter()
    private val areaConverter = AreaConverter()
    private val densityConverter = DensityConverter()
    private val frequencyConverter = FrequencyConverter()
    private val torqueConverter = TorqueConverter()
    private val lightConverter = LightConverter()
    private val digitalStorageConverter = DigitalStorageConverter()
    private val viscosityConverter = ViscosityConverter()
    private val angleConverter = AngleConverter()
    private val fuelConverter = FuelConverter()

    private lateinit var physicalQuantities: List<Pair<String, UnitConverter>>
    private var binding: FragmentUnitConverterBinding by notNull()
    private var adapter: UnitAdapter by notNull()
    private var callback: OnButtonClickListener? = null

    interface OnButtonClickListener {
        fun onUnitResultTextClick(result: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = parentFragment as OnButtonClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnButtonClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnitConverterBinding.inflate(inflater, container, false)

        adapter = UnitAdapter(requireContext(), object : UnitActionListener {
            override fun onUnitCopyButtonClick(result: String) {
                InteractionAndroid.copyToClipboard(result, requireContext())
            }

            override fun onUnitResultTextClick(result: String) {
                callback?.onUnitResultTextClick(result)
            }

            override fun onUnitResultTextLongClick(result: String) {
                InteractionAndroid.copyToClipboard(result, requireContext())
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.unitRecyclerView.layoutManager = layoutManager
        binding.unitRecyclerView.adapter = adapter

        physicalQuantities = listOf(
            Pair(getString(R.string.physical_quantity_length), lengthConverter),
            Pair(getString(R.string.physical_quantity_time), timeConverter),
            Pair(getString(R.string.physical_quantity_mass), massConverter),
            Pair(getString(R.string.physical_quantity_temperature), temperatureConverter),
            Pair(getString(R.string.physical_quantity_speed), speedConverter),
            Pair(getString(R.string.physical_quantity_force), forceConverter),
            Pair(getString(R.string.physical_quantity_energy), energyConverter),
            Pair(getString(R.string.physical_quantity_power), powerConverter),
            Pair(getString(R.string.physical_quantity_pressure), pressureConverter),
            Pair(getString(R.string.physical_quantity_volume), volumeConverter),
            Pair(getString(R.string.physical_quantity_area), areaConverter),
            Pair(getString(R.string.physical_quantity_density), densityConverter),
            Pair(getString(R.string.physical_quantity_frequency), frequencyConverter),
            Pair(getString(R.string.physical_quantity_torque), torqueConverter),
            Pair(getString(R.string.physical_quantity_light), lightConverter),
            Pair(getString(R.string.physical_quantity_storage), digitalStorageConverter),
            Pair(getString(R.string.physical_quantity_viscosity), viscosityConverter),
            Pair(getString(R.string.physical_quantity_angle), angleConverter),
            Pair(getString(R.string.physical_quantity_fuel), fuelConverter)
        )
            .sortedBy { it.second.id }


        val physicalQuantityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, physicalQuantities.map { it.first })
        binding.physicalQuantityAutoCompleteTextView.setAdapter(physicalQuantityAdapter)


        val initialQuantity = physicalQuantities.find { it.second.id == physicalQuantity }
        if (initialQuantity != null) {
            binding.physicalQuantityAutoCompleteTextView.setText(initialQuantity.first, false)
            binding.physicalQuantityAutoCompleteTextView.contentDescription = binding.physicalQuantityAutoCompleteTextView.text
            updateUnitAutoComplete(initialQuantity.second)
        }


        binding.physicalQuantityAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            binding.physicalQuantityAutoCompleteTextView.contentDescription = binding.physicalQuantityAutoCompleteTextView.text
            val selectedQuantity = physicalQuantities[position]
            physicalQuantity = selectedQuantity.second.id
            updateUnitAutoComplete(selectedQuantity.second)
        }

        binding.unitAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            binding.unitAutoCompleteTextView.contentDescription = binding.unitAutoCompleteTextView.text
            unit = physicalQuantities.find { it.second.id == physicalQuantity }!!.second.units[position].id
            updateUnitPager(Evaluator.converterResult.value, physicalQuantities.find { it.second.id == physicalQuantity }!!.second, unit)
        }


        Evaluator.converterResult.observe(requireActivity()){ converterResult ->
            updateUnitPager(converterResult, physicalQuantities.find { it.second.id == physicalQuantity }!!.second, unit)
        }

        return binding.root
    }

    private fun updateUnitAutoComplete(unitConverter: UnitConverter) {
        val units = unitConverter.units.sortedBy { it.id }
        val unitAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, units.map { getString(it.name) })
        binding.unitAutoCompleteTextView.setAdapter(unitAdapter)

        if (units.isNotEmpty()) {
            val selectedUnit = units.find { it.id == unit }
            if (selectedUnit != null) {
                unit = selectedUnit.id
                binding.unitAutoCompleteTextView.setText(getString(selectedUnit.name), false)
                binding.unitAutoCompleteTextView.contentDescription = binding.unitAutoCompleteTextView.text
            } else {
                unit =  units[0].id
                binding.unitAutoCompleteTextView.setText(getString(units[0].name), false)
            }

            updateUnitPager(Evaluator.converterResult.value, unitConverter, unit)
        }
    }

    private fun updateUnitPager(value: Double?, unitConverter: UnitConverter, unitIndex: Int){
        if (value == null){
            adapter.clearUnits()
            return
        }

        val inputUnit = unitConverter.units.sortedBy { it.id }.find { it.id == unitIndex }
        val results = inputUnit?.let { unitConverter.convertAll(value, it) }

        adapter.updateUnits(results, inputUnit)
    }
}
