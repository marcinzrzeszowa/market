package com.mj.market.app.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserRole {

    @Id
    private Integer id;

    private String name;

}
