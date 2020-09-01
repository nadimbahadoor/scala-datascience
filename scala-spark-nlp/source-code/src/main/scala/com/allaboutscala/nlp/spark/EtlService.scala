package com.allaboutscala.nlp.spark

import com.allaboutscala.nlp.utils.Config
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import com.allaboutscala.nlp.domain.LabeledDatom

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
  * This class is responsible for:
  * - loading titles for Covid-19 and Movies from the /data directory
  * - transforming the titles into RDD of type [[LabeledDatom]]
  *
  * @param context - Wrapper for the [[org.apache.spark.sql.SparkSession]]
  * @param config - Configuration loaded from application.conf
  */
class EtlService(context: Context, config: Config) {

  /**
    * Load Covid-19 titles
    *
    * @return - An RDD of type [[LabeledDatom]] of Covid-19 titles
    */
  def loadCovid19Data(): RDD[LabeledDatom] = {
    val dfCovid19: DataFrame = context
      .sparkSession
      .read
      .text(config.Data.covid19Titles)
      .coalesce(1) // No need to partition across more than 1 core

    zipDataFrameWithLabel(dfCovid19, 1.0) // label = 1.0 represents Covid-19
  }

  /**
    * Load Movies titles
    *
    * @return - An RDD of type [[LabeledDatom]] of Movies titles
    */
  def loadMoviesData(): RDD[LabeledDatom] = {
    val dfMovies = context
      .sparkSession
      .read
      .text(config.Data.moviesTitles)
      .coalesce(1) // No need to partition across more than 1 core

    zipDataFrameWithLabel(dfMovies, 0.0) // label = 0.0 represents Movies
  }

  /**
    * Utility function to transform raw titles into typed RDD[[LabeledDatom]]
    *
    * @param dataFrame - A raw [[DataFrame]] to transform into RDD of type [[LabeledDatom]]
    * @param label - A feature value of 0 or 1
    * @return
    */
  private def zipDataFrameWithLabel(dataFrame: DataFrame, label: Double): RDD[LabeledDatom] = {
    /** Create an associated index for each row **/
    val indexedRdd: RDD[(Any, Int)] = for {
      row <- dataFrame.rdd
      indexedRow <- row.toSeq.zipWithIndex
    } yield (indexedRow)

    /** Transform each row into [[LabeledDatom]] type **/
    indexedRdd.map {
      case (quote: String, index: Int) =>
        LabeledDatom(index, quote, label)
    }
  }
}
