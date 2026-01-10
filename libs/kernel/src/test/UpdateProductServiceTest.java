import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductDescriptionUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductNameUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;





class UpdateProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private EventLogRepository eventLogRepository;

    @Mock
    private OutboxRepository outboxRepository;

    @InjectMocks
    private UpdateProductService updateProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUpdateProductName() {
        
        var productId = 1L;
        var newName = "Updated Name";
        var command = new ProductCommand.UpdateProductNameCommand(productId, newName);

        Product mockProduct = mock(Product.class);
        EventEnvelope<ProductNameUpdated> mockEvent = mock(EventEnvelope.class);
        EventLogEntity mockEventLogEntity = mock(EventLogEntity.class);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(mockProduct.updateName(newName)).thenReturn(mockEvent);
        when(eventLogRepository.append(mockEvent)).thenReturn(mockEventLogEntity);

        
        updateProductService.handle(command);

        
        verify(productRepository).save(mockProduct);
        verify(eventLogRepository).append(mockEvent);
        verify(outboxRepository).publish(any(OutboxEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundForNameUpdate() {
        
        var productId = 1L;
        var newName = "Updated Name";
        var command = new ProductCommand.UpdateProductNameCommand(productId, newName);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> updateProductService.handle(command));
        verify(productRepository, never()).save(any());
        verify(eventLogRepository, never()).append(any());
        verify(outboxRepository, never()).publish(any());
    }

    @Test
    void shouldUpdateProductDescription() {
        
        var productId = 1L;
        var newDescription = "Updated Description";
        var command = new ProductCommand.UpdateProductDescriptionCommand(productId, newDescription);

        Product mockProduct = mock(Product.class);
        EventEnvelope<ProductDescriptionUpdated> mockEvent = mock(EventEnvelope.class);
        EventLogEntity mockEventLogEntity = mock(EventLogEntity.class);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(mockProduct.updateDescription(newDescription)).thenReturn(mockEvent);
        when(eventLogRepository.append(mockEvent)).thenReturn(mockEventLogEntity);

        
        updateProductService.handle(command);

        
        verify(productRepository).save(mockProduct);
        verify(eventLogRepository).append(mockEvent);
        verify(outboxRepository).publish(any(OutboxEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundForDescriptionUpdate() {
        var productId = 1L;
        var newDescription = "Updated Description";
        var command = new ProductCommand.UpdateProductDescriptionCommand(productId, newDescription);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> updateProductService.handle(command));
        verify(productRepository, never()).save(any());
        verify(eventLogRepository, never()).append(any());
        verify(outboxRepository, never()).publish(any());
    }
}