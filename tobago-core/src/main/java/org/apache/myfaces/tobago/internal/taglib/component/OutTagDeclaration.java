/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasSanitize;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import javax.faces.component.UIOutput;

/**
 * Renders a text
 */
@Tag(name = "out")
@BodyContentDescription(anyTagOf = "f:converter|f:convertNumber|f:convertDateTime|...")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIOut",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIOut",
    uiComponentFacesClass = "javax.faces.component.UIOutput",
    componentFamily = UIOutput.COMPONENT_FAMILY,
    rendererType = RendererTypes.OUT,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Majorra.
        "javax.faces.component.behavior.ClientBehaviorHolder"
    },
    allowedChildComponenents = "NONE")

public interface OutTagDeclaration
    extends HasIdBindingAndRendered, HasConverter, HasTip, HasValue, IsVisual,
    HasSanitize, HasLabel, HasLabelLayout {

  /**
   * Flag indicating that characters that are
   * sensitive in HTML and XML markup must be escaped.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setEscape(String escape);

  /**
   * Indicates that the renderer should create an element in the output language
   * (e. g. an span or div tag around the output text).
   * Use true, if you enable the possibility to apply styles to the output.
   * Use false, if you want to keep the code small (especially inside of sheets).
   * @deprecated after 4.0.0 release. Use attribute 'compact' instead.
   */
  @Deprecated
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setCreateSpan(String createSpan);

  /**
   * This attribute is useful if labelLayout=skip is set.
   * Use true, if you want to only render the text (no surrounding tag).
   * Use false, if you enable the possibility to apply styles to the output.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setCompact(String compact);
}
