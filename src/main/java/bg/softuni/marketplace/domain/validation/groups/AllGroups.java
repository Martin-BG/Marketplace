package bg.softuni.marketplace.domain.validation.groups;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, GroupOne.class, GroupTwo.class})
public interface AllGroups {
}
