package com.forz.calculator.history

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.forz.calculator.App
import com.forz.calculator.utils.HapticAndSound
import com.forz.calculator.R
import com.forz.calculator.databinding.ItemHistoryDataBinding
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoUnit
import kotlin.properties.Delegates.notNull

interface HistoryDataActionListener {
    fun onExpressionTextClick(expression: String)
    fun onResultTextClick(result: String)
    fun onExpressionTextLongClick(expression: String)
    fun onResultTextLongClick(result: String)
    fun onCopyButtonClick(string: String)
    fun onShareButtonClick(string: String)
    fun onDeleteItemButtonClick(historyData: HistoryData)
}

class HistoryDataDiffCallback(
    private val oldList: List<HistoryData>,
    private val newList: List<HistoryData>
) : DiffUtil.Callback(){

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldHistoryData = oldList[oldItemPosition]
        val newHistoryData = newList[newItemPosition]
        return oldHistoryData.id == newHistoryData.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldHistoryData = oldList[oldItemPosition]
        val newHistoryData = newList[newItemPosition]
        return oldHistoryData == newHistoryData
    }

}

@Suppress("DEPRECATION")
class HistoryDataAdapter(
    private val context: Context,
    private val actionListener: HistoryDataActionListener
) : RecyclerView.Adapter<HistoryDataAdapter.HistoryDataViewHolder>(),
    View.OnClickListener,
    View.OnLongClickListener
{

    private var hapticAndSound: HapticAndSound by notNull()

    private val historyService: HistoryService
        get() = (context.applicationContext as App).historyService

    var historyDataList: List<HistoryData> = emptyList()
        set(newValue) {
            val diffCallback = HistoryDataDiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    fun deleteHistoryData(historyData: HistoryData) {
        val index = historyDataList.indexOfFirst { it.id == historyData.id }
        val newHistoryDataList = ArrayList(historyDataList)
        newHistoryDataList.removeIf {it.id == historyData.id}
        historyDataList = newHistoryDataList
        historyService.deleteHistoryData(historyData.id)

        notifyItemRangeChanged(index, 2)
    }

    fun addHistoryDataUpdate(){
        notifyItemRangeChanged(0, 2)
    }

    override fun onClick(v: View) {
        val historyData = v.tag as HistoryData
        when (v.id){
            R.id.expressionText -> {
                actionListener.onExpressionTextClick(historyData.expression)
                hapticAndSound.vibrateEffectClick()
            }
            R.id.resultText -> {
                actionListener.onResultTextClick(historyData.result)
                hapticAndSound.vibrateEffectClick()
            }
        }
    }
    override fun onLongClick(v: View): Boolean {
        val historyData = v.tag as HistoryData
        when (v.id){
            R.id.expressionText -> {
                actionListener.onExpressionTextLongClick(historyData.expression)
            }
            R.id.resultText -> {
                actionListener.onResultTextLongClick(historyData.result)
            }
        }
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryDataBinding.inflate(inflater, parent, false)

        val views: Array<View> = arrayOf(
            binding.expressionText,
            binding.resultText
        )

        hapticAndSound = HapticAndSound(parent.context, views)
        hapticAndSound.setSoundEffects()

        binding.expressionText.setOnClickListener(this)
        binding.resultText.setOnClickListener(this)
        binding.expressionText.setOnLongClickListener(this)
        binding.resultText.setOnLongClickListener(this)

        return HistoryDataViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return historyDataList.size
    }

    private var lastDate: LocalDate? = null
    override fun onBindViewHolder(holder: HistoryDataViewHolder, position: Int) {
        val locale = context.resources.configuration.locale
        val isRTL = TextUtilsCompat.getLayoutDirectionFromLocale(locale) == ViewCompat.LAYOUT_DIRECTION_RTL

        val historyData = historyDataList[position]
        with(holder.binding) {
            holder.itemView.tag = historyData
            expressionText.tag = historyData
            resultText.tag = historyData

            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.copy -> {
                        actionListener.onCopyButtonClick(combineExpressionAndResult(historyData.expression, historyData.result))
                        true
                    }
                    R.id.share -> {
                        actionListener.onShareButtonClick(combineExpressionAndResult(historyData.expression, historyData.result))
                        true
                    }
                    R.id.delete -> {
                        actionListener.onDeleteItemButtonClick(historyData)
                        true
                    }
                    else -> false
                }
            }

            lastDate = if (position > 0) {
                historyDataList[position - 1].date
            } else {
                null
            }

            if (lastDate == historyData.date){
                dividingLine.visibility = View.GONE
                dateText.visibility = View.GONE
                dateText.text = ""
            } else {
                if (position == 0){
                    dividingLine.visibility = View.GONE
                } else{
                    dividingLine.visibility = View.VISIBLE
                }

                dateText.visibility = View.VISIBLE
                dateText.text = formatDate(historyData.date)
            }

            if (isRTL){
                expressionText.gravity = Gravity.BOTTOM or Gravity.END
                resultText.gravity = Gravity.BOTTOM or Gravity.END
            }

            expressionText.text = historyData.expression
            resultText.text = historyData.result

            lastDate = historyData.date
        }
    }

    private fun formatDate(date: LocalDate): String{
        val dateNow = LocalDate.now()
        val outputDate: String

        val differenceInDays = ChronoUnit.DAYS.between(date, dateNow).toInt()
        if (differenceInDays in 0..7){
            when (differenceInDays) {
                0 -> {
                    outputDate = context.getString(R.string.today_date)
                }
                1 -> {
                    outputDate = context.getString(R.string.yesterday_date)
                }
                2 -> {
                    outputDate = context.getString(R.string.days_ago_2_date)
                }
                3 -> {
                    outputDate = context.getString(R.string.days_ago_3_date)
                }
                4 -> {
                    outputDate = context.getString(R.string.days_ago_4_date)
                }
                5 -> {
                    outputDate = context.getString(R.string.days_ago_5_date)
                }
                6 -> {
                    outputDate = context.getString(R.string.days_ago_6_date)
                }
                else -> {
                    outputDate = context.getString(R.string.days_ago_7_date)
                }
            }
        } else{
            val day = date.dayOfMonth.toString()
            val month: String

            when (date.month) {
                Month.JANUARY -> {
                    month = context.getString(R.string.month_1_date)
                }
                Month.FEBRUARY -> {
                    month = context.getString(R.string.month_2_date)
                }
                Month.MARCH -> {
                    month = context.getString(R.string.month_3_date)
                }
                Month.APRIL -> {
                    month = context.getString(R.string.month_4_date)
                }
                Month.MAY -> {
                    month = context.getString(R.string.month_5_date)
                }
                Month.JUNE -> {
                    month = context.getString(R.string.month_6_date)
                }
                Month.JULY -> {
                    month = context.getString(R.string.month_7_date)
                }
                Month.AUGUST -> {
                    month = context.getString(R.string.month_8_date)
                }
                Month.SEPTEMBER -> {
                    month = context.getString(R.string.month_9_date)
                }
                Month.OCTOBER -> {
                    month = context.getString(R.string.month_10_date)
                }
                Month.NOVEMBER -> {
                    month = context.getString(R.string.month_11_date)
                }
                else -> {
                    month = context.getString(R.string.month_12_date)
                }
            }

            val year: String = if (dateNow.year == date.year){
                ""
            }else{
                date.year.toString()
            }


            outputDate = "$day $month $year"
        }

        return outputDate
    }

    class HistoryDataViewHolder(
        val binding: ItemHistoryDataBinding
    ) : RecyclerView.ViewHolder(binding.root)

    private fun combineExpressionAndResult(expression: String, result: String): String{
        return "$expression = $result"
    }
}