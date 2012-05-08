package org.apache.myfaces.tobago.internal.webapp;

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

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;
import org.apache.myfaces.tobago.internal.util.FastStringWriter;
import org.apache.myfaces.tobago.util.FacesVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;

public class JsonResponseWriter extends HtmlResponseWriter {

  private static final Logger LOG = LoggerFactory.getLogger(JsonResponseWriter.class);

  private Writer javascriptWriter;
  private boolean javascriptMode;

  public JsonResponseWriter(Writer writer, String contentType, String characterEncoding) {
    super(writer, contentType, characterEncoding);
    this.javascriptWriter = new FastStringWriter();
  }

  @Override
  public void endJavascript() throws IOException {
    javascriptMode = false;
  }

  @Override
  public void startJavascript() throws IOException {
    javascriptMode = true;
  }

  @Override
  public void write(String string) throws IOException {
    writeInternal(javascriptMode ? javascriptWriter : getWriter(), AjaxInternalUtils.encodeJavaScriptString(string));
  }

  @Override
  public void write(char[] chars) throws IOException {
    // XXX remove me later:
    // this is a temporary workaround, should be removed after fixing the bug in Mojarra.
    // http://java.net/jira/browse/JAVASERVERFACES-2411
    // https://issues.apache.org/jira/browse/TOBAGO-1124
    if (FacesVersion.isMojarra() && FacesVersion.supports20()) {
      StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
      if (stackTraceElements[2].getClassName().equals("com.sun.faces.renderkit.ServerSideStateHelper")) {
        super.write(StringUtils.replace(new String(chars), "\"", "\\\""));
        return;
      }
    }
    super.write(chars);
  }

  @Override
  public void writeJavascript(String script) throws IOException {
    writeInternal(javascriptWriter, AjaxInternalUtils.encodeJavaScriptString(script));
  }

  public String getJavascript() {
    return javascriptWriter.toString();
  }

  @Override
  protected void startElementInternal(Writer writer, String name, UIComponent currentComponent)
      throws IOException {
    setComponent(currentComponent);
    if (isStartStillOpen()) {
      writer.write(">");
    }
    writer.write("<");
    writer.write(name);
    setStartStillOpen(true);
  }

  @Override
  protected void endElementInternal(Writer writer, String name) throws IOException {
    if (EMPTY_TAG.contains(name)) {
        writer.write(">");
    } else {
      if (isStartStillOpen()) {
        writer.write(">");
      }
      writer.write("</");
      writer.write(name);
      writer.write(">");
    }
    setStartStillOpen(false);
  }

  @Override
  protected void closeOpenTag() throws IOException {
    if (isStartStillOpen()) {
      getWriter().write(">");
      setStartStillOpen(false);
    }
  }

  @Override
  protected void writeAttributeInternal(Writer writer, String name, String value, boolean escape)
      throws IOException {
    if (!isStartStillOpen()) {
      String trace = getCallingClassStackTraceElementString();
      String error = "Cannot write attribute when start-tag not open. "
          + "name = '" + name + "' "
          + "value = '" + value + "' "
          + trace.substring(trace.indexOf('('));
      LOG.error(error);
      throw new IllegalStateException(error);
    }

    if (value != null) {
      writer.write(' ');
      writer.write(name);
      writer.write("=\\\"");
      if (escape) {
        getHelper().writeAttributeValue(value);
      } else {
        writer.write(value);
      }
      writer.write("\\\"");
    }
  }

  public ResponseWriter cloneWithWriter(final Writer originalWriter) {
     return new JsonResponseWriter(
         originalWriter, getContentType(), getCharacterEncoding());
   }


  @Override
  public void startDocument() throws IOException {
  }

  @Override
  public void endDocument() throws IOException {
  }
}
