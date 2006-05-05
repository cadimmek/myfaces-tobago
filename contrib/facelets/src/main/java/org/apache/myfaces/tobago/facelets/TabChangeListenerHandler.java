package org.apache.myfaces.tobago.facelets;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.sun.facelets.tag.TagHandler;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagAttributeException;
import com.sun.facelets.tag.TagException;
import com.sun.facelets.FaceletContext;

import java.io.IOException;

import org.apache.myfaces.tobago.event.TabChangeSource;
import org.apache.myfaces.tobago.event.TabChangeListener;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.el.ValueExpression;
import javax.el.ELException;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 20.04.2006
 * Time: 18:14:11
 * To change this template use File | Settings | File Templates.
 */
public class TabChangeListenerHandler extends TagHandler {

  private Class listenerType;

  private final TagAttribute type;

  private final TagAttribute binding;


  public TabChangeListenerHandler(TagConfig config) {
    super(config);
    binding = getAttribute("binding");
    type = getAttribute("type");
    if (type != null) {
      if (!type.isLiteral()) {
        throw new TagAttributeException(tag, type, "Must be literal");
      }
      try {
        this.listenerType = Class.forName(type.getValue());
      } catch (Exception e) {
        throw new TagAttributeException(tag, type, e);
      }
    }
  }

  public void apply(FaceletContext faceletContext, UIComponent parent)
      throws IOException, FacesException, ELException {
    if (parent instanceof TabChangeSource) {
      // only process if parent was just created
      if (parent.getParent() == null) {
        TabChangeSource changeSource = (TabChangeSource) parent;
        TabChangeListener listener = null;
        ValueExpression valueExpression = null;
        if (binding != null) {
          valueExpression = binding.getValueExpression(faceletContext, TabChangeListener.class);
          listener = (TabChangeListener) valueExpression.getValue(faceletContext);
        }
        if (listener == null) {
          try {
            listener = (TabChangeListener) listenerType.newInstance();
          } catch (Exception e) {
            throw new TagAttributeException(tag, type, e.getCause());
          }
          if (valueExpression != null) {
            valueExpression.setValue(faceletContext, listener);
          }
        }
        changeSource.addTabChangeListener(listener);
      }
    } else {
      throw new TagException(tag, "Parent is not of type TabChangeSource, type is: " + parent);
    }
  }
}
