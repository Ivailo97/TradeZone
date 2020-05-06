package knight.ww3.service;

import TradeZone.data.error.exception.EntityNotFoundException;
import TradeZone.data.model.entity.Advertisement;
import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.repository.AdvertisementRepository;
import TradeZone.service.AdvertisementServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdvertisementServiceTests{

    @InjectMocks
    AdvertisementServiceImpl advertisementService;

    @Mock
    AdvertisementRepository advertisementRepository;

    @Mock
    ModelMapper modelMapper;

    ModelMapper actualMapper = new ModelMapper();

    @Before
    public void init() {
        when(modelMapper.map(any(Advertisement.class), eq(AdvertisementServiceModel.class)))
                .thenAnswer(invocationOnMock ->
                        actualMapper.map(invocationOnMock.getArguments()[0], AdvertisementServiceModel.class));
    }

    @Test
    public void findById_shouldReturnAdvertisementWithThatIdFromDB_whenValidId() {

        Advertisement advertisement = new Advertisement();
        advertisement.setTitle("myAdvertisement");

        Long id = 1L;

        when(advertisementRepository.findById(any()))
                .thenReturn(Optional.of(advertisement));

        AdvertisementServiceModel returned = advertisementService.getById(id);

        assertEquals(advertisement.getTitle(), returned.getTitle());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findById_shouldThrow_whenInvalidId() {

        when(advertisementRepository.findById(any()))
                .thenReturn(Optional.empty());

        advertisementService.getById(1L);
    }
}