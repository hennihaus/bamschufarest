package de.hennihaus.services

interface TrackingService {
    suspend fun trackRequest(username: String?, password: String?)
}
