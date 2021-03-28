package com.xcnk.cryptot.models.crypto

data class CryptoAsset (
    val id: String,
    var name: String, // Bitcoin
    var code: String, // BTC
    var description: String, // ...
    var iconFileData: CloudFileData?,
    var videoFileData: CloudFileData?,
    var suggestedEvent: CryptoEvent?,
)
