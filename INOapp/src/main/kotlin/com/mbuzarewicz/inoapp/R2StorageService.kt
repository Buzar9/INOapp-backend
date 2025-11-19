package com.mbuzarewicz.inoapp

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Path

@Service
class R2StorageService(
    private val s3: S3Client,
    private val props: R2Properties
) : ObjectStorage {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun uploadFile(
        file: Path,
        key: String,
        contentType: String?,
        cacheControl: String?
    ) {
        val req = PutObjectRequest.builder()
            .bucket(props.bucket)
            .key(key)
            .apply {
                contentType?.let { contentType(it) }
                cacheControl?.let { cacheControl(it) }
            }
            .build()

        s3.putObject(req, RequestBody.fromFile(file))

        val base = props.publicBaseUrl.ifBlank {
            "${props.endpoint.trimEnd('/')}/${props.bucket}"
        }
    }

    override fun deleteFile(key: String): Boolean {
        return try {
            val req = DeleteObjectRequest.builder()
                .bucket(props.bucket)
                .key(key)
                .build()

            s3.deleteObject(req)
            logger.info("Deleted object from R2: $key")
            true
        } catch (e: Exception) {
            logger.error("Failed to delete object $key from R2: ${e.message}", e)
            false
        }
    }
}
