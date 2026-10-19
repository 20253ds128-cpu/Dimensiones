package mx.edu.utez.dimensiones.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import mx.edu.utez.dimensiones.R

@Composable
fun DimensionDialog(
    currentWidth: Int,
    currentHeight: Int,
    currentElevation: Int,
    onDismiss: () -> Unit,
    onConfirm: (width: Int, height: Int, elevation: Int) -> Unit
) {
    // Estados locales para los campos de texto
    var widthText by remember { mutableStateOf(currentWidth.toString()) }
    var heightText by remember { mutableStateOf(currentHeight.toString()) }
    var elevationText by remember { mutableStateOf(currentElevation.toString()) }

    // Estado para guardar el mensaje de error de validación
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.dialog_title))
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Campo Ancho
                OutlinedTextField(
                    value = widthText,
                    onValueChange = {
                        widthText = it
                        errorMessage = null // Limpia el error al escribir
                    },
                    label = { Text(stringResource(R.string.label_width)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                // Campo Alto
                OutlinedTextField(
                    value = heightText,
                    onValueChange = {
                        heightText = it
                        errorMessage = null
                    },
                    label = { Text(stringResource(R.string.label_height)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                // Campo Elevación
                OutlinedTextField(
                    value = elevationText,
                    onValueChange = {
                        elevationText = it
                        errorMessage = null
                    },
                    label = { Text(stringResource(R.string.label_elevation)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                // Mensaje de advertencia si la validación falla
                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                // Conversión segura de String a Int (evita NumberFormatException)
                val w = widthText.toIntOrNull()
                val h = heightText.toIntOrNull()
                val e = elevationText.toIntOrNull()

                // VALIDACIONES
                if (w == null || h == null || e == null) {
                    errorMessage = context.getString(R.string.err_empty)
                } else if (w !in 10..300 || h !in 10..300 || e !in 0..24) {
                    errorMessage = context.getString(R.string.err_out_of_range)
                } else {
                    onConfirm(w, h, e)
                }
            }) {
                Text(stringResource(R.string.btn_accept))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_cancel))
            }
        }
    )
}