package com.tubanco.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tubanco.calculadora.ui.theme.CalculadoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    CalculadoraUI()
                }
            }
        }
    }
}

@Composable
fun CalculadoraUI() {
    var display by remember { mutableStateOf("0") }
    var operador by remember { mutableStateOf<String?>(null) }
    var operandoAnterior by remember { mutableStateOf<String?>(null) }
    var resetDisplay by remember { mutableStateOf(false) }

    fun agregarDigito(digito: String) {
        display = if (display == "0" || resetDisplay) digito else display + digito
        resetDisplay = false
    }

    fun agregarPunto() {
        if (!display.contains(".")) {
            display += "."
        }
    }

    fun operar(simbolo: String) {
        operador = simbolo
        operandoAnterior = display
        resetDisplay = true
    }

    fun calcular() {
        val anterior = operandoAnterior?.toDoubleOrNull()
        val actual = display.toDoubleOrNull()
        if (anterior != null && actual != null && operador != null) {
            val resultado = when (operador) {
                "+" -> anterior + actual
                "-" -> anterior - actual
                "×" -> anterior * actual
                "÷" -> if (actual != 0.0) anterior / actual else return
                else -> return
            }
            display = resultado.toString().removeSuffix(".0")
            operandoAnterior = null
            operador = null
            resetDisplay = true
        }
    }

    fun limpiar() {
        display = "0"
        operador = null
        operandoAnterior = null
        resetDisplay = false
    }

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {

        Text(
            text = display,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.End
        )

        val botones = listOf(
            listOf("7", "8", "9", "AC"),
            listOf("4", "5", "6", "×"),
            listOf("1", "2", "3", "-"),
            listOf("÷", "0", ".", "+")
        )

        botones.forEach { fila ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                fila.forEach { texto ->
                    Button(
                        onClick = {
                            when (texto) {
                                in "0".."9" -> agregarDigito(texto)
                                "." -> agregarPunto()
                                "+", "-", "×", "÷" -> operar(texto)
                                "AC" -> limpiar()
                            }
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                    ) {
                        Text(texto)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { calcular() },
                modifier = Modifier
                    .weight(3f)
                    .padding(4.dp)
            ) {
                Text("=")
            }
        }
    }
}
