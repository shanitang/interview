package com.apple.meridian.service.base;

import java.time.Instant;

/**
 * Carries metadata about a save operation into the {@code afterSave} lifecycle hook.
 *
 * <p>Services that override {@link BaseDataModelService#afterSave} receive a
 * {@code SaveContext} so they can react differently to creates vs updates — for example,
 * publishing a domain event, writing an audit log entry, or triggering a downstream
 * notification. The {@link #timestamp} records when the operation was initiated.</p>
 */
public class SaveContext {

    public enum Action {
        CREATE,
        UPDATE,
        DELETE
    }

    private final Action action;
    private final Instant timestamp;

    public SaveContext(Action action) {
        this.action = action;
        this.timestamp = Instant.now();
    }

    public Action getAction() {
        return action;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
