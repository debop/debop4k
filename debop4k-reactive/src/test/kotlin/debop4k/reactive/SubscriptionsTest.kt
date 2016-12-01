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
 */

package debop4k.reactive

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.*

class SubscriptionsTest : AbstractReactiveTest() {
  @Test
  fun testSubscriptionAddTo() {
    val compositeSubscription = CompositeSubscription()

    // Create an asynchronous subscription
    // The delay ensures that we don't automatically unsubscribe because data finished emitting
    val subscription = Observable
        .just("test")
        .delay(100, TimeUnit.MILLISECONDS)
        .subscribe()

    assertThat(subscription.isUnsubscribed).isFalse()

    subscription.addTo(compositeSubscription)

    assertThat(compositeSubscription.hasSubscriptions()).isTrue()
    assertThat(subscription.isUnsubscribed).isFalse()

    compositeSubscription.unsubscribe()
    assertThat(compositeSubscription.isUnsubscribed).isTrue()
  }
}
