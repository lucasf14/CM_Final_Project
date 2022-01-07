package com.example.cmproject.objects;

public class Contact {
    private long id;
    private String name;
    private String contact;

    public Contact(long id, String name, String contact){
        this.id=id;
        this.name=name;
        this.contact=contact;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
