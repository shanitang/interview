package com.apple.meridian.model;

/**
 * Lifecycle states for a {@link Publisher}.
 *
 * <p>State machine:</p>
 * <pre>
 *   PENDING ──activate()──▶ ACTIVE ──suspend()──▶ SUSPENDED
 *                               ▲                      │
 *                               └─────activate()───────┘
 * </pre>
 * Only ACTIVE publishers may submit articles to channels.
 */
public enum PublisherStatus {
    /** Newly registered; awaiting admin approval. Cannot submit articles. */
    PENDING,
    /** Approved and in good standing. Can submit articles to approved channels. */
    ACTIVE,
    /** Account suspended (e.g. policy violation). Cannot submit articles. */
    SUSPENDED
}
