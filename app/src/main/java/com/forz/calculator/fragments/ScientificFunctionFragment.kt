package com.forz.calculator.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.forz.calculator.utils.HapticAndSound
import com.forz.calculator.databinding.FragmentScientificFunctionBinding
import com.forz.calculator.calculator.CalculatorViewModel.updateScienceModActivated
import com.forz.calculator.calculator.Constant
import com.forz.calculator.calculator.ScientificFunction
import com.forz.calculator.calculator.TrigonometricFunction
import kotlin.properties.Delegates

class ScientificFunctionFragment : Fragment() {

    private var binding: FragmentScientificFunctionBinding by Delegates.notNull()
    private var hapticAndSound: HapticAndSound by Delegates.notNull()
    private var callback: OnButtonClickListener? = null

    interface OnButtonClickListener {
        fun onScienceFunctionButtonClick(scienceFunction: String)
        fun onConstantButtonClick(constant: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = parentFragment as? OnButtonClickListener
        if (callback == null) {
            throw ClassCastException("$context must implement OnButtonClickListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScientificFunctionBinding.inflate(inflater, container, false)

        val views: Array<View> = arrayOf(
            binding.absButton,
            binding.lnButton,
            binding.logButton,
            binding.sinButton,
            binding.cosButton,
            binding.tanButton,
            binding.asinButton,
            binding.acosButton,
            binding.atanButton,
            binding.eButton,
            binding.expButton
        )

        hapticAndSound = HapticAndSound(requireContext(), views)

        binding.absButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(ScientificFunction.Absolute.text)
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.lnButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(ScientificFunction.Ln.text)
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.logButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(ScientificFunction.Log.text)
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.sinButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(TrigonometricFunction.Sin.text)
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.cosButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(TrigonometricFunction.Cos.text)
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.tanButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(TrigonometricFunction.Tan.text)
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.asinButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(TrigonometricFunction.ASin.text)
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.acosButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(TrigonometricFunction.ACos.text)
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.atanButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(TrigonometricFunction.ATan.text)
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.eButton.setOnClickListener {
            callback?.onConstantButtonClick(Constant.E.text)
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }
        binding.expButton.setOnClickListener {
            callback?.onScienceFunctionButtonClick(ScientificFunction.PowerE.text)
            updateScienceModActivated()
            hapticAndSound.vibrateEffectClick()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        hapticAndSound.setHapticFeedback()
        hapticAndSound.setSoundEffects()
    }
}