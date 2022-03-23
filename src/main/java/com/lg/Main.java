package com.lg;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        System.out.println("JPA project");

        //Stowrzenie 5 użytkowników
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(null,"test_1","test_1","Andrzej","Kowalski", Sex.MALE));
        users.add(new User(null,"test_2","test_2","Jerzy","Szczepański", Sex.MALE));
        users.add(new User(null,"test_4","test_4","Aneta","Ziółkowska", Sex.FEMALE));
        users.add(new User(null,"test_5","test_5","Paulina","Mazurek", Sex.FEMALE));
        users.add(new User(null,"test_3","test_3","Konrad","Witkowski", Sex.MALE));

        //Stworzenie 5 ról
        ArrayList<Role> roleList = new ArrayList<>();
        roleList.add(new Role(null,"Administrator"));
        roleList.add(new Role(null,"Wydawca"));
        roleList.add(new Role(null,"Redaktor"));
        roleList.add(new Role(null,"Projektant"));
        roleList.add(new Role(null,"Widz"));


        EntityManagerFactory factory = Persistence.createEntityManagerFactory("Hibernate_JPA");
        EntityManager em = factory.createEntityManager();

        //Dodanie zdefiniowanych użytkowników i ról
        em.getTransaction().begin();
        for (User x: users) {
            em.persist(x);
        }
        for (Role x: roleList) {
            em.persist(x);
        }
        em.getTransaction().commit();

        //Zaktualizowanie hasla uzytkownika o id = 1
        User userUpdate = em.find(User.class, 1L);
        userUpdate.setPassword("update_1");
        em.merge(userUpdate);

        //Usnięcie użytkownika o id = 5
        User userRemove = em.find(User.class, 5L);
        em.getTransaction().begin();
        em.remove(userRemove);
        em.getTransaction().commit();

        // Wyświetlenie wszystkich użytkowników o nazwisku Kowalski
        em.getTransaction().begin();
        Query query = em.createQuery("SELECT u FROM User u WHERE u.lastName = 'Kowalski'");
        List<User> kowalscy = query.getResultList();
        em.getTransaction().commit();
        
        if (kowalscy.isEmpty()){
            System.out.println("Brak uzytkownikow o nazwisku Kowalski");
        } else {
            for (User x: kowalscy) {
                System.out.println("Informacje o użytkowniku z nazwiskiem Kowalski");
                x.infoAboutUser();
            }
            System.out.println("\n");
        }

        //Wyświetlenie wszystkich użytkowników których płec to kobieta
        em.getTransaction().begin();
        Query queryKobiety = em.createQuery("SELECT u FROM User u WHERE u.sex = 'FEMALE'");
        List<User> listaKobiety = queryKobiety.getResultList();
        em.getTransaction().commit();
        if (kowalscy.isEmpty()){
            System.out.println("Brak uzytkownikow o płci pięknej");
        } else {
            System.out.println("Liczba użytkowników o płaci pięknej: " + listaKobiety.stream().count() + "\n");

            for (User x: listaKobiety) {
                System.out.println("Informacje o uzytkowniku o płci pięknej");
                x.infoAboutUser();
                System.out.println("\n");
            }
        }

        //Dodanie nowej roli nowo stworzonemu uzytkownikowi
        User userWithRoles = new User(null,"test_6","test_6","Florian","Górski", Sex.MALE);
        em.getTransaction().begin();
        Query queryDwieRole = em.createQuery("SELECT r FROM Role r WHERE r.name = 'Administrator' OR r.name = 'Wydawca'");
        List<Role> roleListQuery = queryDwieRole.getResultList();

        if (!roleListQuery.isEmpty()){
            for (Role rola: roleListQuery) {
                userWithRoles.addRole(rola);
            }
        }
        em.persist(userWithRoles);
        em.getTransaction().commit();


        //Dodanie nowego uzytkownika wraz z dodaniem go do dwóch grup
        //Krok pierwszy dodanie grup do bazy danych
        ArrayList<UsersGroup> userGroupArr = new ArrayList<>();
        userGroupArr.add(new UsersGroup());
        userGroupArr.add(new UsersGroup());
        em.getTransaction().begin();
        for (UsersGroup x: userGroupArr) {
            em.persist(x);
        }
        em.getTransaction().commit();

        //Pobranie z bazy danej grupy przez find i przypisanie do nowego uzytkownika
        UsersGroup group1 = em.find(UsersGroup.class, 1L);
        User userWithGroup = new User(null,"test_7","test_7","Magda","Szewczyk", Sex.FEMALE);
        userWithGroup.addGroup(group1);
        em.getTransaction().begin();
        em.persist(userWithGroup);
        em.getTransaction().commit();


        //Image to dataBase
        File fi = new File("src/main/resources/avatars/avatar.jpg");
        try {
            byte[] fileContent = Files.readAllBytes(fi.toPath());
            User userWithAvatar = new User(null,"test_8","test_8","Aleksander","Mazurek", Sex.FEMALE);
            userWithAvatar.setImage(fileContent);
            em.getTransaction().begin();
            em.persist(userWithAvatar);
            em.getTransaction().commit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        em.close();
        factory.close();
    }
}
