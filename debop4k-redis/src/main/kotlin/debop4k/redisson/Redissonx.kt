/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

@file:JvmName("Redissonx")

package debop4k.redisson

import debop4k.core.io.toInputStream
import org.redisson.client.codec.Codec
import org.redisson.codec.SnappyCodec
import org.redisson.config.Config
import java.net.URL

@JvmField val DEFAULT_HOST = "127.0.0.1"
@JvmField val DEFAULT_PORT = 6379
@JvmField val DEFAULT_ADDRESS = debop4k.redisson.DEFAULT_HOST + ":" + debop4k.redisson.DEFAULT_PORT

@JvmField val DEFAULT_SENTINEL_PORT = 26379
@JvmField val DEFAULT_TIMEOUT = 2000
@JvmField val DEFAULT_DATABASE = 0

@JvmField val DEFAULT_CHARSET = "UTF-8"
@JvmField val DEFAULT_LOGBACK_CHANNEL = "channel:logback:logs"
@JvmField val DEFAULT_DELIMETER = ":"

@JvmField val DEFAULT_CODEC = SnappyCodec()

@JvmOverloads
fun configFromYaml(inputStream: java.io.InputStream, codec: org.redisson.client.codec.Codec = debop4k.redisson.DEFAULT_CODEC): org.redisson.config.Config {
  val cfg = org.redisson.config.Config.fromYAML(inputStream)
  if (cfg != null && cfg.codec == null) {
    cfg.codec = codec
  }
  return cfg
}

@JvmOverloads
fun configFromYaml(content: String, codec: org.redisson.client.codec.Codec = debop4k.redisson.DEFAULT_CODEC): org.redisson.config.Config {
  return configFromYaml(content.toInputStream(), codec)
}

@JvmOverloads
fun configFromYaml(file: java.io.File, codec: org.redisson.client.codec.Codec = debop4k.redisson.DEFAULT_CODEC): org.redisson.config.Config {
  return debop4k.redisson.configFromYaml(file.inputStream(), codec)
}

@JvmOverloads
fun configFromYaml(url: URL, codec: Codec = DEFAULT_CODEC): Config {
  return configFromYaml(url.openStream(), codec)
}


@JvmOverloads
fun configWithSingleServer(host: String = DEFAULT_HOST,
                           port: Int = DEFAULT_PORT,
                           codec: Codec = DEFAULT_CODEC): Config {
  val cfg = Config()
  cfg.useSingleServer().setAddress(host + ":" + port)
  cfg.codec = codec
  return cfg
}
