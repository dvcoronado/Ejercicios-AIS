package es.codeurjc.web.nitflex.integration;

import java.sql.Blob;
import java.util.Arrays;

import javax.sql.rowset.serial.SerialBlob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.codeurjc.web.nitflex.dto.film.FilmDTO;
import es.codeurjc.web.nitflex.dto.film.FilmSimpleDTO;
import es.codeurjc.web.nitflex.model.Film;
import es.codeurjc.web.nitflex.repository.FilmRepository;
import es.codeurjc.web.nitflex.service.FilmService;
import es.codeurjc.web.nitflex.utils.ImageUtils;

@SpringBootTest
public class TestUpdateTitleAndSynopsis {

    @Autowired
    FilmRepository filmRepository;
    
    @Autowired
    ImageUtils imageUtils;

    @Autowired
    FilmService filmService;

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
