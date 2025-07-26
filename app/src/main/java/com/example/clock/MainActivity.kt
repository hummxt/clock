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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A1A), // Top - almost black
                        Color(0xFF2A2333), // Middle - darker violet-gray
                        Color(0xFF3C2F4F))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Analog Clock
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val radius = size.minDimension / 2

                // Draw clock face
                drawCircle(
                    color = Color(0xFFF8F9FA),
                    radius = radius,
                    center = Offset(centerX, centerY)
                )

                // Draw outer ring
                drawCircle(
                    color = Color(0xFF495057),
                    radius = radius,
                    center = Offset(centerX, centerY),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 8f)
                )

                // Draw hour markers
                for (i in 1..12) {
                    val angle = i * 30f - 90f
                    val startRadius = radius - 40f
                    val endRadius = radius - 20f
                    val startX = centerX + cos(Math.toRadians(angle.toDouble())).toFloat() * startRadius
                    val startY = centerY + sin(Math.toRadians(angle.toDouble())).toFloat() * startRadius
                    val endX = centerX + cos(Math.toRadians(angle.toDouble())).toFloat() * endRadius
                    val endY = centerY + sin(Math.toRadians(angle.toDouble())).toFloat() * endRadius

                    drawLine(
                        color = Color(0xFF212529),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 6f,
                        cap = StrokeCap.Round
                    )
                }

                // Draw minute markers
                for (i in 1..60) {
                    if (i % 5 != 0) {
                        val angle = i * 6f - 90f
                        val startRadius = radius - 25f
                        val endRadius = radius - 15f
                        val startX = centerX + cos(Math.toRadians(angle.toDouble())).toFloat() * startRadius
                        val startY = centerY + sin(Math.toRadians(angle.toDouble())).toFloat() * startRadius
                        val endX = centerX + cos(Math.toRadians(angle.toDouble())).toFloat() * endRadius
                        val endY = centerY + sin(Math.toRadians(angle.toDouble())).toFloat() * endRadius

                        drawLine(
                            color = Color(0xFF6C757D),
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = 2f,
                            cap = StrokeCap.Round
                        )
                    }
                }

                // Draw hour hand
                val hourAngle = (hours * 30f + minutes * 0.5f) - 90f
                rotate(hourAngle, Offset(centerX, centerY)) {
                    drawLine(
                        color = Color(0xFF343A40),
                        start = Offset(centerX, centerY),
                        end = Offset(centerX, centerY - radius * 0.5f),
                        strokeWidth = 8f,
                        cap = StrokeCap.Round
                    )
                }

                // Draw minute hand
                val minuteAngle = (minutes * 6f) - 90f
                rotate(minuteAngle, Offset(centerX, centerY)) {
                    drawLine(
                        color = Color(0xFF495057),
                        start = Offset(centerX, centerY),
                        end = Offset(centerX, centerY - radius * 0.7f),
                        strokeWidth = 6f,
                        cap = StrokeCap.Round
                    )
                }

                // Draw second hand
                val secondAngle = (seconds * 6f) - 90f
                rotate(secondAngle, Offset(centerX, centerY)) {
                    drawLine(
                        color = Color(0xFFDC3545),
                        start = Offset(centerX, centerY),
                        end = Offset(centerX, centerY - radius * 0.8f),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )
                }

                // Draw center dot
                drawCircle(
                    color = Color(0xFF343A40),
                    radius = 12f,
                    center = Offset(centerX, centerY)
                )

                drawCircle(
                    color = Color(0xFFDC3545),
                    radius = 6f,
                    center = Offset(centerX, centerY)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Digital time display
        Card(
            modifier = Modifier.padding(horizontal = 32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.2f)
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = timeFormat.format(currentTime),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = dateFormat.format(currentTime),
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
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