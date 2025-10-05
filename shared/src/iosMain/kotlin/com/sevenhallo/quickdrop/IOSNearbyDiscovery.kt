package com.sevenhallo.quickdrop

class IOSNearbyDiscovery : NearbyDiscovery {
    override suspend fun isSupported(): Boolean {
        // TODO: check WiFiAware capability/entitlement
        return true
    }
    override suspend fun startAdvertising(service: String) { /* WiFiAware publish */ }
    override suspend fun startScanning(service: String) { /* WiFiAware subscribe */ }
    override fun stop() { /* close sessions */ }
}
