package com.example.aplikasikalkulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF1C2526)
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var currentOperation by remember { mutableStateOf<String?>(null) }
    var operationHistory by remember { mutableStateOf("") }

    fun appendNumber(value: String) {
        if (currentOperation == null) {
            number1 += value
            result = number1
        } else {
            number2 += value
            result = "$number1 $currentOperation $number2"
            operationHistory = "$number1 $currentOperation $number2"
        }
    }

    fun appendOperation(op: String) {
        if (number1.isNotEmpty() && currentOperation == null) {
            currentOperation = op
            result = "$number1 $op"
            operationHistory = "$number1 $op"
        }
    }

    fun calculateResult() {
        if (number1.isNotEmpty() && number2.isNotEmpty() && currentOperation != null) {
            calculate(number1, number2, currentOperation!!) { res ->
                result = res
                operationHistory = "$number1 $currentOperation $number2 = $res"
                number1 = res
                number2 = ""
                currentOperation = null
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Black.copy(alpha = 0.1f)),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                if (operationHistory.isNotEmpty()) {
                    Text(
                        text = operationHistory,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Text(
                    text = result.ifEmpty { "0" },
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton(text = "C", color = Color.Gray, onClick = {
                    number1 = ""
                    number2 = ""
                    result = ""
                    currentOperation = null
                    operationHistory = ""
                })
                CalculatorButton(text = "%", color = Color.Gray, onClick = { appendOperation("%") })
                CalculatorButton(text = "/", color = Color(0xFFFF9800), onClick = { appendOperation("/") })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton(text = "7", color = Color.DarkGray, onClick = { appendNumber("7") })
                CalculatorButton(text = "8", color = Color.DarkGray, onClick = { appendNumber("8") })
                CalculatorButton(text = "9", color = Color.DarkGray, onClick = { appendNumber("9") })
                CalculatorButton(text = "×", color = Color(0xFFFF9800), onClick = { appendOperation("*") })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton(text = "4", color = Color.DarkGray, onClick = { appendNumber("4") })
                CalculatorButton(text = "5", color = Color.DarkGray, onClick = { appendNumber("5") })
                CalculatorButton(text = "6", color = Color.DarkGray, onClick = { appendNumber("6") })
                CalculatorButton(text = "-", color = Color(0xFFFF9800), onClick = { appendOperation("-") })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton(text = "1", color = Color.DarkGray, onClick = { appendNumber("1") })
                CalculatorButton(text = "2", color = Color.DarkGray, onClick = { appendNumber("2") })
                CalculatorButton(text = "3", color = Color.DarkGray, onClick = { appendNumber("3") })
                CalculatorButton(text = "+", color = Color(0xFFFF9800), onClick = { appendOperation("+") })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton(text = "0", color = Color.DarkGray, onClick = { appendNumber("0") }, modifier = Modifier.weight(1f))
                CalculatorButton(text = ".", color = Color.DarkGray, onClick = { appendNumber(".") })
                CalculatorButton(text = "=", color = Color(0xFFFF9800), onClick = { calculateResult() })
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
        .size(70.dp)
        .padding(4.dp)
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = text, fontSize = 24.sp, color = Color.White)
    }
}

fun calculate(num1: String, num2: String, operation: String, onResult: (String) -> Unit) {
    try {
        val number1 = num1.toDoubleOrNull() ?: return
        val number2 = num2.toDoubleOrNull() ?: return

        val result = when (operation) {
            "+" -> number1 + number2
            "-" -> number1 - number2
            "*" -> number1 * number2
            "/" -> {
                if (number2 == 0.0) {
                    onResult("Tidak bisa membagi dengan nol")
                    return
                }
                number1 / number2
            }
            "%" -> number1 % number2
            else -> 0.0
        }
        onResult(String.format("%.2f", result))
    } catch (e: Exception) {
        onResult("Error: Masukkan angka yang valid")
    }
}
