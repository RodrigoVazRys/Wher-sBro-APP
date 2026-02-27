package com.kazedev.wher_sbro.features.radar.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*
import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.CoordinatesDto
import com.kazedev.wher_sbro.features.radar.domain.location.LocationTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DefaultLocationTracker @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) : LocationTracker {

    @SuppressLint("MissingPermission") // Asumimos que pediremos el permiso en la UI antes de llamar a esto
    override fun getLocationUpdates(intervalMs: Long): Flow<CoordinatesDto> {
        // callbackFlow convierte una API basada en Callbacks (como el GPS de Android) a un Flow reactivo
        return callbackFlow {

            // Configuramos qué tan rápido queremos la ubicación y con qué precisión
            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, intervalMs).build()

            // Este callback se dispara cada vez que el satélite nos da una nueva coordenada
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        // Emitimos la coordenada al Flow
                        trySend(CoordinatesDto(location.latitude, location.longitude))
                    }
                }
            }

            // Encendemos el GPS
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            // Cuando el ViewModel deje de escuchar (ej. cerramos el Radar), apagamos el GPS para no gastar batería
            awaitClose {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
    }
}