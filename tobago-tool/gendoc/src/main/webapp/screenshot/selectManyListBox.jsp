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
  <f:subview id="selectManyListbox">
    <jsp:body>
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout rows="45px;45px;1*" />
        </f:facet>
<%-- code-sniplet-start id="selectManyListbox" --%>
        <tx:selectManyListbox inline="true" id="LabeledInlineMultiSelect"
                             label="Contact via: ">
          <f:selectItem itemValue="Phone" itemLabel="Phone" />
          <f:selectItem itemValue="eMail" itemLabel="eMail"/>
          <f:selectItem itemValue="Mobile" itemLabel="Mobile"/>
          <f:selectItem itemValue="Fax"  itemLabel="Faxscimile"/>
        </tx:selectManyListbox>
        <tx:selectManyListbox inline="true" 
                             label="Contact via: ">
          <f:selectItem itemValue="Phone" itemLabel="Phone" />
          <f:selectItem itemValue="eMail" itemLabel="eMail"/>
          <f:selectItem itemValue="Mobile" itemLabel="Mobile"/>
          <f:selectItem itemValue="Fax"  itemLabel="Faxscimile"/>
          <f:facet name="click">
            <tc:command />
          </f:facet>
        </tx:selectManyListbox>
<%-- code-sniplet-end id="selectManyListbox" --%>
        <tc:cell/>

      </tc:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>