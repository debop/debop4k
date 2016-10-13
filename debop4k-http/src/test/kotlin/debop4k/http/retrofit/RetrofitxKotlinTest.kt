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

package debop4k.http.retrofit

import debop4k.http.AbstractHttpKotlinTest
import org.junit.Test
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.Serializable


class RetrofitxKotlinTest : AbstractHttpKotlinTest() {

  val API_URL = "https://api.github.com"

  class Collaborator @JvmOverloads constructor(val login: String,
                                               val url: String = "") : Serializable

  interface GitHub {
    @GET("/repos/{owner}/{repo}/collaborators?access_token=5365c22a10341a28a2c963d678185f9b6710be00")
    fun contributors(@Path("owner") owner: String, @Path("repo") repo: String): Call<List<Collaborator>>
  }

  @Test
  fun testGetContributors() {
    val retrofit = retrofitOf(API_URL)

    val github = retrofit.service(GitHub::class.java)

    val call = github.contributors("debop", "hibernate-redis")

    val collaborators = call.execute().body()
    println(collaborators)

    collaborators.forEach { collaborator ->
      println("${collaborator.login} (${collaborator.url})")
    }
  }
}