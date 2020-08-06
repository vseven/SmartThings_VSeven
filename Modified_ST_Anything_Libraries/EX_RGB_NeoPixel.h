//******************************************************************************************
//  File: EX_RGB_NeoPixel.h
//  Authors: Allan (vseven) based on EX_Switch_Dim by Dan G Ogorchock
//
//  Summary:  EX_RGB_NeoPixel is a class which implements the SmartThings "Color Control", "Switch", and "Switch Level" device capabilities.
//			  It inherits from the st::Executor class.
//
//			  Create an instance of this class in your sketch's global variable section
//			  For Example:  st::EX_RGB_NeoPixel executor1("RGB_AddressableSwitch1", PIN_RGB, PIXEL_COUNT);
//
//			  st::EX_RGB_NeoPixel() constructor requires the following arguments
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
#ifndef ST_EX_RGB_NeoPixel
#define ST_EX_RGB_NeoPixel

#include "Executor.h"

#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
  #include <avr/power.h>
#endif

namespace st
{
	class EX_RGB_NeoPixel: public Executor
	{
		private:
			bool m_bCurrentState;			//HIGH or LOW
			byte m_nPinRGB;				//Arduino Pin used as a Digital Output for the RGB strip
			byte m_nPixelCount;			//Number of programmable pixels in the RGB strip
			String m_sCurrentHEX;			//HEX value of color currently set

			static Adafruit_NeoPixel m_nStrip;	//NeoPixel library object

			void writeCommandToOutput();		//function to update the Arduino Digital Output Pin

		public:
			//constructor - called in your sketch's global variable declaration section
			EX_RGB_NeoPixel(const __FlashStringHelper *name, byte pinRGB, byte pixelCount);
			
			//destructor
			virtual ~EX_RGB_NeoPixel();

			//initialization routine
			virtual void init();

			//SmartThings Shield data handler (receives command to turn "on" or "off" the switch along with HEX value for LEDs)
			virtual void beSmart(const String &str);
			
			//called periodically to ensure state of the switch is up to date in the SmartThings Cloud (in case an event is missed)
			virtual void refresh();
			
			//gets
			inline byte getRGBPin() const {return m_nPinRGB;}
			inline byte getPixelCount() const {return m_nPixelCount;}

			virtual bool getStatus() const { return m_bCurrentState; } //whether the switch is HIGH or LOW
			virtual String getHEX() const { return m_sCurrentHEX; }	// color value in HEX

			//sets
			virtual void setupPixels(byte pin, byte count);
	};
}

#endif
