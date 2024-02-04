package com.kazurayam.ks

import java.nio.file.Path

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

public class JsonPrettyPrinter {

	/**
	 * just pretty-print a JSON
	 * 
	 * @param json
	 * @return
	 */
	static String prettyPrint(String json) {
		return JsonOutput.prettyPrint(json)
	}

	static String prettyPrint(File json) {
		return prettyPrint(json.text)
	}

	static String prettyPrint(Path json) {
		return prettyPrint(json.toFile().text)
	}

	/**
	 * pretty-print a JSON while ordering the Map entries ("key":"value" pairs) 
	 * by alphabetical order of the keys
	 * 
	 * @param json
	 * @return
	 */
	static String orderMapEntriesByKeys(String json) {
		Objects.requireNonNull(json)
		JsonSlurper slurper = new JsonSlurper()
		def parsed = slurper.parseText(json)
		// serialize an Object
		ObjectMapper mapper = new ObjectMapper()
		mapper.setConfig(mapper.getSerializationConfig()
				.with(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS))
		String prettified = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parsed)
		assert prettified != null
		return prettified
	}

	static String orderMapEntriesByKeys(File file) {
		return orderMapEntriesByKeys(file.text)
	}

	static String orderMapEntriesByKeys(Path file) {
		return orderMapEntriesByKeys(file.toFile())
	}
}
