package seedu.address.ui;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.ModulePanelSelectionChangedEvent;
import seedu.address.model.module.Module;

import java.util.logging.Logger;

/**
 * Panel containing the list of entities.
 *
 * @author alistair
 */
public class EntityListPanel extends UiPart<Region> {
    private static final String FXML = "EntityListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(EntityListPanel.class);

    @FXML
    private ListView<Module> moduleListView;

    public EntityListPanel(ObservableList<Module> moduleList) {
        super(FXML);
        setConnections(moduleList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Module> moduleList) {
        moduleListView.setItems(moduleList);
        moduleListView.setCellFactory(listView -> new moduleListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        moduleListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in module list panel changed to : '" + newValue + "'");
                        raise(new ModulePanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code ModuleCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            moduleListView.scrollTo(index);
            moduleListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Module} using a {@code ModuleCard}.
     */
    class moduleListViewCell extends ListCell<Module> {
        @Override
        protected void updateItem(Module module, boolean empty) {
            super.updateItem(module, empty);

            if (empty || module == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ModuleCard(module, getIndex() + 1).getRoot());
            }
        }
    }

}
