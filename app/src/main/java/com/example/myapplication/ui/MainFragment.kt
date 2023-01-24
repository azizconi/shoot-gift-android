package com.example.myapplication.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.model.Gift
import com.example.myapplication.model.levelsGame
import com.example.myapplication.ui.view.CustomView
import com.example.myapplication.ui.view.GameTask
import com.example.myapplication.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class MainFragment : Fragment(R.layout.main_fragment), GameTask {

    private lateinit var floatButton: FloatingActionButton
    private lateinit var gameLayout: ConstraintLayout
    private lateinit var customView: CustomView

    private var level = 1

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



        floatButton.setOnClickListener {
            if (isGameStart.value != null) {
                isGameStart.value = !isGameStart.value!!
            }
        }


        isGameStart.observe(viewLifecycleOwner) { isStart ->
            if (isStart != null) {
                if (isStart) {
                    floatButton.visibility = View.GONE
                    gameView()
                } else {
                    floatButton.visibility = View.VISIBLE
                }
            }
        }


    }


    private fun gameView() {
        customView = CustomView(requireContext(), this)
        customView.setWindowManager(windowManager = requireActivity().windowManager)

        customView.setData(levelsGame(level)!!.shuffled(), level)

        gameLayout.addView(customView)

        updateDataCanvas()
    }

    override fun nextLevel(gift: Gift) {
        updateDataCanvas().cancel()

        if (gift.isWinGift) {
            nextLevelAlertDialog(gift)
        } else {
            loseAlertDialog(message = Constants.BOX_IS_EMPTY)
        }
    }

    override fun loseGame(message: String) {
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


}