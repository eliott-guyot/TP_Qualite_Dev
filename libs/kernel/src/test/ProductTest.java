package org.ormi.priv.tfa.orderflow.kernel;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Product du Kernel.
 * Couvre la logique métier de création, mise à jour et suppression des produits.
 */
@DisplayName("Product - Tests unitaires")
class ProductTest {

    private static final String VALID_NAME = "Produit Test";
    private static final String VALID_DESCRIPTION = "Description du produit test";
    private static final SkuId VALID_SKU_ID = new SkuId("ABC-12345");

    @Nested
    @DisplayName("Méthode statique create")
    class CreateTests {

        @Test
        @DisplayName("Doit créer un produit valide")
        void shouldCreateValidProduct() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);

            
            assertNotNull(product, "Le produit ne doit pas être null");
            assertNotNull(product.getId(), "L'ID du produit ne doit pas être null");
            assertEquals(VALID_NAME, product.getName(), "Le nom du produit doit correspondre");
            assertEquals(VALID_DESCRIPTION, product.getDescription(), "La description doit correspondre");
            assertEquals(VALID_SKU_ID, product.getSkuId(), "Le SKU ID doit correspondre");
            assertEquals(ProductLifecycle.ACTIVE, product.getStatus(), "Le produit doit être à l'état ACTIVE");
            assertEquals(1L, product.getVersion(), "La version initiale doit être 1");
        }

        @Test
        @DisplayName("Ne doit pas jeter d'exception pour un produit valide")
        void shouldNotThrowExceptionForValidProduct() {
             
            assertDoesNotThrow(() -> Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID),
                    "La création d'un produit valide ne doit pas jeter d'exception");
        }

        @Test
        @DisplayName("Doit mettre le produit à l'état ACTIVE après création")
        void shouldSetProductToActiveState() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);

            
            assertEquals(ProductLifecycle.ACTIVE, product.getStatus(),
                    "Le produit doit passer à l'état ACTIVE après création");
        }

        @Test
        @DisplayName("Doit jeter une exception si le nom est null")
        void shouldThrowExceptionWhenNameIsNull() {
             
            assertThrows(ConstraintViolationException.class,
                    () -> Product.create(null, VALID_DESCRIPTION, VALID_SKU_ID),
                    "La création avec un nom null doit jeter une ConstraintViolationException");
        }

        @Test
        @DisplayName("Doit jeter une exception si le nom est vide")
        void shouldThrowExceptionWhenNameIsEmpty() {
             
            assertThrows(ConstraintViolationException.class,
                    () -> Product.create("", VALID_DESCRIPTION, VALID_SKU_ID),
                    "La création avec un nom vide doit jeter une ConstraintViolationException");
        }

        @Test
        @DisplayName("Doit jeter une exception si le nom est composé uniquement d'espaces")
        void shouldThrowExceptionWhenNameIsBlank() {
             
            assertThrows(ConstraintViolationException.class,
                    () -> Product.create("   ", VALID_DESCRIPTION, VALID_SKU_ID),
                    "La création avec un nom blank doit jeter une ConstraintViolationException");
        }

        @Test
        @DisplayName("Doit jeter une exception si la description est null")
        void shouldThrowExceptionWhenDescriptionIsNull() {
             
            assertThrows(ConstraintViolationException.class,
                    () -> Product.create(VALID_NAME, null, VALID_SKU_ID),
                    "La création avec une description null doit jeter une ConstraintViolationException");
        }

        @Test
        @DisplayName("Doit jeter une exception si le SKU ID est null")
        void shouldThrowExceptionWhenSkuIdIsNull() {
             
            assertThrows(ConstraintViolationException.class,
                    () -> Product.create(VALID_NAME, VALID_DESCRIPTION, null),
                    "La création avec un SKU ID null doit jeter une ConstraintViolationException");
        }
    }

    @Nested
    @DisplayName("Méthode updateName")
    class UpdateNameTests {

        @Test
        @DisplayName("Doit mettre à jour le nom d'un produit actif")
        void shouldUpdateNameOfActiveProduct() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);
            String newName = "Nouveau nom";
            Long initialVersion = product.getVersion();

            
            var event = product.updateName(newName);

            
            assertEquals(newName, product.getName(), "Le nom doit être mis à jour");
            assertEquals(initialVersion + 1, product.getVersion(), "La version doit être incrémentée");
            assertNotNull(event, "L'événement ne doit pas être null");
        }

        @Test
        @DisplayName("Ne doit pas jeter d'exception lors de la mise à jour d'un produit actif")
        void shouldNotThrowExceptionWhenUpdatingActiveProduct() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);

             
            assertDoesNotThrow(() -> product.updateName("Nouveau nom"),
                    "La mise à jour d'un produit actif ne doit pas jeter d'exception");
        }

        @Test
        @DisplayName("Doit jeter une exception si le nouveau nom est null")
        void shouldThrowExceptionWhenNewNameIsNull() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);

             
            assertThrows(ConstraintViolationException.class,
                    () -> product.updateName(null),
                    "La mise à jour avec un nom null doit jeter une ConstraintViolationException");
        }

        @Test
        @DisplayName("Doit jeter une exception si le nouveau nom est vide")
        void shouldThrowExceptionWhenNewNameIsEmpty() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);

             
            assertThrows(ConstraintViolationException.class,
                    () -> product.updateName(""),
                    "La mise à jour avec un nom vide doit jeter une ConstraintViolationException");
        }

        @Test
        @DisplayName("Doit jeter une exception si le nouveau nom est blank")
        void shouldThrowExceptionWhenNewNameIsBlank() {
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);

            assertThrows(ConstraintViolationException.class,
                    () -> product.updateName("   "),
                    "La mise à jour avec un nom blank doit jeter une ConstraintViolationException");
        }

        @Test
        @DisplayName("Doit jeter une exception lors de la mise à jour d'un produit retiré")
        void shouldThrowExceptionWhenUpdatingRetiredProduct() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);
            product.retire(); 

            
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> product.updateName("Nouveau nom"),
                    "La mise à jour d'un produit retiré doit jeter une IllegalStateException");
            assertEquals("Cannot update a retired product", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Méthode updateDescription")
    class UpdateDescriptionTests {

        @Test
        @DisplayName("Doit mettre à jour la description d'un produit actif")
        void shouldUpdateDescriptionOfActiveProduct() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);
            String newDescription = "Nouvelle description";
            Long initialVersion = product.getVersion();

            
            var event = product.updateDescription(newDescription);

            
            assertEquals(newDescription, product.getDescription(), "La description doit être mise à jour");
            assertEquals(initialVersion + 1, product.getVersion(), "La version doit être incrémentée");
            assertNotNull(event, "L'événement ne doit pas être null");
        }

        @Test
        @DisplayName("Ne doit pas jeter d'exception lors de la mise à jour d'un produit actif")
        void shouldNotThrowExceptionWhenUpdatingActiveProduct() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);

             
            assertDoesNotThrow(() -> product.updateDescription("Nouvelle description"),
                    "La mise à jour d'un produit actif ne doit pas jeter d'exception");
        }

        @Test
        @DisplayName("Doit jeter une exception si la nouvelle description est null")
        void shouldThrowExceptionWhenNewDescriptionIsNull() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);

             
            assertThrows(ConstraintViolationException.class,
                    () -> product.updateDescription(null),
                    "La mise à jour avec une description null doit jeter une ConstraintViolationException");
        }

        @Test
        @DisplayName("Doit jeter une exception lors de la mise à jour d'un produit retiré")
        void shouldThrowExceptionWhenUpdatingRetiredProduct() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);
            product.retire();

             
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> product.updateDescription("Nouvelle description"),
                    "La mise à jour d'un produit retiré doit jeter une IllegalStateException");
            assertEquals("Cannot update the product", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Méthode retire (suppression)")
    class RetireTests {

        @Test
        @DisplayName("Doit retirer un produit actif")
        void shouldRetireActiveProduct() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);
            Long initialVersion = product.getVersion();

            
            var event = product.retire();

            
            assertEquals(ProductLifecycle.RETIRED, product.getStatus(),
                    "Le produit doit passer à l'état RETIRED");
            assertEquals(initialVersion + 1, product.getVersion(), "La version doit être incrémentée");
            assertNotNull(event, "L'événement ne doit pas être null");
        }

        @Test
        @DisplayName("Ne doit pas jeter d'exception lors du retrait d'un produit actif")
        void shouldNotThrowExceptionWhenRetiringActiveProduct() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);

             
            assertDoesNotThrow(() -> product.retire(),
                    "Le retrait d'un produit actif ne doit pas jeter d'exception");
        }

        @Test
        @DisplayName("Le produit doit passer à l'état RETIRED après suppression")
        void shouldTransitionToRetiredState() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);

            
            product.retire();

            
            assertEquals(ProductLifecycle.RETIRED, product.getStatus(),
                    "Le produit doit passer à l'état RETIRED après suppression");
        }

        @Test
        @DisplayName("Doit jeter une exception lors du retrait d'un produit déjà retiré")
        void shouldThrowExceptionWhenRetiringAlreadyRetiredProduct() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);
            product.retire(); 

             
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> product.retire(),
                    "Le retrait d'un produit déjà retiré doit jeter une IllegalStateException");
            assertEquals("Cannot retire the product", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests d'intégration des états")
    class StateIntegrationTests {

        @Test
        @DisplayName("Un produit retiré ne peut plus être modifié")
        void retiredProductCannotBeModified() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);
            product.retire();

             
            assertThrows(IllegalStateException.class,
                    () -> product.updateName("Nouveau nom"),
                    "Un produit retiré ne peut pas avoir son nom modifié");
            assertThrows(IllegalStateException.class,
                    () -> product.updateDescription("Nouvelle description"),
                    "Un produit retiré ne peut pas avoir sa description modifiée");
        }

        @Test
        @DisplayName("Les versions doivent s'incrémenter correctement")
        void versionsShouldIncrementCorrectly() {
            
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_SKU_ID);
            assertEquals(1L, product.getVersion(), "Version initiale = 1");

            
            product.updateName("Nouveau nom");
            assertEquals(2L, product.getVersion(), "Version après updateName = 2");

            product.updateDescription("Nouvelle description");
            assertEquals(3L, product.getVersion(), "Version après updateDescription = 3");

            product.retire();
            assertEquals(4L, product.getVersion(), "Version après retire = 4");
        }
    }
}
