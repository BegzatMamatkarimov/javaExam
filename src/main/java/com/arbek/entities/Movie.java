package com.arbek.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Movie {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "movie_id", nullable = false)
  private Long movie_id;

  @Column(name = "title", nullable = false)
  @NotBlank(message = "Movie's title couldn't be empty!")
  private String title;

  @Column(name = "description", nullable = false)
  @NotBlank(message = "Movie's description couldn't be empty!")
  private String description;

  @Column(name = "rating", nullable = false)
  @NotNull(message = "Movie's rating couldn't be empty!")
  private Integer rating;

  @Column(name = "release_year", nullable = false)
  @NotNull(message = "Movie's release year couldn't be empty!")
  private Integer releaseYear;

  @Column(name = "duration", nullable = false)
  @NotNull(message = "Movie's title couldn't be empty!")
  private Integer duration;

  @ElementCollection
  @CollectionTable(name = "movie_genres")
  private List<String> genres;

  @ElementCollection
  @CollectionTable(name = "movie_directors")
  private List<String> directors;

  @ElementCollection
  @CollectionTable(name = "movie_stars")
  private List<String> stars;

  @Column(name = "poster")
  @NotBlank(message = "Movie's poster couldn't be empty!")
  private String poster;
}
