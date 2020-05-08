package TradeZone.service.validation;

import TradeZone.data.model.rest.AdvertisementBaseModel;
import TradeZone.data.model.rest.AdvertisementCreateModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AdvertisementValidationServiceTests {

    private static final int MIN_TITLE_LENGTH = 5;
    private static final int MAX_TITLE_LENGTH = 20;

    AdvertisementValidationService advertisementValidationService;

    AdvertisementBaseModel advertisement;

    @Before
    public void init() {
        advertisementValidationService = new AdvertisementValidationService();
        advertisement = new AdvertisementCreateModel();
        advertisement.setCategory(1L);
        advertisement.setCondition("NEW");
        advertisement.setCreator("creator");
        advertisement.setDelivery("COURIER");
        advertisement.setDescription("lorem ipsum");
        advertisement.setPrice(BigDecimal.valueOf(10));
        advertisement.setTitle("Audi A6");
        ((AdvertisementCreateModel) advertisement).setImages(new String[]{"image"});
    }

    @Test
    public void isValid_shouldReturnTrue_whenValidModel() {
        assertTrue(advertisementValidationService.isValid(advertisement));
    }

    @Test
    public void isValid_shouldReturnFalse_whenTitleStartsWithLowercaseLetter() {
        advertisement.setTitle("invalid");
        assertFalse(advertisementValidationService.isValid(advertisement));
    }

    @Test
    public void isValid_shouldReturnFalse_whenTitleLengthMoreThanMAX() {
        advertisement.setTitle("invaliddddddddddddddddd");
        assertFalse(advertisementValidationService.isValid(advertisement));
    }

    @Test
    public void isValid_shouldReturnFalse_whenTitleLengthLessThanMIN() {
        advertisement.setTitle("inva");
        assertFalse(advertisementValidationService.isValid(advertisement));
    }

    @Test
    public void isValid_shouldReturnFalse_whenHasNoImages() {
        ((AdvertisementCreateModel)advertisement).setImages(null);
        assertFalse(advertisementValidationService.isValid(advertisement));
    }

    @Test
    public void isValid_shouldReturnFalse_whenInvalidCondition() {
        advertisement.setCondition("invalid");
        assertFalse(advertisementValidationService.isValid(advertisement));
    }

    @Test
    public void isValid_shouldReturnFalse_whenInvalidDeliveryType() {
        advertisement.setDelivery("invalid");
        assertFalse(advertisementValidationService.isValid(advertisement));
    }
}
