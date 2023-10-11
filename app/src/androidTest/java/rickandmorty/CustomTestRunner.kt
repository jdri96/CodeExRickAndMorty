package rickandmorty

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/** A [custom runner] to set up the instrumented application class for tests.
 * Used to solve:
 * err-> Hilt test, rickandmorty.NavigationInstrumentedTest, cannot use a @HiltAndroidApp application but found rickandmorty.RickAndMortyApplication.
 * To fix, configure the test to use HiltTestApplication or a custom Hilt test application generated with @CustomTestApplication.
 */

class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}