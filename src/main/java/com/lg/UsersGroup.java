package com.lg;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UsersGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(mappedBy = "userGroupList")
    private final List<User> users = new ArrayList<>();

    public UsersGroup(){
        this(null);
    }

    public UsersGroup(Long id) {
        this.id = id;
    }

}
