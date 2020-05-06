package TradeZone.service;

import TradeZone.data.error.exception.EntityNotFoundException;
import TradeZone.data.error.exception.SearchNotValidException;
import TradeZone.data.model.entity.Advertisement;
import TradeZone.data.model.rest.search.FullSearchRequest;
import TradeZone.data.model.rest.search.SearchRequest;
import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.service.validation.FullSearchRequestValidationService;
import TradeZone.data.model.service.validation.SearchRequestValidationService;
import TradeZone.data.repository.AdvertisementRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdvertisementServiceTests {

    @InjectMocks
    AdvertisementServiceImpl advertisementService;

    @Mock
    AdvertisementRepository advertisementRepository;

    @Mock
    FullSearchRequestValidationService fullSearchRequestValidationService;

    @Mock
    SearchRequestValidationService searchRequestValidationService;

    @Mock
    ModelMapper modelMapper;

    ModelMapper actualMapper = new ModelMapper();

    Advertisement advertisement;

    FullSearchRequest fullSearch;

    SearchRequest search;

    @Before
    public void init() {

        advertisement = new Advertisement();
        advertisement.setTitle("myAdvertisement");

        when(modelMapper.map(any(Advertisement.class), eq(AdvertisementServiceModel.class)))
                .thenAnswer(invocationOnMock ->
                        actualMapper.map(invocationOnMock.getArguments()[0], AdvertisementServiceModel.class));

        when(fullSearchRequestValidationService.isValid(any())).thenReturn(true);
        when(searchRequestValidationService.isValid(any())).thenReturn(true);
    }

    @Test
    public void findById_shouldReturnAdvertisementWithThatIdFromDB_whenValidId() {

        when(advertisementRepository.findById(any()))
                .thenReturn(Optional.of(advertisement));

        AdvertisementServiceModel returned = advertisementService.getById(1L);

        assertEquals(advertisement.getTitle(), returned.getTitle());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findById_shouldThrow_whenInvalidId() {

        when(advertisementRepository.findById(any())).thenReturn(Optional.empty());

        advertisementService.getById(1L);
    }

    @Test
    public void getAllByFullSearch_shouldReturnCorrect_whenValidSearch_firstPagePriceInRangeSortedByViewsDescending() {

        //Arrange
        fullSearch = new FullSearchRequest(BigDecimal.valueOf(0), BigDecimal.valueOf(10000),
                "All", "All",
                "undefined", 1,
                "views", "descending");

        //expected repository method call
        when(advertisementRepository.findAllByPriceBetween(any(), any(), any())).thenReturn(Page.empty());

        //Act
        advertisementService.getAllByFullSearch(fullSearch);

        //Assert
        verify(advertisementRepository).findAllByPriceBetween(any(), any(), any());
    }

    @Test
    public void getAllByFullSearch_shouldReturnCorrect_whenValidSearch_firstPageConcreteCategoryPriceInRangeSortedByViewsAscending() {

        fullSearch = new FullSearchRequest(BigDecimal.valueOf(0), BigDecimal.valueOf(10000),
                "All", "Vehicles",
                "undefined", 1,
                "views", "ascending");

        when(advertisementRepository.findAllByCategoryNameAndPriceBetween(any(), any(), any(), any())).thenReturn(Page.empty());

        advertisementService.getAllByFullSearch(fullSearch);

        verify(advertisementRepository).findAllByCategoryNameAndPriceBetween(any(), any(), any(), any());
    }

    @Test
    public void getAllByFullSearch_shouldReturnCorrect_whenValidSearch_firstPageConcreteConditionPriceInRangeSortedByViewsAscending() {

        fullSearch = new FullSearchRequest(BigDecimal.valueOf(0), BigDecimal.valueOf(10000),
                "NEW", "All",
                "undefined", 1,
                "views", "ascending");

        when(advertisementRepository.findAllByPriceBetweenAndCondition(any(), any(), any(), any())).thenReturn(Page.empty());

        advertisementService.getAllByFullSearch(fullSearch);

        verify(advertisementRepository).findAllByPriceBetweenAndCondition(any(), any(), any(), any());
    }

    @Test
    public void getAllByFullSearch_shouldReturnCorrect_whenValidSearch_firstPageConcreteConditionAndCategoryPriceInRangeSortedByViewsAscending() {

        fullSearch = new FullSearchRequest(BigDecimal.valueOf(0), BigDecimal.valueOf(10000),
                "NEW", "Vehicles",
                "undefined", 1,
                "views", "ascending");

        when(advertisementRepository.findAllByCategoryNameAndPriceBetweenAndCondition(any(), any(), any(), any(), any())).thenReturn(Page.empty());

        advertisementService.getAllByFullSearch(fullSearch);

        verify(advertisementRepository).findAllByCategoryNameAndPriceBetweenAndCondition(any(), any(), any(), any(), any());
    }

    @Test
    public void getAllByFullSearch_shouldReturnCorrect_whenValidSearch_firstPageConcreteConditionAndSearchPriceInRangeSortedByViewsAscending() {

        fullSearch = new FullSearchRequest(BigDecimal.valueOf(0), BigDecimal.valueOf(10000),
                "NEW", "All",
                "spaghetti", 1,
                "views", "ascending");

        when(advertisementRepository.findAllByTitleContainingAndPriceBetweenAndCondition(any(), any(), any(), any(), any())).thenReturn(Page.empty());

        advertisementService.getAllByFullSearch(fullSearch);

        verify(advertisementRepository).findAllByTitleContainingAndPriceBetweenAndCondition(any(), any(), any(), any(), any());
    }

    @Test
    public void getAllByFullSearch_shouldReturnCorrect_whenValidSearch_firstPageConcreteConditionAndCategoryAndSearchPriceInRangeSortedByViewsAscending() {

        fullSearch = new FullSearchRequest(
                BigDecimal.valueOf(0), BigDecimal.valueOf(10000),
                "NEW", "Vehicles",
                "spaghetti", 1,
                "views", "ascending");

        when(advertisementRepository
                .findAllByTitleContainingAndCategoryNameAndPriceBetweenAndCondition(any(), any(), any(), any(), any(), any()))
                .thenReturn(Page.empty());

        advertisementService.getAllByFullSearch(fullSearch);

        verify(advertisementRepository).findAllByTitleContainingAndCategoryNameAndPriceBetweenAndCondition(any(), any(), any(), any(), any(), any());
    }

    @Test
    public void getAllByFullSearch_shouldReturnCorrect_whenValidSearch_firstPageSearchPriceInRangeSortedByViewsAscending() {

        fullSearch = new FullSearchRequest(
                BigDecimal.valueOf(0), BigDecimal.valueOf(10000),
                "All", "All",
                "spaghetti", 1,
                "views", "ascending");

        when(advertisementRepository
                .findAllByPriceBetweenAndTitleContaining(any(), any(), any(), any()))
                .thenReturn(Page.empty());

        advertisementService.getAllByFullSearch(fullSearch);

        verify(advertisementRepository).findAllByPriceBetweenAndTitleContaining(any(), any(), any(), any());
    }

    @Test
    public void getAllByFullSearch_shouldReturnCorrect_whenValidSearch_firstPageConcreteCategorySearchPriceInRangeSortedByViewsAscending() {

        fullSearch = new FullSearchRequest(
                BigDecimal.valueOf(0), BigDecimal.valueOf(10000),
                "All", "Vehicles",
                "spaghetti", 1,
                "views", "ascending");

        when(advertisementRepository
                .findAllByCategoryNameAndTitleContainingAndPriceBetween(any(), any(), any(), any(), any()))
                .thenReturn(Page.empty());

        advertisementService.getAllByFullSearch(fullSearch);

        verify(advertisementRepository).findAllByCategoryNameAndTitleContainingAndPriceBetween(any(), any(), any(), any(), any());
    }

    @Test(expected = SearchNotValidException.class)
    public void getAllByFullSearch_shouldThrow_whenInvalidSearch() {

        when(fullSearchRequestValidationService.isValid(any())).thenReturn(false);
        advertisementService.getAllByFullSearch(fullSearch);
    }

    @Test
    public void getCountBySearch_shouldReturnCorrect_whenValidSearch_byPriceInRange() {

        search = new SearchRequest(BigDecimal.valueOf(0), BigDecimal.valueOf(10000), "All", "All", "undefined");

        when(advertisementRepository.countAdvertisementByPriceBetween(any(), any())).thenReturn(1L);

        long count = advertisementService.getCountBySearch(search);

        assertEquals(1L, count);
        verify(advertisementRepository).countAdvertisementByPriceBetween(any(), any());
    }

    @Test
    public void getCountBySearch_shouldReturnCorrect_whenValidSearch_byPriceInRangeAndSearch() {

        search = new SearchRequest(BigDecimal.valueOf(0), BigDecimal.valueOf(10000), "All", "All", "spaghetti");

        when(advertisementRepository.countByPriceBetweenAndTitleContaining(any(), any(), any())).thenReturn(1L);

        long count = advertisementService.getCountBySearch(search);

        assertEquals(1L, count);
        verify(advertisementRepository).countByPriceBetweenAndTitleContaining(any(), any(), any());
    }

    @Test
    public void getCountBySearch_shouldReturnCorrect_whenValidSearch_byPriceInRangeAndCondition() {

        search = new SearchRequest(BigDecimal.valueOf(0), BigDecimal.valueOf(10000), "NEW", "All", "undefined");

        when(advertisementRepository.countAdvertisementByPriceBetweenAndCondition(any(), any(), any())).thenReturn(1L);

        long count = advertisementService.getCountBySearch(search);

        assertEquals(1L, count);
        verify(advertisementRepository).countAdvertisementByPriceBetweenAndCondition(any(), any(), any());
    }

    @Test
    public void getCountBySearch_shouldReturnCorrect_whenValidSearch_byPriceInRangeAndConditionAndSearch() {

        search = new SearchRequest(BigDecimal.valueOf(0), BigDecimal.valueOf(10000), "NEW", "All", "spaghetti");

        when(advertisementRepository.countByPriceBetweenAndTitleContainingAndCondition(any(), any(), any(), any())).thenReturn(1L);

        long count = advertisementService.getCountBySearch(search);

        assertEquals(1L, count);
        verify(advertisementRepository).countByPriceBetweenAndTitleContainingAndCondition(any(), any(), any(), any());
    }

    @Test
    public void getCountBySearch_shouldReturnCorrect_whenValidSearch_byPriceInRangeAndConcreteCategory() {

        search = new SearchRequest(BigDecimal.valueOf(0), BigDecimal.valueOf(10000), "All", "Vehicles", "undefined");

        when(advertisementRepository.countAdvertisementByCategoryNameAndPriceBetween(any(), any(), any())).thenReturn(1L);

        long count = advertisementService.getCountBySearch(search);

        assertEquals(1L, count);
        verify(advertisementRepository).countAdvertisementByCategoryNameAndPriceBetween(any(), any(), any());
    }

    @Test
    public void getCountBySearch_shouldReturnCorrect_whenValidSearch_byPriceInRangeAndConcreteCategoryAndSearch() {

        search = new SearchRequest(BigDecimal.valueOf(0), BigDecimal.valueOf(10000), "All", "Vehicles", "spaghetti");

        when(advertisementRepository.countByPriceBetweenAndTitleContainingAndCategoryName(any(), any(), any(), any())).thenReturn(1L);

        long count = advertisementService.getCountBySearch(search);

        assertEquals(1L, count);
        verify(advertisementRepository).countByPriceBetweenAndTitleContainingAndCategoryName(any(), any(), any(), any());
    }

    @Test
    public void getCountBySearch_shouldReturnCorrect_whenValidSearch_byPriceInRangeAndConcreteCategoryAndConditionAndSearch() {

        search = new SearchRequest(BigDecimal.valueOf(0), BigDecimal.valueOf(10000), "NEW", "Vehicles", "spaghetti");

        when(advertisementRepository.countByPriceBetweenAndTitleContainingAndCategoryNameAndCondition(any(), any(), any(), any(), any())).thenReturn(1L);

        long count = advertisementService.getCountBySearch(search);

        assertEquals(1L, count);
        verify(advertisementRepository).countByPriceBetweenAndTitleContainingAndCategoryNameAndCondition(any(), any(), any(), any(), any());
    }

    @Test
    public void getCountBySearch_shouldReturnCorrect_whenValidSearch_byPriceInRangeAndConcreteCategoryAndCondition() {

        search = new SearchRequest(BigDecimal.valueOf(0), BigDecimal.valueOf(10000), "NEW", "Vehicles", "undefined");

        when(advertisementRepository.countAdvertisementByCategoryNameAndConditionAndPriceBetween(any(), any(), any(), any())).thenReturn(1L);

        long count = advertisementService.getCountBySearch(search);

        assertEquals(1L, count);
        verify(advertisementRepository).countAdvertisementByCategoryNameAndConditionAndPriceBetween(any(), any(), any(), any());
    }

    @Test(expected = SearchNotValidException.class)
    public void getCountBySearch_shouldThrow_whenInvalidSearch(){

        when(searchRequestValidationService.isValid(any())).thenReturn(false);

        advertisementService.getCountBySearch(search);
    }
}