package io.github.mellamopablo.starwarsbrowser.extensions

import org.json.JSONArray
import org.json.JSONObject

/**
 * Converts a JSONArray to a List<JSONObject>. The array must be composed of
 * only JSONObjects or otherwise this will throw an exception.
 */
fun JSONArray.toJsonObjectList(): List<JSONObject> = if (this.length() > 0) {
	(0 until this.length()).map { this.getJSONObject(it) }
} else {
	emptyList()
}
