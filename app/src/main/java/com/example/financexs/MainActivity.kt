package com.example.financexs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.financexs.ui.navigation.OnboardingNavGraph
import com.example.financexs.ui.theme.FinancexsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinancexsTheme {
                OnboardingNavGraph(
                    onComplete = {
                        // TODO: Navegar a Home
                    }
                )
            }
        }
    }
}
