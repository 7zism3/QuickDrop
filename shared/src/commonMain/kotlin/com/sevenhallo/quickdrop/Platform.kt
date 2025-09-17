package com.sevenhallo.quickdrop

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform