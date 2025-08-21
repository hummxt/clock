package com.example.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clock.ui.theme.ClockTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.*

val ClockFontFamily = FontFamily(
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClockTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ClockScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ClockScreen(modifier: Modifier = Modifier) {
    var currentTime by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000)
        }
    }

    val calendar = Calendar.getInstance().apply { time = currentTime }
    val hours = calendar.get(Calendar.HOUR) % 12
    val minutes = calendar.get(Calendar.MINUTE)
    val seconds = calendar.get(Calendar.SECOND)
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1C1C1E),
                        Color(0xFF0D0D0D)
                    ),
                    radius = 800f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(320.dp)
                    .shadow(
                        elevation = 24.dp,
                        shape = CircleShape,
                        ambientColor = Color.White.copy(alpha = 0.1f),
                        spotColor = Color.White.copy(alpha = 0.1f)
                    )
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF2C2C2E),
                                Color(0xFF1C1C1E)
                            )
                        )
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val radius = size.minDimension / 2

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF3A3A3C),
                                Color(0xFF2C2C2E)
                            )
                        ),
                        radius = radius - 8f,
                        center = Offset(centerX, centerY)
                    )

                    drawCircle(
                        color = Color(0xFF48484A),
                        radius = radius,
                        center = Offset(centerX, centerY),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
                    )

                    drawCircle(
                        color = Color(0xFF636366),
                        radius = radius - 8f,
                        center = Offset(centerX, centerY),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                    )

                    for (i in 1..12) {
                        val angle = i * 30f - 90f
                        val startRadius = radius - 45f
                        val endRadius = radius - 20f
                        val startX = centerX + cos(Math.toRadians(angle.toDouble())).toFloat() * startRadius
                        val startY = centerY + sin(Math.toRadians(angle.toDouble())).toFloat() * startRadius
                        val endX = centerX + cos(Math.toRadians(angle.toDouble())).toFloat() * endRadius
                        val endY = centerY + sin(Math.toRadians(angle.toDouble())).toFloat() * endRadius

                        drawLine(
                            color = Color(0xFFE5E5E7),
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = 4f,
                            cap = StrokeCap.Round
                        )
                    }

                    for (i in 1..60) {
                        if (i % 5 != 0) {
                            val angle = i * 6f - 90f
                            val startRadius = radius - 30f
                            val endRadius = radius - 20f
                            val startX = centerX + cos(Math.toRadians(angle.toDouble())).toFloat() * startRadius
                            val startY = centerY + sin(Math.toRadians(angle.toDouble())).toFloat() * startRadius
                            val endX = centerX + cos(Math.toRadians(angle.toDouble())).toFloat() * endRadius
                            val endY = centerY + sin(Math.toRadians(angle.toDouble())).toFloat() * endRadius

                            drawLine(
                                color = Color(0xFF8E8E93),
                                start = Offset(startX, startY),
                                end = Offset(endX, endY),
                                strokeWidth = 1.5f,
                                cap = StrokeCap.Round
                            )
                        }
                    }

                    val hourAngle = (hours * 30f + minutes * 0.5f) - 90f
                    rotate(hourAngle, Offset(centerX, centerY)) {
                        drawLine(
                            color = Color.Black.copy(alpha = 0.3f),
                            start = Offset(centerX + 3f, centerY + 3f),
                            end = Offset(centerX + 3f, centerY - radius * 0.45f + 3f),
                            strokeWidth = 10f,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = Color(0xFFE5E5E7),
                            start = Offset(centerX, centerY),
                            end = Offset(centerX, centerY - radius * 0.45f),
                            strokeWidth = 8f,
                            cap = StrokeCap.Round
                        )
                    }

                    val minuteAngle = (minutes * 6f) - 90f
                    rotate(minuteAngle, Offset(centerX, centerY)) {
                        drawLine(
                            color = Color.Black.copy(alpha = 0.3f),
                            start = Offset(centerX + 2f, centerY + 2f),
                            end = Offset(centerX + 2f, centerY - radius * 0.65f + 2f),
                            strokeWidth = 6f,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = Color(0xFFE5E5E7),
                            start = Offset(centerX, centerY),
                            end = Offset(centerX, centerY - radius * 0.65f),
                            strokeWidth = 4f,
                            cap = StrokeCap.Round
                        )
                    }

                    val secondAngle = (seconds * 6f) - 90f
                    rotate(secondAngle, Offset(centerX, centerY)) {
                        drawLine(
                            color = Color(0xFFFF453A),
                            start = Offset(centerX, centerY),
                            end = Offset(centerX, centerY + radius * 0.2f),
                            strokeWidth = 2f,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = Color(0xFFFF453A),
                            start = Offset(centerX, centerY),
                            end = Offset(centerX, centerY - radius * 0.75f),
                            strokeWidth = 2f,
                            cap = StrokeCap.Round
                        )
                    }

                    drawCircle(
                        color = Color(0xFF48484A),
                        radius = 16f,
                        center = Offset(centerX, centerY)
                    )

                    drawCircle(
                        color = Color(0xFFFF453A),
                        radius = 8f,
                        center = Offset(centerX, centerY)
                    )

                    drawCircle(
                        color = Color(0xFF2C2C2E),
                        radius = 4f,
                        center = Offset(centerX, centerY)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Card(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                        ambientColor = Color.White.copy(alpha = 0.05f)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1C1C1E).copy(alpha = 0.8f)
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = timeFormat.format(currentTime),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = ClockFontFamily,
                        color = Color(0xFFE5E5E7),
                        textAlign = TextAlign.Center,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = dateFormat.format(currentTime),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = ClockFontFamily,
                        color = Color(0xFF8E8E93),
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClockScreenPreview() {
    ClockTheme {
        ClockScreen()
    }
}
