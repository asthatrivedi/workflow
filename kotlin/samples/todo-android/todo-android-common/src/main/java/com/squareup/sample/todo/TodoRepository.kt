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
package com.squareup.sample.todo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.delayEach
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

@UseExperimental(FlowPreview::class, ExperimentalCoroutinesApi::class)
class TodoRepository(context: CoroutineContext) {

  private val scope = CoroutineScope(context)
  private val todoes = ConflatedBroadcastChannel<List<TodoList>>(emptyList())
  private val lock = Mutex()

  fun getAllTodos(): Flow<List<TodoList>> = todoes.asFlow()
      .distinctUntilChanged()
      .delayEach(1000)

  fun getTodo(id: String): Flow<TodoList> =
    getAllTodos()
        .map { todoes ->
          println("got list: $todoes")
          todoes.singleOrNull { it.id == id } ?: TodoList(id, "empty", emptyList())
        }
        .distinctUntilChanged()

  fun putTodo(todo: TodoList) {
    scope.launch {
      putTodoSync(todo)
    }
  }

  private suspend fun putTodoSync(todo: TodoList) {
    delay(1000)
    lock.withLock {
      val newTodoes = todoes.value.map {
        if (it.id == todo.id) todo else it
      }
      todoes.send(newTodoes)
    }
  }
}
