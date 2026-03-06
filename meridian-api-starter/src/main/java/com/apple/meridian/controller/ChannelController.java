package com.apple.meridian.controller;

import com.apple.meridian.controller.base.BaseDataModelController;
import com.apple.meridian.model.Channel;
import com.apple.meridian.service.ChannelService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.apple.meridian.SecurityConfig.REST_API_PATH_PREFIX;

/**
 * REST controller for {@link Channel} resources.
 *
 * <p>Standard CRUD is inherited from {@link BaseDataModelController}. This class adds
 * domain-specific endpoints for lifecycle transitions:</p>
 * <ul>
 *   <li>{@code POST /{id}/activate} — reopen a channel for new submissions</li>
 *   <li>{@code POST /{id}/deactivate} — mark the channel INACTIVE, preventing new submissions</li>
 * </ul>
 */
@RestController
@RequestMapping(value = REST_API_PATH_PREFIX + "/channels")
public class ChannelController extends BaseDataModelController<UUID, Channel, ChannelService> {

    @Override
    protected UUID parseId(String rawId) {
        return UUID.fromString(rawId);
    }

    @RequestMapping(value = "/{id}/activate", method = RequestMethod.POST)
    public @ResponseBody Channel activate(@PathVariable UUID id) throws Exception {
        return modelService.activate(id);
    }

    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.POST)
    public @ResponseBody Channel deactivate(@PathVariable UUID id) throws Exception {
        return modelService.deactivate(id);
    }
}
