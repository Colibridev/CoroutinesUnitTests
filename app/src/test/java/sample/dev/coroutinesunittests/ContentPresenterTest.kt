@file:Suppress("IllegalIdentifier")

package sample.dev.coroutinesunittests

import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.coroutines.experimental.CoroutineContext

class ContentPresenterTest {
    @Test
    fun `Display content after receiving`() = runBlocking {
        // arrange
        val repository = mock(ContentRepository::class.java)
        val view = mock(ContentView::class.java)
        val presenter = ContentPresenter(repository, view, TestContextProvider())

        val expectedResult = "Result"
        `when`(repository.requestContent()).thenReturn(expectedResult)

        // act
        presenter.onViewInit()

        // assert
        verify(view).displayContent(expectedResult)
    }
}

class TestContextProvider : CoroutineContextProvider() {
    override val Main: CoroutineContext = Unconfined
    override val IO: CoroutineContext = Unconfined
}