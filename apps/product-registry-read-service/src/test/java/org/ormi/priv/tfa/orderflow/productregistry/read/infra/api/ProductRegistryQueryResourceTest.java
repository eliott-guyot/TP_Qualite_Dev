package org.ormi.priv.tfa.orderflow.productregistry.read.infra.api;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ormi.priv.tfa.orderflow.productregistry.read.application.ReadProductService;
import org.ormi.priv.tfa.orderflow.productregistry.read.application.ReadProductService.SearchPaginatedResult;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@QuarkusTest
public class ProductRegistryQueryResourceTest {

    @InjectMock
    ReadProductService readProductService;

    @Test
    public void testSearchProducts_Valid() {
        // Mock du service pour retourner un résultat vide mais valide
        Mockito.when(readProductService.searchProducts(anyString(), anyInt(), anyInt()))
               .thenReturn(new SearchPaginatedResult(Collections.emptyList(), 0));

        given()
            .queryParam("page", 0)
            .queryParam("size", 10)
        .when()
            .get("/products")
        .then()
            .statusCode(200);
    }

    @Test
    public void testSearchProducts_InvalidPage() {
        // Test avec page négative -> Doit retourner 400 Bad Request
        given()
            .queryParam("page", -1)
            .queryParam("size", 10)
        .when()
            .get("/products")
        .then()
            .statusCode(400);
    }

    @Test
    public void testSearchProducts_InvalidSize() {
        // Test avec size à 0 -> Doit retourner 400 Bad Request
        given()
            .queryParam("page", 0)
            .queryParam("size", 0)
        .when()
            .get("/products")
        .then()
            .statusCode(400);
    }

    @Test
    public void testGetProductById_InvalidUUID() {
        // Test avec un ID qui n'est pas un UUID -> Doit retourner 400 Bad Request (géré par votre try-catch)
        given()
        .when()
            .get("/products/ceci-n-est-pas-un-uuid")
        .then()
            .statusCode(400);
    }

    @Test
    public void testGetProductById_NotFound() {
        // Mock du service pour simuler un produit non trouvé
        Mockito.when(readProductService.findById(any()))
               .thenReturn(Optional.empty());

        String randomId = UUID.randomUUID().toString();

        given()
        .when()
            .get("/products/" + randomId)
        .then()
            .statusCode(404);
    }
}