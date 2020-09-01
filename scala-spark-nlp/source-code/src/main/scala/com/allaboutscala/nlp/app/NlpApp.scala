package com.allaboutscala.nlp.app

import com.allaboutscala.nlp.spark.{NlpServices, NlpAppController}

/**
  * Created by Nadim Bahadoor on 27/08/2020.
  *
  * The content was inspired by the original tutorial below, and feedback from readers at http://allaboutscala.com.
  *
  * [[http://allaboutscala.com/data-science/]]
  *
  *
  * Copyright 2016 - 2020 Nadim Bahadoor (http://allaboutscala.com)
  *
  * Licensed under the Apache License, Version 2.0 (the "License"); you may not
  * use this file except in compliance with the License. You may obtain a copy of
  * the License at
  *
  *  [http://www.apache.org/licenses/LICENSE-2.0]
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  * License for the specific language governing permissions and limitations under
  * the License.
  */

object NlpApp extends App {

  // kick off the spark nlp pipeline
  NlpBootstrap.start()

  // stop the spark nlp pipeline
  NlpBootstrap.stop()

}

/**
  * Single instance to launch [[NlpAppController]] with [[NlpServices]] mixed in.
  *
  * @author Nadim Bahadoor
  */
object NlpBootstrap extends NlpAppController with NlpServices