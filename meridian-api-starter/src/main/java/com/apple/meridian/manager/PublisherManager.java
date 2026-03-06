package com.apple.meridian.manager;

import com.apple.meridian.manager.base.Manager;
import com.apple.meridian.model.Publisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory data store for {@link Publisher} entities, keyed by UUID.
 *
 * <p>In production this class would extend a Cassandra DAO base and issue CQL statements
 * against the {@code publisher} table (partitioned on {@code id}). The {@code ConcurrentHashMap}
 * here provides the same thread-safety guarantees needed by a concurrent web server.</p>
 */
@Component
public class PublisherManager implements Manager<Publisher, UUID> {

    private final Map<UUID, Publisher> store = new ConcurrentHashMap<>();

    @Override
    public Publisher findById(UUID id) {
        return store.get(id);
    }

    @Override
    public List<Publisher> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Publisher save(Publisher publisher) {
        store.put(publisher.getId(), publisher);
        return publisher;
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }
}
