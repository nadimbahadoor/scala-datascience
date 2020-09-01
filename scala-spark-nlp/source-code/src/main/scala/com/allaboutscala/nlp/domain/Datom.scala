package com.allaboutscala.nlp.domain

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
  * Base class to represent a single title data point.
  *
  * @author Nadim Bahadoor
  */
abstract class Datom {
  def id: Long

  def title: String
}

/**
  * Concrete class to represent a single title data point.
  *
  * @param id - The index number for a given title
  * @param title - The title as a [[String]] type
  */
case class RawDatom(id: Long, title: String) extends Datom

/**
  * A title data point with a feature label.
  *
  * @param id - The index number for a given title
  * @param title - The title as a [[String]] type
  * @param label - Feature label with value 0 or 1
  */
case class LabeledDatom(id: Long, title: String, label: Double) extends Datom