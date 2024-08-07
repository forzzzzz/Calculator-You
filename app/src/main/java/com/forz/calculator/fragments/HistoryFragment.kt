package com.forz.calculator.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.forz.calculator.App
import com.forz.calculator.R
import com.forz.calculator.databinding.FragmentHistoryBinding
import com.forz.calculator.history.HistoryData
import com.forz.calculator.history.HistoryDataActionListener
import com.forz.calculator.history.HistoryDataAdapter
import com.forz.calculator.history.HistoryDataListListener
import com.forz.calculator.history.HistoryService
import com.forz.calculator.utils.InteractionAndroid
import kotlin.properties.Delegates.notNull

class HistoryFragment : Fragment() {

    companion object{
        private var oldSizeRecyclerViewHistory = 0
        private var newSizeRecyclerViewHistory = 0
        private var currentPositionRecyclerViewHistory = 0
        private var recyclerViewHistoryElementIsAdded = false
        private var recyclerViewHistoryElementIsDeleted = false

        var recyclerViewHistoryIsRecreated = false
    }

    private var binding: FragmentHistoryBinding by notNull()
    private var adapter: HistoryDataAdapter by notNull()
    private var callback: OnButtonClickListener? = null

    private val historyService: HistoryService
        get() = (requireContext().applicationContext as App).historyService


    interface OnButtonClickListener {
        fun onExpressionTextClick(expression: String)
        fun onResultTextClick(result: String)
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
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        adapter = HistoryDataAdapter(requireContext(), object : HistoryDataActionListener {
            override fun onExpressionTextClick(expression: String) {
                callback?.onExpressionTextClick(expression)
            }

            override fun onResultTextClick(result: String) {
                callback?.onResultTextClick(result)
            }

            override fun onExpressionTextLongClick(expression: String) {
                InteractionAndroid.copyToClipboard(expression, requireContext())
            }

            override fun onResultTextLongClick(result: String) {
                InteractionAndroid.copyToClipboard(result, requireContext())
            }

            override fun onCopyButtonClick(string: String) {
                InteractionAndroid.copyToClipboard(string, requireContext())
            }

            override fun onShareButtonClick(string: String) {
                InteractionAndroid.share(string, requireContext())
            }

            override fun onDeleteItemButtonClick(historyData: HistoryData) {
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

        if (oldSizeRecyclerViewHistory < newSizeRecyclerViewHistory || recyclerViewHistoryElementIsAdded){
            adapter.addHistoryDataUpdate()
            binding.recyclerView.scrollToPosition(0)
            if (recyclerViewHistoryIsRecreated){
                recyclerViewHistoryElementIsAdded = !recyclerViewHistoryElementIsAdded
            }
        }else if (oldSizeRecyclerViewHistory > newSizeRecyclerViewHistory || recyclerViewHistoryElementIsDeleted){
            if (recyclerViewHistoryIsRecreated){
                recyclerViewHistoryElementIsDeleted = !recyclerViewHistoryElementIsDeleted
            }
        }else{
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
}