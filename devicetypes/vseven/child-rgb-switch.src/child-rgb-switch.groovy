/**
 *  Child RGB Switch
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
 *  Change History:
 *
 *    Date        Who            What
 *    ----        ---            ----
 *    2017-09-24  Allan (vseven) Original Creation (based on Dan Ogorchock's child dimmer switch)
 * 
 */

// for the UI
metadata {
	definition (name: "Child RGB Switch", namespace: "vseven", author: "Alan (vseven) - based on code by Dan Ogorchock") {
	capability "Switch Level"
	capability "Switch"
	capability "Relay Switch"
	capability "Actuator"
	capability "Sensor"

	capability "Color Control"

	command "generateEvent", ["string", "string"]
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles (scale: 2){
		multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "on", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
				attributeState "off", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
				attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
				attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
			}
			tileAttribute ("device.level", key: "SLIDER_CONTROL") {
				attributeState "level", action:"switch level.setLevel", range:"(0..100)"
            		}
			tileAttribute ("device.color", key: "COLOR_CONTROL") {
				attributeState "color", action:"color control.setColor"
			}
		}
		
		valueTile("level", "device.level", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "level", label:'${currentValue} %', unit:"%", backgroundColor:"#ffffff"
		}
 		valueTile("lastUpdated", "device.lastUpdated", inactiveLabel: false, decoration: "flat", width: 4, height: 2) {
    			state "default", label:'Last Updated ${currentValue}', backgroundColor:"#ffffff"
		}

		main(["switch"])
		details(["switch", "level", "lastUpdated"])
	}
}


void on() {
	parent.childOn(device.deviceNetworkId)
}

void off() {
	parent.childOff(device.deviceNetworkId)
}

def setLevel(value) {
	log.debug "setLevel >> value: $value"
	def valueaux = value as Integer
	def level = Math.max(Math.min(valueaux, 99), 0)
	if (level > 0) {
		sendEvent(name: "switch", value: "on")
	} else {
		sendEvent(name: "switch", value: "off")
	}
	sendEvent(name: "level", value: level, unit: "%")
    
    parent.childSetLevel(device.deviceNetworkId, level)
}

def setColor(value) {
	def result = []
	log.debug "setColor: ${value}"
	if (value.hex) {
		def c = value.hex.findAll(/[0-9a-fA-F]{2}/).collect { Integer.parseInt(it, 16) }
		result << zwave.switchColorV3.switchColorSet(red:c[0], green:c[1], blue:c[2], warmWhite:0, coldWhite:0)
	} else {
		def hue = value.hue ?: device.currentValue("hue")
		def saturation = value.saturation ?: device.currentValue("saturation")
		if(hue == null) hue = 13
		if(saturation == null) saturation = 13
		def rgb = huesatToRGB(hue, saturation)
		result << zwave.switchColorV3.switchColorSet(red: rgb[0], green: rgb[1], blue: rgb[2], warmWhite:0, coldWhite:0)
	}

	if(value.hue) sendEvent(name: "hue", value: value.hue)
	if(value.hex) sendEvent(name: "color", value: value.hex)
	if(value.switch) sendEvent(name: "switch", value: value.switch)
	if(value.saturation) sendEvent(name: "saturation", value: value.saturation)

	commands(result)
}

def generateEvent(String name, String value) {
	//log.debug("Passed values to routine generateEvent in device named $device: Name - $name  -  Value - $value")
  // The name coming in from ST_Anything will be "dimmerSwitch", but we want to the ST standard "switch" attribute for compatibility with normal SmartApps
	sendEvent(name: "switch", value: value)
  // Update lastUpdated date and time
  def nowDay = new Date().format("MMM dd", location.timeZone)
  def nowTime = new Date().format("h:mm a", location.timeZone)
  sendEvent(name: "lastUpdated", value: nowDay + " at " + nowTime, displayed: false)
}

