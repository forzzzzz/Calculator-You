package com.forz.calculator.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.forz.calculator.utils.Anim
import com.forz.calculator.utils.HapticAndSound
import com.forz.calculator.databinding.FragmentDigitBinding
import com.forz.calculator.settings.Config.decimalSeparatorSymbol
import kotlin.properties.Delegates

class DigitFragment : Fragment() {

    private var binding: FragmentDigitBinding by Delegates.notNull()
    private var hapticAndSound: HapticAndSound by Delegates.notNull()
    private var callback: OnButtonClickListener? = null

    interface OnButtonClickListener {
        fun onDigitButtonClick(digit: String)
        fun onDotButtonClick()
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
        binding = FragmentDigitBinding.inflate(inflater, container, false)

        binding.dotButton.text = decimalSeparatorSymbol

        val views: Array<View> = arrayOf(
            binding.digit0Button,
            binding.digit1Button,
            binding.digit2Button,
            binding.digit3Button,
            binding.digit4Button,
            binding.digit5Button,
            binding.digit6Button,
            binding.digit7Button,
            binding.digit8Button,
            binding.digit9Button,
            binding.dotButton
        )

        hapticAndSound = HapticAndSound(requireContext(), views)

        binding.digit0Button.setOnClickListener {
            callback?.onDigitButtonClick("0")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit0Button, requireContext())
        }
        binding.digit1Button.setOnClickListener {
            callback?.onDigitButtonClick("1")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit1Button, requireContext())
        }
        binding.digit2Button.setOnClickListener {
            callback?.onDigitButtonClick("2")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit2Button, requireContext())
        }
        binding.digit3Button.setOnClickListener {
            callback?.onDigitButtonClick("3")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit3Button, requireContext())
        }
        binding.digit4Button.setOnClickListener {
            callback?.onDigitButtonClick("4")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit4Button, requireContext())
        }
        binding.digit5Button.setOnClickListener {
            callback?.onDigitButtonClick("5")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit5Button, requireContext())
        }
        binding.digit6Button.setOnClickListener {
            callback?.onDigitButtonClick("6")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit6Button, requireContext())
        }
        binding.digit7Button.setOnClickListener {
            callback?.onDigitButtonClick("7")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit7Button, requireContext())
        }
        binding.digit8Button.setOnClickListener {
            callback?.onDigitButtonClick("8")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit8Button, requireContext())
        }
        binding.digit9Button.setOnClickListener {
            callback?.onDigitButtonClick("9")
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.digit9Button, requireContext())
        }
        binding.dotButton.setOnClickListener {
            callback?.onDotButtonClick()
            hapticAndSound.vibrateEffectClick()
            Anim.buttonAnim(binding.dotButton, requireContext())
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        hapticAndSound.setHapticFeedback()
        hapticAndSound.setSoundEffects()
    }
}