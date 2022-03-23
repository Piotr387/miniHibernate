package com.lg;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users",
        indexes = @Index(columnList = "login"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Type(type="org.hibernate.type.BinaryType")
    @Column (name = "image", length = 100_000)
    private byte[] image;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private final List<Role> roles = new ArrayList<>();
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "user_usersgroup",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "group_id") }
    )
    private final List<UsersGroup> userGroupList = new ArrayList<>();


    public User() {
    }

    public User(Long id, String login, String password, String firstName, String lastName, Sex sex) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void infoAboutUser(){
        System.out.println("Uzytkownik id: " + id +
                "\nLogin: " + login +
                "\nHaslo: " + password +
                "\nImie: " + firstName +
                "\nNazwisko: " + lastName +
                "\nPłeć: " + sex.getEnumString()

        );
    }

    public void addRole(Role role){
        this.roles.add(role);
    }

    public void addGroup(UsersGroup userGroup){
        this.userGroupList.add(userGroup);
    }

}
