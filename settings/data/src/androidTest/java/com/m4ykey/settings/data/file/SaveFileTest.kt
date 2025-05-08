package com.m4ykey.settings.data.file

import android.content.Context
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class SaveFileTest {

    private lateinit var context : Context
    private lateinit var testFile : File
    private lateinit var testUri : Uri

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        testFile = File(context.cacheDir, "test.json")
        testUri = Uri.fromFile(testFile)
    }

    @After
    fun tearDown() {
        testFile.delete()
    }

    @Test
    fun saveJsonToFileShouldCorrectlyWriteJSONDataToFile() = runBlocking {
        val jsonData = """{"name":"Test","value":123}"""

        saveJsonToFile(context, testUri, jsonData)

        val savedData = testFile.readText()
        assertEquals(jsonData, savedData)
    }

}