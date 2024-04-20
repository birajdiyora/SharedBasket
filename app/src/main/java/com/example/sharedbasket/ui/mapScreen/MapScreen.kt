package com.example.sharedbasket.ui.mapScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap

@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {
    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
    ) {

    }
}