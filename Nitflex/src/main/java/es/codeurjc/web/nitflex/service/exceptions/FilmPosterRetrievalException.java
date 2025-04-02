package es.codeurjc.web.nitflex.service.exceptions;

public class FilmPosterRetrievalException extends RuntimeException {

    public FilmPosterRetrievalException(long filmId, Throwable cause) {
        super("Error retrieving poster for film with ID " + filmId, cause);
    }
}
