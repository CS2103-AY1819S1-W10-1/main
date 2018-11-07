package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.TypeUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleDescriptor;
import seedu.address.model.module.UniqueModuleList;
import seedu.address.model.occasion.Occasion;
import seedu.address.model.occasion.OccasionDescriptor;
import seedu.address.model.occasion.UniqueOccasionList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.testutil.ModuleBuilder;
import seedu.address.testutil.OccasionBuilder;
import seedu.address.testutil.PersonBuilder;

public class InsertPersonCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown =  ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullPerson_nullOccasion_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new InsertPersonCommand(null, null, new Occasion(new OccasionDescriptor()));
    }

    @Test
    public void constructor_nullPerson_nullModule_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new InsertPersonCommand(null, null, new Module(new ModuleDescriptor()));
    }

    @Test
    public void constructor_nullPerson_nonNullOccasion_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new InsertPersonCommand(null, Index.fromZeroBased(1), new Occasion(new OccasionDescriptor()));
    }

    @Test
    public void constructor_nullPerson_nonNullModule_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new InsertPersonCommand(null, Index.fromZeroBased(1), new Module(new ModuleDescriptor()));
    }

    @Test
    public void constructor_nonNullPerson_nullOccasion_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new InsertPersonCommand(null, Index.fromZeroBased(1), new Occasion(new OccasionDescriptor()));
    }

    @Test
    public void constructor_nonNullPerson_nullModule_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new InsertPersonCommand(Index.fromZeroBased(1), null, new Module(new ModuleDescriptor()));
    }

    @Test
    public void execute_personInsertedIntoModule_success() throws Exception {
        Person validPerson = new PersonBuilder().build();
        Module validModule = new ModuleBuilder().build();
        Occasion validOccasion = new OccasionBuilder().build();

        ModelStubInsertingPersons stub = new ModelStubInsertingPersons(validPerson, validOccasion, validModule);

        CommandResult commandResult = new InsertPersonCommand(Index.fromZeroBased(0),
                                            Index.fromZeroBased(0),
                                            new Module(new ModuleDescriptor())).execute(stub, commandHistory);

        assertEquals(InsertPersonCommand.MESSAGE_SUCCESS_INSERT_INTO_MODULE, commandResult.feedbackToUser);
        assertEquals(stub.getFilteredPersonList().get(0).getModuleList().asUnmodifiableObservableList().get(0),
                        validModule);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_personInsertedIntoOccasion_success() throws Exception {
        Person validPerson = new PersonBuilder().build();
        Module validModule = new ModuleBuilder().build();
        Occasion validOccasion = new OccasionBuilder().build();

        ModelStubInsertingPersons stub = new ModelStubInsertingPersons(validPerson, validOccasion, validModule);

        CommandResult commandResult = new InsertPersonCommand(Index.fromZeroBased(0),
                Index.fromZeroBased(0),
                new Occasion(new OccasionDescriptor())).execute(stub, commandHistory);

        assertEquals(InsertPersonCommand.MESSAGE_SUCCESS_INSERT_INTO_OCCASION, commandResult.feedbackToUser);
        assertEquals(stub.getFilteredPersonList().get(0).getOccasionList().asUnmodifiableObservableList().get(0),
                validOccasion);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicatePersonInsertionIntoModule() throws Exception {
        Person validPerson = new PersonBuilder().build();
        Module validModule = new ModuleBuilder().build();
        Occasion validOccasion = new OccasionBuilder().build();
        InsertPersonCommand insertPerson = new InsertPersonCommand(Index.fromZeroBased(0),
                                                                    Index.fromZeroBased(0),
                                                                    new Module(new ModuleDescriptor()));
        ModelStubInsertingPersons stub = new ModelStubInsertingPersons(validPerson, validOccasion, validModule);
        stub.getFilteredPersonList().get(0).getModuleList().add(validModule);
        stub.getFilteredModuleList().get(0).getStudents().add(validPerson);

        thrown.expect(CommandException.class);
        thrown.expectMessage(InsertPersonCommand.MESSAGE_FAILURE);
        insertPerson.execute(stub, commandHistory);
    }

    @Test
    public void execute_duplicatePersonInsertionIntoOccasion() throws Exception {
        Person validPerson = new PersonBuilder().build();
        Module validModule = new ModuleBuilder().build();
        Occasion validOccasion = new OccasionBuilder().build();
        InsertPersonCommand insertPerson = new InsertPersonCommand(Index.fromZeroBased(0),
                Index.fromZeroBased(0),
                new Occasion(new OccasionDescriptor()));
        ModelStubInsertingPersons stub = new ModelStubInsertingPersons(validPerson, validOccasion, validModule);
        stub.getFilteredPersonList().get(0).getOccasionList().add(validOccasion);
        stub.getFilteredOccasionList().get(0).getAttendanceList().add(validPerson);

        thrown.expect(CommandException.class);
        thrown.expectMessage(InsertPersonCommand.MESSAGE_FAILURE);
        insertPerson.execute(stub, commandHistory);
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addModule(Module module) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addOccasion(Occasion occasion) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasModule(Module module) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasOccasion(Occasion occasion) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteModule(Module module) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteOccasion(Occasion occasion) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updatePerson(Person person, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateModule(Module module, Module editedModule) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateOccasion(Occasion occasion, Occasion editedOccasion) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Module> getFilteredModuleList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Occasion> getFilteredOccasionList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredModuleList(Predicate<Module> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredOccasionList(Predicate<Occasion> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void insertPerson(Person person, Module module, Person personToInsert,
                                 Module moduleToInsert) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void insertPerson(Person person, Occasion occasion, Person personToInsert,
                                 Occasion moduleToInsert) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public TypeUtil getActiveType() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setActiveType(TypeUtil typeToSet) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A stub that always inserts persons into one of either
     * module or occasion.
     */
    private class ModelStubInsertingPersons extends ModelStub {
        UniquePersonList personList = new UniquePersonList();
        UniqueOccasionList occasionList = new UniqueOccasionList();
        UniqueModuleList moduleList = new UniqueModuleList();

        ModelStubInsertingPersons(Person person, Occasion occasion, Module module) {
            requireAllNonNull(person, occasion, module);
            this.personList.add(person);
            this.occasionList.add(occasion);
            this.moduleList.add(module);
        }

        @Override
        public void insertPerson(Person personToInsert, Occasion occasionToInsert,
                                        Person personToReplace, Occasion occasionToReplace) {
            requireAllNonNull(personToInsert, occasionToInsert, personToReplace, occasionToReplace);
            personToInsert.getOccasionList().add(occasionToInsert);
            occasionToInsert.getAttendanceList().add(personToInsert);
        }

        @Override
        public void insertPerson(Person personToInsert, Module moduleToInsert,
                                        Person personToReplace, Module moduleToReplace) {
            requireAllNonNull(personToInsert, moduleToInsert, personToReplace, moduleToReplace);
            personToInsert.getModuleList().add(moduleToInsert);
            moduleToInsert.getStudents().add(personToInsert);
            updatePerson(personToInsert, personToReplace);
            updateModule(moduleToInsert, moduleToReplace);
        }

        @Override
        public void updatePerson(Person target, Person editedPerson) {
            requireNonNull(editedPerson);

            personList.setPerson(target, editedPerson);
        }

        @Override
        public void updateModule(Module target, Module editedModule) {
            requireNonNull(editedModule);

            moduleList.setModule(target, editedModule);
        }

        @Override
        public void updateOccasion(Occasion target, Occasion editedOccasion) {
            requireNonNull(editedOccasion);

            occasionList.setOccasion(target, editedOccasion);
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return this.personList.asUnmodifiableObservableList();
        }

        @Override
        public ObservableList<Module> getFilteredModuleList() {
            return this.moduleList.asUnmodifiableObservableList();
        }

        @Override
        public ObservableList<Occasion> getFilteredOccasionList() {
            return this.occasionList.asUnmodifiableObservableList();
        }

        @Override
        public void commitAddressBook() {
            // called by {@code AddPersonCommand#execute()}
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
