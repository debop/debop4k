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
@file:JvmName("Hibernatex")
@file:Suppress("UNCHECKED_CAST")

package debop4k.data.orm.hibernate

import debop4k.core.loggerOf
import debop4k.data.orm.hibernate.listener.UpdatedTimestampListener
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import org.hibernate.event.service.spi.EventListenerRegistry
import org.hibernate.event.spi.EventType
import org.hibernate.internal.SessionFactoryImpl
import javax.persistence.EntityManager

private val log = loggerOf("Hibernatex")

fun Configuration.buildSessonFactory(): SessionFactory {

  log.info("SessionFactory를 빌드합니다...")

  val registry = StandardServiceRegistryBuilder()
      .applySettings(this.properties)
      .build()

  val factory = this.buildSessionFactory(registry)
  log.info("SessionFactory 를 빌드했습니다.")

  return factory
}

fun <T> SessionFactory.registEventListener(listener: T,
                                           eventTypes: Collection<EventType<*>>): Unit {
  val registry = (this as SessionFactoryImpl)
      .serviceRegistry
      .getService(EventListenerRegistry::class.java)

  for (eventType in eventTypes) {
    registry.getEventListenerGroup(eventType as EventType<T>).appendListener(listener)
  }
}

fun SessionFactory.registUpdateTimestampEventListener(): Unit {
  registEventListener(UpdatedTimestampListener(),
                      listOf(EventType.PRE_INSERT, EventType.PRE_UPDATE))
}

val EntityManager.sessionFactory: SessionFactory
  get() = this.unwrap(Session::class.java).sessionFactory

val EntityManager.currentSession: Session get() = unwrap(Session::class.java)

fun SessionFactory.getEntityName(entityClass: Class<*>): String
    = getClassMetadata(entityClass).entityName

