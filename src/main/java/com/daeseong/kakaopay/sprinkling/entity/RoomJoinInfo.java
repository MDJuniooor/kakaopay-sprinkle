package com.daeseong.kakaopay.sprinkling.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor()
public class RoomJoinInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime joinedDate;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    public static RoomJoinInfo joinRoom(User user, Room room) {
        RoomJoinInfo roomJoinInfo = new RoomJoinInfo();
        roomJoinInfo.setUser(user);
        roomJoinInfo.setRoom(room);
        roomJoinInfo.setJoinedDate(LocalDateTime.now());
        return roomJoinInfo;
    }
}
