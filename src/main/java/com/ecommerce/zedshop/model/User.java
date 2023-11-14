package com.ecommerce.zedshop.model;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.*;


@Entity
@Setter
@Getter

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements Serializable {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;

    @Column(nullable = false,unique = true)
    private String email;
    private String password;
    private boolean enabled;
    private Date date;
    private boolean seller;

    @OneToOne(mappedBy = "user")
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @OneToMany( mappedBy = "user")
    private List<Product> product;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    public void setRoles(List<Role> newRoles) {
        roles.clear();
        roles.addAll(newRoles);
    }

    public boolean hasRole(String roleName) {

        for (Role role : this.roles) {
            if (role.getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    private String resetPasswordToken;
    public boolean getSeller(){
       return seller;
   }



}
