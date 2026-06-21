package com.example.financexs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.financexs.ui.components.clearFocusOnTapOutside
import com.example.financexs.ui.navigation.OnboardingNavGraph
import com.example.financexs.ui.theme.FinancexsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinancexsTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clearFocusOnTapOutside()
                ) {
                    OnboardingNavGraph(
                        onComplete = {
                            // TODO: Navegar a Home
                        }
                    )
                }
            }
        }
    }
}
