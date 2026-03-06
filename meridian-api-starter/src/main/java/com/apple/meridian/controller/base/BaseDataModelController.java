package com.apple.meridian.controller.base;

import com.apple.meridian.service.base.BaseDataModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Abstract base controller providing standard CRUD endpoints.
 *
 * <p>Concrete controllers extend this class, supply a {@link BaseDataModelService} via
 * {@link Autowired}, and implement {@link #parseId} to convert path-variable strings to the
 * entity's ID type. Domain-specific endpoints are added in the concrete class.</p>
 *
 * @param <ID>           type of the entity's primary key
 * @param <MODEL>        entity type handled by this controller
 * @param <MODEL_SERVICE> service type for MODEL
 */
public abstract class BaseDataModelController<ID, MODEL, MODEL_SERVICE extends BaseDataModelService<ID, MODEL, ?>> {
    @Autowired
    protected MODEL_SERVICE modelService;

    protected abstract ID parseId(String rawId);

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<MODEL> list() throws Exception {
        return modelService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody MODEL getById(@PathVariable String id) throws Exception {
        return modelService.findById(parseId(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody MODEL create(@RequestBody MODEL model) throws Exception {
        return modelService.createModel(model);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody MODEL update(@RequestBody MODEL model) throws Exception {
        return modelService.updateModel(model);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) throws Exception {
        MODEL model = modelService.findById(parseId(id));
        modelService.deleteModel(model);
    }
}
