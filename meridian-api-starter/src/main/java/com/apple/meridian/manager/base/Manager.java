package com.apple.meridian.manager.base;

import java.util.List;

/**
 * Data-access contract for a single entity type.
 *
 * <p>This is the in-memory analogue of a Cassandra DAO. In production each method would
 * translate to a CQL statement; here the implementations use a {@link java.util.concurrent.ConcurrentHashMap}.</p>
 *
 * <p>Conventions:</p>
 * <ul>
 *   <li>{@link #findById} returns {@code null} when the record does not exist — callers
 *       (typically {@code BaseDataModelService.findById}) are responsible for throwing
 *       {@link com.apple.meridian.exception.NotFoundException}.</li>
 *   <li>{@link #save} is used for both insert and update; the caller sets all fields.</li>
 * </ul>
 *
 * @param <MODEL> entity type
 * @param <ID>    primary key type
 */
public interface Manager<MODEL, ID> {

    /** Returns the entity with the given id, or {@code null} if not found. */
    MODEL findById(ID id);

    /** Returns all entities currently in the store. */
    List<MODEL> findAll();

    /** Persists {@code model} (insert or update) and returns it. */
    MODEL save(MODEL model);

    /** Removes the entity with the given id; no-op if not found. */
    void deleteById(ID id);
}
