package com.rtcomps.data;

import java.io.IOException;
import java.util.Properties;

public class ActionsConfig {

	private static final Properties properties = new Properties();
	static {
		try {
			properties.load(ActionsConfig.class.getResourceAsStream("/DataConfig.properties"));
		} catch (IOException e) {
			throw new RuntimeException("Error loading actions config.", e);
		}
	}
	
	/**
	 * Return a string property. Throws an exception if the property does not exist.
	 */
	public static String getString(String key) {
		return get(key);
	}
	
	/**
	 * Return an integer property. Throws an exception if the property does not exist
	 * or the value is not an integer. 
	 */
	public static int getInt(String key) {
		return Integer.valueOf(get(key));
	}
	
	private static String get(String key) {
		String value = properties.getProperty(key);
		if (value == null) {
			throw new RuntimeException("Missing action config property: " + key);
		}
		return value;
	}
	
	public static int getIntOrDefault(String key, int defaultValue) {
		if (isSet(key)) {
			String value = get(key);
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				throw new RuntimeException(String.format("Expected an integer for property '%s' but was '%s'. Returning default value '%d'.", key, value, defaultValue));
			}
		}
		
		return defaultValue;
	}
	
	private static boolean isSet(String key) {
		return properties.getProperty(key) != null;
	}
	
	
	/** Override a property value for testing. */
	public static void override(String key, String value) {
		properties.setProperty(key, value);
	}
	
	/** remove a property for testing. */
	public static void remove(String key) {
		properties.remove(key);
	}
	
}
