/*
* NJR 2024/12/09
* random color generator
*
* Changes:
* added Weezer Mode - its just fun feature one of my friends suggested
* its a toggleable button like the dark/light mode button
* #189bbc is the blue color on blue album
*
* */

package com.example.randocolorgen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import kotlin.random.Random
import androidx.compose.ui.graphics.Color as ComposeColor

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
            val isDarkModePref = sharedPreferences.getBoolean("isDarkMode", false)

            var isDarkMode by remember { mutableStateOf(isDarkModePref) }

            // Save the preference when it changes
            LaunchedEffect(isDarkMode) {
                sharedPreferences.edit().putBoolean("isDarkMode", isDarkMode).apply()
            }

            MaterialTheme(colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RandocolorgenGenerator(
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { isDarkMode = !isDarkMode }
                    )
                }
            }
        }
    }
}

@Composable
fun RandocolorgenGenerator(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    var randomColor by remember { mutableStateOf(ComposeColor.Black) }
    var hexInput by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    var showImage by remember { mutableStateOf(false) } // State for toggling the image

    val context = LocalContext.current
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current

    val isValidHex = { hex: String ->
        hex.matches(Regex("^#([A-Fa-f0-9]{6})$"))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display Color Box with optional image overlay
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(color = randomColor),
            contentAlignment = Alignment.Center
        ) {
            if (showImage) {
                // Display an image overlay
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.weezer), // Replace with your drawable
                    contentDescription = "Overlay Image",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Color Values
        Text(
            text = "HEX: ${randomColor.toHex()}",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = "RGB: ${randomColor.toRgb()}",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = "RGBA: ${randomColor.toRgba()}",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = "ARGB: ${randomColor.toArgb()}",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = "HSL: ${randomColor.toHsl()}",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Generate Random Color Button
        Button(
            onClick = {
                randomColor = getRandomColor()
                errorMessage = ""
            }
        ) {
            Text("Generate Random Color")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // HEX Code Input Field
        TextField(
            value = hexInput,
            onValueChange = { hexInput = it },
            label = { Text("Enter HEX Code") },
            modifier = Modifier.fillMaxWidth()
        )

        // Display error message if input is invalid
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Apply HEX Code Button
        Button(
            onClick = {
                if (isValidHex(hexInput.text)) {
                    randomColor = hexToComposeColor(hexInput.text)
                    errorMessage = ""
                } else {
                    errorMessage = "Invalid HEX Code! Please enter a valid format (#RRGGBB)."
                }
            }
        ) {
            Text("Apply HEX Code")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Copy Color Values Button
        Button(
            onClick = {
                val colorValues = """
                    HEX: ${randomColor.toHex()}
                    RGB: ${randomColor.toRgb()}
                    RGBA: ${randomColor.toRgba()}
                    ARGB: ${randomColor.toArgb()}
                    HSL: ${randomColor.toHsl()}
                """.trimIndent()

                clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(colorValues))
                Toast.makeText(context, "Color values copied to clipboard!", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text("Copy Color Values")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dark/Light Mode Toggle
        Button(onClick = onToggleDarkMode) {
            Text(if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Help Button
        Button(onClick = {
            val intent = Intent(context, HelpActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Help")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Toggle Image Button
        Button(
            onClick = { showImage = !showImage }
        ) {
            Text(if (showImage) "Weezer Mode Off" else "Weezer Mode On")
        }

    }
}



// Function to convert HEX to ComposeColor
fun hexToComposeColor(hex: String): ComposeColor {
    val color = android.graphics.Color.parseColor(hex)
    return ComposeColor(
        red = android.graphics.Color.red(color) / 255f,
        green = android.graphics.Color.green(color) / 255f,
        blue = android.graphics.Color.blue(color) / 255f
    )
}

// Extension functions to convert ComposeColor to different formats
fun ComposeColor.toHex(): String {
    val red = (red * 255).toInt()
    val green = (green * 255).toInt()
    val blue = (blue * 255).toInt()
    return "#%02X%02X%02X".format(red, green, blue)
}

fun ComposeColor.toRgb(): String {
    val red = (red * 255).toInt()
    val green = (green * 255).toInt()
    val blue = (blue * 255).toInt()
    return "$red, $green, $blue"
}

fun ComposeColor.toRgba(): String {
    val red = (red * 255).toInt()
    val green = (green * 255).toInt()
    val blue = (blue * 255).toInt()
    return "$red, $green, $blue, 255"
}

fun ComposeColor.toArgb(): String {
    val red = (red * 255).toInt()
    val green = (green * 255).toInt()
    val blue = (blue * 255).toInt()
    return "255, $red, $green, $blue"
}

// Manually convert ComposeColor to HSL using ColorUtils
fun ComposeColor.toHsl(): String {
    val red = (red * 255).toInt()
    val green = (green * 255).toInt()
    val blue = (blue * 255).toInt()

    // Using Android's ColorUtils to convert RGB to HSL
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(android.graphics.Color.rgb(red, green, blue), hsl)

    return "(${hsl[0].toInt()}, ${hsl[1] * 100}%, ${hsl[2] * 100}%)"
}

private fun getRandomColor(): ComposeColor {
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    return ComposeColor(red / 255f, green / 255f, blue / 255f)
}

