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

import {Listener, Phase} from "./tobago-listener";
import {Overlay} from "./tobago-overlay";
import {DomUtils, Tobago4Utils} from "./tobago-utils";
import {Collapse} from "./tobago-popup";
import {Setup} from "./tobago-core";

class Behavior extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback() {
    switch (this.event) {
      case "load": // this is a special case, because the "load" is too late now.
        this.callback();
        break;
      case "resize":
        document.body.addEventListener(this.event, this.callback.bind(this));
        break;
      default:
        this.parentElement.addEventListener(this.event, this.callback.bind(this));
    }
  }

  callback(event?: Event) {

    if (this.collapseAction && this.collapseTarget) {
      const target = this.getRootNode() as ShadowRoot | Document;
      Collapse.execute(this.collapseAction, target.getElementById(this.collapseTarget));
    }

    if (this.execute || this.render) { // this means: AJAX case?
      if (this.render) {
        // prepare overlay for all by AJAX reloaded elements
        let partialIds = this.render.split(" ");
        for (let i = 0; i < partialIds.length; i++) {
          new Overlay(document.getElementById(partialIds[i]), true);
        }
      }
      jsf.ajax.request(
          this.parentElement,
          event, {
            //"javax.faces.behavior.event": this.event,
            execute: this.execute,
            render: this.render
          });
    } else {
      if (!this.omit) {
        setTimeout(this.submit.bind(this), this.delay);
      }
    }
  }

  submit() {
    const actionId = this.action != null ? this.action : this.element.id;
    CommandHelper.submitAction(this, actionId, !this.decoupled, this.target);
  }

  get event(): string {
    return this.getAttribute("event");
  }

  set event(event: string) {
    this.setAttribute("event", event);
  }

  get action(): string {
    return this.getAttribute("action");
  }

  set action(action: string) {
    this.setAttribute("action", action);
  }

  get execute(): string {
    return this.getAttribute("execute");
  }

  set execute(execute: string) {
    this.setAttribute("execute", execute);
  }

  get render(): string {
    return this.getAttribute("render");
  }

  set render(render: string) {
    this.setAttribute("render", render);
  }

  get delay(): number {
    return parseInt(this.getAttribute("delay")) || 0;
  }

  set delay(delay: number) {
    this.setAttribute("delay", String(delay));
  }

  get omit(): boolean {
    return this.hasAttribute("omit");
  }

  set omit(omit: boolean) {
    if (omit) {
      this.setAttribute("omit", "");
    } else {
      this.removeAttribute("omit");
    }
  }

  get target(): string {
    return this.getAttribute("target");
  }

  set target(target: string) {
    this.setAttribute("target", target);
  }

  get confirmation(): string {
    return this.getAttribute("confirmation");
  }

  set confirmation(confirmation: string) {
    this.setAttribute("confirmation", confirmation);
  }

  get collapseAction(): string {
    return this.getAttribute("collapse-action");
  }

  set collapseAction(collapseAction: string) {
    this.setAttribute("collapse-action", collapseAction);
  }

  get collapseTarget(): string {
    return this.getAttribute("collapse-target");
  }

  set collapseTarget(collapseTarget: string) {
    this.setAttribute("collapse-target", collapseTarget);
  }

  get decoupled(): boolean {
    return this.hasAttribute("decoupled");
  }

  set decoupled(decoupled: boolean) {
    if (decoupled) {
      this.setAttribute("decoupled", "");
    } else {
      this.removeAttribute("decoupled");
    }
  }

  get focusId(): string {
    return this.getAttribute("focus-id");
  }

  set focusId(focusId: string) {
    this.setAttribute("focus-id", focusId);
  }

  get element(): HTMLElement {
    return this.parentElement;
  }
}

document.addEventListener("DOMContentLoaded", function (event) {
  window.customElements.define('tobago-behavior', Behavior);
});

export class CommandHelper {
  static isSubmit: boolean = false;

  /**
   * Submitting the page with specified actionId.
   * @param source
   * @param actionId
   * @param decoupled
   * @param target
   */
  public static submitAction = function (source: HTMLElement, actionId: string, decoupled: boolean = true, target?: string) {

    Transport.request(function () {
      if (!CommandHelper.isSubmit) {
        CommandHelper.isSubmit = true;
        const form = document.getElementsByTagName("form")[0] as HTMLFormElement;
        const oldTarget = form.getAttribute("target");
        const sourceHidden = document.getElementById("javax.faces.source") as HTMLInputElement;
        sourceHidden.disabled = false;
        sourceHidden.value = actionId;
        if (target) {
          form.setAttribute("target", target);
        }
        var listenerOptions = {
          source: source,
          actionId: actionId/*,
          options: commandHelper*/
        };
        var onSubmitResult = CommandHelper.onSubmit(listenerOptions);
        if (onSubmitResult) {
          try {
            form.submit();
            // reset the source field after submit, to be prepared for possible next AJAX with decoupled=true
            sourceHidden.disabled = true;
            sourceHidden.value = "";
          } catch (e) {
            Overlay.destroy(DomUtils.page().id);
            CommandHelper.isSubmit = false;
            alert('Submit failed: ' + e); // XXX localization, better error handling
          }
        }
        if (target) {
          if (oldTarget) {
            form.setAttribute("target", oldTarget);
          } else {
            form.removeAttribute("target");
          }
        }
        if (target || decoupled || !onSubmitResult) {
          CommandHelper.isSubmit = false;
          Transport.pageSubmitted = false;
        }
      }
      if (!CommandHelper.isSubmit) {
        Transport.requestComplete(); // remove this from queue
      }
    }, true);
  };

  static initEnter(element: HTMLElement) {
    for (const page of DomUtils.selfOrQuerySelectorAll(element, ".tobago-page")) {
      page.addEventListener("keypress", function (event: KeyboardEvent) {
        let code = event.which; // XXX deprecated
        if (code === 0) {
          code = event.keyCode;
        }
        if (code === 13) {
          let target = event.target as HTMLElement;
          if (target.tagName === "A" || target.tagName === "BUTTON") {
            return;
          }
          if (target.tagName === "TEXTAREA") {
            if (!event.metaKey && !event.ctrlKey) {
              return;
            }
          }
          const name = target.getAttribute("name");
          let id = name ? name : target.id;
          while (id != null) {
            const command = document.querySelector("[data-tobago-default='" + id + "']");
            if (command) {
              command.dispatchEvent(new MouseEvent("click"));
              break;
            }
            id = Tobago4Utils.getNamingContainerId(id);
          }
          return false;
        }
      });
    }
  }

  static onSubmit = function (listenerOptions) {
    Listener.executeBeforeSubmit();
    /*
    XXX check if we need the return false case
    XXX maybe we cancel the submit, but we continue the rest?
    XXX should the other phases also have this feature?

        var result = true; // Do not continue if any function returns false
        for (var order = 0; order < Listeners.beforeSubmit.length; order++) {
          var list = Listeners.beforeSubmit[order];
          for (var i = 0; i < list.length; i++) {
            result = list[i](listenerOptions);
            if (result === false) {
              break;
            }
          }
        }
        if (result === false) {
          this.isSubmit = false;
          return false;
        }
    */
    CommandHelper.isSubmit = true;

    Setup.onBeforeUnload();

    return true;
  };

}

Listener.register(CommandHelper.initEnter, Phase.DOCUMENT_READY);

class Transport {
  static requests = [];
  static currentActionId = null;
  static pageSubmitted = false;
  static startTime: Date;

  /**
   * @return true if the request is queued.
   */
  static request = function (req, submitPage, actionId?) {
    var index = 0;
    if (submitPage) {
      Transport.pageSubmitted = true;
      index = Transport.requests.push(req);
      //console.debug('index = ' + index)
    } else if (!Transport.pageSubmitted) { // AJAX case
      console.debug('Current ActionId = ' + Transport.currentActionId + ' action= ' + actionId);
      if (actionId && Transport.currentActionId === actionId) {
        console.info('Ignoring request');
        // If actionId equals currentActionId assume double request: do nothing
        return false;
      }
      index = Transport.requests.push(req);
      //console.debug('index = ' + index)
      Transport.currentActionId = actionId;
    } else {
      console.debug("else case");
      return false;
    }
    console.debug('index = ' + index);
    if (index === 1) {
      console.info('Execute request!');
      Transport.startTime = new Date();
      Transport.requests[0]();
    } else {
      console.info('Request queued!');
    }
    return true;
  };


// TBD XXX REMOVE is this called in non AJAX case?

  static requestComplete = function () {
    Transport.requests.shift();
    Transport.currentActionId = null;
    console.debug('Request complete! Duration: ' + (new Date().getTime() - Transport.startTime.getTime()) + 'ms; '
        + 'Queue size : ' + Transport.requests.length);
    if (Transport.requests.length > 0) {
      console.debug('Execute request!');
      Transport.startTime = new Date();
      Transport.requests[0]();
    }
  };
}
