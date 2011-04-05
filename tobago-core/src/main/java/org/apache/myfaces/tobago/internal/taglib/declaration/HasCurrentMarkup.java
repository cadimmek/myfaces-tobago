package org.apache.myfaces.tobago.internal.taglib.declaration;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

public interface HasCurrentMarkup {
  
  /**
   * The current markup is the current internal state of the markup.
   * It is the same as markup plus additional values like "required", "error", "selected", ....
   * TODO: this property may be transient! TOBAGO-912
   */
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.context.Markup",
      isTransient = true)
  void setCurrentMarkup(String markup);
}
