package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.occasion.Occasion;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Deletes a occasion identified using it's displayed index from the address book.
 */
public class DeleteOccasionCommand extends Command {

    public static final String COMMAND_WORD = "deleteoccasion";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the occasion identified by the index number used in the displayed occasion list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_OCCASION_SUCCESS = "Deleted Occasion: %1$s";

    private final Index targetIndex;

    public DeleteOccasionCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Occasion> lastShownList = model.getFilteredOccasionList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_OCCASION_DISPLAYED_INDEX);
        }

        Occasion occasionToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteOccasion(occasionToDelete);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_DELETE_OCCASION_SUCCESS, occasionToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteOccasionCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteOccasionCommand) other).targetIndex)); // state check
    }
}
