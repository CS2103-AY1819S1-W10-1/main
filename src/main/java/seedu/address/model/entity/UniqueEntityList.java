package seedu.address.model.entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static java.util.Objects.requireNonNull;

public class UniqueEntityList {
    private final ObservableList<? extends Entity> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent entity as the given argument.
     */
    public boolean contains(Entity toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameEntity);
    }
}
