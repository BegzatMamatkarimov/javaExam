package com.arbek.service;

import com.arbek.dto.MovieDto;
import com.arbek.dto.MoviePageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface MovieService {

  MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;

  MovieDto getMovie(Integer movieId);

  List<MovieDto> getAlMovies();

  MovieDto updateMovie(Long movieId, MovieDto movieDto) throws IOException;

  void deleteMovie(Long movieId);

  MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize);

  MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir);

  List<MovieDto> getMoviesByTitle(String title);

  List<MovieDto> getMoviesByGenre(String genre);
}
