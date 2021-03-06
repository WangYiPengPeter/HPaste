/** Licensed to Gravity.com under one
  * or more contributor license agreements. See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership. Gravity.com licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License. You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package com.gravity.hbase.schema

import org.apache.hadoop.hbase.client.{Get, Scan}

/*             )\._.,--....,'``.
 .b--.        /;   _.. \   _\  (`._ ,.
`=,-,-'~~~   `----(,_..'--(,_..'`-.;.'  */


case class ScanCachePolicy(ttlMinutes: Int)

/**
 * An interface that can be injected into an HbaseTable implementation that supports caching
 * of queries.
 * @tparam T
 * @tparam R
 * @tparam RR
 */
trait QueryResultCache[T <: HbaseTable[T, R, RR], R, RR <: HRow[T,R]] {

  def getScanResult(key: Scan): Option[Seq[RR]]

  def putScanResult(key: Scan, value: Seq[RR], ttl: Int)

  def getResult(key: Get): Option[RR]

  def getResults(keys:Iterable[Get]) : Map[Get,Option[RR]]

  def putResult(key: Get, value: RR, ttl: Int)
}

/**
 * The default implementation of QueryResultCache.  Will do nothing.
 * @tparam T
 * @tparam R
 * @tparam RR
 */
class NoOpCache[T <: HbaseTable[T, R,RR], R, RR <: HRow[T,R]] extends QueryResultCache[T, R, RR] {

  override def getScanResult(key: Scan): Option[Seq[RR]] = None

  override def putScanResult(key: Scan, value: Seq[RR], ttl: Int) {}

  override def putResult(key: Get, value: RR, ttl: Int) {}

  override def getResults(keys:Iterable[Get]) = Map[Get,Option[RR]]()
  override def getResult(key:Get) = None
}
