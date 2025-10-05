package com.sevenhallo.quickdrop

interface NearbyDiscovery {
    suspend fun isSupported(): Boolean
    suspend fun startAdvertising(service: String)
    suspend fun startScanning(service: String)
    fun stop()
}
