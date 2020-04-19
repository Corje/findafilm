package ru.otus.danilov.findafilm

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), BackDialogFragment.BackDialogListener {

    private lateinit var snatchTextView: TextView
    private lateinit var lockStockTextView: TextView
    private lateinit var rocknrollaTextView: TextView
    private lateinit var snatchButton: Button
    private lateinit var lockStockButton: Button
    private lateinit var rocknrollaButton: Button
    private lateinit var textViewsList: List<TextView>

    private val filmViewModel: FilmViewModel by lazy {
        ViewModelProvider(this).get(FilmViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        snatchTextView = findViewById(R.id.snatch_text_view)
        lockStockTextView = findViewById(R.id.lock_stock_text_view)
        rocknrollaTextView = findViewById(R.id.rocknrolla_text_view)
        snatchButton = findViewById(R.id.snatch_button)
        lockStockButton = findViewById(R.id.lock_stock_button)
        rocknrollaButton = findViewById(R.id.rocknrolla_button)
        textViewsList = listOf(snatchTextView, lockStockTextView, rocknrollaTextView)

        fun setListener(textView: TextView, film: Film) {
            filmViewModel.selectedViewIndex?.let { textViewsList[it].setBackgroundColor(Color.TRANSPARENT) }
            textView.setBackgroundResource(R.color.selected_color)
            filmViewModel.selectedViewIndex = textViewsList.indexOf(textView)

            Intent(this, ConcreteFilmActivity::class.java).apply {
                putExtra("film", film)
                startActivityForResult(this, REQUEST_CODE)
            }
        }

        snatchButton.setOnClickListener {
            setListener(snatchTextView, SNATCH)
        }

        lockStockButton.setOnClickListener {
            setListener(lockStockTextView, LOCK_STOCK)
        }

        rocknrollaButton.setOnClickListener {
            setListener(rocknrollaTextView, ROCKNROLLA)
        }

        savedInstanceState?.let {
            if (it.containsKey(SELECTED_VIEW_INDEX)) {
                filmViewModel.selectedViewIndex = savedInstanceState.getInt(SELECTED_VIEW_INDEX)
            }
            filmViewModel.selectedViewIndex?.let { textViewsList[it].setBackgroundResource(R.color.selected_color) }
        }

        //AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuItem = menu.findItem(R.id.action_night_mode)
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            menuItem.isChecked = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_night_mode -> {
                if (item.isChecked) {
                    item.isChecked = false
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                    true
                } else {
                    item.isChecked = true
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                    true
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        filmViewModel.selectedViewIndex?.let { outState.putInt(SELECTED_VIEW_INDEX, it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var isLiked: Boolean? = null
        var comment: String? = null
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    isLiked = it.getBooleanExtra(LIKE_VALUE, false)
                    comment = it.getStringExtra(COMMENT_VALUE)
                }
            }
        }

        Log.i(MY_TAG, "checkbox value is $isLiked")
        Log.i(MY_TAG, if (comment.isNullOrEmpty()) "no comment" else "comment value is $comment")
    }

    override fun onBackPressed() {
        BackDialogFragment().show(supportFragmentManager, "DialogTag")
    }

    override fun onDialogPositiveClick() {
        super.onBackPressed()
    }

    companion object {
        val SNATCH = Film(R.drawable.snatch, R.string.snatch_description, R.string.snatch)
        val LOCK_STOCK = Film(
            R.drawable.lock_stock_and_two_smoking_barrels,
            R.string.lock_stock_description,
            R.string.lock_stock_and_two_smoking_barrels
        )
        val ROCKNROLLA =
            Film(R.drawable.rocknrolla, R.string.rocknrolla_description, R.string.rocknrolla)

        const val MY_TAG = "myTag"
        const val SELECTED_VIEW_INDEX = "selectedViewIndex"
        const val LIKE_VALUE = "likeValue"
        const val COMMENT_VALUE = "commentValue"
        const val REQUEST_CODE = 1
    }

}
