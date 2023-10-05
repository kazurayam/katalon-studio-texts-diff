package com.kazurayam.ks

import java.util.TreeMap
import com.google.gson.Gson
import groovy.json.JsonSlurper
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

public class JsonConverter {

	static String sort(String json) {
		Objects.requireNonNull(json)
		JsonSlurper slurper = new JsonSlurper()
		def parsed = slurper.parseText(json)
		// serialize an Object
		ObjectMapper mapper = new ObjectMapper()
		mapper.setConfig(mapper.getSerializationConfig().with(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS))

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parsed)
	}
}
