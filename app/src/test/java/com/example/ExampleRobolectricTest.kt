package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.GrievanceReport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("HyperEdge", appName)
    }

    @Test
    fun `verify sealed grievance record integrity`() {
        val report = GrievanceReport(
            id = "HYE-0042",
            category = "Sanitation & Garbage Overflow",
            description = "Trikuta Nagar Sector 4 corridor bin overflow",
            location = "Ward 12 · Trikuta Nagar",
            status = "SEALED",
            formattedTime = "14:12:08 IST",
            shaHash = "SHA-256: 8F2A-91C8-3D4E-7B21",
            aiConfidence = 94
        )

        assertNotNull(report.id)
        assertEquals("SEALED", report.status)
        assertEquals(94, report.aiConfidence)
        assertEquals("ChaCha20-Poly1305 · Hardware Keyring Sealed", report.cryptoKey)
    }
}
