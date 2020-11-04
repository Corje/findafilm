package ru.alex.danilov.findafilm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import ru.alex.danilov.findafilm.MainActivity.Companion.COMMENT_VALUE
import ru.alex.danilov.findafilm.MainActivity.Companion.LIKE_VALUE

class ConcreteFilmActivity : AppCompatActivity() {

    private lateinit var image: ImageView
    private lateinit var description: TextView
    private lateinit var shareButton: Button
    private lateinit var likeCheckBox: CheckBox
    private lateinit var commentField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concrete_film)

        image = findViewById(R.id.selected_film_image)
        description = findViewById(R.id.selected_film_text_view)
        shareButton = findViewById(R.id.share_button)
        likeCheckBox = findViewById(R.id.like_checkbox)
        commentField = findViewById(R.id.comment_field)


        val film = intent.getParcelableExtra<Film>("film")
        film?.let {
            image.setImageResource(it.drawableId)
            description.setText(it.descriptionId)
        }

        shareButton.setOnClickListener {
            val shareIntent = Intent().apply {
                val likedFilm = if (film != null) resources.getString(film.titleId) else ""
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "I liked the film $likedFilm")
                type = "text/plain"
            }
            val chooser = Intent.createChooser(shareIntent, getString(R.string.share))
            shareIntent.resolveActivity(packageManager)?.let {
                startActivity(chooser)
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(LIKE_VALUE, likeCheckBox.isChecked)
            putExtra(COMMENT_VALUE, commentField.text.toString())
        })
        super.onBackPressed()
    }

}
