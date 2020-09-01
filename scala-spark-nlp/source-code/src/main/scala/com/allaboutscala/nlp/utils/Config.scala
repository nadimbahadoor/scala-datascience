package com.allaboutscala.nlp.utils

import com.typesafe.config.ConfigFactory

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
  * Encapsulate and load key value pairs from src/main/resources/application.conf.
  *
  * @author Nadim Bahadoor
  */
trait Config {

  lazy val config = ConfigFactory.load()

  lazy val appName = config.getString("nlp.appName")

  lazy val sparkMaster = config.getString("nlp.spark.master")

  lazy val Data = new {

    lazy val covid19Titles = config.getString("nlp.dataset.covid.19.titles ")

    lazy val moviesTitles = config.getString("nlp.dataset.movies.titles")
  }

  lazy val ML = new {

    lazy val tokenizerInputColumn = config.getString("nlp.machine-learning.tokenizer-input-column")

    lazy val tokenizerOutputColumn = config.getString("nlp.machine-learning.tokenizer-output-column")

    lazy val featureVectorSize = config.getInt("nlp.machine-learning.feature-vector-size")

    lazy val featureOutputColumn = config.getString("nlp.machine-learning.feature-output-column")
  }
}