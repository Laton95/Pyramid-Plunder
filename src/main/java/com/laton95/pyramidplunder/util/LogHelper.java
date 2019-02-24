package com.laton95.pyramidplunder.util;

import com.laton95.pyramidplunder.reference.ModReference;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper
{
	
	private static Logger log = LogManager.getLogger(ModReference.MOD_ID);
	
	public static void all(Object object)
	{
		log.log(Level.ALL, object);
	}
	
	public static void off(Object object)
	{
		log.log(Level.OFF, object);
	}
	
	public static void fatal(Object object)
	{
		log.log(Level.FATAL, object);
	}
	
	public static void error(Object object)
	{
		log.log(Level.ERROR, object);
	}
	
	public static void warn(Object object)
	{
		log.log(Level.WARN, object);
	}
	
	public static void info(Object object)
	{
		log.log(Level.INFO, ModReference.MOD_ID + ": " + object);
	}
	
	// Below this, these outputs do not appear in the FML log.
	public static void debug(Object object)
	{
		log.log(Level.DEBUG, object);
	}
	
	public static void trace(Object object)
	{
		log.log(Level.TRACE, object);
	}
	
}
