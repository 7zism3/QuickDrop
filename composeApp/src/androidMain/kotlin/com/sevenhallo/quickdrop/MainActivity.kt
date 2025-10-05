package com.sevenhallo.quickdrop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sevenhallo.quickdrop.ui.WifiAwareApp
import com.sevenhallo.quickdrop.ui.theme.QuickDropTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            QuickDropTheme {
                WifiAwareApp()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    QuickDropTheme {
        WifiAwareApp()
    }
}
