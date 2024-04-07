package com.forz.calculator.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.forz.calculator.HapticAndSound
import com.forz.calculator.R
import com.forz.calculator.databinding.FragmentScientificFunctionBinding
import com.forz.calculator.viewModels.CalculatorViewModel.isDegreeModActivated
import com.forz.calculator.viewModels.CalculatorViewModel.updateDegreeModActivated
import com.forz.calculator.viewModels.CalculatorViewModel.updateScienceModActivated
import com.forz.calculator.viewModels.ExpressionViewModel
import kotlin.properties.Delegates

class ScientificFunctionFragment : Fragment() {

    private var binding: FragmentScientificFunctionBinding by Delegates.notNull()
    private var hapticAndSound: HapticAndSound by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScientificFunctionBinding.inflate(inflater, container, false)

        val views: Array<View> = arrayOf(
            binding.degreeButton,
            binding.lnButton,
            binding.logButton,
            binding.sinButton,
            binding.cosButton,
            binding.tanButton,
            binding.sin1Button,
            binding.cos1Button,
            binding.tan1Button,
            binding.eButton,
            binding.expButton
        )

        hapticAndSound = HapticAndSound(requireContext(), views)
        hapticAndSound.setHapticFeedback()
        hapticAndSound.setSoundEffects()

        binding.degreeButton.setOnClickListener {
            updateDegreeModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.lnButton.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("ln(")
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.logButton.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("log(")
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.sinButton.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("sin(")
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.cosButton.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("cos(")
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.tanButton.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("tan(")
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.sin1Button.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("sin⁻¹(")
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.cos1Button.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("cos⁻¹(")
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.tan1Button.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("tan⁻¹(")
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.eButton.setOnClickListener {
            ExpressionViewModel.enterConstant("e")
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.expButton.setOnClickListener {
            ExpressionViewModel.enterScienceFunction("exp(")
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }


        isDegreeModActivated.observe(viewLifecycleOwner){ isDegreeModActivated ->
            if (isDegreeModActivated) {
                binding.degreeButton.text = getString(R.string.deg)
            } else {
                binding.degreeButton.text = getString(R.string.rad)
            }
        }



        return binding.root
    }
}