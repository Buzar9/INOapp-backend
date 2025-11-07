package com.mbuzarewicz.inoapp

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import java.net.URI

@ConfigurationProperties(prefix = "cloudflare.r2")
data class R2Properties(
    var accountId: String = "",
    var accessKeyId: String = "",
    var secretAccessKey: String = "",
    var bucket: String = "",
    var endpoint: String = "",
    var publicBaseUrl: String = "",
    var pathStyle: Boolean = true
)

@Configuration
@EnableConfigurationProperties(R2Properties::class)
class R2Config {

    @Bean
    fun r2S3Client(props: R2Properties): S3Client {
        val creds = StaticCredentialsProvider.create(
            AwsBasicCredentials.create(props.accessKeyId, props.secretAccessKey)
        )

        val s3cfg = S3Configuration.builder()
            .pathStyleAccessEnabled(props.pathStyle) // R2 zwykle wymaga path-style
            .checksumValidationEnabled(false)        // unikamy zbędnych weryfikacji
            .build()

        return S3Client.builder()
            .region(Region.US_EAST_1) // wymagane przez SDK, R2 to i tak ignoruje
            .credentialsProvider(creds)
            .endpointOverride(URI.create(props.endpoint))
            .serviceConfiguration(s3cfg)
            .build()
    }
}