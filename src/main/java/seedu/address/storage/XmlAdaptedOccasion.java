package seedu.address.storage;

import java.util.*;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.occasion.Occasion;
import seedu.address.model.occasion.OccasionDate;
import seedu.address.model.occasion.OccasionName;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.tag.TagKey;
import seedu.address.model.tag.TagMap;
import seedu.address.model.tag.TagValue;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;


/**
 * JAXB-friendly version of the Occasion.
 *
 * @author alistair
 */
public class XmlAdaptedOccasion {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Occasion's %s field is missing!";

    @XmlElement(required = true)
    private String occasionName;
    @XmlElement(required = true)
    private String occasionDateTime;
    @XmlElement(required = true)
    private String location;
    @XmlElement
    private List<XmlAdaptedPerson> attendanceList = new ArrayList<>();
    @XmlElement
    private Map<XmlAdaptedTagKey, XmlAdaptedTagValue> tagMap;
    /**
     * Constructs an XmlAdaptedOccasion.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedOccasion() {}

    /**
     * Constructs an {@code XmlAdaptedOccasion} with the given occasion details.
     */
    public XmlAdaptedOccasion(String occasionName, String occasionDate,
                              String location, Map<XmlAdaptedTagKey, XmlAdaptedTagValue> tagged, List<XmlAdaptedPerson> attendanceList) {
        requireAllNonNull(occasionName, occasionDate, location, tagged, attendanceList);
        this.occasionName = occasionName;
        this.occasionDateTime = occasionDate;
        this.location = location;
        this.attendanceList = attendanceList;
        this.tagMap = tagged;
    }

    /**
     * Converts a given Occasion into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedOccasion
     */
    public XmlAdaptedOccasion(Occasion source) {
        requireNonNull(source);
        source.getTags().forEach((key, value) ->
                tagMap.put(new XmlAdaptedTagKey(key.tagKey), new XmlAdaptedTagValue(value.tagValue)));
        occasionName = source.getOccasionName().toString();
        occasionDateTime = source.getOccasionDate().toString();
        location = source.getLocation();
        attendanceList = source.getAttendanceList().asNormalList().stream()
                            .map(XmlAdaptedPerson::new)
                            .collect(Collectors.toList());
    }

    /**
     * Converts this jaxb-friendly adapted occasion object into the model's Occasion object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted occasion
     */
    public Occasion toModelType() throws IllegalValueException {
        requireAllNonNull(this.occasionName, this.occasionDateTime, this.attendanceList);
        OccasionName occasionName = new OccasionName(this.occasionName);
        OccasionDate occasionDate = new OccasionDate(this.occasionDateTime);
        List<Person> attendanceList = new ArrayList<>();
        for (XmlAdaptedPerson person : this.attendanceList) {
            attendanceList.add(person.toModelType());
        }
        UniquePersonList uniqueAttendanceList = new UniquePersonList(attendanceList);
        Map<TagKey, TagValue> tagMap = new HashMap<>();
        this.tagMap.forEach((key, value) -> tagMap.put(key.toModelType(), value.toModelType()));

        return new Occasion(occasionName, occasionDate, location, new TagMap(tagMap), uniqueAttendanceList);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedOccasion)) {
            return false;
        }

        XmlAdaptedOccasion otherOccasion = (XmlAdaptedOccasion) other;
        return Objects.equals(occasionName, otherOccasion.occasionName)
                && Objects.equals(occasionDateTime, otherOccasion.occasionDateTime)
                && Objects.equals(location, otherOccasion.location)
                && tagMap.equals(otherOccasion.tagMap);
    }
}
