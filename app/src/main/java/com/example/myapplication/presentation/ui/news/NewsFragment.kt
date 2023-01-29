package com.example.myapplication.presentation.ui.news

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.local.entity.news.ArticleEntity
import com.example.myapplication.presentation.ui.news.adapter.NewsAdapter
import com.example.myapplication.presentation.ui.news.viewModel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news) {

    private lateinit var backBtn: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var topBarName: TextView

    private lateinit var adapter: NewsAdapter

    private lateinit var progressBar: ProgressBar


    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backBtn = view.findViewById(R.id.back_btn)
        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView = view.findViewById(R.id.news_recycler_view)
        topBarName = view.findViewById(R.id.top_bar_name)


        topBarName.text = "Новости"

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter = NewsAdapter(requireContext())
        recyclerView.adapter = adapter

        backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        val newsObserver = Observer<List<ArticleEntity>> { state ->
            if (state != null) {
                adapter.setData(state)
            }
        }

        viewModel.newsState.observe(viewLifecycleOwner) { state ->
            progressBar.visibility = if (state.isLoading) {
//                viewModel.getNewsFromDB.removeObserver(newsObserver)
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        viewModel.getNewsFromDB.observe(viewLifecycleOwner, newsObserver)




    }

}