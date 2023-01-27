package com.example.myapplication.presentation.ui.history

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.local.entity.history.HistoryEntity
import com.example.myapplication.presentation.ui.main.adapter.HistoryAdapter
import com.example.myapplication.presentation.ui.main.viewModel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment: Fragment(R.layout.fragment_history) {

    private lateinit var backBtn: ImageView
    private lateinit var topBarName: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter

    private val viewModel: HistoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backBtn = view.findViewById(R.id.back_btn)
        topBarName = view.findViewById(R.id.top_bar_name)
        recyclerView = view.findViewById(R.id.history_recycler_view)
        topBarName.text = "Истории"

        backBtn.setOnClickListener {
            findNavController().navigateUp()
        }



        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter = HistoryAdapter()
        recyclerView.adapter = adapter



        viewModel.histories.observe(viewLifecycleOwner) {
                adapter.setData(it.asReversed() ?: emptyList())
        }

    }


}