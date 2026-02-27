package com.kazedev.wher_sbro.features.radar.domain.location

import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.CoordinatesDto
import kotlinx.coroutines.flow.Flow

interface LocationTracker {
    fun getLocationUpdates(intervalMs: Long): Flow<CoordinatesDto>
}