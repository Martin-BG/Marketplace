package bg.softuni.marketplace.domain.validation.groups;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({Default.class, GroupOne.class, GroupTwo.class, GroupThree.class})
public interface AllGroups {
}
