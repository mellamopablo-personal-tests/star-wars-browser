package io.github.mellamopablo.starwarsbrowser

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import io.github.mellamopablo.starwarsbrowser.model.Person

class MainActivity : AppCompatActivity() {

	private lateinit var peopleListView: ListView
	private lateinit var adapter: ArrayAdapter<Person>
	private val people = mutableListOf<Person>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		peopleListView = findViewById(R.id.people_list)

		adapter = ArrayAdapter(
			this,
			R.layout.list_item_person,
			R.id.person_name,
			people
		)

		peopleListView.adapter = adapter
		peopleListView.setOnItemClickListener { _, _, position, _ ->
			val intent = Intent(
				this,
				PersonDetailActivity::class.java
			)
			intent.putExtra("personId", people[position].id)
			startActivity(intent)
		}

		SwapiClient.getAllPeople()
			.subscribe(
				{ adapter.add(it) },
				{
					Toast.makeText(
						this,
						"¡La gran petada!",
						Toast.LENGTH_SHORT
					).show()
					println(it)
				}
			)
	}

}
