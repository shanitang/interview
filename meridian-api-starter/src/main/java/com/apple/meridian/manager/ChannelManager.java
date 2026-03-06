package com.apple.meridian.manager;

import com.apple.meridian.manager.base.Manager;
import com.apple.meridian.model.Channel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory data store for {@link Channel} entities, keyed by UUID.
 *
 * <p>In production this class would extend a Cassandra DAO base and issue CQL statements
 * against the {@code channel} table (partitioned on {@code id}).</p>
 */
@Component
public class ChannelManager implements Manager<Channel, UUID> {

    private final Map<UUID, Channel> store = new ConcurrentHashMap<>();

    @Override
    public Channel findById(UUID id) {
        return store.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Channel save(Channel channel) {
        store.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }
}
