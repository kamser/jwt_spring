package com.example.demo.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "_user")
@Builder
@Data
@NoArgsConstructor //This next two annotation is for the cases when I have a constructor with no args and with all args.
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) //This annotiation is the one than made posible to autogenerate the id with no need to pass it.
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
