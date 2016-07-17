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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAction;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasActionListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAjaxBehavior;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasImage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLink;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRenderedPartially;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasResource;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTarget;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabledBySecurity;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsImmediateCommand;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsOmit;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsTransition;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import javax.faces.component.UICommand;

/**
 * Renders a menu item. (This tag was renamed from tc:menuItem since Tobago 1.5.0)
 * @deprecated Please use &lt;tc:command/&gt; instead.
 */
@Deprecated
@Tag(name = "menuCommand")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIMenuCommand",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUICommand",
    uiComponentFacesClass = "javax.faces.component.UICommand",
    interfaces = "org.apache.myfaces.tobago.component.SupportsAccessKey",
    componentFamily = UICommand.COMPONENT_FAMILY,
    rendererType = RendererTypes.MENU_COMMAND,
    allowedChildComponenents = "NONE")
public interface MenuCommandTagDeclaration
    extends HasAction, HasActionListener, IsImmediateCommand,
            HasLink, HasResource, IsTransition, HasTarget, HasRenderedPartially, HasAjaxBehavior, IsDisabledBySecurity, IsOmit, IsVisual,
            HasIdBindingAndRendered, HasLabelAndAccessKey, HasTip, HasImage {
}
