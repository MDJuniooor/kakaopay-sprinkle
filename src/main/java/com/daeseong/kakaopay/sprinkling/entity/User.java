package com.daeseong.kakaopay.sprinkling.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
