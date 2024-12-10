package com.example.randocolorgen

import android.app.Activity  // Import Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext  // Import LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class HelpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HelpContent()
                }
            }
        }
    }
}

@Composable
fun HelpContent() {
    val context = LocalContext.current  // Get the current context

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Help",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "About the App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "This app generates random colors and displays their corresponding HEX, RGB, RGBA, ARGB, and HSL values. "
                    + "It helps developers or designers easily explore color combinations or understand color formats.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Preferences",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "The app includes a dark mode preference. Switching between dark and light mode allows users to "
                    + "choose a theme that is easier on their eyes. This preference is saved and automatically applied the next time the app is opened.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Add Exit Button
        Spacer(modifier = Modifier.weight(1f))  // To push the exit button to the bottom
        Button(
            onClick = { (context as? Activity)?.finish() },  // Use context to finish the activity
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Exit", fontSize = 18.sp)
        }
    }
}
