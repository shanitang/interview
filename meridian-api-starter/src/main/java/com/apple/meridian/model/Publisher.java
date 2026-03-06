package com.apple.meridian.model;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

/**
 * A content publisher organization.
 *
 * <p>Cassandra access patterns:</p>
 * <ul>
 *   <li>Primary table {@code publisher}: partitioned on {@code id} for direct lookup.</li>
 * </ul>
 */
@Table("publisher")
public class Publisher extends DataModel {

    @Column("name")
    private String name;

    @Column("email")
    private String email;

    /** Stored as the enum's {@link PublisherStatus#name()} string. */
    @Column("status")
    private String status;

    /**
     * Channels this publisher is approved to submit articles to.
     * Stored as a Cassandra {@code SET<UUID>}.
     */
    @Column("approved_channel_ids")
    @CassandraType(type = CassandraType.Name.SET, typeArguments = CassandraType.Name.UUID)
    private Set<UUID> approvedChannelIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PublisherStatus getStatus() {
        return status != null ? PublisherStatus.valueOf(status) : null;
    }

    public void setStatus(PublisherStatus status) {
        this.status = status != null ? status.name() : null;
    }

    public Set<UUID> getApprovedChannelIds() {
        return approvedChannelIds;
    }

    public void setApprovedChannelIds(Set<UUID> approvedChannelIds) {
        this.approvedChannelIds = approvedChannelIds;
    }
}
