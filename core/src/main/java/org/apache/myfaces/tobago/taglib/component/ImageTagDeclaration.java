package org.apache.myfaces.tobago.taglib.component;

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

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.taglib.decl.HasBorder;
import org.apache.myfaces.tobago.taglib.decl.HasDimension;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasTip;

/*
 * Created: Aug 6, 2005 3:19:55 PM
 * User: bommel
 * $Id$
 */
/**
 * Renders a Image.
 */
@Tag(name = "image", bodyContent = BodyContent.JSP)
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIGraphic",
    uiComponentBaseClass = "javax.faces.component.UIGraphic",
    rendererType = RendererTypes.IMAGE,
    allowedChildComponenents = "NONE")
public interface ImageTagDeclaration extends HasIdBindingAndRendered, HasBorder, HasDimension, HasTip {

  /**
   * Absolute url to an image or image name to lookup in tobago resource path
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute()
  void setValue(String value);

  /**
   * Alternate textual description of the image rendered by this component.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setAlt(String alt);

}
