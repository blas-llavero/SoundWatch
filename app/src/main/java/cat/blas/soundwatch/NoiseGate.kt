package cat.blas.soundwatch

/** Decides when a sustained threshold crossing may produce an alert. */
class NoiseGate(
    private val thresholdDb: Double = 80.0,
    private val requiredDurationMs: Long = 1_000,
    private val cooldownMs: Long = 60_000,
) {
    private var aboveSince: Long? = null
    private var lastAlertAt = Long.MIN_VALUE

    fun sample(db: Double, nowMs: Long): Boolean {
        if (db < thresholdDb) {
            aboveSince = null
            return false
        }
        if (aboveSince == null) aboveSince = nowMs
        val sustained = nowMs - (aboveSince ?: nowMs) >= requiredDurationMs
        val cooledDown = lastAlertAt == Long.MIN_VALUE || nowMs - lastAlertAt >= cooldownMs
        if (sustained && cooledDown) {
            lastAlertAt = nowMs
            return true
        }
        return false
    }
}

