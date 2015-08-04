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

import com.sumologic.maven.stats.profiler.SessionProfiler;
import com.sumologic.maven.stats.publisher.ProfilingPublisher;
import org.apache.maven.execution.AbstractExecutionListener;
import org.apache.maven.execution.ExecutionEvent;

/**
 * @author Chris (chris@sumologic.com)
 */
public class ProfilePublishingExecutionListener extends AbstractExecutionListener {
  private final ProfilingPublisher publisher;
  private SessionProfiler sessionProfiler;

  public ProfilePublishingExecutionListener(ProfilingPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void projectDiscoveryStarted(ExecutionEvent event) {
    ensureProfilerStarted(event).projectDiscoveryStarted(event);
  }

  @Override
  public void sessionStarted(ExecutionEvent event) {
    ensureProfilerStarted(event).sessionStarted(event);
  }

  @Override
  public void sessionEnded(ExecutionEvent event) {
    ensureProfilerStarted(event).sessionEnded(event);

    try {
      publisher.publishSessionProfile(this.sessionProfiler);
    } catch (Exception e) {
      // We should fail silently.  The publisher should publish success/error messages, not use.
    }
  }

  @Override
  public void projectSkipped(ExecutionEvent event) {
    ensureProfilerStarted(event).projectSkipped(event);
  }

  @Override
  public void projectStarted(ExecutionEvent event) {
    ensureProfilerStarted(event).projectStarted(event);
  }

  @Override
  public void projectSucceeded(ExecutionEvent event) {
    ensureProfilerStarted(event).projectSucceeded(event);
  }

  @Override
  public void projectFailed(ExecutionEvent event) {
    ensureProfilerStarted(event).projectFailed(event);
  }

  @Override
  public void mojoSkipped(ExecutionEvent event) {
    ensureProfilerStarted(event).mojoSkipped(event);
  }

  @Override
  public void mojoStarted(ExecutionEvent event) {
    ensureProfilerStarted(event).mojoStarted(event);
  }

  @Override
  public void mojoSucceeded(ExecutionEvent event) {
    ensureProfilerStarted(event).mojoSucceeded(event);
  }

  @Override
  public void mojoFailed(ExecutionEvent event) {
    ensureProfilerStarted(event).mojoFailed(event);
  }

  private SessionProfiler ensureProfilerStarted(ExecutionEvent event) {
    if (this.sessionProfiler == null) {
      synchronized(this) {
        if (this.sessionProfiler == null) {
          this.sessionProfiler = new SessionProfiler(event.getSession());
        }
      }
    }
    return this.sessionProfiler;
  }

}
