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

/**
 * @author Chris (chris@sumologic.com)
 */
public class MojoProfiler extends AbstractExecutionListener {
  private long startTime;
  private long endTime; // TODO: Make thread safe only if necessary
  private boolean skipped = false;
  private boolean failed = false;
  private MojoExecution mojoExecution;

  public MojoProfiler(MojoExecution mojoExecution) {
    this.mojoExecution = mojoExecution;
  }

  @Override
  public void mojoSkipped(ExecutionEvent event) {
    skipped = true;
  }

  @Override
  public void mojoStarted(ExecutionEvent event) {
    startTime = System.currentTimeMillis();
  }

  @Override
  public void mojoSucceeded(ExecutionEvent event) {
    endTime = System.currentTimeMillis();
  }

  @Override
  public void mojoFailed(ExecutionEvent event) {
    endTime = System.currentTimeMillis();
    failed = true;
  }

  public long getStartTime() {
    return startTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public String getGoal() {
    return mojoExecution.getGoal();
  }

  public String getArtifactId() {
    return mojoExecution.getArtifactId();
  }

  public String getExecutionId() {
    return mojoExecution.getExecutionId();
  }

  public String getGroupId() {
    return mojoExecution.getGroupId();
  }

  public boolean isSkipped() {
    return skipped;
  }

  public boolean isFailed() {
    return failed;
  }
}
