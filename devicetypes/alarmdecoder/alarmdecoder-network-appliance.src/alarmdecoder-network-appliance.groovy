/**
 *  AlarmDecoder Network Appliance
 *
 *  Copyright 2016-2018 Nu Tech Software Solutions, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
import groovy.json.JsonSlurper;
import groovy.util.XmlParser;

import groovy.transform.Field
/*
 * Turn on verbose debugging
 */
@Field debug = false

def getSensorMap() {
    return ['10':'Front Door', '11':'Dining Room S.G.D', '12':'Garage Entry Door', '13':'Laundry Rm Bath Window', '14':'Kitchen Window', '15':'Kitchen Nook Window', '16':'Family Rm Door','17':'Master Bd Rm Window 1', '18':'Master Bd Rm Window 2', '19':'Master Bath Door', '20':'Hall Bathroom Window', '21':'Bedroom 2 Window', '22':'Bedroom 3 Window', '23':'Bedroom 4 Window','24':'Bedroom 5 Window', '25':'Motion - Hallway'];
}
def sensorMap = getSensorMap();	
									
preferences {
    section() {
        input("api_key", "password", title: "API Key", description: "The key to access the REST API", required: true)
        input("user_code", "password", title: "Alarm Code", description: "The user code for the panel", required: true)
        input("panel_type", "enum", title: "Panel Type", description: "Type of panel", options: ["ADEMCO", "DSC"], defaultValue: "ADEMCO", required: true)
    }
    section() {
        input("zonetracker1zone", "number", title: "10: " + sensorMap['10'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker2zone", "number", title: "11: " + sensorMap['11'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker3zone", "number", title: "12: " + sensorMap['12'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker4zone", "number", title: "13: " + sensorMap['13'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker5zone", "number", title: "14: " + sensorMap['14'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker6zone", "number", title: "15: " + sensorMap['15'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker7zone", "number", title: "16: " + sensorMap['16'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker8zone", "number", title: "17: " + sensorMap['17'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker9zone", "number", title: "18: " + sensorMap['18'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker10zone", "number", title: "19: " + sensorMap['19'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker11zone", "number", title: "20: " + sensorMap['20'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker12zone", "number", title: "21: " + sensorMap['21'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker13zone", "number", title: "22: " + sensorMap['22'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker14zone", "number", title: "23: " + sensorMap['23'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker15zone", "number", title: "24: " + sensorMap['24'], description: "Zone number to associate with this contact sensor.")
        input("zonetracker16zone", "number", title: "25: " + sensorMap['25'], description: "Zone number to associate with this contact sensor.")
    }
}

metadata {
    definition (name: "AlarmDecoder network appliance", namespace: "alarmdecoder", author: "Scott Petersen") {
        capability "Refresh"
        capability "Switch"             // STAY
        capability "Lock"               // AWAY
        capability "Alarm"              // PANIC
        capability "Contact Sensor"     // ALARM -> Smart Home Monitor

        attribute "panel_state", "enum", ["armed", "armed_stay", "disarmed", "alarming", "fire", "ready", "notready"]
        attribute "armed", "enum", ["armed", "disarmed"]
        attribute "panic_state", "string"
        attribute "zoneStatus1", "string"
        attribute "zoneStatus2", "string"
        attribute "zoneStatus3", "string"
        attribute "zoneStatus4", "string"
        attribute "zoneStatus5", "string"
        attribute "zoneStatus6", "string"
        attribute "zoneStatus7", "string"
        attribute "zoneStatus8", "string"
        attribute "zoneStatus9", "string"
        attribute "zoneStatus10", "string"
        attribute "zoneStatus11", "string"
        attribute "zoneStatus12", "string"
        attribute "zoneStatus13", "string"
        attribute "zoneStatus14", "string"
        attribute "zoneStatus15", "string"
        attribute "zoneStatus16", "string"											  

        command "disarm"
        command "arm_stay"
        command "arm_away"
        command "fire"
        command "fire1"
        command "fire2"
        command "panic"
        command "panic1"
        command "panic2"
        command "aux"
        command "aux1"
        command "aux2"
    }

    simulator {
        // TODO: define status and reply messages here
    }

    tiles(scale: 2) {
        multiAttributeTile(name: "status", type: "generic", width: 6, height: 4) {
            tileAttribute("device.panel_state", key: "PRIMARY_CONTROL") {
                attributeState "armed", label: 'Armed', icon: "st.security.alarm.on", backgroundColor: "#ffa81e"
                attributeState "armed_stay", label: 'Armed (stay)', icon: "st.security.alarm.on", backgroundColor: "#ffa81e"
                attributeState "disarmed", label: 'Disarmed', icon: "st.security.alarm.off", backgroundColor: "#79b821", defaultState: true
                attributeState "alarming", label: 'Alarming!', icon: "st.home.home2", backgroundColor: "#ff4000"
                attributeState "fire", label: 'Fire!', icon: "st.contact.contact.closed", backgroundColor: "#ff0000"
                attributeState "ready", label: 'Ready', icon: "st.security.alarm.off", backgroundColor: "#79b821"
                attributeState "notready", label: 'Not Ready', icon: "st.security.alarm.off", backgroundColor: "#e86d13"
            }
        }

        standardTile("arm_disarm", "device.panel_state", inactiveLabel: false, width: 1, height: 1) {
            state "armed", action:"lock.unlock", icon:"st.security.alarm.off", label: "DISARM"
            state "armed_stay", action:"lock.unlock", icon:"st.security.alarm.off", label: "DISARM"
            state "disarmed", action:"lock.lock", icon:"st.security.alarm.on", label: "AWAY"
            state "alarming", action:"lock.unlock", icon:"st.security.alarm.off", label: "DISARM"
            state "fire", action:"lock.unlock", icon:"st.security.alarm.off", label: "DISARM"
            state "ready", action:"lock.lock", icon:"st.security.alarm.off", label: "AWAY"
            state "notready", action:"lock.unlock", icon:"st.security.alarm.off", label: "DISARM"
        }

        standardTile("stay_disarm", "device.panel_state", inactiveLabel: false, width: 1, height: 1) {
            state "armed", action:"switch.off", icon:"st.security.alarm.off", label: "DISARM"
            state "armed_stay", action:"switch.off", icon:"st.security.alarm.off", label: "DISARM"
            state "disarmed", action:"switch.on", icon:"st.Home.home4", label: "STAY"
            state "alarming", action:"switch.off", icon:"st.security.alarm.off", label: "DISARM"
            state "fire", action:"switch.off", icon:"st.security.alarm.off", label: "DISARM"
            state "ready", action:"switch.on", icon:"st.security.alarm.off", label: "STAY"
            state "notready", action:"switch.off", icon:"st.security.alarm.off", label: "DISARM"
        }

        standardTile("panic", "device.panic_state", inactiveLabel: false, width: 2, height: 2) {
            state "default", icon:"http://www.alarmdecoder.com/st/ad2-police.png", label: "PANIC", nextState: "panic1", action: "panic1"
            state "panic1", icon: "http://www.alarmdecoder.com/st/ad2-police.png", label: "PANIC", nextState: "panic2", action: "panic2", backgroundColor: "#ffa81e"
            state "panic2", icon: "http://www.alarmdecoder.com/st/ad2-police.png", label: "PANIC", nextState: "default", action: "panic", backgroundColor: "#ff4000"
        }

        standardTile("fire", "device.fire_state", inactiveLabel: false, width: 2, height: 2) {
            state "default", icon:"http://www.alarmdecoder.com/st/ad2-fire.png", label: "FIRE", nextState: "fire1", action: "fire1"
            state "fire1", icon: "http://www.alarmdecoder.com/st/ad2-fire.png", label: "FIRE", nextState: "fire2", action: "fire2", backgroundColor: "#ffa81e"
            state "fire2", icon: "http://www.alarmdecoder.com/st/ad2-fire.png", label: "FIRE", nextState: "default", action: "fire", backgroundColor: "#ff4000"
        }

        standardTile("aux", "device.aux_state", inactiveLabel: false, width: 2, height: 2) {
            state "default", icon:"http://www.alarmdecoder.com/st/ad2-aux.png", label: "AUX", nextState: "aux1", action: "aux1"
            state "aux1", icon: "http://www.alarmdecoder.com/st/ad2-aux.png", label: "AUX", nextState: "aux2", action: "aux2", backgroundColor: "#ffa81e"
            state "aux2", icon: "http://www.alarmdecoder.com/st/ad2-aux.png", label: "AUX", nextState: "default", action: "aux", backgroundColor: "#ff4000"
        }

        valueTile("zoneStatus1", "device.zoneStatus1", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus2", "device.zoneStatus2", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus3", "device.zoneStatus3", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus4", "device.zoneStatus4", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus5", "device.zoneStatus5", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus6", "device.zoneStatus6", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus7", "device.zoneStatus7", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus8", "device.zoneStatus8", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus9", "device.zoneStatus9", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus10", "device.zoneStatus10", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus11", "device.zoneStatus11", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus12", "device.zoneStatus12", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

		valueTile("zoneStatus13", "device.zoneStatus13", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus14", "device.zoneStatus14", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }

        valueTile("zoneStatus15", "device.zoneStatus15", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }
        
        valueTile("zoneStatus16", "device.zoneStatus16", inactiveLabel: false, width: 1, height: 1) {
            state "default", icon:"", label: '${currentValue}', backgroundColors: [
                [value: 0, color: "#ffffff"],
                [value: 1, color: "#ff0000"],
                [value: 99, color: "#ff0000"]
            ]
        }																							 
        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 6, height: 2) {
            state "default", action:"refresh.refresh", icon:"st.secondary.refresh"
        }

        main(["status"])
        details(["status", "arm_disarm", "stay_disarm", "panic", "fire", "aux", "zoneStatus1", "zoneStatus2", "zoneStatus3", "zoneStatus4", "zoneStatus5", "zoneStatus6", "zoneStatus7", "zoneStatus8", "zoneStatus9", "zoneStatus10", "zoneStatus11", "zoneStatus12", "zoneStatus13", "zoneStatus14", "zoneStatus15", "zoneStatus16", "refresh", "teststuff"])
    }
}

/*** Handlers ***/

def installed() {
    for (def i = 1; i <= sensorMap.size(); i++)
        sendEvent(name: "zoneStatus${i}", value: "", displayed: false)
}

def updated() {
    log.trace "--- handler.updated"

    state.faulted_zones = []

    // Raw panel state values
    state.panel_ready = true
    state.panel_armed = false
    state.panel_armed_stay = false
    state.panel_fire_detected = false
    state.panel_alarming = false
    state.panel_panicked = false
    state.panel_on_battery = true
    state.panel_powered = true

    // Calculated alarm state ENUM
    state.panel_state = "disarmed"
    state.alarm_status = "off"
    state.armed = false

    // internal state vars
    state.panic_started = null;
    state.fire_started = null;
    state.aux_started = null;

    for (def i = 1; i <= sensorMap.size(); i++)
        sendEvent(name: "zoneStatus${i}", value: "", displayed: false)

    // subscribe if settings change
    subscribeNotifications()
}

def uninstalled() {
    log.trace "--- handler.uninstalled"
}

// Subscribe to the AlarmDecoder upnp event notification
// FIXME: Need to get this from the eventSubURL in the ssdpPath: /static/device_description.xml
def subscribeNotifications() {
    subscribeAction(getDataValue("urn"), "/api/v1/alarmdecoder/event?apikey=${_get_api_key()}")
}

// Parse JSON and new state. Build UI and return UI update events
def parse_json(String headers, String body) {
    log.trace("--- parse_json")

    def slurper = new JsonSlurper()
    def result = slurper.parseText(body)

    // Build our events list from our current state
    def events = []
    update_state(result).each { e-> events << e }
}

// Parse XML and new state. Build UI and return UI update events
def parse_xml(String headers, String body) {
    log.trace("--- parse_xml")

    def xmlResult = new XmlSlurper().parseText(body)

    def resultMap = [:]
    resultMap['last_message_received'] = xmlResult.property.panelstate.last_message_received.text()
    resultMap['panel_alarming'] = xmlResult.property.panelstate.panel_alarming.toBoolean()
    resultMap['panel_armed'] = xmlResult.property.panelstate.panel_armed.toBoolean()
    resultMap['panel_armed_stay'] = xmlResult.property.panelstate.panel_armed_stay.toBoolean()
    resultMap['panel_bypassed'] = xmlResult.property.panelstate.panel_bypassed.toBoolean()
    resultMap['panel_fire_detected'] = xmlResult.property.panelstate.panel_fire_detected.toBoolean()
    resultMap['panel_on_battery'] = xmlResult.property.panelstate.panel_on_battery.toBoolean()
    resultMap['panel_panicked'] = xmlResult.property.panelstate.panel_panicked.toBoolean()
    resultMap['panel_powered'] = xmlResult.property.panelstate.panel_powered.toBoolean()
    resultMap['panel_ready'] = xmlResult.property.panelstate.panel_ready.toBoolean()
    resultMap['panel_type'] = xmlResult.property.panelstate.panel_type.text()

    // build list of faulted zones unpack xml
    // only update zone list on zone change events
    // <eventmessage><![CDATA[Zone <unnamed> (9) has been faulted.]]></eventmessage>
    if (xmlResult.property.eventmessage.text().startsWith('Zone ')) {
        def zones = []
        xmlResult.property.panelstate.panel_zones_faulted.z.each { e-> zones << e.toInteger() }
        resultMap['panel_zones_faulted'] = zones
    }

    // unpack the relay xml
    def relays = []
    xmlResult.property.panelstate.panel_relay_status.r.each { e->
        relays << ['address': e.a, 'channel': e.c, 'value': e.v]
    }
    resultMap['panel_relay_status'] = relays

    // Build our events list from our current state
    def events = []
    update_state(resultMap).each { e-> events << e }

    return events
}

// parse events into attributes
def parse(String description) {
    def events = []
    def event = parseEventMessage(description)

    // If we initiate a connection it will get an id.
    // Just grab the last for loging.
    def rID = ((event.requestId && event.requestId.length() > 12) ? event.requestId.substring(event.requestId.length()-12) : "000000000000")

    log.info("---  parse: mac: ${event?.mac} requestId: ${rID}")

    // HTTP we just need headers for a valid HTTP request.
    if (event?.headers) {
        def headerString = new String(event.headers.decodeBase64())

        // body may be empty.
        def bodyString = (event?.body) ? (new String(event.body.decodeBase64())) : ""

        def type = (headerString =~ /Content-Type:.*/) ? (headerString =~ /Content-Type:.*/)[0] : null

        if (debug) {
            log.debug("---  parse: ${rID}: headers: ${headerString}")
            log.debug("---  parse: ${rID}: body: ${bodyString}")
        }

        def result

        // Based upon content type process the headers and body
        if (type?.contains("json") && bodyString.length()) {
            parse_json(headerString, bodyString).each { e-> events << e }
        } else
        if (type?.contains("xml") && bodyString.length()) {
            parse_xml(headerString, bodyString).each { e-> events << e }
        }

    }

    if (debug)
       log.debug("---  parse: ${rID}: events: ${events}")

    return events
}

/*** Capabilities ***/

def on() {
    log.trace("--- switch.on (arm stay)")
    arm_stay()
}

def off() {
    log.trace("--- switch.off (disarm)")
    disarm()
}

def strobe() {
    log.trace("--- alarm.strobe, do nothing")
}

def siren() {
    log.trace("--- alarm.siren, do nothing")
}

def lock() {
    log.trace("--- lock.lock (arm)")
    arm_away()
}

def unlock() {
    log.trace("--- lock.unlock (disarm)")
    disarm()
}

def refresh() {
    log.trace("--- handler.refresh")

    def urn = getDataValue("urn")
    def apikey = _get_api_key()

    return hub_http_get(urn, "/api/v1/alarmdecoder?apikey=${apikey}")
}

/*** Commands ***/

def disarm() {
    log.trace("--- disarm")

    def user_code = _get_user_code()
    def keys = ""

    if (settings.panel_type == "ADEMCO")
        keys = "${user_code}1"
    else if (settings.panel_type == "DSC")
        keys = "${user_code}"
    else
        log.warn("--- disarm: unknown panel_type.")

    return send_keys(keys)
}

def arm_away() {
    log.trace("--- arm_away")

    def user_code = _get_user_code()
    def keys = ""

    if (settings.panel_type == "ADEMCO")
        keys = "${user_code}2"
    else if (settings.panel_type == "DSC")
        keys = "<S5>"
    else
        log.warn("--- arm_away: unknown panel_type.")

    return send_keys(keys)
}

def arm_stay() {
    log.trace("--- arm_stay")

    def user_code = _get_user_code()
    def keys = ""

    if (settings.panel_type == "ADEMCO")
        keys = "${user_code}3"
    else if (settings.panel_type == "DSC")
        keys = "<S4>"
    else
        log.warn("--- arm_stay: unknown panel_type.")

    return send_keys(keys)
}

def fire() {
    log.trace("--- fire")
    state.fire_started = null
    def keys = "<S1>"
    return send_keys(keys)
}

def fire1() {
    state.fire_started = new Date().time

    runIn(10, checkFire);

    log.trace("Fire stage 1: ${state.fire_started}")
}

def fire2() {
    state.fire_started = new Date().time

    runIn(10, checkFire);

    log.trace("Fire stage 2: ${state.fire_started}")
}

def checkFire() {
    log.trace("checkFire");
    if (state.fire_started != null && new Date().time - state.fire_started >= 5) {
        sendEvent(name: "fire_state", value: "default", isStateChange: true);
        log.trace("clearing fire");
    }
}

def panic() {
    log.trace("--- panic")
    state.panic_started = null
    def keys = "<S2>"
    return send_keys(keys)
}

def panic1() {
    state.panic_started = new Date().time

    runIn(10, checkPanic);

    log.trace("Panic stage 1: ${state.panic_started}")
}

def panic2() {
    state.panic_started = new Date().time

    runIn(10, checkPanic);

    log.trace("Panic stage 2: ${state.panic_started}")
}

def checkPanic() {
    log.trace("checkPanic");
    if (state.panic_started != null && new Date().time - state.panic_started >= 5) {
        sendEvent(name: "panic_state", value: "default", isStateChange: true);
        log.trace("clearing panic");
    }
}

def aux() {
    log.trace("--- aux")
    state.aux_started = null
    def keys = "<S3>"
    return send_keys(keys)
}

def aux1() {
    state.aux_started = new Date().time

    runIn(10, checkAux);

    log.trace("Aux stage 1: ${state.aux_started}")
}

def aux2() {
    state.aux_started = new Date().time

    runIn(10, checkAux);

    log.trace("Aux stage 2: ${state.aux_started}")
}

def checkAux() {
    log.trace("checkAux");
    if (state.aux_started != null && new Date().time - state.aux_started >= 5) {
        sendEvent(name: "aux_state", value: "default", isStateChange: true);
        log.trace("clearing aux");
    }
}

/*** Business Logic ***/

def update_state(data) {
    log.trace("--- update_state")
    def forceguiUpdate = false
    def events = []

    /*
     * WARNING: we can get the Ready and Armed state events out of
     * order thanks to async io and the fact that both events occure
     * from a single AD2 message.Handle this gracefully to avoid client
     * update glitches.
     *
     * [10000001000100003A0-],008,[f702000f1008005c08020000000000],"****DISARMED****  Ready to Arm  "
     * [00100301000100003A0-],008,[f702000f100803cc08020000000000],"ARMED ***STAY***You may exit now"
     * [10000101000100003A0-],008,[f702000f1008015c08020000000000],"****DISARMED****  Ready to Arm  "
     */

     // Get our armed state from our new state data
    def armed = data.panel_armed || (data.panel_armed_stay != null && data.panel_armed_stay == true)

    def panel_state = (data.panel_ready ? "ready" : "notready")

    if (armed) {
        panel_state = (data.panel_armed_stay ? "armed_stay" : "armed")
    }

    //FORCE ARMED if ALARMING to be sure SHM gets it as it will not show alarms if not armed :(
    if (data.panel_alarming) {
        armed = true
        panel_state = "alarming"
    }

    // NOTE: Fire overrides alarm since it's definitely more serious.
    if (data.panel_fire_detected) {
        panel_state = "fire"
    }

    // Update our Smoke Sensor virtual device that SHM or others the current state.
    if (forceguiUpdate || data.panel_fire_detected != state.panel_fire_detected)
        events << createEvent(name: "smoke-set", value: data.panel_fire_detected ? "detected" : "clear", displayed: true, isStateChange: true)

    // If STAY/AWAY changes data.panel_armed_stay
    if (forceguiUpdate || data.panel_armed_stay != state.panel_armed_stay)
        events << createEvent(name: "lock", value: data.panel_armed_stay ? "lock" : "unlock", displayed: true, isStateChange: true)

    // If the panel ARMED state changes
    if (forceguiUpdate || armed != state.armed)
        events << createEvent(name: "armed", value: armed ? "armed" : "disarmed", displayed: true, isStateChange: true)

    // If the PANIC state changes
    if (forceguiUpdate || data.panel_alarming != state.alarming)
        events << createEvent(name: "alarm", value: data.panel_alarming ? "both" : "off", displayed: true, isStateChange: true)

    // set our panel_state
    if (forceguiUpdate || panel_state != state.panel_state)
        events << createEvent(name: "panel_state", value: panel_state, displayed: true, isStateChange: true)

    // Create an event to notify Smart Home Monitor.
    def alarm_status = "off"
    if (armed)
    {
        alarm_status = "away"
        if (data.panel_armed_stay == true)
            alarm_status = "stay"
    }

    // Update SHM
    // "enum", ["off", "stay", "away"]
    if (forceguiUpdate || alarm_status != state.alarm_status)
        events << createEvent(name: "alarmStatus", value: alarm_status, displayed: true, isStateChange: true)

    // Update our contact sensor so SHM or others know we are in an alarm state. In alarm close contact.
    // "enum", ["open", "close"]
    if (forceguiUpdate || data.panel_alarming != state.alarming)
        events << createEvent(name: "contact", value: data.panel_alarming ? "open" : "close", displayed: true, isStateChange: true)

    // will only add events for zones that change state.
    def zone_events = build_zone_events(data)
    events = events.plus(zone_events)

    // Update our saved state
    /// Calculated state enum
    state.panel_state = panel_state
    state.armed = armed

    /// raw panel state bits
    state.panel_armed = data.panel_armed
    state.panel_armed_stay = data.panel_armed_stay
    state.panel_fire_detected = data.panel_fire_detected
    state.panel_alarming = data.panel_alarming
    state.alarm_status = alarm_status
    state.panel_powered = data.panel_powered
    state.panel_on_battery = data.panel_on_battery
    state.panel_ready = data.panel_ready

    return events
}

/*** Utility ***/

private def build_zone_events(data) {
    def events = []

    // TODO: probably remove this.
    if (state.faulted_zones == null)
        state.faulted_zones = []

    //log.trace("Previous faulted zones: ${state.faulted_zones}")

    // if we have no tag then do nothing.
    def current_faults = data.panel_zones_faulted
    if (current_faults == null)
      return
      
    def number_of_zones_faulted = current_faults.size()

    def new_faults = current_faults.minus(state.faulted_zones)
    def cleared_faults = state.faulted_zones.minus(current_faults)

    //log.trace("Current faulted zones: ${temp_faultedzones}")
    //log.trace("New faults: ${new_faults}")
    //log.trace("Cleared faults: ${cleared_faults}")

    // Trigger switches for newly faulted zones.
    for (def i = 0; i < new_faults.size(); i++)
    {
        //log.trace("Setting switch ${new_faults[i]}")
        def switch_events = update_zone_switches(new_faults[i], true)
        events = events.plus(switch_events)
    }

    // Reset switches for cleared zones.
    for (def i = 0; i < cleared_faults.size(); i++)
    {
        //log.trace("Clearing switch ${cleared_faults[i]}")
        def switch_events = update_zone_switches(cleared_faults[i], false)
        events = events.plus(switch_events)
    }

    //log.trace("Filling zone tiles..")

    // Fill zone tiles
	def sensorMap = getSensorMap();									   
    for (def i = 1; i <= sensorMap.size(); i++) {
        if (number_of_zones_faulted > 0 && i <= number_of_zones_faulted) {
            if ((device.currentValue("zoneStatus${i}") ?: "0").toInteger() != current_faults[i-1])
                events << createEvent(name: "zoneStatus${i}", value: current_faults[i-1], displayed: true)
        }
        else {
            if (device.currentValue("zoneStatus${i}") != null)
                events << createEvent(name: "zoneStatus${i}", value: "", displayed: true)
        }
    }

    state.faulted_zones = current_faults

    return events
}

private def update_zone_switches(zone, faulted) {
    def events = []
def sensorMap = getSensorMap();
    // Iterate through the zone tracker settings.  If the zone number matches,
    // trigger an event for the service manager to use to flip the virtual
    // switches.
    for (def i = 1; i <= sensorMap.size(); i++) {
        if (zone == settings."zonetracker${i}zone") {
            if (faulted)
                events << createEvent(name: "zone-on", value: i, isStateChange: true, displayed: false)
            else
                events << createEvent(name: "zone-off", value: i, isStateChange: true, displayed: false)
        }
    }

    return events
}

private def parseEventMessage(String description) {
    def event = [:]
    def parts = description.split(',')

    parts.each { part ->
        part = part.trim()
        if (part.startsWith('devicetype:')) {
            def valueString = part.split(":")[1].trim()
            event.devicetype = valueString
        }
        else if (part.startsWith('mac:')) {
            def valueString = part.split(":")[1].trim()
            if (valueString) {
                event.mac = valueString
            }
        }
        // If we made the request we will get the requestId of the host we contacted.
        // If we did not provide one in HubAction() then it will be auto generated
        // ex. c089d06f-ba3c-4baa-a1a4-950b9ffd372a
        else if (part.startsWith('requestId:')) {
            part -= "requestId:"
            def valueString = part.trim()
            if (valueString) {
                event.requestId = valueString
            }
        }
        // If we made the request we will get the IP of the host we contacted.
        else if (part.startsWith('ip:')) {
            part -= "ip:"
            def valueString = part.trim()
            if (valueString) {
                event.ip = valueString
            }
        }
        // If we made the request we will get the PORT of the host we contacted.
        else if (part.startsWith('port:')) {
            part -= "port:"
            def valueString = part.trim()
            if (valueString) {
                event.port = valueString
            }
        }
        else if (part.startsWith('networkAddress:')) {
            def valueString = part.split(":")[1].trim()
            if (valueString) {
                event.ip = valueString
            }
        }
        else if (part.startsWith('deviceAddress:')) {
            def valueString = part.split(":")[1].trim()
            if (valueString) {
                event.port = valueString
            }
        }
        else if (part.startsWith('ssdpPath:')) {
            part -= "ssdpPath:"
            def valueString = part.trim()
            if (valueString) {
                event.ssdpPath = valueString
            }
        }
        else if (part.startsWith('ssdpUSN:')) {
            part -= "ssdpUSN:"
            def valueString = part.trim()
            if (valueString) {
                event.ssdpUSN = valueString
            }
        }
        else if (part.startsWith('ssdpTerm:')) {
            part -= "ssdpTerm:"
            def valueString = part.trim()
            if (valueString) {
                event.ssdpTerm = valueString
            }
        }
        else if (part.startsWith('headers:')) {
            part -= "headers:"
            def valueString = part.trim()
            if (valueString) {
                event.headers = valueString
            }
        }
        else if (part.startsWith('body:')) {
            part -= "body:"
            def valueString = part.trim()
            if (valueString) {
                event.body = valueString
            }
        }
    }

    event
}

def send_keys(keys) {
    log.trace("--- send_keys: keys=${keys}")

    def urn = getDataValue("urn")
    def apikey = _get_api_key()

    return hub_http_post(urn, "/api/v1/alarmdecoder/send?apikey=${apikey}", """{ "keys": "${keys}" }""")
}

def hub_http_get(host, path) {
    log.trace "--- hub_http_get: host=${host}, path=${path}"

    def httpRequest = [
        method:     "GET",
        path:       path,
        headers:    [ HOST: host ]
    ]

    return new physicalgraph.device.HubAction(httpRequest, "${host}")
}

def hub_http_post(host, path, body) {
    log.trace "--- hub_http_post: host=${host}, path=${path}"

    def httpRequest = [
        method:     "POST",
        path:       path,
        headers:    [ HOST: host, "Content-Type": "application/json" ],
        body:       body
    ]

    return new physicalgraph.device.HubAction(httpRequest, "${host}")
}

def _get_user_code() {
    def user_code = settings.user_code

    return user_code
}

def _get_api_key() {
    def api_key = settings.api_key

    return api_key
}

/**
 * Subscribe to an AlarmDecoder for event PUSH notifications.
 *
 * Let the AlarmDecoder know you want to be notified of events for a given path.
 *
 */
def subscribeAction(urn, path, callbackPath="") {
    log.trace "subscribeAction(${urn}, ${path}, ${callbackPath})"

    // get our HUBs details so the AlarmDecoder knows how to call us back on events
    def address = getCallBackAddress()

    def result = new physicalgraph.device.HubAction(
        method: "SUBSCRIBE",
        path: path,
        headers: [
            HOST: urn,
            CALLBACK: "<http://${address}/notify$callbackPath>",
            NT: "upnp:event",
            TIMEOUT: "Second-28800"
        ]
    )

    // We get requestId back in parse() so we know what it is.
    result.requestId = "SUBSCRIBE"

    // log.debug "SUBSCRIBE result: ${result}"

    return result

}


/**
 * gets the address of the hub
 */
def getCallBackAddress() {
    return device.hub.getDataValue("localIP") + ":" + device.hub.getDataValue("localSrvPortTCP")
}