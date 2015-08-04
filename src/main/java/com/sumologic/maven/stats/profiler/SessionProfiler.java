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

package com.sumologic.maven.stats.profiler;

import org.apache.maven.execution.AbstractExecutionListener;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.MavenSession;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Chris (chris@sumologic.com)
 */
public class SessionProfiler extends AbstractExecutionListener {
  private long startTime;
  private long endTime; // TODO: Make thread safe only if necessary
  private ConcurrentHashMap<String, ProjectProfiler> projectProfilerMap = new ConcurrentHashMap<String, ProjectProfiler>();
  private MavenSession session;

  public SessionProfiler(MavenSession session) {
    this.session = session;
  }

  @Override
  public void projectDiscoveryStarted(ExecutionEvent event) {
//    ensureProfilerStarted(event).projectDiscoveryStarted(event);
    // TODO: Do anything here?
  }

  @Override
  public void sessionStarted(ExecutionEvent event) {
    startTime = System.currentTimeMillis();
  }

  @Override
  public void sessionEnded(ExecutionEvent event) {
    endTime = System.currentTimeMillis();
  }

  @Override
  public void projectSkipped(ExecutionEvent event) {
    profilerForProject(event).projectSkipped(event);
  }

  @Override
  public void projectStarted(ExecutionEvent event) {
    profilerForProject(event).projectStarted(event);
  }

  @Override
  public void projectSucceeded(ExecutionEvent event) {
    profilerForProject(event).projectSucceeded(event);
  }

  @Override
  public void projectFailed(ExecutionEvent event) {
    profilerForProject(event).projectFailed(event);
  }

  @Override
  public void mojoSkipped(ExecutionEvent event) {
    profilerForProject(event).mojoSkipped(event);
  }

  @Override
  public void mojoStarted(ExecutionEvent event) {
    profilerForProject(event).mojoStarted(event);
  }

  @Override
  public void mojoSucceeded(ExecutionEvent event) {
    profilerForProject(event).mojoSucceeded(event);
  }

  @Override
  public void mojoFailed(ExecutionEvent event) {
    profilerForProject(event).mojoFailed(event);
  }

  private ProjectProfiler profilerForProject(ExecutionEvent event) {
    String key = event.getProject().getName();
    if (projectProfilerMap.containsKey(key)) {
      return projectProfilerMap.get(key);
    } else {
      ProjectProfiler newProfiler = new ProjectProfiler(event.getProject());
      ProjectProfiler oldProfiler = projectProfilerMap.putIfAbsent(key, newProfiler);
      return (oldProfiler == null) ? newProfiler : oldProfiler;
    }
  }

  public long getStartTime() {
    return startTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public String getName() {
    return "build_session_" + session.getStartTime().toString(); // TODO: Get a better name for session
  }

  public int getProjectsSkipped() {
    int count = 0;
    for (ProjectProfiler projectProfiler : projectProfilerMap.values()) {
      if (projectProfiler.isSkipped()) {
        count += 1;
      }
    }
    return count;
  }

  public int getProjectsFailed() {
    int count = 0;
    for (ProjectProfiler projectProfiler : projectProfilerMap.values()) {
      if (projectProfiler.isFailed()) {
        count += 1;
      }
    }
    return count;
  }

  public Collection<ProjectProfiler> getProjectProfilers() {
    return projectProfilerMap.values();
  }
}
