/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 15:44:53.
 * $Id$
 */
package com.atanion.tobago.renderkit;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.config.ThemeConfig;
import com.atanion.tobago.context.ClientProperties;
import com.atanion.tobago.renderkit.html.HtmlDefaultLayoutManager;
import com.atanion.tobago.util.LayoutUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

// todo: in java 1.5 use: import static com.atanion.tobago.TobagoConstants.*;
public abstract class RendererBase
    extends Renderer implements TobagoConstants, TobagoRenderer {


// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(RendererBase.class);

  public static final String BEGIN_POSTFIX = "Begin";

  public static final String CHILDREN_POSTFIX = "Children";

  public static final String END_POSTFIX = "";

// ///////////////////////////////////////////// attribute

  protected String beginSniplet;

  protected String childrenSniplet;

  protected String endSniplet;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeBegin(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("*** begin    " + component);
    }
    try {
      createClassAttribute(component);

      LayoutManager layoutManager = getLayoutManager(facesContext, component);

      if (layoutManager != null) {
        layoutManager.layoutBegin(facesContext, component);
      }

      encodeBeginTobago(facesContext, component);
    } catch (IOException e) {
      throw e;
    } catch (RuntimeException e) {
      LOG.error("catched RuntimeException :", e);
      throw e;
    } catch (Throwable e) {
      LOG.error("catched Throwable :", e);
      throw new RuntimeException(e);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("*   begin    " + component);
    }
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {

    if (LOG.isDebugEnabled()) {
      LOG.debug("*** children " + component);
    }
    UIComponent layout = null;
    //if (LayoutUtil.isTransparentForLayout(component)) {
    //   layout = component.getParent().getFacet("layout");
    //} else {
      layout = component.getFacet("layout");
    //}
    if (layout != null) {
      // ((LayoutManager)ComponentUtil.getRenderer(layout, facesContext))
       //     .layoutBegin(facesContext, component);
      layout.encodeBegin(facesContext);
      /*for (Iterator iterator = component.getChildren().iterator(); iterator.hasNext();) {
        UIComponent child = (UIComponent) iterator.next();
        ((LayoutManager)ComponentUtil.getRenderer(layout, facesContext))
            .layoutBegin(facesContext, child);
      }*/
      layout.encodeChildren(facesContext);
      layout.encodeEnd(facesContext);
    } else {

      encodeChildrenTobago(facesContext, component);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("*   children " + component);
    }
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("*** end      " + component);
    }
    try {
      encodeEndTobago(facesContext, component);
    } catch (IOException e) {
      throw e;
    } catch (RuntimeException e) {
      LOG.error("catched " + e + " :" + e.getMessage(), e);
      throw e;
    } catch (Throwable e) {
      LOG.error("catched Throwable :", e);
      throw new RuntimeException(e);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("*   end      " + component);
    }
  }

  private LayoutManager getLayoutManager(
      FacesContext facesContext, UIComponent component) {
    LayoutManager layoutManager = null;

    Renderer renderer = ComponentUtil.getRenderer(component, facesContext);
    if (renderer instanceof LayoutManager) {
      layoutManager = (LayoutManager) renderer;
    } else {
      UIComponent parent = LayoutUtil.getLayoutParent(component);
      if (parent instanceof UIPanel
          && ComponentUtil.getBooleanAttribute(parent, ATTR_LAYOUT_DIRECTIVE)) {
        component = parent;
        parent = LayoutUtil.getLayoutParent(component);
      }
      if (parent != null) {
        UIComponent layout = parent.getFacet("layout");
//        if (layout instanceof UIComponentBase) {
        if (layout != null) {
          renderer = ComponentUtil.getRenderer(layout, facesContext);
          if (renderer instanceof LayoutManager) {
            layoutManager = (LayoutManager) renderer;
          }
        }
      }
    }

    // fixme:
    if (layoutManager == null) {
      UIComponent parent = LayoutUtil.getLayoutParent(component);
      if (parent != null) {
        UIComponent layout = parent.getFacet("layout");
//        if (layout instanceof UIComponentBase) {
        if (layout != null) {
          renderer = ComponentUtil.getRenderer(layout, facesContext);
          if (renderer instanceof LayoutManager) {
            layoutManager = (LayoutManager) renderer;
          }
        }
      }
    }

    if (layoutManager == null) {
      layoutManager = getDefaultLayoutManager(facesContext);
    }
    return layoutManager;
  }

  private LayoutManager getDefaultLayoutManager(FacesContext facesContext) {
    LayoutManager layoutManager = null;
    String contentType = ClientProperties.getInstance(facesContext.getViewRoot())
        .getContentType();
    if (contentType.equals("html")) {
      layoutManager = new HtmlDefaultLayoutManager();
    }
    return layoutManager;
  }

  public void decode(FacesContext facesContext, UIComponent component) {
    // nothing to do

    // fixme later:
    if (component instanceof UIInput) {
      LOG.warn(
          "decode() should be overwritten! Renderer: " +
          this.getClass().getName());
    }
  }

  protected void createClassAttribute(UIComponent component) {
    String rendererType = component.getRendererType();
    if (rendererType != null) {
      rendererType = rendererType.toLowerCase();
      Object styleClassO
          = component.getAttributes().get(ATTR_STYLE_CLASS);
      if (styleClassO != null && LOG.isDebugEnabled()) {
        LOG.debug("styleClassO = '" + styleClassO.getClass().getName() + "'");
      }
      String styleClass
          = (String) component.getAttributes().get(
              ATTR_STYLE_CLASS);
      styleClass = updateClassAttribute(styleClass, rendererType, component);
      component.getAttributes().put(
          ATTR_STYLE_CLASS,
          styleClass);
    }
  }

  protected String updateClassAttribute(
      String cssClass, String rendererType,
      UIComponent component) {
    if (rendererType.startsWith("javax.faces.")) { // fixme: this is a hotfix from jsf1.0beta to jsf1.0fr
      rendererType = rendererType.substring("javax.faces.".length());
    }
    if (cssClass != null) {
      // first remove old tobago-<rendererType>-<type> classes from class-attribute
      cssClass = cssClass.replaceAll(
          "tobago-" + rendererType
          + "-(default|disabled|readonly|inline|error)", "").trim();
      // remove old tobago-<rendererType>-theme-<type> classes from class-attribute
      cssClass = cssClass.replaceAll(
          "tobago-" + rendererType + "-theme-(strong|deleted)", "").trim();
    } else {
      cssClass = "";
    }
    String tobagoClass = "tobago-" + rendererType + "-default ";
    if (ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      tobagoClass += "tobago-" + rendererType + "-disabled ";
    }
    if (ComponentUtil.getBooleanAttribute(component, ATTR_READONLY)) {
      tobagoClass += "tobago-" + rendererType + "-readonly ";
    }
    if (ComponentUtil.getBooleanAttribute(component, ATTR_INLINE)) {
      tobagoClass += "tobago-" + rendererType + "-inline ";
    }
    if (ComponentUtil.isError(component)) {
      tobagoClass += "tobago-" + rendererType + "-error ";
    }
    String markup;
    ValueBinding vb = component.getValueBinding(ATTR_MARKUP);
    if (vb != null) {
      markup = (String) vb.getValue(FacesContext.getCurrentInstance());
    } else {
      markup = (String) component.getAttributes().get(ATTR_MARKUP);
    }
    if (markup != null && markup.trim().length() > 0
        && "strong deleted".indexOf(markup) != -1) { // fixme
      tobagoClass += "tobago-" + rendererType + "-theme-" + markup + " ";
    }

    return tobagoClass + cssClass;
  }

  protected int getConfiguredValue(FacesContext facesContext,
      UIComponent component, String key) {
    try {
      return ThemeConfig.getValue(facesContext, component, key);
    } catch (Exception e) {
      LOG.error("Can't take '" + key + "' for " + getClass().getName()
          + " from config-file: " + e.getMessage(), e);
    }
    return 0;
  }

  public int getHeaderHeight(
      FacesContext facesContext, UIComponent component) {
    int height = getConfiguredValue(facesContext, component, "headerHeight");
    final UIComponent menubar = component.getFacet(FACET_MENUBAR);
    if (menubar != null) {
      height += getConfiguredValue(facesContext, menubar, "headerHeight");
    }
    return height;
  }

  public int getPaddingWidth(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "paddingWidth");
  }

  public int getPaddingHeight(
      FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "paddingHeight");
  }

  public int getComponentExtraWidth(
      FacesContext facesContext,
      UIComponent component) {
    return getConfiguredValue(facesContext, component, "componentExtraWidth");
  }

  public int getComponentExtraHeight(
      FacesContext facesContext,
      UIComponent component) {
    return getConfiguredValue(facesContext, component, "componentExtraHeight");
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    int fixedHeight = 0;

    if (component instanceof UIPanel
        && ComponentUtil.getBooleanAttribute(component, ATTR_LAYOUT_DIRECTIVE)) {
      List children = LayoutUtil.addChildren(new ArrayList(), component);
      for (Iterator childs = children.iterator(); childs.hasNext();) {
        UIComponent child = (UIComponent) childs.next();

        RendererBase renderer = ComponentUtil.getRenderer(child, facesContext);
        if (renderer != null) {
          fixedHeight = Math.max(
              fixedHeight,
              renderer.getFixedHeight(facesContext, child));
        }
      }
    } else {

      fixedHeight = getConfiguredValue(facesContext, component, "fixedHeight");

//      String theme
//          = Theme.fromRenderKitId(facesContext.getViewRoot().getRenderKitId());
//
//      // todo: make this configurable
//
//      if (theme.equals(Theme.SCARBOROUGH)) {
//        fixedHeight = 25;
//      } else if (theme.equals(Theme.SPEYSIDE)) {
//        fixedHeight = 20;
//      } else if (theme.equals(Theme.INEXSO)) {
//        fixedHeight = 20;
//      } else if (theme.equals(Theme.SAP)) {
//        fixedHeight = 20;
//      } else if (theme.equals(Theme.TUI)) {
//        fixedHeight = 20;
//      } else {
//        LOG.warn("unknown Theme :\"" + theme + "\"");
//      }

    }
    return fixedHeight;
  }

  /**
   * Normally not needed to overrwrite
   * */
  public void encodeBeginTobago(
      FacesContext facesContext, UIComponent component) throws IOException {
  }

  public void encodeChildrenTobago(
      FacesContext facesContext, UIComponent component)
      throws IOException {
    for (Iterator i = component.getChildren().iterator(); i.hasNext();) {
      UIComponent child = (UIComponent) i.next();
      if (child.isRendered()) {
//        if (ComponentUtil.getBooleanAttribute(
//            child,
//            ATTR_SUPPRESSED)) {
          child.encodeBegin(facesContext);
          if (child.getRendersChildren()) {
            child.encodeChildren(facesContext);
          }
          child.encodeEnd(facesContext);
//        }
      }
    }
  }

  public void encodeEndTobago (
      FacesContext facesContext,
      UIComponent component) throws IOException {
  }

  protected String getCurrentValue(
      FacesContext facesContext, UIComponent component) {

    if (component instanceof UIInput) {
      Object submittedValue = ((UIInput) component).getSubmittedValue();
      if (submittedValue != null) {
        return (String) submittedValue;
      }
    }
    String currentValue = null;
    Object currentObj = getValue(component);
    if (currentObj != null) {
      currentValue   = getFormattedValue(facesContext, component, currentObj);
    }
    return currentValue;
  }

  protected Object getValue(UIComponent component) {
    if (component instanceof ValueHolder) {
      Object value = ((ValueHolder) component).getValue();
      if (LOG.isDebugEnabled()) {
        LOG.debug("component.getValue() returned " + value);
      }
      return value;
    } else {
      return null;
    }
  }

  public Object getConvertedValue(FacesContext context,
      UIComponent component, Object submittedValue)
      throws ConverterException {
    ValueBinding valueBinding = component.getValueBinding("value");
    Converter converter = null;
    Object result;
    if (component instanceof ValueHolder) {
      converter = ((ValueHolder) component).getConverter();
    }
    if (null == converter && null != valueBinding) {
      Class converterType = valueBinding.getType(context);
      if (converterType == null || converterType == String.class
          || converterType == Object.class) {
        return submittedValue;
      }
      Application application = context.getApplication();
      converter = application.createConverter(converterType);
    } else if (converter == null) {
      return submittedValue;
    }
    if (converter != null && submittedValue instanceof String) {
      result
          = converter.getAsObject(context, component, (String)submittedValue);
      return result;
    } else {
      throw new ConverterException("type conversion error: "
          + "submittedValue='" + submittedValue + "'");
    }
  }

  protected String getFormattedValue(
      FacesContext context, UIComponent component, Object currentValue)
      throws ConverterException {

    if (currentValue == null) {
      return "";
    }

    if (!(component instanceof ValueHolder)) {
      return currentValue.toString();
    }

    Converter converter = ((ValueHolder) component).getConverter();

    if (converter == null) {
      if (currentValue instanceof String) {
        return (String) currentValue;
      }
      Class converterType = currentValue.getClass();
      converter = context.getApplication().createConverter(converterType);
    }

    if (converter == null) {
      return currentValue.toString();
    } else {
      return converter.getAsString(context, component, currentValue);
    }
  }


// ///////////////////////////////////////////// bean getter + setter

}
