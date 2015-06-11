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
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Chris (chris@sumologic.com)
 */
public class ProjectProfiler extends AbstractExecutionListener {
  private long startTime;
  private long endTime; // TODO: Make thread safe only if necessary
  private boolean skipped = false;
  private boolean failed = false;
  private ConcurrentHashMap<String, MojoProfiler> mojoProfilerMap = new ConcurrentHashMap<String, MojoProfiler>();
  private MavenProject mavenProject;

  public ProjectProfiler(MavenProject mavenProject) {
    this.mavenProject = mavenProject;
  }

  @Override
  public void projectSkipped(ExecutionEvent event) {
    skipped = true;
  }

  @Override
  public void projectStarted(ExecutionEvent event) {
    startTime = System.currentTimeMillis();
  }

  @Override
  public void projectSucceeded(ExecutionEvent event) {
    endTime = System.currentTimeMillis();
  }

  @Override
  public void projectFailed(ExecutionEvent event) {
    endTime = System.currentTimeMillis();
    failed = true;
  }

  @Override
  public void mojoSkipped(ExecutionEvent event) {
    profilerForMojo(event).mojoSkipped(event);
  }

  @Override
  public void mojoStarted(ExecutionEvent event) {
    profilerForMojo(event).mojoStarted(event);
  }

  @Override
  public void mojoSucceeded(ExecutionEvent event) {
    profilerForMojo(event).mojoSucceeded(event);
  }

  @Override
  public void mojoFailed(ExecutionEvent event) {
    profilerForMojo(event).mojoFailed(event);
  }

  private MojoProfiler profilerForMojo(ExecutionEvent event) {
    MojoExecution mojo = event.getMojoExecution();
    String key = event.getProject().getName() + " - " + mojo.getGroupId() + ":" + mojo.getArtifactId() + " - " + mojo.getGoal() + " - " + mojo.getExecutionId();
    if (mojoProfilerMap.containsKey(key)) {
      return mojoProfilerMap.get(key);
    } else {
      MojoProfiler newProfiler = new MojoProfiler(event.getMojoExecution());
      MojoProfiler oldProfiler = mojoProfilerMap.putIfAbsent(key, newProfiler);
      return (oldProfiler == null) ? newProfiler : oldProfiler;
    }
  }

  public long getStartTime() {
    return startTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public int getMojosSkipped() {
    int count = 0;
    for (MojoProfiler mojoProfiler : mojoProfilerMap.values()) {
      if (mojoProfiler.isSkipped()) {
        count += 1;
      }
    }
    return count;
  }

  public int getMojosFailed() {
    int count = 0;
    for (MojoProfiler mojoProfiler : mojoProfilerMap.values()) {
      if (mojoProfiler.isFailed()) {
        count += 1;
      }
    }
    return count;
  }

  public boolean isSkipped() {
    return skipped;
  }

  public boolean isFailed() {
    return failed;
  }

  public String getProjectName() {
    return mavenProject.getName();
  }

  public Collection<MojoProfiler> getMojoProfilers() {
    return mojoProfilerMap.values();
  }
}
