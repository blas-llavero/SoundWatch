package org.dbnora.app

import org.junit.Assert.*
import org.junit.Test

class NoiseGateTest {
    @Test fun alertsAfterOneSecondAndHonoursCooldown() {
        val gate = NoiseGate()
        assertFalse(gate.sample(81.0, 0))
        assertFalse(gate.sample(81.0, 999))
        assertTrue(gate.sample(81.0, 1_000))
        assertFalse(gate.sample(90.0, 60_999))
        assertTrue(gate.sample(90.0, 61_000))
    }

    @Test fun droppingBelowThresholdResetsDuration() {
        val gate = NoiseGate()
        assertFalse(gate.sample(85.0, 0))
        assertFalse(gate.sample(70.0, 900))
        assertFalse(gate.sample(85.0, 1_000))
        assertTrue(gate.sample(85.0, 2_000))
    }
}

