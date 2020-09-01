package com.allaboutscala.nlp.spark

import com.allaboutscala.nlp.utils.Config
import com.allaboutscala.nlp.app.NlpBootstrap

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

/**
  * Provide the wiring of all required services to be used by [[NlpBootstrap]]
  *
  * @author Nadim Bahadoor
  */
trait NlpServices {

  /** Load configuration details from resources/application.conf **/
  val config = new Config{}

  /** Create Spark Context **/
  val context = new Context(config)

  /** Responsible for loading data files from /data directory **/
  val etlService = new EtlService(context, config)
}
