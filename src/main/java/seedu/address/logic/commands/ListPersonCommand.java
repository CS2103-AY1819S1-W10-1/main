package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.TypeUtil.PERSON;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowPersonRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListPersonCommand extends Command {

    public static final String COMMAND_WORD = "listperson";

    public static final String MESSAGE_SUCCESS = "Listed all persons";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.setActiveType(PERSON);
        EventsCenter.getInstance().post(new ShowPersonRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
