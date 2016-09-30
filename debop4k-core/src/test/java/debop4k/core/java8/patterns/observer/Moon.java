/*
 * Copyright (c) 2016. KESTI co, ltd
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.java8.patterns.observer;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;

import java.util.List;

@Slf4j
public class Moon {

  private final List<LandingObserver> observers = FastList.newList();

  public void land(String name) {
    observers.forEach(observer -> observer.observeLanding(name));
  }

  public void startSpying(LandingObserver observer) {
    observers.add(observer);
  }

  private static void classBasedExample() {
    Moon moon = new Moon();
    moon.startSpying(new Nasa());
    moon.startSpying(new Aliens());

    moon.land("An asteroid");
    moon.land("Apollo 11");
  }

  private static void lambdaBasedExample() {
    Moon moon = new Moon();

    // Nasa
    moon.startSpying(name -> {
      if (name.contains("Apollo")) {
        log.debug("We made it!");
      }
    });
    // Alien
    moon.startSpying(name -> {
      if (name.contains("Apollo")) {
        log.debug("They're distracted, lets invade earth!");
      }
    });

    moon.land("An asteroid");
    moon.land("Apollo 11");
  }
}
