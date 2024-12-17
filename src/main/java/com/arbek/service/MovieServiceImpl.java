package com.arbek.service;

import com.arbek.dto.MovieDto;
import com.arbek.dto.MoviePageResponse;
import com.arbek.entities.Movie;
import com.arbek.repositories.MovieRepositoty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

  private final MovieRepositoty movieRepository;

  private final FileService fileService;

  @Value("${project.poster}")
  private String path;

  @Value("${base.url}")
  private String baseUrl;

  public MovieServiceImpl(MovieRepositoty movieRepositoty, FileService fileService) {
    this.movieRepository = movieRepositoty;
    this.fileService = fileService;
  }

  @Override
  public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
    String uploadedFile = fileService.uploadFile(path, file);

    movieDto.setPoster(uploadedFile);

    Movie movie = new Movie(
        movieDto.getMovie_id(),
        movieDto.getTitle(),
        movieDto.getDescription(),
        movieDto.getRating(),
        movieDto.getReleaseYear(),
        movieDto.getDuration(),
        movieDto.getGenres(),
        movieDto.getDirectors(),
        movieDto.getStars(),
        movieDto.getPoster()
    );
    Movie savedMovie = movieRepository.save(movie);

    String posterUrl = baseUrl + "/file/" + uploadedFile;

    MovieDto response = new MovieDto(
        savedMovie.getMovie_id(),
        savedMovie.getTitle(),
        savedMovie.getDescription(),
        savedMovie.getRating(),
        savedMovie.getReleaseYear(),
        savedMovie.getDuration(),
        savedMovie.getGenres(),
        savedMovie.getDirectors(),
        savedMovie.getStars(),
        savedMovie.getPoster(),
        posterUrl
    );

    return response;
  }

  @Override
  public MovieDto getMovie(Integer movieId) {
    Movie movie = movieRepository.findById(Long.valueOf(movieId)).orElseThrow(() -> new RuntimeException("Movie not found"));

    String posterUrl = baseUrl + "/file/" + movie.getPoster();

    MovieDto response = new MovieDto(
        movie.getMovie_id(),
        movie.getTitle(),
        movie.getDescription(),
        movie.getRating(),
        movie.getReleaseYear(),
        movie.getDuration(),
        movie.getGenres(),
        movie.getDirectors(),
        movie.getStars(),
        movie.getPoster(),
        posterUrl
    );
    return response;
  }

  @Override
  public List<MovieDto> getAlMovies() {
    List<Movie> movies = movieRepository.findAll();

    List<MovieDto> movieDtos = new ArrayList<>();
    for (Movie movie : movies) {
      String posterUrl = baseUrl + "/file/" + movie.getPoster();
      MovieDto movieDto = new MovieDto(
          movie.getMovie_id(),
          movie.getTitle(),
          movie.getDescription(),
          movie.getRating(),
          movie.getReleaseYear(),
          movie.getDuration(),
          movie.getGenres(),
          movie.getDirectors(),
          movie.getStars(),
          movie.getPoster(),
          posterUrl
      );
      movieDtos.add(movieDto);
    }
    return movieDtos;
  }

  @Override
  public MovieDto updateMovie(Long movieId, MovieDto movieDto) throws IOException {
    Movie existingMovie = movieRepository.findById(movieId)
        .orElseThrow(() -> new RuntimeException("Movie not found"));

    existingMovie.setTitle(movieDto.getTitle());
    existingMovie.setDescription(movieDto.getDescription());
    existingMovie.setRating(movieDto.getRating());
    existingMovie.setReleaseYear(movieDto.getReleaseYear());
    existingMovie.setDuration(movieDto.getDuration());
    existingMovie.setGenres(movieDto.getGenres());
    existingMovie.setDirectors(movieDto.getDirectors());
    existingMovie.setStars(movieDto.getStars());
    existingMovie.setPoster(movieDto.getPoster());

    Movie updatedMovie = movieRepository.save(existingMovie);

    String posterUrl = baseUrl + "/file/" + updatedMovie.getPoster();

    return new MovieDto(
        updatedMovie.getMovie_id(),
        updatedMovie.getTitle(),
        updatedMovie.getDescription(),
        updatedMovie.getRating(),
        updatedMovie.getReleaseYear(),
        updatedMovie.getDuration(),
        updatedMovie.getGenres(),
        updatedMovie.getDirectors(),
        updatedMovie.getStars(),
        updatedMovie.getPoster(),
        posterUrl
    );
  }

  @Override
  public void deleteMovie(Long movieId) {
    Movie movieToDelete = movieRepository.findById(movieId)
        .orElseThrow(() -> new RuntimeException("Movie not found"));

    movieRepository.delete(movieToDelete);
  }

  @Override
  public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    Page<Movie> moviePages = movieRepository.findAll(pageable);
    List<Movie> movies = moviePages.getContent();

    List<MovieDto> movieDtos = new ArrayList<>();
    for (Movie movie : movies) {
      String posterUrl = baseUrl + "/file/" + movie.getPoster();
      MovieDto movieDto = new MovieDto(
          movie.getMovie_id(),
          movie.getTitle(),
          movie.getDescription(),
          movie.getRating(),
          movie.getReleaseYear(),
          movie.getDuration(),
          movie.getGenres(),
          movie.getDirectors(),
          movie.getStars(),
          movie.getPoster(),
          posterUrl
      );
      movieDtos.add(movieDto);
    }

    return new MoviePageResponse(movieDtos, pageNumber, pageSize,
        moviePages.getTotalPages(),
        moviePages.getTotalElements(),
        moviePages.isLast());
  }

  @Override
  public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {

    Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    Page<Movie> moviePages = movieRepository.findAll(pageable);
    List<Movie> movies = moviePages.getContent();

    List<MovieDto> movieDtos = new ArrayList<>();
    for (Movie movie : movies) {
      String posterUrl = baseUrl + "/file/" + movie.getPoster();
      MovieDto movieDto = new MovieDto(
          movie.getMovie_id(),
          movie.getTitle(),
          movie.getDescription(),
          movie.getRating(),
          movie.getReleaseYear(),
          movie.getDuration(),
          movie.getGenres(),
          movie.getDirectors(),
          movie.getStars(),
          movie.getPoster(),
          posterUrl
      );
      movieDtos.add(movieDto);
    }

    return new MoviePageResponse(movieDtos, pageNumber, pageSize,
        moviePages.getTotalPages(),
        moviePages.getTotalElements(),
        moviePages.isLast());
  }

  @Override
  public List<MovieDto> getMoviesByTitle(String title) {
    List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(title);
    List<MovieDto> movieDtos = new ArrayList<>();
    for (Movie movie : movies) {
      String posterUrl = baseUrl + "/file/" + movie.getPoster();
      MovieDto movieDto = new MovieDto(
              movie.getMovie_id(),
              movie.getTitle(),
              movie.getDescription(),
              movie.getRating(),
              movie.getReleaseYear(),
              movie.getDuration(),
              movie.getGenres(),
              movie.getDirectors(),
              movie.getStars(),
              movie.getPoster(),
              posterUrl
      );
      movieDtos.add(movieDto);
    }
    return movieDtos;
  }

  @Override
  public List<MovieDto> getMoviesByGenre(String genre) {
    List<Movie> movies = movieRepository.findByGenresContaining(genre);
    List<MovieDto> movieDtos = new ArrayList<>();
    for (Movie movie : movies) {
      String posterUrl = baseUrl + "/file/" + movie.getPoster();
      MovieDto movieDto = new MovieDto(
              movie.getMovie_id(),
              movie.getTitle(),
              movie.getDescription(),
              movie.getRating(),
              movie.getReleaseYear(),
              movie.getDuration(),
              movie.getGenres(),
              movie.getDirectors(),
              movie.getStars(),
              movie.getPoster(),
              posterUrl
      );
      movieDtos.add(movieDto);
    }
    return movieDtos;
  }

}
