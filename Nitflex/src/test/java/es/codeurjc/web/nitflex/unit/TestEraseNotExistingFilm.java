package es.codeurjc.web.nitflex.unit;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.codeurjc.web.nitflex.dto.film.FilmMapper;
import es.codeurjc.web.nitflex.repository.FilmRepository;
import es.codeurjc.web.nitflex.service.FilmService;
import es.codeurjc.web.nitflex.service.exceptions.FilmNotFoundException;
import es.codeurjc.web.nitflex.utils.ImageUtils;

public class TestEraseNotExistingFilm {

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


