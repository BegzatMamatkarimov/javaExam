package com.arbek.controllers;

import com.arbek.dto.MovieDto;
import com.arbek.dto.MoviePageResponse;
import com.arbek.service.MovieService;
import com.arbek.utils.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/movies/")
@CrossOrigin(origins = "*")
public class MovieController {
  private final MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  // добавления фильма
  @PostMapping("/")
  public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file,
                                                  @RequestPart String movieDto) throws IOException {
    MovieDto dto = convertToMovieDto(movieDto);
    return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);
  }

  @GetMapping("/{movieId}")
  public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId) {
    return ResponseEntity.ok(movieService.getMovie(movieId));
  }

  @GetMapping("/")
  public ResponseEntity<List<MovieDto>> getAllMoviesHandler() {
    return ResponseEntity.ok(movieService.getAlMovies());
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/{movieId}")
  public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Long movieId,
                                                     @RequestPart String movieDto) throws IOException {
    MovieDto dto = convertToMovieDto(movieDto);
    return ResponseEntity.ok(movieService.updateMovie(movieId, dto));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("/{movieId}")
  public ResponseEntity<Void> deleteMovieHandler(@PathVariable Long movieId) {
    movieService.deleteMovie(movieId);
    return ResponseEntity.noContent().build();
  }


  @GetMapping("/allMoviesPage")
  public ResponseEntity<MoviePageResponse> getMoviesWithPagination(
          @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
          @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
  ) {
    return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber, pageSize));
  }

  @GetMapping("/allMoviesPageSort")
  public ResponseEntity<MoviePageResponse> getMoviesWithPaginationAndSorting(
          @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
          @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
          @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
          @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
  ) {
    return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDir));
  }

  // Поиск фильмов по названию
  @GetMapping("/by_title")
  public ResponseEntity<List<MovieDto>> getMoviesByTitle(@RequestParam String title) {
    List<MovieDto> movies = movieService.getMoviesByTitle(title);
    return ResponseEntity.ok(movies);
  }

  // Фильтр фильмов по жанру
  @GetMapping("/filter")
  public ResponseEntity<List<MovieDto>> getMoviesByGenre(@RequestParam String genre) {
    List<MovieDto> movies = movieService.getMoviesByGenre(genre);
    return ResponseEntity.ok(movies);
  }

  // строк -> JSON
  private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(movieDtoObj, MovieDto.class);
  }
}
