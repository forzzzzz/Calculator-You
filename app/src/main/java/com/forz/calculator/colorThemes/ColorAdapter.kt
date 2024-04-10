package com.forz.calculator.colorThemes

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.forz.calculator.R
import com.forz.calculator.databinding.ItemColorBinding
import com.forz.calculator.databinding.ItemHistoryDataBinding
import com.forz.calculator.history.HistoryDataAdapter
import com.forz.calculator.viewModels.SettingsViewModel

typealias OnSelectListener = (Color) -> Unit

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
        Color(id = 6, name = "color_5", color = R.color.colorPrimaryContainer_5, isSelected = false)
    )

    init {
        colors.forEach { color ->
            if (color.name == SettingsViewModel.color.value!!) {
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
        SettingsViewModel.setColor(color.name)
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

            if (position == 0) {
                binding.selectColorBorder.visibility = View.GONE
                (binding.background.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 0
            } else if (position == 1) {
                binding.selectColorBorder.visibility = View.VISIBLE
                (binding.background.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 0
            } else {
                binding.selectColorBorder.visibility = View.GONE

                val density = context.resources.displayMetrics.density
                val marginStart = (8 * density).toInt()
                (binding.background.layoutParams as ViewGroup.MarginLayoutParams).marginStart = marginStart
            }

            if (color.isSelected) {
                binding.selected.visibility = View.VISIBLE
            } else {
                binding.selected.visibility = View.GONE
            }
        }
    }



    class ColorViewHolder(
        val binding: ItemColorBinding
    ) : RecyclerView.ViewHolder(binding.root)
}