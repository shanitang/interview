package com.apple.meridian.model;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import java.util.Date;
import java.util.UUID;

/**
 * Base class for all persisted entities.
 *
 * <p>Mirrors the {@code DataModel} base in java-data: every entity has a UUID primary key
 * plus {@code createdAt} and {@code modifiedAt} timestamps stored in their respective
 * Cassandra columns.</p>
 */
public abstract class DataModel {

    @PrimaryKey
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID id;

    @Column("created_at")
    private Date createdAt;

    @Column("modified_at")
    private Date modifiedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
