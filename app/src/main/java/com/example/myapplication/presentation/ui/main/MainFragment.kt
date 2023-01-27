package com.example.myapplication.presentation.ui.main

import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.myapplication.R
import com.example.myapplication.data.local.entity.history.HistoryEntity
import com.example.myapplication.data.model.gift.Gift
import com.example.myapplication.data.model.gift.levelsGame
import com.example.myapplication.data.model.local.position.Position
import com.example.myapplication.presentation.ui.main.viewModel.HistoryViewModel
import com.example.myapplication.ui.view.CustomView
import com.example.myapplication.ui.view.GameTask
import com.example.myapplication.utils.Constants
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*


@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), GameTask {

    private lateinit var floatButton: FloatingActionButton
    private lateinit var gameLayout: ConstraintLayout
    private lateinit var customView: CustomView
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var historyTextView: TextView
    private lateinit var newsTextView: TextView
    private lateinit var adView: AdView


    private var level = 1

    private lateinit var mp: MediaPlayer

    private val viewModel: HistoryViewModel by viewModels()

    private val mainScope = CoroutineScope(Dispatchers.Main)


    private fun updateDataCanvas() =
        CoroutineScope(Dispatchers.IO + Job()).launch {
            while (true) {
                customView.postInvalidateOnAnimation()
                delay(2)
            }
        }

    private val isGameStart = MutableLiveData(false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        floatButton = view.findViewById(R.id.float_button)
        gameLayout = view.findViewById(R.id.game_layout)
        lottieAnimationView = view.findViewById(R.id.lottie_animation_view)
        historyTextView = view.findViewById(R.id.hitory_txt)
        newsTextView = view.findViewById(R.id.news_txt)
        adView = view.findViewById(R.id.adView)


        lottieAnimationView.visibility = View.GONE


        mp = MediaPlayer.create(requireContext(), R.raw.explosion_sound)

        floatButton.setOnClickListener {
            if (isGameStart.value != null) {
                isGameStart.value = !isGameStart.value!!
            }
        }


        isGameStart.observe(viewLifecycleOwner) { isStart ->
            if (isStart != null) {
                if (isStart) {
                    historyTextView.visibility = View.GONE
                    floatButton.visibility = View.GONE
                    newsTextView.visibility = View.GONE
                    adView.visibility = View.GONE
                    gameView()
                } else {
                    historyTextView.visibility = View.VISIBLE
                    floatButton.visibility = View.VISIBLE
                    newsTextView.visibility = View.VISIBLE
                    showAds()
                }
            }
        }

        historyTextView.setOnClickListener {
//            historyAlertDialog()
            findNavController().navigate(R.id.historyFragment)
        }

        newsTextView.setOnClickListener {
//            findNavController().popBackStack()
            findNavController().navigate(R.id.newsFragment)
        }
    }

    private fun gameView() {
        customView = CustomView(requireContext(), this)
        customView.setData(levelsGame(level)!!.shuffled(), level)
        gameLayout.addView(customView)

        updateDataCanvas()
    }

    override fun nextLevel(gift: Gift, position: Position) {
        updateDataCanvas().cancel()

        mainScope.launch {

            lottieAnimationView.layoutParams.width = position.width * 3
            lottieAnimationView.layoutParams.height = position.height * 3
            lottieAnimationView.x = position.x - position.width
            lottieAnimationView.y = position.y - position.height
            lottieAnimationView.visibility = View.VISIBLE
            mp.start()
            lottieAnimationView.speed = 0.4f

            lottieAnimationView.playAnimation()

            delay(lottieAnimationView.duration * 4)

            lottieAnimationView.visibility = View.GONE
            if (gift.isWinGift) {
                nextLevelAlertDialog(gift)
            } else {
                viewModel.addHistory(
                    HistoryEntity(
                        prizeWon = Constants.BOX_IS_EMPTY,
                        level = level,
                        date = Date()
                    )
                )
                loseAlertDialog(message = Constants.BOX_IS_EMPTY)
            }
        }

    }

    override fun loseGame(message: String) {
        viewModel.addHistory(
            HistoryEntity(
                prizeWon = message,
                level = level,
                date = Date()
            )
        )

        loseAlertDialog(message = message)
        updateDataCanvas().cancel()
        gameLayout.removeAllViews()
    }

    private fun loseAlertDialog(message: String) {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        val inflater: LayoutInflater = LayoutInflater.from(requireContext())
        val loseView: View = inflater.inflate(R.layout.dialog_lose_game, null)
        dialog.setView(loseView)

        val alertDialog = dialog.create()

        val goToMainBtn = loseView.findViewById<Button>(R.id.go_to_main)
        val messageTextView = loseView.findViewById<TextView>(R.id.message_textView)
        messageTextView.text = message

        fun dismissAlert() {
            isGameStart.value = false
            gameLayout.removeAllViews()
            level = 1
            updateDataCanvas().cancel()
            alertDialog.dismiss()
        }

        goToMainBtn.setOnClickListener {
            dismissAlert()
        }
        alertDialog.setOnDismissListener {
            dismissAlert()
        }
        alertDialog.show()
    }

    private fun nextLevelAlertDialog(gift: Gift) {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        val inflater: LayoutInflater = LayoutInflater.from(requireContext())
        val loseView: View = inflater.inflate(R.layout.dialog_next_level, null)

        dialog.setView(loseView)

        val alertDialog = dialog.create()

        val nextLevelBtn: Button = loseView.findViewById(R.id.next_level_btn)
        val giveGiftBtn: Button = loseView.findViewById(R.id.give_gift_btn)


        if (level == 3) {
            nextLevelBtn.visibility = View.GONE
        }

        nextLevelBtn.setOnClickListener {
            isGameStart.value = false
            gameLayout.removeAllViews()
            alertDialog.cancel()

            level += 1
            isGameStart.value = true
        }
        giveGiftBtn.setOnClickListener {
            alertDialog.cancel()
            giveGiftAlertDialog(gift)
            viewModel.addHistory(
                HistoryEntity(
                    prizeWon = gift.inside,
                    level = level,
                    date = Date()
                )
            )
        }

        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)

        alertDialog.show()
    }


    private fun giveGiftAlertDialog(gift: Gift) {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        val inflater: LayoutInflater = LayoutInflater.from(requireContext())
        val winView: View = inflater.inflate(R.layout.dialog_win_game, null)

        dialog.setView(winView)

        val alertDialog = dialog.create()

        val dismissButton = winView.findViewById<Button>(R.id.go_to_main)
        val text = winView.findViewById<TextView>(R.id.textView1)
        val image = winView.findViewById<ImageView>(R.id.image_win)



        text.text = gift.inside
        image.setImageResource(gift.logo!!)

        dismissButton.setOnClickListener {
            alertDialog.dismiss()
            isGameStart.value = false
            level = 1

            gameLayout.removeAllViews()
        }


        alertDialog.setOnDismissListener {
            level = 1
            isGameStart.value = false
        }

        alertDialog.show()
    }


    private fun showAds() {
        adView.visibility = View.VISIBLE
        val request = AdRequest.Builder().build()
        adView.loadAd(request)
    }

}