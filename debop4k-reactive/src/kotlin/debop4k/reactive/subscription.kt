package debop4k.reactive

import rx.Subscription
import rx.subscriptions.CompositeSubscription

operator fun CompositeSubscription.plusAssign(subscription: Subscription) = add(subscription)