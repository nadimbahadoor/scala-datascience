package com.allaboutscala.nlp.spark


import com.allaboutscala.nlp.domain.{LabeledDatom, RawDatom}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row}

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

trait NlpAppController {
  this: NlpServices =>

  /**
    * Start the machine learning pipeline in the following order:
    * 1. Load titles from Covid-19 and Movies titles datasets
    * 2. Inject the titles into a machine learning pipeline which defaults to [[LogisticRegression]]
    * 3. Inject dummy title for Covid-19 and Movies into the pipeline and predict
    *    which titles are relate to Covid-19 and Movies respectively.
    */
  def start(): Unit = {
    /** 1. Load titles for Covid-19 and Movies **/
    println("\nStep 1: Loading Covid-19 and Movies titles training data")
    val trainingData = loadData()


    /** 2. Machine learning pipeline using [[LogisticRegression]] as classifier **/
    println("\nStep 2: Creating NLP Pipeline = Word Tokenizer + Hashing Term Frequency + Logistic Regression")
    val nlpPipeline = createNLPPipeline(trainingData)


    /** 3. Inject dummy titles and predict which ones are for Covid-19 or Movies **/
    println("\nStep 3: Load dummy titles and predict which ones relate to Covid-19 v/s Movies")
    predictTitles(nlpPipeline)
  }

  def stop(): Unit = {
    context.sparkSession.stop()
  }

  protected def loadData(): RDD[LabeledDatom] = {
    etlService.loadCovid19Data() ++ etlService.loadMoviesData()
  }

  protected def createNLPPipeline(trainingData: RDD[LabeledDatom]): PipelineModel = {
    val wordTokenizer = new Tokenizer()
      .setInputCol(config.ML.tokenizerInputColumn)
      .setOutputCol(config.ML.tokenizerOutputColumn)

    val hashingTermFrequency = new HashingTF()
      .setNumFeatures(config.ML.featureVectorSize)
      .setInputCol(wordTokenizer.getOutputCol)
      .setOutputCol(config.ML.featureOutputColumn)

    val logisticRegression = new LogisticRegression()

    val pipeline = new Pipeline()
      .setStages(Array(wordTokenizer, hashingTermFrequency, logisticRegression))

    val dfTrainingData = context
      .sparkSession
      .createDataFrame(trainingData)
      .coalesce(1) // No need to partition across more than 1 core
      .toDF("id", "title", "label")

    pipeline.fit(dfTrainingData)
  }

  protected def createDummyData(): DataFrame = {
    val dummyMovieTitle =
      """
        |This is the best mission impossible movie ever. Totally amazing movie.
      """.stripMargin

    val dummyCovid19Title =
      """
        |Covid-19 is a global pandemic. There is a prospective cohort study of Epidemiology, clinical course,
        |and outcomes of critically ill adults with COVID-19 in New York City.
      """.stripMargin

    val rddDummyQuotes: RDD[RawDatom] = context
      .sparkSession
      .sparkContext
      .parallelize(Seq(RawDatom(10, dummyCovid19Title), RawDatom(100, dummyMovieTitle)))
      .coalesce(1) // No need to partition across more than 1 core

    context
      .sparkSession
      .createDataFrame(rddDummyQuotes)
      .toDF("id", "title")
  }

  protected def predictTitles(pipelineModel: PipelineModel): Unit = {
    pipelineModel
      .transform(createDummyData())
      .select("id", "title", "prediction")
      .collect()
      .foreach { case Row(id, title, prediction) =>
        prediction match {
          case 0 => println(s"Title is related to Movies, id = $id, prediction = $prediction, title = $title")
          case 1 => println(s"Title is from Covid-19, id = $id, prediction = $prediction, title = $title")
        }
      }
  }

}
