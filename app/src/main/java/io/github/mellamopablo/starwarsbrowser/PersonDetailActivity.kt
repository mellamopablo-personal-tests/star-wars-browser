package io.github.mellamopablo.starwarsbrowser

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class PersonDetailActivity : AppCompatActivity() {

	private lateinit var nameText: TextView
	private lateinit var heightText: TextView
	private lateinit var massText: TextView
	private lateinit var genderText: TextView

	@SuppressLint("SetTextI18n")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_person_detail)

		nameText = findViewById(R.id.person_detail_name)
		heightText = findViewById(R.id.person_detail_height)
		massText = findViewById(R.id.person_detail_mass)
		genderText = findViewById(R.id.person_detail_gender)

		if (intent.hasExtra("personId")) {
			SwapiClient
				.getPerson(intent.getIntExtra("personId", 1))
				.subscribe(
					{
						nameText.text = "Nombre: ${it.name}"
						heightText.text  = "Altura: ${it.height} cm"
						massText.text  = "Masa: ${it.mass} kg"
						genderText.text  = "Género: ${it.gender}"
					},
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
}
