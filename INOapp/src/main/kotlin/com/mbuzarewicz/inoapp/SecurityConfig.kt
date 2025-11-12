//// kotlin
//package com.mbuzarewicz.inoapp
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.web.SecurityFilterChain
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.CorsConfigurationSource
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource
//
//@Configuration
//class SecurityConfig {
//
//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        val config = CorsConfiguration()
//        config.allowedOrigins = listOf("https://inoapp-frontend.pages.dev", "http://localhost:4200")
//        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
//        config.allowedHeaders = listOf("*")
//        config.allowCredentials = true
//
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", config)
//        return source
//    }
//
//    @Bean
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        http.cors().and().csrf().disable()
//        http.authorizeHttpRequests { it.anyRequest().permitAll() } // adjust if you have auth rules
//        return http.build()
//    }
//}