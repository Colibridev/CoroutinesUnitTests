package sample.dev.coroutinesunittests

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.CoroutineContext

class ContentActivity : AppCompatActivity(), ContentView {
    private lateinit var textView: TextView
    private lateinit var presenter: ContentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.content_text_view)

        // emulation of dagger
        injectDependencies()

        presenter.onViewInit()
    }

    private fun injectDependencies() {
        presenter = ContentPresenter(ContentRepository(), this)
    }

    override fun displayContent(content: String) {
        textView.text = content
    }
}


// interface for View Presenter communication
interface ContentView {
    fun displayContent(content: String)
}

// Presenter class
class ContentPresenter(private val repository: ContentRepository,
                       private val view: ContentView,
                       private val contextPool: CoroutineContextProvider = CoroutineContextProvider()) {

    fun onViewInit() {
        launch(contextPool.Main) {
            // move to another Thread
            val content = withContext(contextPool.IO) {
                repository.requestContent()
            }
            view.displayContent(content)
        }
    }
}

open class CoroutineContextProvider() {
    open val Main: CoroutineContext by lazy { UI }
    open val IO: CoroutineContext by lazy { CommonPool }
}

// Repository class
open class ContentRepository {

    suspend open fun requestContent(): String {
        delay(1000L)
        return "Content"
    }
}
