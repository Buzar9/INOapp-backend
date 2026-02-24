package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.domain.model.vo.Size
import java.nio.file.Path

data class SliceResult(
    val zipPath: Path,
    val fileSizeByZoom: Map<Int, Size>,
)
