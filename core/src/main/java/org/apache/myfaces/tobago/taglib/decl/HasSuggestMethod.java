package org.apache.myfaces.tobago.taglib.decl;

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

import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasSuggestMethod {

  /**
   * MethodBinding which generates a list of suggested input values based on
   * the currently entered text, which could be retrieved via getSubmittedValue() on the UIInput.
   * The expression has to evaluate to a public method which has a javax.faces.component.UIInput parameter
   * and returns a List&lt;String>(deprecated), a List&lt;org.apache.myfaces.tobago.model.AutoSuggestItem>
   * or a org.apache.myfaces.tobago.model.AutoSuggestItems.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {},
      expression = DynamicExpression.METHOD_BINDING_REQUIRED,
      methodSignature = "javax.faces.component.UIInput",
      methodReturnType = "java.lang.Object")
  void setSuggestMethod(String suggestMethod);

}
