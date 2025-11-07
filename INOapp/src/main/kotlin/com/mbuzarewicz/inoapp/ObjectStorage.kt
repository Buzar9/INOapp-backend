package com.mbuzarewicz.inoapp

import java.nio.file.Path

interface ObjectStorage {
    fun uploadFile(
        file: Path,
        key: String,
        contentType: String? = null,
        cacheControl: String? = null
    ): String
}