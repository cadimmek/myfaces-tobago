package org.apache.myfaces.tobago.taglib.sandbox;

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

import org.apache.myfaces.tobago.apt.annotation.*;
import org.apache.myfaces.tobago.model.Wizard;
import org.apache.myfaces.tobago.taglib.component.TobagoTagDeclaration;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;

/**
 * Renders a fexible Wizard.
 */
@Tag(name = "wizardController")
@BodyContentDescription(anyTagOf = "facestag")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIWizard",
    rendererType = "Wizard")
public interface WizardTagDeclaration
    extends TobagoTagDeclaration, HasIdBindingAndRendered {

  @TagAttribute(required = true, type = Wizard.class)
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.model.Wizard")
  void setController(String controller);
}
