package com.apple.meridian.service;

import com.apple.meridian.exception.AlreadyExistsException;
import com.apple.meridian.exception.ValidationException;
import com.apple.meridian.manager.ChannelManager;
import com.apple.meridian.model.Channel;
import com.apple.meridian.model.ChannelStatus;
import com.apple.meridian.service.base.BaseDataModelService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ChannelService extends BaseDataModelService<UUID, Channel, ChannelManager> {

    @Override
    protected String entityName() {
        return "Channel";
    }

    @Override
    protected UUID extractId(Channel channel) {
        return channel.getId();
    }

    @Override
    protected void prepareForCreate(Channel channel) {
        channel.setId(UUID.randomUUID());
        channel.setStatus(ChannelStatus.ACTIVE);
        channel.setActiveSubmissions(0);
        channel.setCreatedAt(new Date());
        channel.setModifiedAt(new Date());
    }

    @Override
    protected void beforeSave(Channel channel) throws Exception {
        boolean nameTaken = manager.findAll().stream()
                .filter(existing -> !existing.getId().equals(channel.getId()))
                .anyMatch(existing -> existing.getName().equalsIgnoreCase(channel.getName()));
        if (nameTaken) {
            throw new AlreadyExistsException("Channel with name already exists: " + channel.getName());
        }
    }

    public Channel activate(UUID channelId) {
        Channel channel = findById(channelId);
        if (channel.getStatus() == ChannelStatus.ACTIVE) {
            throw new ValidationException("Channel is already active");
        }
        channel.setStatus(ChannelStatus.ACTIVE);
        channel.setModifiedAt(new Date());
        return manager.save(channel);
    }

    public Channel deactivate(UUID channelId) {
        Channel channel = findById(channelId);
        if (channel.getStatus() == ChannelStatus.INACTIVE) {
            throw new ValidationException("Channel is already inactive");
        }
        channel.setStatus(ChannelStatus.INACTIVE);
        channel.setModifiedAt(new Date());
        return manager.save(channel);
    }

    /**
     * Increments the count of active submissions for a channel.
     * Called by SubmissionService when a new submission is created.
     */
    public void incrementActiveSubmissions(UUID channelId) {
        Channel channel = findById(channelId);
        channel.setActiveSubmissions(channel.getActiveSubmissions() + 1);
        channel.setModifiedAt(new Date());
        manager.save(channel);
    }

    /**
     * Decrements the count of active submissions for a channel.
     * Called by SubmissionService when a submission is withdrawn or rejected.
     */
    public void decrementActiveSubmissions(UUID channelId) {
        Channel channel = findById(channelId);
        channel.setActiveSubmissions(Math.max(0, channel.getActiveSubmissions() - 1));
        channel.setModifiedAt(new Date());
        manager.save(channel);
    }
}
