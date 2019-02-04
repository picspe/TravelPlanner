package com.pepic.TravelPlanner.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private String content;
    private Long tripId;
    private String author;
}
