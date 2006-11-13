<%--
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
--%>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <jsp:body>
    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout columns="300px;1*"
                       rows="fixed;fixed;fixed;fixed;fixed;1*"/>
      </f:facet>
      <%-- code-sniplet-start id="in" --%>
      <tx:in label="Input" value="Some Text"/>
      <%-- code-sniplet-end id="in" --%>
      <tc:cell/>

      <tx:in label="Read Only" readonly="true" value="Some Text"/>
      <tc:cell/>

      <tx:in label="Disabled" disabled="true" value="Some Text"/>
      <tc:cell/>

      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout columns="100px;*"/>
        </f:facet>
        <tc:label value="Short Label"/>
        <tc:in value="Some Text"/>
      </tc:panel>
      <tc:cell/>

      <tx:in label="Input (focus)" focus="true" value="Some Text"/>
      <tc:cell/>

      <tc:cell spanX="2"/>
    </tc:panel>
  </jsp:body>
</layout:screenshot>