package top.thinapps.notificationmanager

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val placeholder = TextView(this).apply {
            text = getString(R.string.app_placeholder)
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        }

        setContentView(placeholder)
    }
}
