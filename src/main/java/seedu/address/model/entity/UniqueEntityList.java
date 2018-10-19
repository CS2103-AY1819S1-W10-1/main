package seedu.address.model.entity;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.TypeUtil;
import seedu.address.model.entity.exceptions.DuplicateEntityException;
import seedu.address.model.entity.exceptions.EntityNotFoundException;
import seedu.address.model.entity.exceptions.WrongEntityTypeException;

/**
 * A list of entities that enforces uniqueness between its elements and does not allow nulls.
 * An entity is considered unique by comparing using {@code Entity#isSameEntity(Entity)}.
 * @@author waytan
 * Supports a minimal set of list operations.
 */

public class UniqueEntityList implements Iterable<Entity> {
    private ObservableList<Entity> internalList = FXCollections.observableArrayList();
    private TypeUtil type; // contains the type of entries in that list

    public ObservableList<Entity> getInternalList() {
        return internalList;
    }

    public TypeUtil getType() {
        return type;
    }

    /**
     * Returns true if another UniqueEntityList has the same type.
     */
    public boolean isSameType(UniqueEntityList other) {
        return this.type == other.type;
    }

    /**
     * Returns true if an Entity is of the type that this list contains.
     */
    public boolean isSameType(Entity entity) {
        return type == entity.getType();
    }

    /**
     * Returns true if the list contains the same type of entities.
     * List should not be empty, and should only contain 1 type of entity.
     */
    public boolean isSameType(List<? extends Entity> entities) {
        return isSameType(entities.get(0));
    }

    /**
     * Returns true if the list contains an equivalent entity as the given argument.
     */
    public boolean contains(Entity toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameEntity);
    }

    /**
     * Adds an entity to the list.
     * The entity must be of the correct type, and not already in the list.
     */
    public void add(Entity toAdd) {
        requireNonNull(toAdd);
        if (isSameType(toAdd)) {
            throw new WrongEntityTypeException();
        } else if (contains(toAdd)) {
            throw new DuplicateEntityException();
        } else {
            internalList.add(toAdd);
        }
    }

    /**
     * Replaces the entity {@code target} in the list with {@code editedEntity}.
     * {@code target} must exist in the list.
     * The identity of {@code editedEntity} must not be the same as another entity in the list.
     */
    public void setEntity(Entity target, Entity editedEntity) {
        requireAllNonNull(target, editedEntity);
        if (!target.isSameType(editedEntity)) {
            throw new WrongEntityTypeException();
        }

        int index = internalList.indexOf(target);

        if (index == -1) {
            throw new EntityNotFoundException();
        }

        if (!target.isSameEntity(editedEntity) && contains(editedEntity)) {
            throw new DuplicateEntityException();
        }

        internalList.set(index, editedEntity);
    }

    /**
     * Clears the internalList, and replace it with the replacement internalList
     * {@replacement} must have the same type.
     * The identity of {@code editedEntity} must not be the same as another entity in the list.
     */
    public void setEntities(UniqueEntityList replacement) {
        requireNonNull(replacement);

        if(!isSameType(replacement)) {
            throw new WrongEntityTypeException();
        }
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code entities}.
     * {@code entities} must not contain duplicate entities.
     */
    public void setPersons(List<? extends Entity> entities) {
        requireAllNonNull(entities);

        if (!isSameType(entities)) {
            throw new WrongEntityTypeException();
        }

        if (!entitiesAreUnique(entities)) {
            throw new DuplicateEntityException();
        }

        internalList.setAll(entities);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Entity> asUnmodifiableObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Entity> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueEntityList // instanceof handles nulls
                && internalList.equals(((UniqueEntityList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code entities} contains only unique entities.
     */
    private boolean entitiesAreUnique(List<? extends Entity> entities) {
        for (int i = 0; i < entities.size() - 1; i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                if (entities.get(i).isSameEntity(entities.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
