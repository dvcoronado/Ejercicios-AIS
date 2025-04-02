package es.codeurjc.web.nitflex.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TestNewFilmWithoutImage {
    private WebDriver driver;

    @Test
    public void testCreateFilm() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:8080");

        WebElement createFilmButton = driver.findElement(By.id("create-film"));
        assertNotNull(createFilmButton, "El botón de 'Nueva Película' no está presente");
        createFilmButton.click();

        WebElement titleField = driver.findElement(By.name("title"));
        WebElement releaseYearField = driver.findElement(By.name("releaseYear"));
        WebElement synopsisField = driver.findElement(By.name("synopsis"));

        titleField.sendKeys("El silencio de los corderos");
        releaseYearField.clear();
        releaseYearField.sendKeys("2025");
        synopsisField.sendKeys("This is a test movie description.");

        WebElement submitButton = driver.findElement(By.id("Save"));
        submitButton.click();

        WebElement filmTitle = driver.findElement(By.id("film-title"));
        assertNotNull(filmTitle, "La película no aparece en la página de detalles");
        assertEquals("El silencio de los corderos", filmTitle.getText(), "El título de la película no es correcto");

        WebElement filmReleaseYear = driver.findElement(By.id("film-releaseYear"));
        assertEquals("2025", filmReleaseYear.getText(), "El año de estreno de la película no es correcto");

        WebElement filmSynopsis = driver.findElement(By.id("film-synopsis"));
        assertEquals("This is a test movie description.", filmSynopsis.getText(),
                "La sinopsis de la película no es correcta");

        filmTitle.click();
        WebElement filmDetailTitle = driver.findElement(By.id("film-title"));
        assertEquals("El silencio de los corderos", filmDetailTitle.getText(),
                "El título de la película no es correcto en la página de detalles");

        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    public void setUp() {
        // Establece el path del driver de Chrome
        driver = new ChromeDriver();
    }

    @Test
    public void testCreateFilmWithoutTitle() {
        driver.get("http://localhost:8080/films/new");

        WebElement yearField = driver.findElement(By.name("releaseYear"));
        WebElement synopsisField = driver.findElement(By.name("synopsis"));
        WebElement saveButton = driver.findElement(By.id("Save"));

        yearField.clear();
        yearField.sendKeys("2023"); 
        synopsisField.sendKeys("A synopsis of the film");

        saveButton.click();

        String errorMessage = driver.findElement(By.cssSelector("#error-list li")).getText();
        assertEquals("The title is empty", errorMessage);

        WebElement errorList = driver.findElement(By.id("error-list"));
        List<WebElement> errors = errorList.findElements(By.tagName("li"));
        boolean foundError = false;

        for (WebElement error : errors) {
            if (error.getText().contains("The title is empty")) {
                foundError = true;
                break;
            }
        }

        assertTrue(foundError, "Expected error message not found");

        driver.get("http://localhost:8080/");

        List<WebElement> films = driver.findElements(By.className("film-item"));
        boolean filmFound = false;

        for (WebElement film : films) {
            if (film.getText().contains("A title")) {
                filmFound = true;
                break;
            }
        }

        assertFalse(filmFound, "Film without title appears on the main page");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
