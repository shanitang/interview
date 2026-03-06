package com.apple.meridian.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * A topic-based distribution channel that articles can be submitted to.
 *
 * <p>Cassandra access pattern: primary table {@code channel} partitioned on {@code id}.</p>
 */
@Table("channel")
public class Channel extends DataModel {

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    /** Stored as the enum's {@link ChannelStatus#name()} string. */
    @Column("status")
    private String status;

    @Column("max_capacity")
    private int maxCapacity;

    /**
     * Count of submissions currently in PENDING or APPROVED state.
     * Incremented on submission create; decremented on withdrawal/rejection.
     */
    @Column("active_submissions")
    private int activeSubmissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChannelStatus getStatus() {
        return status != null ? ChannelStatus.valueOf(status) : null;
    }

    public void setStatus(ChannelStatus status) {
        this.status = status != null ? status.name() : null;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getActiveSubmissions() {
        return activeSubmissions;
    }

    public void setActiveSubmissions(int activeSubmissions) {
        this.activeSubmissions = activeSubmissions;
    }
}
