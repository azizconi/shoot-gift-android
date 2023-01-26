package com.example.myapplication.ui.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.myapplication.R
import com.example.myapplication.data.model.gift.Gift
import com.example.myapplication.data.model.local.position.Position
import com.example.myapplication.utils.Constants
import kotlin.math.abs
import kotlin.math.atan


class CustomView(
    context: Context,
    private val gameTask: GameTask
) : View(context) {

    private var isShootBall: Boolean = false
    private var isShowLine: Boolean = false
    private var isGameEnd: Boolean = false


    private var paint: Paint = Paint()
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


    private var startLineX = 100f
    private var startStartAndEndLineY = (400 * 1.5).toFloat()
    private var directionLineX = 1.0
    private var endLineX = (600).toFloat()

    private var metrics = Resources.getSystem().displayMetrics

    init {
        xPos = (metrics.widthPixels / 2).toFloat()
        yPos = (metrics.heightPixels * 80 / 100).toFloat()

        xStartPointer = (metrics.widthPixels / 2).toFloat() + 40
        yStartPointer = (metrics.heightPixels * 80 / 100).toFloat() + 40
    }

    fun setData(list: List<Gift>, level: Int) {
        gifts = list
        if (level == 3) {
            startStartAndEndLineY = 400f
        }
        levelGame = level
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        fun items(data: List<Gift>) {
            val borderToNextItem = metrics.widthPixels / 3
            var itemX = if (data.size % 2 == 0) {
                (borderToNextItem / 3).toFloat()
            } else {
                (borderToNextItem * 1.2).toFloat()
            }
            var itemY = 80f

            for (item in data.indices) {
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
                            gameTask.nextLevel(
                                data[item],
                                Position(gift!!.width, gift.height, itemX, itemY)
                            )
                            isShootBall = false
                        }
                    }


                    itemX += borderToNextItem
                    if (itemX > metrics.widthPixels) {
                        itemX = (borderToNextItem / 3).toFloat()
                        itemY += borderToNextItem / 1.5f
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }



        paint.color = Color.LTGRAY
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 2f

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

            } else if (xPos >= metrics.widthPixels) {
                directionX = -1.0
            }


            if (yPos <= 0) {
                directionY = 1.0
            } else if (yPos >= metrics.heightPixels - 80) {
                directionY = -1.0
                isGameEnd = true
                gameTask.loseGame(Constants.BALL_DONT_TOUCH_TO_BOX_MESSAGE)
            }

            xPos += directionX.toFloat() * speed * 2f
            yPos += directionY.toFloat() * speed * 2f
        }


        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 10f


        if (levelGame in 2..3) {
            canvas?.drawLine(
                startLineX, startStartAndEndLineY, endLineX, startStartAndEndLineY, paint
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
            } else if (endLineX >= metrics.widthPixels) {
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
                    if ((metrics.heightPixels * 78 / 100).toFloat() >= event.y) {
                        yEndPointer = event.y
                    }

                }
                MotionEvent.ACTION_MOVE -> {
                    isShowLine = true
                    xEndPointer = event.x
                    if ((metrics.heightPixels * 78 / 100).toFloat() >= event.y) {
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


