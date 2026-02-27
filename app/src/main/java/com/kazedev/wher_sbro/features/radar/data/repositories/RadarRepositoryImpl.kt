package com.kazedev.wher_sbro.features.radar.data.repositories

import android.util.Log
import com.google.gson.Gson
import com.kazedev.wher_sbro.BuildConfig
import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.CoordinatesDto
import com.kazedev.wher_sbro.features.radar.data.datasources.remote.models.WsMessageDto
import com.kazedev.wher_sbro.features.radar.domain.repositories.RadarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.*
import javax.inject.Inject

class RadarRepositoryImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : RadarRepository {

    private var webSocket: WebSocket? = null
    private val gson = Gson()

    // Un SharedFlow con un buffer extra para no perder mensajes rápidos
    private val _incomingMessages = MutableSharedFlow<WsMessageDto>(extraBufferCapacity = 10)

    override fun connectToRadar(roomCode: String, token: String) {
        // Transformamos tu URL base HTTP a WS (WebSocket)
        // OJO: Ajusta "/ws/rooms/" según cómo hayas nombrado tu ruta en FastAPI
        val baseUrl = BuildConfig.BASE_URL_AUTH.replace("http", "ws").replace("https", "wss")
        val wsUrl = "${baseUrl}ws/rooms/$roomCode?token=$token"

        val request = Request.Builder().url(wsUrl).build()

        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("RadarWS", "Conexión abierta en sala: $roomCode")
                _incomingMessages.tryEmit(
                    WsMessageDto(event = "CONNECTED", data = null, message = "Radar en línea")
                )
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("RadarWS", "Mensaje recibido: $text")
                try {
                    // Convertimos el JSON de Python a nuestro objeto Kotlin
                    val message = gson.fromJson(text, WsMessageDto::class.java)
                    _incomingMessages.tryEmit(message)
                } catch (e: Exception) {
                    Log.e("RadarWS", "Error al parsear el JSON", e)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("RadarWS", "Error de conexión: ${t.message}", t)
                _incomingMessages.tryEmit(
                    WsMessageDto(event = "ERROR", data = null, message = "Se perdió la conexión")
                )
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("RadarWS", "Conexión cerrada: $reason")
            }
        })
    }

    override fun getIncomingMessages(): Flow<WsMessageDto> {
        // Exponemos el flujo de datos para que el ViewModel lo escuche
        return _incomingMessages.asSharedFlow()
    }

    override fun sendMyLocation(coordinates: CoordinatesDto) {
        if (webSocket == null) {
            Log.e("RadarWS", "Intento de enviar ubicación sin conexión activa")
            return
        }

        // Empaquetamos la coordenada y la mandamos a Python
        val message = WsMessageDto(
            event = "UPDATE_LOCATION",
            data = coordinates,
            message = null
        )
        val jsonString = gson.toJson(message)
        webSocket?.send(jsonString)
        Log.d("RadarWS", "Ubicación enviada: $jsonString")
    }

    override fun disconnect() {
        // El código 1000 es el estándar de éxito para cerrar un WS
        webSocket?.close(1000, "Cierre normal por el usuario")
        webSocket = null
    }
}