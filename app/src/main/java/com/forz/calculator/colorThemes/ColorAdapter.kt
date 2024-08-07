package com.forz.calculator.colorThemes

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.forz.calculator.R
import com.forz.calculator.databinding.ItemColorBinding
import com.forz.calculator.settings.Config

interface ColorActionListener {
    fun onSelectColor(color: Color)
}

class ColorAdapter(
    private val context: Context,
    private val actionListener: ColorActionListener
    ) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>(), View.OnClickListener {

     private val colors = listOf(
        Color(id = 0, name = "color_0", color = R.color.colorPrimaryContainer_0, isSelected = false),
        Color(id = 1, name = "color_Black_And_White", color = R.color.colorPrimaryContainer_Black_And_White, isSelected = false),
        Color(id = 2, name = "color_1", color = R.color.colorPrimaryContainer_1, isSelected = false),
        Color(id = 3, name = "color_2", color = R.color.colorPrimaryContainer_2, isSelected = false),
        Color(id = 4, name = "color_3", color = R.color.colorPrimaryContainer_3, isSelected = false),
        Color(id = 5, name = "color_4", color = R.color.colorPrimaryContainer_4, isSelected = false),
        Color(id = 6, name = "color_5", color = R.color.colorPrimaryContainer_5, isSelected = false),
        Color(id = 7, name = "color_6", color = R.color.colorPrimaryContainer_6, isSelected = false),
        Color(id = 8, name = "color_7", color = R.color.colorPrimaryContainer_7, isSelected = false),
        Color(id = 9, name = "color_8", color = R.color.colorPrimaryContainer_8, isSelected = false),
        Color(id = 10, name = "color_9", color = R.color.colorPrimaryContainer_9, isSelected = false),
        Color(id = 11, name = "color_10", color = R.color.colorPrimaryContainer_10, isSelected = false),
        Color(id = 12, name = "color_11", color = R.color.colorPrimaryContainer_11, isSelected = false),
        Color(id = 13, name = "color_12", color = R.color.colorPrimaryContainer_12, isSelected = false),
        Color(id = 14, name = "color_13", color = R.color.colorPrimaryContainer_13, isSelected = false),
        Color(id = 15, name = "color_14", color = R.color.colorPrimaryContainer_14, isSelected = false),
        Color(id = 16, name = "color_15", color = R.color.colorPrimaryContainer_15, isSelected = false),
        Color(id = 17, name = "color_16", color = R.color.colorPrimaryContainer_16, isSelected = false),
        Color(id = 18, name = "color_17", color = R.color.colorPrimaryContainer_17, isSelected = false),
        Color(id = 19, name = "color_18", color = R.color.colorPrimaryContainer_18, isSelected = false),
        Color(id = 20, name = "color_19", color = R.color.colorPrimaryContainer_19, isSelected = false),
        Color(id = 21, name = "color_20", color = R.color.colorPrimaryContainer_20, isSelected = false),
        Color(id = 22, name = "color_21", color = R.color.colorPrimaryContainer_21, isSelected = false),
        Color(id = 23, name = "color_22", color = R.color.colorPrimaryContainer_22, isSelected = false),
        Color(id = 24, name = "color_23", color = R.color.colorPrimaryContainer_23, isSelected = false),
        Color(id = 25, name = "color_24", color = R.color.colorPrimaryContainer_24, isSelected = false),
        Color(id = 26, name = "color_25", color = R.color.colorPrimaryContainer_25, isSelected = false),
        Color(id = 27, name = "color_26", color = R.color.colorPrimaryContainer_26, isSelected = false),
        Color(id = 28, name = "color_27", color = R.color.colorPrimaryContainer_27, isSelected = false),
        Color(id = 29, name = "color_28", color = R.color.colorPrimaryContainer_28, isSelected = false),
        Color(id = 30, name = "color_29", color = R.color.colorPrimaryContainer_29, isSelected = false),
        Color(id = 31, name = "color_30", color = R.color.colorPrimaryContainer_30, isSelected = false),
        Color(id = 32, name = "color_31", color = R.color.colorPrimaryContainer_31, isSelected = false),
        Color(id = 33, name = "color_32", color = R.color.colorPrimaryContainer_32, isSelected = false),
        Color(id = 34, name = "color_33", color = R.color.colorPrimaryContainer_33, isSelected = false),
        Color(id = 35, name = "color_34", color = R.color.colorPrimaryContainer_34, isSelected = false),
        Color(id = 36, name = "color_35", color = R.color.colorPrimaryContainer_35, isSelected = false),
        Color(id = 37, name = "color_36", color = R.color.colorPrimaryContainer_36, isSelected = false),
        Color(id = 38, name = "color_37", color = R.color.colorPrimaryContainer_37, isSelected = false),
        Color(id = 39, name = "color_38", color = R.color.colorPrimaryContainer_38, isSelected = false),
        Color(id = 40, name = "color_39", color = R.color.colorPrimaryContainer_39, isSelected = false),
        Color(id = 41, name = "color_40", color = R.color.colorPrimaryContainer_40, isSelected = false),
        Color(id = 42, name = "color_41", color = R.color.colorPrimaryContainer_41, isSelected = false),
        Color(id = 43, name = "color_42", color = R.color.colorPrimaryContainer_42, isSelected = false),
        Color(id = 44, name = "color_43", color = R.color.colorPrimaryContainer_43, isSelected = false),
        Color(id = 45, name = "color_44", color = R.color.colorPrimaryContainer_44, isSelected = false),
        Color(id = 46, name = "color_45", color = R.color.colorPrimaryContainer_45, isSelected = false),
        Color(id = 47, name = "color_46", color = R.color.colorPrimaryContainer_46, isSelected = false),
        Color(id = 48, name = "color_47", color = R.color.colorPrimaryContainer_47, isSelected = false),
        Color(id = 49, name = "color_48", color = R.color.colorPrimaryContainer_48, isSelected = false),
        Color(id = 50, name = "color_49", color = R.color.colorPrimaryContainer_49, isSelected = false),
        Color(id = 51, name = "color_50", color = R.color.colorPrimaryContainer_50, isSelected = false),
        Color(id = 52, name = "color_51", color = R.color.colorPrimaryContainer_51, isSelected = false),
        Color(id = 53, name = "color_52", color = R.color.colorPrimaryContainer_52, isSelected = false),
        Color(id = 54, name = "color_53", color = R.color.colorPrimaryContainer_53, isSelected = false),
        Color(id = 55, name = "color_54", color = R.color.colorPrimaryContainer_54, isSelected = false),
        Color(id = 56, name = "color_55", color = R.color.colorPrimaryContainer_55, isSelected = false),
        Color(id = 57, name = "color_56", color = R.color.colorPrimaryContainer_56, isSelected = false),
        Color(id = 58, name = "color_57", color = R.color.colorPrimaryContainer_57, isSelected = false),
        Color(id = 59, name = "color_58", color = R.color.colorPrimaryContainer_58, isSelected = false),
        Color(id = 60, name = "color_59", color = R.color.colorPrimaryContainer_59, isSelected = false),
        Color(id = 61, name = "color_60", color = R.color.colorPrimaryContainer_60, isSelected = false)
    )

    init {
        colors.forEach { color ->
            if (color.name == Config.color) {
                color.isSelected = true
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun selectColor(color: Color) {
        colors.forEach { c ->
            c.isSelected = false
        }

        color.isSelected = true
        Config.color = color.name
    }

    fun getIdSelectedColor(): Int {
        colors.forEach { c ->
            if (c.isSelected) {
                return c.id.toInt()
            }
        }
        return -1
    }
    override fun onClick(v: View) {
        val color = v.tag as Color
        actionListener.onSelectColor(color)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemColorBinding.inflate(inflater, parent, false)

        binding.background.setOnClickListener(this)

        return ColorViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = colors[position]
        with(holder) {
            binding.background.tag = color

            binding.color.circleColor = ContextCompat.getColor(context, color.color)

            when (position) {
                0 -> {
                    binding.selectColorBorder.visibility = View.GONE
                    (binding.background.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 0
                }
                1 -> {
                    binding.selectColorBorder.visibility = View.VISIBLE
                    (binding.background.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 0
                }
                else -> {
                    binding.selectColorBorder.visibility = View.GONE

                    val density = context.resources.displayMetrics.density
                    val marginStart = (8 * density).toInt()
                    (binding.background.layoutParams as ViewGroup.MarginLayoutParams).marginStart = marginStart
                }
            }

            if (color.isSelected) {
                binding.selected.visibility = View.VISIBLE
            } else {
                binding.selected.visibility = View.GONE
            }

            binding.background.contentDescription = color.name
        }
    }



    class ColorViewHolder(
        val binding: ItemColorBinding
    ) : RecyclerView.ViewHolder(binding.root)
}