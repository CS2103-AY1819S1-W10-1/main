package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULEINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OCCASIONINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSONINDEX;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.InsertPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleDescriptor;
import seedu.address.model.occasion.Occasion;
import seedu.address.model.occasion.OccasionDescriptor;

public class InsertPersonCommandParser implements Parser<InsertPersonCommand> {

    @Override
    public InsertPersonCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultiMap = ArgumentTokenizer
                .tokenize(args, PREFIX_PERSONINDEX, PREFIX_MODULEINDEX, PREFIX_OCCASIONINDEX);
        if (!arePrefixesPresent(argMultiMap, PREFIX_PERSONINDEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, InsertPersonCommand.MESSAGE_USAGE));
        }

        Index personIndex = ParserUtil.parseIndex(argMultiMap.getValue(PREFIX_PERSONINDEX).get());

        if (arePrefixesPresent(argMultiMap, PREFIX_MODULEINDEX)) { // The user wants to insert into a module.
            Index moduleIndex = ParserUtil.parseIndex(argMultiMap.getValue(PREFIX_MODULEINDEX).get());
            ModuleDescriptor dummyModuleDescriptor = new ModuleDescriptor();
            Module dummyModule = new Module(dummyModuleDescriptor);
            return new InsertPersonCommand(personIndex, moduleIndex, dummyModule);

        } else if (arePrefixesPresent(argMultiMap, PREFIX_OCCASIONINDEX)) { // The user wants to insert into an occasion.
            Index occasionIndex = ParserUtil.parseIndex(argMultiMap.getValue(PREFIX_OCCASIONINDEX).get());
            OccasionDescriptor dummyOccasionDescriptor = new OccasionDescriptor();
            Occasion dummyOccasion = new Occasion(dummyOccasionDescriptor);
            return new InsertPersonCommand(personIndex, occasionIndex, dummyOccasion);

        }

        return new InsertPersonCommand();
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
