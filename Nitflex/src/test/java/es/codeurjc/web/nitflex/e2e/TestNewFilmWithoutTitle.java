/*package es.codeurjc.web.nitflex.e2e;

import java.util.List;

import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestNewFilmWithoutTitle {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Establece el path del driver de Chrome
        driver = new ChromeDriver();
    }

    @Test
    public void testCreateFilmWithoutTitle() throws InterruptedException {
        // Abrir la página de creación de película
        driver.get("http://localhost:8080/films/new");

        // Encontrar el formulario de creación de película
        WebElement yearField = driver.findElement(By.name("releaseYear"));
        WebElement synopsisField = driver.findElement(By.name("synopsis"));
        WebElement saveButton = driver.findElement(By.id("Save"));

        // Dejar el título vacío
        yearField.clear();
        yearField.sendKeys("2023"); // Año de lanzamiento válido
        synopsisField.sendKeys("A synopsis of the film");

        // Hacer clic en el botón de guardar (submit)
        saveButton.click();

        // Verificar que se muestra el mensaje de error
        String errorMessage = driver.findElement(By.cssSelector("#error-list li")).getText();
        assertEquals("The title is empty", errorMessage);

        // Verificar que el error contiene el mensaje esperado
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

        // Verificar que la película no aparece en la página principal
        driver.get("http://localhost:8080/");

        // Esperar unos segundos para que se cargue la página principal
        Thread.sleep(2000); // Pausa de 2 segundos (ajustar según el tiempo de carga real)

        // Verificar que no aparece la película sin título en la lista
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

    // Método de limpieza después de la ejecución del test
    @AfterEach
    public void tearDown() {
        // Cerrar el navegador
        driver.quit();
    }
}
*/
