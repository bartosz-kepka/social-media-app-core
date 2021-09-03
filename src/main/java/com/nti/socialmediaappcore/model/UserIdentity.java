package com.nti.socialmediaappcore.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Document(collection = "usersidentities")
public class UserIdentity {
    // underscore to set mongo document id manually
    @Id
    private String _id;

    @Getter
    @Setter
    private String fullName;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public UserIdentity(User user) {
        this._id = user.getId();
        this.fullName = user.getFirstName() + ' ' + user.getLastName();
    }
}
