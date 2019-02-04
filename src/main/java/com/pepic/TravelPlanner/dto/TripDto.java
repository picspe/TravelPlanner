package com.pepic.TravelPlanner.dto;

import com.pepic.TravelPlanner.models.Comment;
import com.pepic.TravelPlanner.models.Trip;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TripDto {
    private Long id;
    private String username;
    private String destination;
    private LocalDate start;
    private LocalDate end;
    private Long dayCount;
    private List<Comment> comments;

    public TripDto(Trip trip, Collection<Comment> comments) {
        this.comments = new ArrayList<>(comments);
        this.id = trip.getId();
        this.destination = trip.getDestination();
        this.start = trip.getStart();
        this.end = trip.getEnd();
    }
}
