package models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Chat() {
    }

    public Long getId() { return id; }
}
