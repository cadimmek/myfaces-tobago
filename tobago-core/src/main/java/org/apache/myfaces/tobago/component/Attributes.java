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

package org.apache.myfaces.tobago.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Name constants of the attributes of the Tobago components.
 */
public enum Attributes {

  accessKey,
  action,
  actionExpression,
  actionListener,
  actionListenerExpression,
  align,
  alignItems,
  alt,
  applicationIcon,
  autocomplete,
  autoReload,
  bodyContent,
  border,
  /**
   * Used by a layout manager
   */
  borderBottom,
  /**
   * Used by a layout manager
   */
  borderLeft,
  /**
   * Used by a layout manager
   */
  borderRight,
  /**
   * Used by a layout manager
   */
  borderTop,
  bottom,
  charset,
  clientProperties,
  closed,
  collapsed,
  collapsedMode,
  column,
  columnSpacing,
  columns,
  confirmation,
  converter,
  customClass,
  createSpan,
  css,
  cssClassesBlocks,
  dateStyle,
  defaultCommand,
  delay,
  directLinkCount,
  disabled,
  display,
  enctype,
  escape,
  expanded,
  execute,
  event,
  extraSmall,
  fieldId,
  file,
  filter,
  first,
  fixed,
  frequency,
  focus,
  focusId,
  formatPattern,
  forValue("for"),
  globalOnly,
  height,
  hidden,
  hover,
  i18n,
  iconSize,
  id,
  immediate,
  image,
  inline,
  itemDescription,
  itemDisabled,
  itemLabel,
  itemImage,
  itemValue,
  justifyContent,
  label,
  labelLayout,
  labelPosition,
  labelWidth,
  large,
  layoutOrder,
  left,
  link,
  /** @deprecated since Tobago 2.0.0 */
  @Deprecated
  margin,
  /**
   * Used by a layout manager
   */
  marginBottom,
  /**
   * Used by a layout manager
   */
  marginLeft,
  /**
   * Used by a layout manager
   */
  marginRight,
  /**
   * Used by a layout manager
   */
  marginTop,
  marked,
  markup,
  max,
  maxSeverity,
  maxNumber,
  maximumItems,
  maximumHeight,
  maximumWidth,
  maxHeight,
  maxWidth,
  method,
  min,
  minHeight,
  minWidth,
  minSeverity,
  minimumCharacters,
  minimumHeight,
  minimumWidth,
  medium,
  modal,
  mode,
  multiple,
  mutable,
  name,
  navigate,
  numberStyle,
  /**
   * Used by a layout manager
   */
  offsetExtraSmall,
  /**
   * Used by a layout manager
   */
  offsetLarge,
  /**
   * Used by a layout manager
   */
  offsetMedium,
  /**
   * Used by a layout manager
   */
  offsetSmall,
  omit,
  /** @deprecated */
  @Deprecated
  onclick,
  /** @deprecated */
  @Deprecated
  onchange,
  open,
  orderBy,
  orientation,
  overflowX,
  overflowY,
  /**
   * Used by a layout manager
   */
  paddingBottom,
  /**
   * Used by a layout manager
   */
  paddingLeft,
  /**
   * Used by a layout manager
   */
  paddingRight,
  /**
   * Used by a layout manager
   */
  paddingTop,
  pagingTarget,
  password,
  placeholder,
  popupClose,
  popupList,
  popupReset,
  popupCalendarId,
  position,
  preferredHeight,
  preferredWidth,
  preformated,
  readonly,
  reference,
  relative,
  rendered,
  renderedIndex,
  rendererType,
  renderAs,
  renderRange,
  renderRangeExtern,
  required,
  resizable,
  right,
  rigid,
  rowId,
  row,
  rowSpacing,
  rows,
  sanitize,
  scriptFiles,
  scrollbarHeight,
  scrollbars,
  // Attribute name could not be the same as the method name
  // this cause an infinite loop on attribute map
  scrollPosition,
  selectedIndex,
  selectedLabel,
  selectedListString,
  selectable,
  sheetAction,
  showCheckbox,
  showDirectLinks,
  showDirectLinksArrows,
  showHeader,
  showJunctions,
  showNavigationBar,
  showPageRange,
  showPageRangeArrows,
  showPagingAlways,
  showRoot,
  showRootJunction,
  showRowRange,
  showSummary,
  showDetail,
  size,
  sortable,
  sortActionListener,
  sortActionListenerExpression,
  small,
  spanX,
  spanY,
  src,
  state,
  stateChangeListener,
  stateChangeListenerExpression,
  statePreview,
  style,
  suggestMethod,
  suggestMethodExpression,
  switchType,
  tabChangeListener,
  tabChangeListenerExpression,
  tabIndex,
  target,
  timeStyle,
  textAlign,
  timezone,
  title,
  tip,
  top,
  totalCount,
  transition,
  type,
  value,
  valueChangeListener,
  var,
  unit,
  unselectedLabel,
  update,
  validator,
  viewport,
  width,
  widthList,
  zIndex;

  /** This constants are needed for annotations, because they can't use the enums. */
  public static final String EXECUTE = "execute";

  private static final Logger LOG = LoggerFactory.getLogger(Attributes.class);

  private final String explicit;

  Attributes() {
    this(null);
  }

  Attributes(String explicit) {
    this.explicit = explicit;
  }

  public String getName() {
    if (explicit != null) {
      return explicit;
    } else {
      return name();
    }
  }

  public static Attributes valueOfFailsafe(String name) {
    try {
      return Attributes.valueOf(name);
    } catch (IllegalArgumentException e) {
      LOG.warn("Can't find enum for {} with name '{}'", Attributes.class.getName(), name);
      return null;
    }
  }

}
