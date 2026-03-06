package com.apple.meridian.service.base;

import com.apple.meridian.exception.NotFoundException;
import com.apple.meridian.manager.base.Manager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Generic base service providing CRUD operations with lifecycle hooks.
 *
 * <p>Subclasses implement {@link #prepareForCreate} to initialize transient fields
 * (id, timestamps, defaults) and override {@link #beforeSave}/{@link #afterSave} to
 * inject domain validation and side-effect logic without duplicating the persistence
 * scaffolding in every service.</p>
 *
 * <p>Note: {@link #beforeSave} is called <em>before</em> {@link #prepareForCreate}, so
 * {@code model.getId()} is {@code null} on create. Subclasses can use this to distinguish
 * create from update paths.</p>
 *
 * @param <ID>      type of the entity's primary key
 * @param <MODEL>   entity type
 * @param <MANAGER> data-access manager for MODEL
 */
public abstract class BaseDataModelService<ID, MODEL, MANAGER extends Manager<MODEL, ID>> {
    @Autowired
    protected MANAGER manager;

    public MODEL findById(ID id) {
        MODEL result = manager.findById(id);
        if (result == null) {
            throw new NotFoundException(entityName() + " not found: " + id);
        }
        return result;
    }

    public List<MODEL> findAll() {
        return manager.findAll();
    }

    public MODEL createModel(MODEL model) throws Exception {
        beforeSave(model);
        prepareForCreate(model);
        MODEL saved = manager.save(model);
        afterSave(saved, new SaveContext(SaveContext.Action.CREATE));
        return saved;
    }

    public MODEL updateModel(MODEL model) throws Exception {
        ID id = extractId(model);
        if (manager.findById(id) == null) {
            throw new NotFoundException(entityName() + " not found: " + id);
        }
        beforeSave(model);
        MODEL saved = manager.save(model);
        afterSave(saved, new SaveContext(SaveContext.Action.UPDATE));
        return saved;
    }

    public void deleteModel(MODEL model) throws Exception {
        beforeDelete(model);
        manager.deleteById(extractId(model));
        afterDelete(model);
    }

    /** Human-readable entity name used in error messages. */
    protected abstract String entityName();

    /** Extract the primary key from an entity instance. */
    protected abstract ID extractId(MODEL model);

    /**
     * Called before {@link #prepareForCreate} on create, and before persistence on update.
     * {@code model.getId()} is {@code null} on create. Throw a typed exception to abort.
     */
    protected void beforeSave(MODEL model) throws Exception {}

    /**
     * Called at the start of {@link #createModel} before any persistence. Implementations
     * must assign a new id and set timestamps. They may also set default field values.
     */
    protected abstract void prepareForCreate(MODEL model);

    /** Called after the entity has been successfully persisted. */
    protected void afterSave(MODEL model, SaveContext context) throws Exception {}

    /** Called before an entity is removed from the store. */
    protected void beforeDelete(MODEL model) throws Exception {}

    /** Called after an entity has been removed from the store. */
    protected void afterDelete(MODEL model) throws Exception {}
}
