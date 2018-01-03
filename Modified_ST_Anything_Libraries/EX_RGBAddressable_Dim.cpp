//******************************************************************************************
//  File: EX_RGBAddressable_Dim.h
//  Authors: Allan (vseven) based on EX_Switch_Dim by Dan G Ogorchock
//
//  Summary:  EX_RGBAddressable_Dim is a class which implements the SmartThings "Color Control", "Switch", and "Switch Level" device capabilities.
//			  It inherits from the st::Executor class.
//
//			  Create an instance of this class in your sketch's global variable section
//			  For Example:  st::EX_RGBAddressable_Dim executor1("rgbAddressableSwitch1", PIN_RGB, PIXEL_COUNT);
//
//			  st::EX_RGBAddressable_Dim() constructor requires the following arguments
//				- String &name - REQUIRED - the name of the object - must match the Groovy ST_Anything DeviceType tile name.
//				- byte pin - REQUIRED - the Arduino Pin to be used as a digital output for the RGB strip
//				- byte count - REQUIRED - the number of programmable pixels in the RGB strip
//
//  Change History:
//
//    Date        Who            What
//    ----        ---            ----
//    2016-04-30  Dan Ogorchock  Original Creation
//    2018-08-14  Dan Ogorchock  Modified to avoid compiler errors on ESP32 since it currently does not support "analogWrite()"
//    2017-08-30  Dan Ogorchock  Modified comment section above to comply with new Parent/Child Device Handler requirements
//    2017-10-06  Allan (vseven) Modified original code from EX_Switch_Dim to be used for RGB lighting
//    2017-12-27  Allan (vseven) Modified the EX_RGB_Dim for use with addressable (NeoPixel) LED strips
//
//******************************************************************************************
#include "EX_RGBAddressable_Dim.h"

#include "Constants.h"
#include "Everything.h"

#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
  #include <avr/power.h>
#endif


namespace st
{

//private
	void EX_RGBAddressable_Dim::writeCommandToOutput()
	{
		int subStringR;
		int subStringG;
		int subStringB;

		if (st::Executor::debug) {
			Serial.println("m_bCurrentState: " + String(m_bCurrentState));
			Serial.println("m_sCurrentHEX: " + String(m_sCurrentHEX));
		}


		if (m_bCurrentState == HIGH){
			// Our status is on so get the RGB value from the hex
			String hexstring = m_sCurrentHEX;
			long number = (long) strtol( &hexstring[1], NULL, 16);
      		// Split them up into r, g, b values
      		subStringR = number >> 16;
      		subStringG = number >> 8 & 0xFF;
      		subStringB = number & 0xFF;
		} else {
			// Status is off so turn off LED
			subStringR = 00;
      		subStringG = 00;
      		subStringB = 00;
		}
		

		// Any adjustments to the colors can be done here before sending the commands.  For example if red is always too bright reduce it:
		// subStringR = subStringR * 0.95

		// Write to our output
		uint16_t n = m_nStrip.numPixels();
		uint16_t i;

		if (st::Executor::debug) {
			Serial.print(F("subString R:G:B = "));
			Serial.println(String(subStringR) + ":" + String(subStringG) + ":" + String(subStringB));
			Serial.println("number of pixels in the strip: " + String(n,DEC) );
		}


		// set all pixels to the same color

		for (i = 0; i < n; i++){
			if (st::Executor::debug) {
				Serial.println("Writing to pixel " + String(i) + " a color of " + String(subStringR) + "," + String(subStringG) + "," + String(subStringB));
			}
			m_nStrip.setPixelColor(i, subStringR, subStringG, subStringB);
		}

		if (st::Executor::debug) {
			Serial.println(F("All pixels set.  Sending show command."));
		}

		// then display it
		m_nStrip.show();

	}

//public
	//constructor
	EX_RGBAddressable_Dim::EX_RGBAddressable_Dim(const __FlashStringHelper *name, byte pinRGB, byte count):
		Executor(name)
	{
		setupPixels(pinRGB,count);
	}

	//destructor
	EX_RGBAddressable_Dim::~EX_RGBAddressable_Dim()
	{
	
	}
	
	void EX_RGBAddressable_Dim::init()
	{
		Everything::sendSmartString(getName() + " " + (m_bCurrentState == HIGH ? F("on") : F("off")));
		
		if (st::Executor::debug) {
			Serial.println("setupPixels called.  Pin number: " + String(m_nPinRGB, DEC) + ". Pixel count: " + String(m_nPixelCount, DEC) + ".");
		}

		// Parameter 1 = number of pixels in strip
		// Parameter 2 = Arduino pin number (most are valid)
		// Parameter 3 = pixel type flags, add together as needed:
		//   NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
		//   NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
		//   NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
		//   NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
		//   NEO_RGBW    Pixels are wired for RGBW bitstream (NeoPixel RGBW products)


		m_nStrip= Adafruit_NeoPixel(m_nPixelCount, m_nPinRGB, NEO_GRB + NEO_KHZ800);

  		m_nStrip.begin();
  		m_nStrip.show(); // Initialize all pixels to 'off'

	}

	void EX_RGBAddressable_Dim::beSmart(const String &str)
	{
		String s=str.substring(str.indexOf(' ')+1);
		if (st::Executor::debug) {
			Serial.print(F("EX_RGBAddressable_Dim::beSmart s = "));
			Serial.println(s);
		}
		if(s==F("on"))
		{
			m_bCurrentState=HIGH;
		}
		else if(s==F("off"))
		{
			m_bCurrentState=LOW;
		}
		else //must be a set color command
		{
			s.trim();
			m_sCurrentHEX = s;
		}

		writeCommandToOutput();

		Everything::sendSmartString(getName() + " " + (m_bCurrentState == HIGH?F("on"):F("off")));
	}
	
	void EX_RGBAddressable_Dim::refresh()
	{
		Everything::sendSmartString(getName() + " " + (m_bCurrentState == HIGH?F("on"):F("off")));
	}
	
	void EX_RGBAddressable_Dim::setupPixels(byte pin, byte count)
	{
		m_nPinRGB=pin;
		m_nPixelCount=count;

	}

	//initialize static members
	Adafruit_NeoPixel EX_RGBAddressable_Dim::m_nStrip;  // NeoPixel library object

}
