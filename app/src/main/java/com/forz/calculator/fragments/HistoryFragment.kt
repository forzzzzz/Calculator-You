package com.forz.calculator.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.forz.calculator.App
import com.forz.calculator.R
import com.forz.calculator.StateViews.currentPositionRecyclerViewHistory
import com.forz.calculator.StateViews.isFirstStartRecyclerViewHistory
import com.forz.calculator.databinding.FragmentHistoryBinding
import com.forz.calculator.history.HistoryData
import com.forz.calculator.history.HistoryDataActionListener
import com.forz.calculator.history.HistoryDataAdapter
import com.forz.calculator.history.HistoryDataListListener
import com.forz.calculator.history.HistoryService
import com.forz.calculator.viewModels.ExpressionViewModel
import com.forz.calculator.StateViews.newSizeRecyclerViewHistory
import com.forz.calculator.StateViews.oldSizeRecyclerViewHistory
import kotlin.properties.Delegates.notNull

class HistoryFragment : Fragment() {

    private var binding: FragmentHistoryBinding by notNull()
    private var adapter: HistoryDataAdapter by notNull()

    private val historyService: HistoryService
        get() = (requireContext().applicationContext as App).historyService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        adapter = HistoryDataAdapter(requireContext(), object : HistoryDataActionListener {
            override fun insertExpression(historyData: HistoryData) {
                ExpressionViewModel.insertExpression(historyData.expression)
            }

            override fun insertResult(historyData: HistoryData) {
                ExpressionViewModel.insertResult(historyData.result)
            }

            override fun copyExpression(historyData: HistoryData) {
                copy(historyData.expression)
            }

            override fun copyResult(historyData: HistoryData) {
                copy(historyData.result)
            }

            override fun copy(historyData: HistoryData) {
                copy(combineExpressionAndResult(historyData.expression, historyData.result))
            }

            override fun share(historyData: HistoryData) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, combineExpressionAndResult(historyData.expression, historyData.result))
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            override fun deleteItem(historyData: HistoryData) {
                adapter.deleteHistoryData(historyData)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        historyService.addListener(historyDataListListener)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        historyService.removeListener(historyDataListListener)
    }

    override fun onStop() {
        super.onStop()

        val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager?
        currentPositionRecyclerViewHistory = layoutManager!!.findFirstVisibleItemPosition()
    }

    private val historyDataListListener: HistoryDataListListener = {
        adapter.historyDataList = it

        newSizeRecyclerViewHistory = adapter.historyDataList.size

        if (isFirstStartRecyclerViewHistory){
            binding.recyclerView.scrollToPosition(newSizeRecyclerViewHistory - 1)
            isFirstStartRecyclerViewHistory = false
        }else if (oldSizeRecyclerViewHistory < newSizeRecyclerViewHistory){
            binding.recyclerView.scrollToPosition(newSizeRecyclerViewHistory - 1)
        }else if (oldSizeRecyclerViewHistory == newSizeRecyclerViewHistory){
            binding.recyclerView.scrollToPosition(currentPositionRecyclerViewHistory)
        }

        oldSizeRecyclerViewHistory = adapter.historyDataList.size

        if (adapter.historyDataList.isEmpty()){
            val fadeInAnimation200: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_200)

            binding.recyclerView.visibility = View.GONE
            binding.emptyHistoryImageView.visibility = View.VISIBLE
            binding.emptyHistoryText.visibility = View.VISIBLE

            binding.emptyHistoryImageView.startAnimation(fadeInAnimation200)
            binding.emptyHistoryText.startAnimation(fadeInAnimation200)
        } else{
            binding.recyclerView.visibility = View.VISIBLE
            binding.emptyHistoryImageView.visibility = View.GONE
            binding.emptyHistoryText.visibility = View.GONE
        }
    }

    private fun copy(string: String){
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(null, string)
        clipboardManager.setPrimaryClip(clipData)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
        }
    }

    private fun combineExpressionAndResult(expression: String, result: String): String{
        return "$expression = $result"
    }
}