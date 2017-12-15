package io.github.mellamopablo.starwarsbrowser

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import io.github.mellamopablo.starwarsbrowser.extensions.toJsonObjectList
import io.github.mellamopablo.starwarsbrowser.model.Person
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.json.JSONObject

object SwapiClient {

	private val BASE_URL = "https://swapi.co/api"
	private val client = AsyncHttpClient()

	fun getPerson(id: Int): Observable<Person> {
		val subject = PublishSubject.create<Person>()

		get("people/$id") { response, error ->
			if (error == null) {
				subject.onNext(Person(
					id,
					response.getString("name"),
					response.getString("height").toInt(),
					response.getString("mass").toInt(),
					response.getString("gender").toString()
				))
			} else {
				subject.onError(error)
			}

			subject.onComplete()
		}

		return subject.share()
	}

	fun getAllPeople(): Observable<Person> {
		val subject = PublishSubject.create<Person>()

		get("people") { response, error ->
			if (error == null) {
				response
					.getJSONArray("results")
					.toJsonObjectList()
					.map {
						Person(
							personUrlToId(it.getString("url")),
							it.getString("name"),
							it.getString("height").toInt(),
							it.getString("mass").toInt(),
							it.getString("gender").toString()
						)
					}
					.forEach(subject::onNext)
			} else {
				subject.onError(error)
			}

			subject.onComplete()
		}

		return subject.share()
	}

	private fun personUrlToId(url: String): Int =
		"""https://swapi\.co/api/people/([0-9]+)/"""
			.toRegex().matchEntire(url)!!.groups[1]!!.value.toInt()


	private fun get(
		relativePath: String,
		onResponse: (response: JSONObject, error: Throwable?) -> Unit
	) {
		client.get("$BASE_URL/$relativePath", object : AsyncHttpResponseHandler() {
			override fun onSuccess(
				statusCode: Int,
				headers: Array<out Header>?,
				responseBody: ByteArray?
			) {
				onResponse(
					JSONObject(responseBody?.let { String(it) } ?: "{}"),
					null
				)
			}

			override fun onFailure(
				statusCode: Int,
				headers: Array<out Header>?,
				responseBody: ByteArray?,
				error: Throwable?
			) {
				onResponse(
					JSONObject(responseBody?.let { String(it) } ?: "{}"),
					error ?: RuntimeException(
						"Fallo al hacer una petición a $relativePath"
					)
				)
			}
		})
	}

}