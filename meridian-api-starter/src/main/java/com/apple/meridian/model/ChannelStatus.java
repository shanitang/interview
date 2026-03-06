package com.apple.meridian.model;

/**
 * Operational states for a {@link Channel}.
 *
 * <p>Only ACTIVE channels accept new submissions. Deactivating a channel does not affect
 * submissions already in progress — those continue until individually resolved.</p>
 */
public enum ChannelStatus {
    /** Open for new article submissions (subject to capacity). */
    ACTIVE,
    /** Administratively closed; no new submissions accepted. */
    INACTIVE
}
