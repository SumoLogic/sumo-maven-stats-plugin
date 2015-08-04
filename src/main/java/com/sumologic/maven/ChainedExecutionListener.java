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

package com.sumologic.maven;

import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.ExecutionListener;

/**
 * Chains multiple execution listeners together
 *
 * @author Chris (chris@sumologic.com)
 */
public class ChainedExecutionListener implements ExecutionListener {

  private final ExecutionListener firstListener;
  private final ExecutionListener secondListener;
  private ChainedExecutionListener(ExecutionListener firstListener, ExecutionListener secondListener) {
    this.firstListener = firstListener;
    this.secondListener = secondListener;
  }

  /**
   * Creates a chained listener if the two listeners are not null.  Otherwise, returns the possibly non-null listener
   *
   * @param firstListener
   * @param secondListener
   * @return null if both firstListener and secondListener are nulls
   */
  public static ExecutionListener buildFrom(ExecutionListener firstListener, ExecutionListener secondListener) {
    if (firstListener == null) {
      return secondListener;
    } else if (secondListener == null) {
      return firstListener;
    } else {
      return new ChainedExecutionListener(firstListener, secondListener);
    }
  }

  public void projectDiscoveryStarted(ExecutionEvent event) {
    firstListener.projectDiscoveryStarted(event);
    secondListener.projectDiscoveryStarted(event);
  }

  public void sessionStarted(ExecutionEvent event) {
    firstListener.sessionStarted(event);
    secondListener.sessionStarted(event);
  }

  public void forkedProjectFailed(ExecutionEvent event) {
    firstListener.forkedProjectFailed(event);
    secondListener.forkedProjectFailed(event);
  }

  public void forkedProjectStarted(ExecutionEvent event) {
    firstListener.forkedProjectStarted(event);
    secondListener.forkedProjectStarted(event);
  }

  public void forkedProjectSucceeded(ExecutionEvent event) {
    firstListener.forkedProjectSucceeded(event);
    secondListener.forkedProjectSucceeded(event);
  }

  public void forkFailed(ExecutionEvent event) {
    firstListener.forkFailed(event);
    secondListener.forkFailed(event);
  }

  public void forkStarted(ExecutionEvent event) {
    firstListener.forkStarted(event);
    secondListener.forkStarted(event);
  }

  public void forkSucceeded(ExecutionEvent event) {
    firstListener.forkSucceeded(event);
    secondListener.forkSucceeded(event);
  }

  public void mojoFailed(ExecutionEvent event) {
    firstListener.mojoFailed(event);
    secondListener.mojoFailed(event);
  }

  public void mojoSkipped(ExecutionEvent event) {
    firstListener.mojoSkipped(event);
    secondListener.mojoSkipped(event);
  }

  public void mojoStarted(ExecutionEvent event) {
    firstListener.mojoStarted(event);
    secondListener.mojoStarted(event);
  }

  public void mojoSucceeded(ExecutionEvent event) {
    firstListener.mojoSucceeded(event);
    secondListener.mojoSucceeded(event);
  }

  public void projectFailed(ExecutionEvent event) {
    firstListener.projectFailed(event);
    secondListener.projectFailed(event);
  }

  public void projectSkipped(ExecutionEvent event) {
    firstListener.projectSkipped(event);
    secondListener.projectSkipped(event);
  }

  public void projectStarted(ExecutionEvent event) {
    firstListener.projectStarted(event);
    secondListener.projectStarted(event);
  }

  public void projectSucceeded(ExecutionEvent event) {
    firstListener.projectSucceeded(event);
    secondListener.projectSucceeded(event);
  }

  public void sessionEnded(ExecutionEvent event) {
    firstListener.sessionEnded(event);
    secondListener.sessionEnded(event);
  }
}
