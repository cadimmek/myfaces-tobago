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

function TobagoTestTool(assert) {
  this.assert = assert;
  this.steps = [];
}

TobagoTestTool.stepType = {
  ACTION: 1,
  WAIT_RESPONSE: 2,
  WAIT_MS: 3,
  ASSERTS: 4
};

class JasmineUtils {
  static isMsie() {
    return navigator.userAgent.indexOf("MSIE") > -1 || navigator.userAgent.indexOf("Trident") > -1;
  };

  static checkGridCss(element, columnStart, columnEnd, rowStart, rowEnd) {
    columnEnd = this.convertGridCss(columnEnd);
    rowEnd = this.convertGridCss(rowEnd);

    if (this.isMsie()) {
      expect(getComputedStyle(element).msGridColumn).toBe(columnStart);
      expect(getComputedStyle(element).msGridColumnSpan).toBe(columnEnd);
      expect(getComputedStyle(element).msGridRow).toBe(rowStart);
      expect(getComputedStyle(element).msGridRowSpan).toBe(rowEnd);
    } else {
      expect(getComputedStyle(element).gridColumnStart).toBe(columnStart);
      expect(getComputedStyle(element).gridColumnEnd).toBe(columnEnd);
      expect(getComputedStyle(element).gridRowStart).toBe(rowStart);
      expect(getComputedStyle(element).gridRowEnd).toBe(rowEnd);
    }
  };

  static convertGridCss(end) {
    if (JasmineTestTool.msie) {
      switch (end) {
        case "auto":
          return "1";
        case "span 2":
          return "2";
        case "span 3":
          return "3";
        case "span 4":
          return "4";
        default:
          return end;
      }
    } else {
      return end;
    }
  };
}

TobagoTestTool.prototype = {
  action: function (func) {
    this.steps.push({
      type: TobagoTestTool.stepType.ACTION,
      func: func
    });
  },
  waitForResponse: function () {
    this.steps.push({
      type: TobagoTestTool.stepType.WAIT_RESPONSE
    });
  },
  waitMs: function (ms) {
    this.steps.push({
      type: TobagoTestTool.stepType.WAIT_MS,
      ms: ms ? ms : 0
    });
  },
  asserts: function (numOfAssertions, func) {
    this.steps.push({
      type: TobagoTestTool.stepType.ASSERTS,
      numOfAssertions: numOfAssertions ? numOfAssertions : 0,
      func: func
    });
  },
  startTest: function () {
    const steps = this.steps.slice(0);
    const cycleTiming = 50;
    let currentStep = 0;
    let testStepTimeout;

    function getAssertExpect() {
      var expect = 0;
      steps.forEach(function (step) {
        if (step.type === TobagoTestTool.stepType.ASSERTS) {
          expect += step.numOfAssertions;
        }
      });
      return expect;
    }

    function getAssertAsync() {
      var async = 0;
      steps.forEach(function (step) {
        if (step.type === TobagoTestTool.stepType.ASSERTS) {
          async++;
        }
      });
      return async;
    }

    this.assert.expect(getAssertExpect());
    const done = this.assert.async(getAssertAsync());
    const assert = this.assert;

    function resetTestStepTimeout(additionalMs) {
      const timeout = additionalMs ? 20000 + additionalMs : 20000;
      testStepTimeout = Date.now() + timeout;
    }

    let waitForResponse = false;
    let ajaxRequestDetected = false;
    let ajaxRequestDone = false;
    let fullPageReloadDetected = false;
    let fullPageReloadDone = false;

    function registerAjaxReadyStateListener() {
      let oldXHR = document.getElementById("page:testframe").contentWindow.XMLHttpRequest;

      function newXHR() {
        let realXHR = new oldXHR();
        realXHR.addEventListener("readystatechange", function () {
          if (realXHR.readyState !== XMLHttpRequest.UNSENT && realXHR.readyState !== XMLHttpRequest.DONE) {
            ajaxRequestDetected = true;
          } else if (ajaxRequestDetected && realXHR.readyState === XMLHttpRequest.DONE) {
            ajaxRequestDone = true;
            waitForResponse = false;
          }
        }, false);
        return realXHR;
      }

      document.getElementById("page:testframe").contentWindow.XMLHttpRequest = newXHR;
    }

    function fullPageReloadPolling() {
      const testframe = document.getElementById("page:testframe");
      if (testframe === null
          || testframe.contentWindow.document.readyState !== "complete"
          || testframe.contentWindow.document.querySelector("html") === null) {
        fullPageReloadDetected = true;
      } else if (fullPageReloadDetected) {
        fullPageReloadDone = true;
        waitForResponse = false;
      }

      if (!fullPageReloadDone && !ajaxRequestDone) {
        setTimeout(fullPageReloadPolling, cycleTiming);
      }
    }

    function cycle() {
      if (currentStep >= steps.length) {
        // we are done here
      } else if (Date.now() >= testStepTimeout) {
        assert.ok(false, "Timeout!");
        if (steps[currentStep].stepType === TobagoTestTool.stepType.ASSERTS) {
          done();
        }
        currentStep++;
        cycle();
      } else if (waitForResponse) {
        // we need to wait more
        setTimeout(cycle, cycleTiming);
      } else if (steps[currentStep].type === TobagoTestTool.stepType.ACTION) {
        if (currentStep + 1 < steps.length && steps[currentStep + 1].type === TobagoTestTool.stepType.WAIT_RESPONSE) {
          // register listener for ajax before action is executed, otherwise the ajax listener is registered too late
          registerAjaxReadyStateListener();
          steps[currentStep].func();
          currentStep++;
          cycle();
        } else {
          steps[currentStep].func();
          currentStep++;
          resetTestStepTimeout();
          setTimeout(cycle, cycleTiming);
        }
      } else if (steps[currentStep].type === TobagoTestTool.stepType.WAIT_RESPONSE) {
        waitForResponse = true;
        ajaxRequestDetected = false;
        ajaxRequestDone = false;
        fullPageReloadDetected = false;
        fullPageReloadDone = false;
        registerAjaxReadyStateListener();
        fullPageReloadPolling();

        currentStep++;
        resetTestStepTimeout();
        setTimeout(cycle, cycleTiming);
      } else if (steps[currentStep].type === TobagoTestTool.stepType.WAIT_MS) {
        const ms = steps[currentStep].ms;

        currentStep++;
        resetTestStepTimeout(ms);
        setTimeout(cycle, ms);
      } else if (steps[currentStep].type === TobagoTestTool.stepType.ASSERTS) {
        steps[currentStep].func();
        currentStep++;
        done();
        resetTestStepTimeout();
        setTimeout(cycle, cycleTiming);
      }
    }

    resetTestStepTimeout();
    cycle();
  }
};

export {TobagoTestTool};

class JasmineTestTool {

  static ajaxReadyStateChangeEvent = "tobago.jtt.ajax.readyStateChange";
  static ajaxReadyState;
  steps = [];
  done;
  timeout;
  lastStepExecution;

  /**
   * @param done function from Jasmine; must called if all Steps done or timeout
   * @param timeout for a single step; default 20000ms
   */
  constructor(done, timeout) {
    this.done = done;
    this.timeout = timeout ? timeout : 20000;
    this.registerAjaxReadyStateListener();
  }

  /**
   * The require function (require) defines the desired state before the main test starts. The fix function (fix)
   * will be executed if the require function returns false.
   * @param require function
   * @param fix function
   */
  setup(require, fix) {
    this.do(() => {
      if (!require()) {
        console.debug("[JasmineTestTool] require() returns false, execute fix()");
        fix();
      }
    })
    this.wait(require);
  }

  /**
   * Execute dispatchEvent() on the given element. The result function must return false before dispatch.
   * After the dispatch
   * @param type of the event
   * @param element function
   * @param result function
   */
  event(type, element, result) {
    this.do(() => {
      if (result()) {
        fail("The result function (" + result + ") returns true BEFORE the '" + type + "' event is dispatched."
            + " Please define a result function that return false before dispatch event and return true after dispatch"
            + " event.");
      }
    });
    this.do(() => element().dispatchEvent(new Event(type, {bubbles: true})));
    this.wait(result);
  }

  do(fn) {
    this.steps.push({
      type: "do",
      func: fn,
      done: false
    });
  }

  wait(fn) {
    this.steps.push({
      type: "wait",
      func: fn,
      done: false
    });
  }

  start() {
    console.debug("[JasmineTestTool] start");
    this.resetTimeout();
    this.cycle();
  }

  cycle() {
    const nextStep = this.getNextStep();

    if (this.isFinished()) {
      this.done();
    } else if (this.isTimeout()) {
      fail("Timeout of '" + nextStep.type + "'-step: " + nextStep.func);
      nextStep.done = true;
      this.resetTimeout();
      window.setTimeout(this.cycle.bind(this), 0);
    } else if (!this.isDocumentReady() || !this.isAjaxReady()) {
      console.debug("[JasmineTestTool] documentReady: " + this.isDocumentReady()
          + " - ajaxReady: " + this.isAjaxReady());
      window.setTimeout(this.cycle.bind(this), 50);
    } else if (nextStep.type === "do") {
      console.debug("[JasmineTestTool] do-step: " + nextStep.func);
      this.registerCustomXmlHttpRequest();
      nextStep.func();
      nextStep.done = true;
      this.resetTimeout();
      window.setTimeout(this.cycle.bind(this), 0);
    } else if (nextStep.type === "wait") {
      console.debug("[JasmineTestTool] wait-step: " + nextStep.func);
      if (nextStep.func()) {
        nextStep.done = true;
        this.resetTimeout();
      }
      window.setTimeout(this.cycle.bind(this), 50);
    } else {
      fail("an unexpected error has occurred!");
      this.done();
    }
  }

  isFinished() {
    for (let step of this.steps) {
      if (!step.done) {
        return false;
      }
    }
    return true;
  }

  isDocumentReady() {
    return document.getElementById("page:testframe").contentWindow.document.readyState === "complete";
  }

  registerAjaxReadyStateListener() {
    JasmineTestTool.ajaxReadyState = XMLHttpRequest.UNSENT;
    window.removeEventListener(JasmineTestTool.ajaxReadyStateChangeEvent, JasmineTestTool.changeAjaxReadyState);
    window.addEventListener(JasmineTestTool.ajaxReadyStateChangeEvent, JasmineTestTool.changeAjaxReadyState);
  }

  static changeAjaxReadyState(event) {
    JasmineTestTool.ajaxReadyState = event.detail.readyState;
    console.debug("[JasmineTestTool] ajaxReadyState: " + JasmineTestTool.ajaxReadyState);
  }

  registerCustomXmlHttpRequest() {
    class JasmineXMLHttpRequest extends XMLHttpRequest {
      constructor() {
        super();
        this.addEventListener("readystatechange", function () {
          window.dispatchEvent(new CustomEvent(JasmineTestTool.ajaxReadyStateChangeEvent,
              {detail: {readyState: this.readyState}}));
        });
      }
    }

    document.getElementById("page:testframe").contentWindow.XMLHttpRequest = JasmineXMLHttpRequest;
  }

  isAjaxReady() {
    return JasmineTestTool.ajaxReadyState === XMLHttpRequest.UNSENT
        || JasmineTestTool.ajaxReadyState === XMLHttpRequest.DONE;
  }

  getNextStep() {
    for (let step of this.steps) {
      if (!step.done) {
        return step;
      }
    }
    return null;
  }

  isTimeout() {
    return Date.now() > (this.lastStepExecution + this.timeout);
  }

  resetTimeout() {
    this.lastStepExecution = Date.now();
  }
}

export {JasmineUtils, JasmineTestTool};
