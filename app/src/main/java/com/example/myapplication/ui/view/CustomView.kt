package com.example.myapplication.ui.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.myapplication.R
import com.example.myapplication.model.Gift
import com.example.myapplication.utils.Constants
import kotlinx.coroutines.*
import kotlin.math.abs
import kotlin.math.atan

class CustomView(
    context: Context,
    private val gameTask: GameTask
) : View(context) {

    private var isShootBall: Boolean = false
    private var isShowLine: Boolean = false
    private var isGameEnd: Boolean = false


    private lateinit var paint: Paint
    private var xPos = 0f
    private var yPos = 0f


    private var directionX = -1.0
    private var directionY = -1.0

    private val speed = 10f


    private var xStartPointer = 0f
    private var yStartPointer = 0f
    private var xEndPointer = 0f
    private var yEndPointer = 0f



    private var gifts: List<Gift>? = null
    private var levelGame: Int = 1

    private lateinit var windowManager: WindowManager


    private var startLineX = 100f
    private var startStartAndEndLineY = (400 * 1.5).toFloat()
    private var directionLineX = 1.0
    private var endLineX = (600).toFloat()

    fun setWindowManager(windowManager: WindowManager) {
        this.windowManager = windowManager

        xPos = (windowManager.defaultDisplay.width / 2).toFloat()
        yPos = (windowManager.defaultDisplay.height * 80 / 100).toFloat()

        xStartPointer = (windowManager.defaultDisplay.width / 2).toFloat() + 40
        yStartPointer = (windowManager.defaultDisplay.height * 80 / 100).toFloat() + 40
    }

    fun setData(list: List<Gift>, level: Int) {
        gifts = list
        if (level == 3) {
            startStartAndEndLineY = 400f
        }
        levelGame = level
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        fun items(data: List<Gift>) {
            val borderToNextItem = display.width / 3
            var itemX = if (data.size % 2 == 0) {
                (borderToNextItem / 3).toFloat()
            } else {
                (borderToNextItem * 1.2).toFloat()
            }
            var itemY = 80f

            for (item in data) {
                try {
                    val gift =
                        ResourcesCompat.getDrawable(context.resources, R.drawable.gift_icon, null)
                            ?.toBitmap(width = borderToNextItem / 2, height = borderToNextItem / 2)

                    canvas?.drawBitmap(gift!!, itemX, itemY, paint)


                    if (isShootBall) {
                        if (
                            yPos in itemY..itemY + ((borderToNextItem - 20) / 2) &&
                            xPos in itemX..itemX + ((borderToNextItem - 20) / 2) ||
                            yPos in itemY..itemY + ((borderToNextItem - 20) / 2) &&
                            xPos + 60 in itemX..itemX + ((borderToNextItem - 20) / 2)
                        ) {
                            isGameEnd = true
                            gameTask.nextLevel(item)
                            isShootBall = false
                        }
                    }


                    itemX += borderToNextItem
                    if (itemX > display.width) {
                        itemX = (borderToNextItem / 3).toFloat()
                        itemY += borderToNextItem / 1.5f
                    }


                } catch (e: Exception) {

                }
            }

        }



        paint = Paint()
        paint.color = Color.LTGRAY
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 2f


        val display = windowManager.defaultDisplay


        val bitmap = ResourcesCompat.getDrawable(context.resources, R.drawable.ball_icon, null)
            ?.toBitmap(width = 80, height = 80)

        canvas?.drawBitmap(bitmap!!, xPos, yPos, paint)


        if (isShowLine) {
            canvas?.drawLine(
                xStartPointer,
                yStartPointer,
                xEndPointer,
                yEndPointer,
                paint
            )
        }

        items(gifts!!)



        if (isShootBall) {
            if (xPos <= 0) {
                directionX = 1.0

            } else if (xPos >= display.width) {
                directionX = -1.0
            }


            if (yPos <= 0) {
                directionY = 1.0
            } else if (yPos >= display.height - 80) {
                directionY = -1.0
                isGameEnd = true
                gameTask.loseGame(Constants.BALL_DONT_TOUCH_TO_BOX_MESSAGE)
            }

            xPos += directionX.toFloat() * speed * 2f
            yPos += directionY.toFloat() * speed * 2f
        }


        paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 10f


        if (levelGame == 2 || levelGame == 3) {

            canvas?.drawLine(/*startX, startY, 50f, paint*/startLineX,
                startStartAndEndLineY,
                endLineX,
                startStartAndEndLineY,
                paint
            )


            if (
                yPos in startStartAndEndLineY - 10..startStartAndEndLineY + 10 &&
                xPos in startLineX..endLineX
            ) {
                gameTask.loseGame(Constants.BALL_IN_LINE_MESSAGE)
                isShootBall = false
                isGameEnd = true
            } else if (
                xPos + 80 in startLineX..endLineX &&
                yPos + 80 in startStartAndEndLineY - 10..startStartAndEndLineY + 10
            ) {
                gameTask.loseGame(Constants.BALL_IN_LINE_MESSAGE)
                isShootBall = false
                isGameEnd = true
            }

                if (startLineX < 0) {
                    directionLineX = 1.0
                } else if (endLineX >= display.width) {
                    directionLineX = -1.0
                }

            if (!isGameEnd) {
                startLineX += directionLineX.toFloat() * speed
                endLineX += directionLineX.toFloat() * speed
            }


        }




    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)


        if (!isShootBall && !isGameEnd) {
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {

                    xEndPointer = event.x
                    if ((windowManager.defaultDisplay.height * 78 / 100).toFloat() >= event.y) {
                        yEndPointer = event.y
                    }

                }
                MotionEvent.ACTION_MOVE -> {
                    isShowLine = true
                    xEndPointer = event.x
                    if ((windowManager.defaultDisplay.height * 78 / 100).toFloat() >= event.y) {
                        yEndPointer = event.y
                    }
                }
                MotionEvent.ACTION_UP -> {
                    xEndPointer = event.x
                    yEndPointer = event.y


                    val center = xPos
                    val startBallPosition = yPos


                    val degree = if (xEndPointer <= center) {
                        Math.toDegrees(
                            atan(
                                abs(yEndPointer.toDouble() - startBallPosition) / abs(xEndPointer.toDouble() - center.toDouble())
                            )
                        )
                    } else {
                        Math.toDegrees(
                            atan(
                                abs(startBallPosition - yEndPointer.toDouble()) / abs(center.toDouble() - xEndPointer.toDouble())
                            )
                        )
                    }

                    val resultDegree = (1 * degree) / 90

                    if (xEndPointer <= center) {
                        if (resultDegree < 0.5) {
                            directionY = -(resultDegree + resultDegree)
                        } else {
                            directionX = resultDegree - 1.0
                        }
                    } else {
                        if (resultDegree < 0.5) {
                            directionY = -(resultDegree + resultDegree)
                            directionX = abs(directionX)
                        } else {
                            directionX = 1.0 - resultDegree
                        }
                    }


                    isShootBall = true
                    isShowLine = false
                }
            }
        }


        return true
    }
}


