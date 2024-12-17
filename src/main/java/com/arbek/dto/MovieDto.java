package com.arbek.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

  private Long movie_id;

  @NotBlank(message = "Movie's title couldn't be empty!")
  private String title;

  @NotBlank(message = "Movie's description couldn't be empty!")
  private String description;

  @NotNull(message = "Movie's rating couldn't be empty!")
  private Integer rating;

  @NotNull(message = "Movie's release year couldn't be empty!")
  private Integer releaseYear;

  @NotNull(message = "Movie's title couldn't be empty!")
  private Integer duration;

  private List<String> genres;

  private List<String> directors;

  private List<String> stars;

  @NotBlank(message = "Please private movie's poster!")
  private String poster;

  @NotBlank(message = "Please private poster's URL!")
  private String posterUrl;



}
