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

package com.sumologic.maven.stats;

import com.sumologic.maven.ChainedExecutionListener;
import com.sumologic.maven.stats.publisher.ProfilingPublisher;
import com.sumologic.maven.stats.publisher.SumoLogicProfilingPublisher;
import com.sumologic.maven.stats.serializer.JsonProfilingSerializer;
import com.sumologic.maven.stats.serializer.ProfilingSerializer;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.ExecutionListener;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.slf4j.Logger;

/**
 * @author Chris (chris@sumologic.com)
 */
@Component(role = AbstractMavenLifecycleParticipant.class)
public class SumoLogicProfilingParticipant extends AbstractMavenLifecycleParticipant {

  @Requirement
  private Logger logger;

  private boolean installed = false;

  @Override
  public void afterProjectsRead(MavenSession session) throws MavenExecutionException {
    ensureInstalled(session);
  }

  @Override
  public void afterSessionStart(MavenSession session) throws MavenExecutionException {
    ensureInstalled(session);
  }

  @Override
  public void afterSessionEnd(MavenSession session) throws MavenExecutionException {
    ensureInstalled(session);
  }

  private void ensureInstalled(MavenSession session) {
    if (!installed) {
      synchronized (this) {
        if (!installed) { // Double lock makes this thread safe and does not repeat the work
          MavenExecutionRequest request = session.getRequest();
          ExecutionListener currentListener = request.getExecutionListener();

          String sumoEndpoint = session.getCurrentProject().getProperties().getProperty("sumologic.http.endpoint");
          ProfilingSerializer serializer = new JsonProfilingSerializer();
          ProfilingPublisher profilingPublisher = new SumoLogicProfilingPublisher(logger, sumoEndpoint, serializer);
          ExecutionListener profilingListener = new ProfilePublishingExecutionListener(profilingPublisher);

          ExecutionListener chainedListener = ChainedExecutionListener.buildFrom(currentListener, profilingListener);
          request.setExecutionListener(chainedListener);

          installed = true;
        }
      }
    }
  }

}
