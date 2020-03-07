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
package com.squareup.sample.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.sample.recyclerview.editablelistworkflow.EditableListLayoutRunner
import com.squareup.workflow.diagnostic.SimpleLoggingDiagnosticListener
import com.squareup.workflow.ui.ViewRegistry
import com.squareup.workflow.ui.WorkflowRunner
import com.squareup.workflow.ui.setContentWorkflow
import timber.log.Timber
import timber.log.Timber.DebugTree

private val viewRegistry = ViewRegistry(
    AddRowContainer,
    BaseScreenViewFactory,
    ChooseRowTypeViewFactory,
    EditableListLayoutRunner
)

class EditableListActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentWorkflow(viewRegistry) {
      WorkflowRunner.Config(
          AppWorkflow,
          diagnosticListener = object : SimpleLoggingDiagnosticListener() {
            override fun println(text: String) = Timber.v(text)
          }
      )
    }
  }

  companion object {
    init {
      Timber.plant(DebugTree())
    }
  }
}
