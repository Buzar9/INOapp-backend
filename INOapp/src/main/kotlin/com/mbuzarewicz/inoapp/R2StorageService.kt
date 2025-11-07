package com.mbuzarewicz.inoapp

import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Path

@Service
class R2StorageService(
    private val s3: S3Client,
    private val props: R2Properties
) : ObjectStorage {

    override fun uploadFile(
        file: Path,
        key: String,
        contentType: String?,
        cacheControl: String?
    ): String {
        val req = PutObjectRequest.builder()
            .bucket(props.bucket)
            .key(key)
            .apply {
                contentType?.let { contentType(it) }
                cacheControl?.let { cacheControl(it) }
            }
            .build()

        s3.putObject(req, RequestBody.fromFile(file))

        // Zwracamy URL do odczytu. Dla path-style będzie to endpoint/bucket/key.
        val base = if (props.publicBaseUrl.isNotBlank()) props.publicBaseUrl
        else "${props.endpoint.trimEnd('/')}/${props.bucket}"
        return "$base/$key"
    }
}