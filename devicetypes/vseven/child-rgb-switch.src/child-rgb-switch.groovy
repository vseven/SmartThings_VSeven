/**
 *  Child RGBLED Switch
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
 *    2017-10-01  Allan (vseven) Original Creation (based on Dan Ogorchock's child dimmer switch)
 * 
 */

// for the UI
metadata {
	definition (name: "Child RGB Switch", namespace: "vseven", author: "Alan (vseven) - based on code by Dan Ogorchock") {
	capability "Switch"		
	capability "Switch Level"
	capability "Actuator"
	capability "Color Control"
	capability "Sensor"

	command "generateEvent", ["string", "string"]
	command "softwhite"
        command "daylight"
        command "warmwhite"
        command "red"
        command "green"
        command "blue"
        command "cyan"
        command "magenta"
        command "orange"
        command "purple"
        command "yellow"
	command "white"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles (scale: 2){
		multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "on", label:'${name}', action:"switch.off", icon:"st.illuminance.illuminance.bright", backgroundColor:"#00A0DC", nextState:"turningOff"
				attributeState "off", label:'${name}', action:"switch.on", icon:"st.illuminance.illuminance.dark", backgroundColor:"#ffffff", nextState:"turningOn"
				attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.illuminance.illuminance.light", backgroundColor:"#00A0DC", nextState:"turningOff"
				attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.illuminance.illuminance.light", backgroundColor:"#ffffff", nextState:"turningOn"
			}    
			tileAttribute ("device.level", key: "SLIDER_CONTROL") {
        			attributeState "level", action:"switch level.setLevel"
    			}
			tileAttribute ("device.color", key: "COLOR_CONTROL") {
				attributeState "color", action:"color control.setColor"
			}
		}
 		valueTile("lastUpdated", "device.lastUpdated", inactiveLabel: false, decoration: "flat", width: 6, height: 2) {
    			state "default", label:'Last Updated ${currentValue}', backgroundColor:"#ffffff"
		}
		standardTile("softwhite", "device.softwhite", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offsoftwhite", label:"soft white", action:"softwhite", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onsoftwhite", label:"soft white", action:"softwhite", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FFF1E0"
		}
		standardTile("daylight", "device.daylight", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offdaylight", label:"daylight", action:"daylight", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "ondaylight", label:"daylight", action:"daylight", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FFFFFB"
		}
		standardTile("warmwhite", "device.warmwhite", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offwarmwhite", label:"warm white", action:"warmwhite", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onwarmwhite", label:"warm white", action:"warmwhite", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FFF4E5"
		}
		standardTile("red", "device.red", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offred", label:"red", action:"red", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onred", label:"red", action:"red", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FF0000"
		}
		standardTile("green", "device.green", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offgreen", label:"green", action:"green", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "ongreen", label:"green", action:"green", icon:"st.illuminance.illuminance.bright", backgroundColor:"#00FF00"
		}
		standardTile("blue", "device.blue", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offblue", label:"blue", action:"blue", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onblue", label:"blue", action:"blue", icon:"st.illuminance.illuminance.bright", backgroundColor:"#0000FF"
		}
		standardTile("cyan", "device.cyan", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offcyan", label:"cyan", action:"cyan", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "oncyan", label:"cyan", action:"cyan", icon:"st.illuminance.illuminance.bright", backgroundColor:"#00FFFF"
		}
		standardTile("magenta", "device.magenta", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offmagenta", label:"magenta", action:"magenta", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onmagenta", label:"magenta", action:"magenta", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FF00FF"
		}
		standardTile("orange", "device.orange", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offorange", label:"orange", action:"orange", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onorange", label:"orange", action:"orange", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FF6600"
		}
		standardTile("purple", "device.purple", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offpurple", label:"purple", action:"purple", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onpurple", label:"purple", action:"purple", icon:"st.illuminance.illuminance.bright", backgroundColor:"#BF00FF"
		}
		standardTile("yellow", "device.yellow", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offyellow", label:"yellow", action:"yellow", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onyellow", label:"yellow", action:"yellow", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FFFF00"
		}
		standardTile("white", "device.white", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
		    state "offwhite", label:"White", action:"white", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8"
		    state "onwhite", label:"White", action:"white", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FFFFFF"
		}
		main(["switch"])
		details(["switch", "level", "color", "softwhite","daylight","warmwhite","red","green","blue","white","cyan",
			 "magenta","orange","purple","yellow","lastUpdated"])
	}
}


void on() {
	def lastColor = device.latestValue("color.hex")
	//log.debug("On pressed.  Turning status on and sending last known HEX value of $lastColor")
	sendEvent(name: "switch", value: "on")
	//parent.childOn(device.deviceNetworkId)
	// Send the last hex value to turn back on
	//parent.childSetColor(device.deviceNetworkId, lastColor)
}

void off() {
	toggleTiles("off")
	//log.debug("Off pressed.  Sending HEX of #000000 but not updating device (retain last set color).")
	sendEvent(name: "switch", value: "off")
	//parent.childOff(device.deviceNetworkId)
	// Send a all 0 hex value to turn off the LED
	//parent.childSetColor(device.deviceNetworkId, "#000000")
}

def setColor(value) {
    toggleTiles("off") //turn off the hard color tiles
    //log.debug("Color value: $value")
    // Update our color and then just call the set level with the current level
    sendEvent(name: "color", value: value)
    def lastLevel = device.latestValue("level")
    if (lastLevel == null) {lastLevel = 50}
    setLevel(lastLevel)
}

def setColorFromButtons(value) {
    //log.debug("Color value: $value")
    // Update our color and then just call the set level with the current level
    sendEvent(name: "color", value: value)
    def lastLevel = device.latestValue("level")
    if (lastLevel == null) {lastLevel = 50}
    setLevel(lastLevel)
}

def setLevel(level) {
    //log.debug("Level value in percentage: $level")
    sendEvent(name: "level", value: level)
    //parent.childSetLevel(device.deviceNetworkId, level)
	
    // Turn on or off based on level selection
    if (level == 0) { 
	off() 
    } else if (device.latestValue("switch") == "off") {
	on()
	// Get the last known color and if null use full on
	def colorHex = device.latestValue("color.hex")
	if (colorHex == null) {colorHex = "#FFFFFF"}
	adjustColor(colorHex,level)
    }
}

def adjustColor(hex, level) {
    log.debug("adjustColor Routine.  Passed values:  Hex: $hex  Level: $level")
    // Convert the hex color, apply the level, then send to the setColor routine
    def c = hexToRgb(hex)

    def r = hex(c.r * (level/100))
    def g = hex(c.g * (level/100))
    def b = hex(c.b * (level/100))

    def adjustedColor = "#" + $r + $g + $b
    //log.debug("Adjusted color is $adjustedColor")
	
    //parent.childSetColorRGB(device.deviceNetworkId, adjustedColor)
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

def doColorButton(colorName) {
    //log.debug "doColorButton: '${colorName}()'"

    toggleTiles(colorName.toLowerCase().replaceAll("\\s",""))

    def colorButtonHEX = getColorData(colorName)
    
    setColorFromButtons(colorButtonHEX)
}

def getColorData(colorName) {
    //log.debug "getColorData: ${colorName}"

    def colorRGB = colorNameToRgb(colorName)
    def colorHex = rgbToHex(colorRGB)

    colorHex
}

private hex(value, width=2) {
	def s = new BigInteger(Math.round(value).toString()).toString(16)
	while (s.size() < width) {
		s = "0" + s
	}
	s
}

def hexToRgb(colorHex) {
    def rrInt = Integer.parseInt(colorHex.substring(1,3),16)
    def ggInt = Integer.parseInt(colorHex.substring(3,5),16)
    def bbInt = Integer.parseInt(colorHex.substring(5,7),16)

    def colorData = [:]
    colorData = [r: rrInt, g: ggInt, b: bbInt]
	
    colorData
}

def rgbToHex(rgb) {
    def r = hex(rgb.r)
    def g = hex(rgb.g)
    def b = hex(rgb.b)
	
    def hexColor = "#${r}${g}${b}"

    hexColor
}

def colorNameToRgb(color) {
	final colors = [
		[name:"Soft White",	r: 255, g: 241, b: 224	],
		[name:"Daylight", 	r: 255, g: 255, b: 251	],
		[name:"Warm White", 	r: 255, g: 244, b: 229	],

		[name:"Red", 		r: 255, g: 0,	b: 0	],
		[name:"Green", 		r: 0, 	g: 255,	b: 0	],
		[name:"Blue", 		r: 0, 	g: 0,	b: 255	],

		[name:"Cyan", 		r: 0, 	g: 255,	b: 255	],
		[name:"Magenta", 	r: 255, g: 0,	b: 33	],
		[name:"Orange", 	r: 255, g: 102, b: 0	],

		[name:"Purple", 	r: 170, g: 0,	b: 255	],
		[name:"Yellow", 	r: 255, g: 255, b: 0	],
		[name:"White", 		r: 255, g: 255, b: 255	]
	]
    def colorData = [:]
    colorData = colors.find { it.name == color }
    colorData
}

def toggleTiles(color) {
	state.colorTiles = []
	if ( !state.colorTiles ) {
    	state.colorTiles = ["softwhite","daylight","warmwhite","red","green","blue","cyan","magenta","orange","purple","yellow","white"]
    }

    def cmds = []

    state.colorTiles.each({
    	if ( it == color ) {
        	log.debug "Turning ${it} on"
            cmds << sendEvent(name: it, value: "on${it}", displayed: True, descriptionText: "${device.displayName} ${color} is 'ON'", isStateChange: true)
        } else {
        	//log.debug "Turning ${it} off"
        	cmds << sendEvent(name: it, value: "off${it}", displayed: false)
	}
    })
}

// rows of buttons
def softwhite() { doColorButton("Soft White") }
def daylight()  { doColorButton("Daylight") }
def warmwhite() { doColorButton("Warm White") }

def red() 	{ doColorButton("Red") }
def green() 	{ doColorButton("Green") }
def blue() 	{ doColorButton("Blue") }

def cyan() 	{ doColorButton("Cyan") }
def magenta()	{ doColorButton("Magenta") }
def orange() 	{ doColorButton("Orange") }

def purple()	{ doColorButton("Purple") }
def yellow() 	{ doColorButton("Yellow") }
def white() 	{ doColorButton("White") }
