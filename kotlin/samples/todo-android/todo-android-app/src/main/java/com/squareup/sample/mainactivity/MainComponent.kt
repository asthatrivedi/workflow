/*
 * Copyright 2019 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.sample.mainactivity

import com.squareup.sample.todo.TodoEditorWorkflow
import com.squareup.sample.todo.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

/**
 * Pretend generated code of a pretend DI framework.
 */
internal class MainComponent {

  val rootContext = SupervisorJob() + Dispatchers.Main.immediate

  val todoRepository = TodoRepository(rootContext)

  val todoEditorWorkflow = TodoEditorWorkflow(todoRepository)

  companion object {
    init {
      Timber.plant(Timber.DebugTree())

      val stock = Thread.getDefaultUncaughtExceptionHandler()
      Thread.setDefaultUncaughtExceptionHandler { thread, error ->
        Timber.e(error)
        stock.uncaughtException(thread, error)
      }
    }
  }
}
