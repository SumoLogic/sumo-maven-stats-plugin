/**
 * _____ _____ _____ _____    __    _____ _____ _____ _____
 * |   __|  |  |     |     |  |  |  |     |   __|     |     |
 * |__   |  |  | | | |  |  |  |  |__|  |  |  |  |-   -|   --|
 * |_____|_____|_|_|_|_____|  |_____|_____|_____|_____|_____|
 * <p/>
 * UNICORNS AT WARP SPEED SINCE 2010
 * <p/>
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.sumologic.maven.stats.publisher;

import com.sumologic.log4j.http.SumoHttpSender;
import com.sumologic.maven.stats.profiler.SessionProfiler;
import com.sumologic.maven.stats.serializer.ProfilingSerializer;
import org.slf4j.Logger;

/**
 * @author Chris (chris@sumologic.com)
 */
public class SumoLogicProfilingPublisher implements ProfilingPublisher {

  private Logger logger;
  private String sumoEndpoint;
  private ProfilingSerializer serializer;
  private SumoHttpSender httpSender = null;

  public SumoLogicProfilingPublisher(Logger logger, String sumoEndpoint, ProfilingSerializer serializer) {
    this.logger = logger;
    this.sumoEndpoint = sumoEndpoint;
    this.serializer = serializer;
  }

  public void publishSessionProfile(SessionProfiler session) throws Exception {
    String serializedBlob = serializer.serialize(session);

    // TODO: We should prefix the log with a timestamp, otherwise the build start time is used as the message time
    try {
      ensureHttpSenderStarted().send(serializedBlob, session.getName());
      logger.info("Successfully published build session information to Sumo Logic");
    } catch (Exception e) {
      logger.warn("Unable to publish build session information to Sumo Logic.");
    } finally {
      try {
        if (httpSender != null) {
          httpSender.close();
        }
      } catch (Exception e) {
        // Not doing anything in this case
      }
    }
  }

  private SumoHttpSender ensureHttpSenderStarted() {
    if (httpSender == null) {
      httpSender = new SumoHttpSender(logger);
      httpSender.setUrl(sumoEndpoint);
      httpSender.init();
    }

    return httpSender;
  }
}
