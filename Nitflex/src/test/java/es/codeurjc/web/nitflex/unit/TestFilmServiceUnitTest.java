package es.codeurjc.web.nitflex.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import es.codeurjc.web.nitflex.dto.film.CreateFilmRequest;
import es.codeurjc.web.nitflex.dto.film.FilmDTO;
import es.codeurjc.web.nitflex.dto.film.FilmMapper;
import es.codeurjc.web.nitflex.model.Film;
import es.codeurjc.web.nitflex.repository.FilmRepository;
import es.codeurjc.web.nitflex.service.FilmService;
import es.codeurjc.web.nitflex.service.exceptions.FilmNotFoundException;
import es.codeurjc.web.nitflex.utils.ImageUtils;

public class TestFilmServiceUnitTest {

    @Test
    public void saveFilmWithoutImage() {

        FilmRepository filmRepository = mock(FilmRepository.class);
        ImageUtils imageUtils = mock(ImageUtils.class);
        FilmMapper filmMapper = mock(FilmMapper.class);

        FilmService filmService = new FilmService(filmRepository, null, imageUtils, filmMapper);

        CreateFilmRequest createFilmRequest = new CreateFilmRequest("El silencio de los corderos",
                "Tras una serie de crímenes donde a las víctimas les faltaba parte de la piel, una agente del FBI inicia su carrera particular para dar con el asesino. Para resolver el caso la agente encargada deberá envtrevistarse con el doctor Hannibal Lecter.",
                1991, "+18");
        Film film = new Film();
        film.setId(1L);
        film.setTitle("El silencio de los corderos");

        FilmDTO filmDTO = new FilmDTO(1L, "El silencio de los corderos",
                "Tras una serie de crímenes donde a las víctimas les faltaba parte de la piel, una agente del FBI inicia su carrera particular para dar con el asesino. Para resolver el caso la agente encargada deberá envtrevistarse con el doctor Hannibal Lecter.",
                1991, "+18", List.of(), List.of());

        when(filmMapper.toDomain(createFilmRequest)).thenReturn(film);
        when(filmRepository.save(any(Film.class))).thenReturn(film);
        when(filmMapper.toDTO(film)).thenReturn(filmDTO);

        FilmDTO result = filmService.save(createFilmRequest);

        assertNotNull(result);
        assertEquals("El silencio de los corderos",result.title());
    }

    @Test
    public void EraseANotExistingFilm() {

        FilmRepository filmRepository = mock(FilmRepository.class);
        ImageUtils imageUtils = mock(ImageUtils.class);
        FilmMapper filmMapper = mock(FilmMapper.class);
        FilmService filmService = new FilmService(filmRepository, null, imageUtils, filmMapper);
        long id=0;

        when(filmRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(FilmNotFoundException.class, () -> filmService.delete(id));
        verify(filmRepository, times(1)).findById(id);
    }
}
