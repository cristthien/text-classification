package com.example.text_classification

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.widget.TextView
import android.widget.Button
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.text.textclassifier.TextClassifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private val currentModel = "average_word_classifier.tflite"
    private val baseOptionsBuilder = BaseOptions.builder()
        .setModelAssetPath(currentModel)
    private val baseOptions = baseOptionsBuilder.build()
    private val optionsBuilder = TextClassifier.TextClassifierOptions.builder()
        .setBaseOptions(baseOptions)
    private val options = optionsBuilder.build()

    private var textClassifier: TextClassifier? = null

    private lateinit var textView: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textView = findViewById(R.id.textview)
        button = findViewById(R.id.btn)

        textClassifier = TextClassifier.createFromOptions(this, options)

        button.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val results = textClassifier?.classify("shutup")
                    withContext(Dispatchers.Main) {
                        val data = results?.classificationResult()?.classifications()
                        if (data?.isNotEmpty() == true) {
                            val categories = data[0].categories()
                            if (categories.isNotEmpty()) {
                                val name = "${categories[0].categoryName()} ${categories[0].displayName()}"
                                println(name)
                                textView.text = name
                            }
                        }
                    }
                }
            }
        }
    }

}