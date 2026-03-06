package com.apple.meridian.controller;

import com.apple.meridian.controller.base.BaseDataModelController;
import com.apple.meridian.model.Publisher;
import com.apple.meridian.service.PublisherService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.apple.meridian.SecurityConfig.REST_API_PATH_PREFIX;

/**
 * REST controller for {@link Publisher} resources.
 *
 * <p>Standard CRUD (GET list, GET by id, POST create, PUT update, DELETE) is inherited from
 * {@link BaseDataModelController}. This class adds domain-specific endpoints for lifecycle
 * transitions and channel approval management:</p>
 * <ul>
 *   <li>{@code POST /{id}/activate} — move publisher from PENDING/SUSPENDED to ACTIVE</li>
 *   <li>{@code POST /{id}/suspend} — move publisher from ACTIVE to SUSPENDED</li>
 *   <li>{@code POST /{id}/approveChannel/{channelId}} — grant submission rights to a channel</li>
 *   <li>{@code POST /{id}/revokeChannel/{channelId}} — remove submission rights from a channel</li>
 * </ul>
 */
@RestController
@RequestMapping(value = REST_API_PATH_PREFIX + "/publishers")
public class PublisherController extends BaseDataModelController<UUID, Publisher, PublisherService> {

    @Override
    protected UUID parseId(String rawId) {
        return UUID.fromString(rawId);
    }

    @RequestMapping(value = "/{id}/activate", method = RequestMethod.POST)
    public @ResponseBody Publisher activate(@PathVariable UUID id) throws Exception {
        return modelService.activate(id);
    }

    @RequestMapping(value = "/{id}/suspend", method = RequestMethod.POST)
    public @ResponseBody Publisher suspend(@PathVariable UUID id) throws Exception {
        return modelService.suspend(id);
    }

    @RequestMapping(value = "/{id}/approveChannel/{channelId}", method = RequestMethod.POST)
    public @ResponseBody Publisher approveForChannel(
            @PathVariable UUID id,
            @PathVariable UUID channelId) throws Exception {
        return modelService.approveForChannel(id, channelId);
    }

    @RequestMapping(value = "/{id}/revokeChannel/{channelId}", method = RequestMethod.POST)
    public @ResponseBody Publisher revokeChannelApproval(
            @PathVariable UUID id,
            @PathVariable UUID channelId) throws Exception {
        return modelService.revokeChannelApproval(id, channelId);
    }
}
