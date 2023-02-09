package ipca.test.musiceditor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var percentagemValue = findViewById<TextView>(R.id.textView6)

        var customView = findViewById<VideoEditorView>(R.id.videoEditorView2)

        customView.setSelectionChangedListener(object : VideoEditorView.PercentageChanger {
            override fun onSelectionChanged(percentage: Float) {
                percentagemValue.text = "${percentage.toInt()}%"
            }
        })
    }
    }