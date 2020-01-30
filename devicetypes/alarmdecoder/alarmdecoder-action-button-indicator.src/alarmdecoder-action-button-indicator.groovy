/**
 *  Virtual Momentary Switch Indicator for alarm panel
 *
 *  Copyright 2016-2019 Nu Tech Software Solutions, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy
 *  of the License at:
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 *
 */

/*
 * global support
 */
import groovy.transform.Field
@Field APPNAMESPACE = "alarmdecoder"

metadata {
  definition(
    name: "AlarmDecoder action button indicator",
    namespace: APPNAMESPACE,
    author: "Nu Tech Software Solutions, Inc.") {
    capability "Switch"
    capability "Momentary"
    command "push"
    command "on"
    command "off"
  }

  // tile definitions
  tiles {
    standardTile(
      "switch",
      "device.switch",
      width: 2, height: 2,
      canChangeIcon: true) {
      state(
        "off",
        label: 'Push',
        action: "momentary.push",
        backgroundColor: "#ffffff")
      state(
        "on",
        label: 'Push',
        action: "momentary.push",
        backgroundColor: "#00a0dc")
    }
    main "switch"
    details "switch"
  }

  // preferences
  preferences {
    input(
      name: "invert",
      type: "bool",
      title: "Invert signal [true,false]",
      description: "Invert signal [true,false]." +
      " Changes ON/OFF,OPEN/CLOSE,DETECTED/CLEAR",
      required: false)
    input(
      name: "zone",
      type: "number",
      title: "Zone Number",
      description: "Zone # required for zone events.",
      required: false)
  }

}

/**
 * installed()/updated()
 *
 * It is not possible for a service to access preferences directly so
 * update device data value to allow access from parent
 * using getDeviceDataByName getDataValue
 * FIXME: diff ^ docs not clear.
 *
 */
def installed() {
  updateDataValue("invert", invert.toString())
  updateDataValue("zone", zone.toString())
}

def updated() {
  updateDataValue("invert", invert.toString())
  updateDataValue("zone", zone.toString())
}

// Send the request to the service for processing.
def push() {
  if (parent.debug)
    log.debug "AlarmDecoderActionButtonIndicator: Executing 'actionButton'"
  parent.actionButton(device.getDeviceNetworkId())
}

def on() {
  push()
}

def off() {
  push()
}
