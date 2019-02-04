package com.pepic.TravelPlanner.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private User user;
    @Column(name = "start")
    private LocalDate start;
    @Column(name = "end")
    private LocalDate end;
    @Column(name = "destination")
    private String destination;
    @OneToMany(mappedBy = "trip")
    private Collection<Comment> comments;

    public Trip(User user, LocalDate start, LocalDate end, String destination) {
        this.user = user;
        this.start = start;
        this.end = end;
        this.destination = destination;
    }
}
