package com.apple.meridian.service;

import com.apple.meridian.exception.AlreadyExistsException;
import com.apple.meridian.exception.ValidationException;
import com.apple.meridian.manager.PublisherManager;
import com.apple.meridian.model.Publisher;
import com.apple.meridian.model.PublisherStatus;
import com.apple.meridian.service.base.BaseDataModelService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

/**
 * Business logic for {@link Publisher} entities.
 *
 * <p>Inherits standard CRUD from {@link BaseDataModelService}. Domain methods manage the
 * publisher lifecycle (PENDING → ACTIVE ↔ SUSPENDED) and the per-channel approval set
 * that controls which channels a publisher is allowed to submit articles to.</p>
 *
 * <p>Note: {@link #approveForChannel} and {@link #revokeChannelApproval} do not validate
 * that the channel ID exists — a candidate could be asked to add that check as an
 * extension to the task.</p>
 */
@Service
public class PublisherService extends BaseDataModelService<UUID, Publisher, PublisherManager> {

    @Override
    protected String entityName() {
        return "Publisher";
    }

    @Override
    protected UUID extractId(Publisher publisher) {
        return publisher.getId();
    }

    @Override
    protected void prepareForCreate(Publisher publisher) {
        publisher.setId(UUID.randomUUID());
        publisher.setStatus(PublisherStatus.PENDING);
        if (publisher.getApprovedChannelIds() == null) {
            publisher.setApprovedChannelIds(new HashSet<>());
        }
        publisher.setCreatedAt(new Date());
        publisher.setModifiedAt(new Date());
    }

    @Override
    protected void beforeSave(Publisher publisher) throws Exception {
        boolean emailTaken = manager.findAll().stream()
                .filter(existing -> !existing.getId().equals(publisher.getId()))
                .anyMatch(existing -> existing.getEmail().equalsIgnoreCase(publisher.getEmail()));
        if (emailTaken) {
            throw new AlreadyExistsException("Publisher with email already exists: " + publisher.getEmail());
        }
    }

    public Publisher activate(UUID publisherId) {
        Publisher publisher = findById(publisherId);
        if (publisher.getStatus() == PublisherStatus.ACTIVE) {
            throw new ValidationException("Publisher is already active");
        }
        publisher.setStatus(PublisherStatus.ACTIVE);
        publisher.setModifiedAt(new Date());
        return manager.save(publisher);
    }

    public Publisher suspend(UUID publisherId) {
        Publisher publisher = findById(publisherId);
        if (publisher.getStatus() == PublisherStatus.SUSPENDED) {
            throw new ValidationException("Publisher is already suspended");
        }
        publisher.setStatus(PublisherStatus.SUSPENDED);
        publisher.setModifiedAt(new Date());
        return manager.save(publisher);
    }

    public Publisher approveForChannel(UUID publisherId, UUID channelId) {
        Publisher publisher = findById(publisherId);
        publisher.getApprovedChannelIds().add(channelId);
        publisher.setModifiedAt(new Date());
        return manager.save(publisher);
    }

    public Publisher revokeChannelApproval(UUID publisherId, UUID channelId) {
        Publisher publisher = findById(publisherId);
        publisher.getApprovedChannelIds().remove(channelId);
        publisher.setModifiedAt(new Date());
        return manager.save(publisher);
    }
}
