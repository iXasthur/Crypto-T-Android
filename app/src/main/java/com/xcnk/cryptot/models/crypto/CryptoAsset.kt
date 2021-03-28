package com.xcnk.cryptot.models.crypto

data class CryptoAsset (
    val id: String,
    val name: String, // Bitcoin
    val code: String, // BTC
    val description: String, // ...
    val iconFileData: CloudFileData?,
    val videoFileData: CloudFileData?,
    val suggestedEvent: CryptoEvent?,
)
