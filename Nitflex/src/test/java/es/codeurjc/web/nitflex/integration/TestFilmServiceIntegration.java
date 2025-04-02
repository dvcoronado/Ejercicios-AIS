package es.codeurjc.web.nitflex.integration;

import java.sql.Blob;
import java.util.Arrays;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.codeurjc.web.nitflex.dto.film.CreateFilmRequest;
import es.codeurjc.web.nitflex.dto.film.FilmDTO;
import es.codeurjc.web.nitflex.dto.film.FilmSimpleDTO;
import es.codeurjc.web.nitflex.model.Film;
import es.codeurjc.web.nitflex.repository.FilmRepository;
import es.codeurjc.web.nitflex.service.FilmService;

@SpringBootTest
public class TestFilmServiceIntegration {

    @Autowired
    private FilmService filmService;

    @Autowired
    private FilmRepository filmRepository;

    @Test
    public void testSaveFilmWithValidTitle() {
        CreateFilmRequest film = new CreateFilmRequest(
                "El silencio de los corderos",
                "Tras una serie de crímenes donde a las víctimas les faltaba parte de la piel, una agente del FBI inicia su carrera particular para dar con el asesino. Para resolver el caso la agente encargada deberá entrevistarse con el doctor Hannibal Lecter.",
                1991, "+18");

        FilmDTO result = filmService.save(film);

        assertNotNull(result);
        assertEquals("El silencio de los corderos", result.title());
        assertEquals(1991, result.releaseYear());
        assertEquals("+18", result.ageRating());

        Optional<Film> savedFilm = filmRepository.findById(result.id());
        assertTrue(savedFilm.isPresent());
        assertEquals("El silencio de los corderos", savedFilm.get().getTitle());
        assertEquals(1991, savedFilm.get().getReleaseYear());
        assertEquals("+18", savedFilm.get().getAgeRating());
    }

    @Test
    public void updateTitleAndSynopsisWithExistingImage() throws Exception {
        
        Film existingFilm = new Film();
        existingFilm.setTitle("El silencio de los corderos");
        existingFilm.setSynopsis("Synopsis");
        existingFilm.setReleaseYear(1991);
        existingFilm.setAgeRating("+18");
        byte[] imageData = "dummy_image_data".getBytes();
        Blob imageBlob = new SerialBlob(imageData);
        existingFilm.setPosterFile(imageBlob);
        
        Film savedFilm =filmRepository.save(existingFilm);
        Long filmId = savedFilm.getId();
        assertNotNull(filmId, "Film ID should not be null after saving");

        FilmSimpleDTO updatedFilm = new FilmSimpleDTO(filmId, "Nuevo título", "Nueva sinopsis", 1991, "+18");
        FilmDTO result = filmService.update(filmId, updatedFilm, null);
    
        assertNotNull(result);
        assertEquals("Nuevo título", result.title());
        assertEquals("Nueva sinopsis", result.synopsis());
        assertEquals(1991, result.releaseYear());
        assertEquals("+18", result.ageRating());
    
        byte[] originalImageData = imageBlob.getBytes(1, (int) imageBlob.length());
        byte[] updatedImageData = existingFilm.getPosterFile().getBytes(1, (int) existingFilm.getPosterFile().length());
        
        assertTrue(Arrays.equals(originalImageData, updatedImageData));
        Film updatedFilmFromDB = filmRepository.findById(filmId).orElseThrow();
        byte[] updatedImageFromDB = updatedFilmFromDB.getPosterFile().getBytes(1, (int) updatedFilmFromDB.getPosterFile().length());
        assertTrue(Arrays.equals(originalImageData, updatedImageFromDB));

    }
}
