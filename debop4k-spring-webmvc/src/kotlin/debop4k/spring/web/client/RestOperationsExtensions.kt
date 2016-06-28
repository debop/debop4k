/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("restextensions")

package debop4k.spring.web.client

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestOperations
import java.net.URI

/**
 * @author sunghyouk.bae@gmail.com
 */

// GET

@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.getForObject(url: String, vararg uriVariables: Any): T
    = getForObject(url, T::class.java, *uriVariables)

@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.getForObject(url: String, uriVariables: Map<String, Any?>): T
    = getForObject(url, T::class.java, uriVariables)

@Throws(RestClientException::class)
inline fun<reified T : Any> RestOperations.getForObject(uri: URI): T
    = getForObject(uri, T::class.java)

@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.getForEntity(url: String, vararg uriVariables: Any): ResponseEntity<T>
    = getForEntity(url, T::class.java, *uriVariables)

@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.getForEntity(url: String, uriVariables: Map<String, Any?>): ResponseEntity<T>
    = getForEntity(url, T::class.java, uriVariables)


// POST

@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.postForObject(url: String, request: Any, vararg uriVariables: Any): T
    = postForObject(url, request, T::class.java, *uriVariables)

@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.postForObject(url: String, request: Any, uriVariables: Map<String, Any?>): T
    = postForObject(url, request, T::class.java, uriVariables)

@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.postForObject(uri: URI, request: Any): T
    = postForObject(uri, request, T::class.java)

@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.postForEntity(url: String, request: Any, vararg uriVariables: Any): ResponseEntity<T>
    = postForEntity(url, request, T::class.java, *uriVariables)

@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.postForEntity(url: String, request: Any, uriVariables: Map<String, Any?>): ResponseEntity<T>
    = postForEntity(url, request, T::class.java, uriVariables)

@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.postForEntity(uri: URI, request: Any): ResponseEntity<T>
    = postForEntity(uri, request, T::class.java)

// exchange

@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.exchange(url: String,
                                                     method: HttpMethod,
                                                     requestEntity: HttpEntity<*>,
                                                     vararg uriVariables: Any): ResponseEntity<T>
    = exchange(url, method, requestEntity, T::class.java, *uriVariables)


@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.exchange(url: String,
                                                     method: HttpMethod,
                                                     requestEntity: HttpEntity<*>,
                                                     uriVariables: Map<String, Any?>): ResponseEntity<T>
    = exchange(url, method, requestEntity, T::class.java, uriVariables)

@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.exchange(uri: URI,
                                                     method: HttpMethod,
                                                     requestEntity: HttpEntity<*>): ResponseEntity<T>
    = exchange(uri, method, requestEntity, T::class.java)

@Throws(RestClientException::class)
inline fun <reified T : Any> RestOperations.exchange(requestEntity: RequestEntity<*>): ResponseEntity<T>
    = exchange(requestEntity, T::class.java)

