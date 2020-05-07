package TradeZone.service;

import TradeZone.data.error.exception.AdvertisementNotValidException;
import TradeZone.data.error.exception.EntityNotFoundException;
import TradeZone.data.error.exception.NotAllowedException;
import TradeZone.data.error.exception.SearchNotValidException;
import TradeZone.data.model.entity.*;
import TradeZone.data.model.rest.AdvertisementCreateModel;
import TradeZone.data.model.rest.AdvertisementEditedModel;
import TradeZone.data.model.rest.search.FullSearchRequest;
import TradeZone.data.model.rest.search.SearchRequest;
import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.service.PhotoServiceModel;
import TradeZone.data.model.service.validation.AdvertisementValidationService;
import TradeZone.data.model.service.validation.FullSearchRequestValidationService;
import TradeZone.data.model.service.validation.SearchRequestValidationService;
import TradeZone.data.repository.AdvertisementRepository;
import TradeZone.data.repository.CategoryRepository;
import TradeZone.data.repository.PhotoRepository;
import TradeZone.data.repository.UserProfileRepository;
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

//White box testing
@RunWith(MockitoJUnitRunner.class)
public class AdvertisementServiceTests {

    @InjectMocks
    AdvertisementServiceImpl advertisementService;

    @Mock
    AdvertisementRepository advertisementRepository;

    @Mock
    FullSearchRequestValidationService fullSearchRequestValidationService;

    @Mock
    PhotoServiceImpl photoService;

    @Mock
    UserProfileRepository userProfileRepository;

    @Mock
    PhotoRepository photoRepository;

    @Mock
    AdvertisementValidationService advertisementValidationService;

    @Mock
    CategoryRepository categoryRepository;

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
        when(advertisementValidationService.isValid(any())).thenReturn(true);
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
    public void getCountBySearch_shouldThrow_whenInvalidSearch() {

        when(searchRequestValidationService.isValid(any())).thenReturn(false);

        advertisementService.getCountBySearch(search);
    }

    @Test
    public void create_shouldWorkCorrectly_whenValidArgument_andCategoryRepositoryNotEmpty() {

        when(categoryRepository.count()).thenReturn(1L);

        when(categoryRepository.findById(any())).thenReturn(Optional.of(new Category()));

        when(userProfileRepository.findByUserUsername(any())).thenReturn(Optional.of(new UserProfile()));

        when(modelMapper.map(any(AdvertisementCreateModel.class), eq(Advertisement.class)))
                .thenAnswer(invocationOnMock ->
                        actualMapper.map(invocationOnMock.getArguments()[0], Advertisement.class));

        when(photoService.create(anyString())).thenReturn(new PhotoServiceModel());

        when(modelMapper.map(any(PhotoServiceModel.class), eq(Photo.class)))
                .thenAnswer(invocationOnMock ->
                        actualMapper.map(invocationOnMock.getArguments()[0], Photo.class));


        AdvertisementCreateModel model = new AdvertisementCreateModel();
        model.setImages(new String[]{"image1", "image2"});
        model.setTitle("myTitle");

        when(advertisementRepository.save(any(Advertisement.class)))
                .thenAnswer(invocation ->
                        actualMapper.map(invocation.getArguments()[0], Advertisement.class));

        //Act
        AdvertisementServiceModel result = advertisementService.create(model);
        long actualViews = result.getViews();
        long expectedViews = 0;

        //Assert
        assertEquals(model.getTitle(), result.getTitle());
        assertEquals(model.getImages().length, result.getPhotos().size());
        assertEquals(expectedViews, actualViews);
        verify(advertisementRepository).save(any());
    }

    @Test
    public void create_shouldWorkCorrectly_whenValidArgument_andCategoryRepositoryEmpty() {

        when(photoRepository.save(any())).thenReturn(null);

        Category[] firstSeeded = new Category[1];

        when(categoryRepository.save(any())).thenAnswer(invocation -> {
            Category seeded = actualMapper.map(invocation.getArguments()[0], Category.class);
            firstSeeded[0] = seeded;
            return seeded;
        });

        when(categoryRepository.findById(any())).thenReturn(Optional.of(new Category()));

        when(userProfileRepository.findByUserUsername(any())).thenReturn(Optional.of(new UserProfile()));

        when(modelMapper.map(any(AdvertisementCreateModel.class), eq(Advertisement.class)))
                .thenAnswer(invocationOnMock ->
                        actualMapper.map(invocationOnMock.getArguments()[0], Advertisement.class));

        when(photoService.create(anyString())).thenReturn(new PhotoServiceModel());

        when(modelMapper.map(any(PhotoServiceModel.class), eq(Photo.class)))
                .thenAnswer(invocationOnMock ->
                        actualMapper.map(invocationOnMock.getArguments()[0], Photo.class));


        AdvertisementCreateModel model = new AdvertisementCreateModel();
        model.setImages(new String[]{"image1", "image2"});
        model.setTitle("myTitle");

        when(advertisementRepository.save(any(Advertisement.class)))
                .thenAnswer(invocation ->
                        actualMapper.map(invocation.getArguments()[0], Advertisement.class));

        //Act
        AdvertisementServiceModel result = advertisementService.create(model);
        long actualViews = result.getViews();
        long expectedViews = 0;

        //Assert
        assertEquals("Vehicles", firstSeeded[0].getName());
        assertEquals(model.getTitle(), result.getTitle());
        assertEquals(model.getImages().length, result.getPhotos().size());
        assertEquals(expectedViews, actualViews);
        verify(advertisementRepository).save(any());
    }

    @Test(expected = AdvertisementNotValidException.class)
    public void create_shouldThrow_whenInvalidArgument() {

        when(advertisementValidationService.isValid(any())).thenReturn(false);

        advertisementService.create(new AdvertisementCreateModel());
    }

    @Test(expected = EntityNotFoundException.class)
    public void create_shouldThrow_whenInvalidCategory() {

        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        advertisementService.create(new AdvertisementCreateModel());
    }

    @Test(expected = EntityNotFoundException.class)
    public void create_shouldThrow_whenInvalidCreator() {

        when(categoryRepository.findById(any())).thenReturn(Optional.of(new Category()));

        when(userProfileRepository.findByUserUsername(any())).thenReturn(Optional.empty());

        advertisementService.create(new AdvertisementCreateModel());
    }

    @Test
    public void edit_shouldWorkCorrectly_whenValidArgument_allPossibleFieldsUpdate() {

        Advertisement advertisementFromDB = new Advertisement();
        advertisementFromDB.setId(5L);
        Category category = new Category();
        category.setId(1L);
        advertisementFromDB.setCategory(category);
        UserProfile creator = new UserProfile();
        User user = new User();
        user.setUsername("user");
        creator.setUser(user);
        advertisementFromDB.setCreator(creator);

        when(advertisementRepository.findById(any())).thenReturn(Optional.of(advertisementFromDB));

        Category newCategory = new Category();
        newCategory.setName("newCategory");
        newCategory.setId(2L);

        AdvertisementEditedModel editedModel = new AdvertisementEditedModel();
        editedModel.setId(5L);
        editedModel.setCategory(2L);
        editedModel.setCondition("NEW");
        editedModel.setTitle("newTittle");
        editedModel.setDelivery("NEGOTIATING");
        editedModel.setDescription("newDescription");
        editedModel.setCreator(user.getUsername());
        editedModel.setPrice(BigDecimal.valueOf(100));

        when(categoryRepository.findById(any())).thenReturn(Optional.of(newCategory));

        when(advertisementRepository.save(any(Advertisement.class)))
                .thenAnswer(invocation ->
                        actualMapper.map(invocation.getArguments()[0], Advertisement.class));

        AdvertisementServiceModel result = advertisementService.edit(editedModel);

        assertEquals(editedModel.getTitle(), result.getTitle());
        assertEquals(editedModel.getId(),result.getId());
        assertEquals(editedModel.getCategory(),result.getCategory().getId());
        assertEquals(editedModel.getCondition(),result.getCondition().name());
        assertEquals(editedModel.getCreator(),result.getCreator().getUser().getUsername());
        assertEquals(editedModel.getDescription(),result.getDescription());
        assertEquals(editedModel.getPrice(),result.getPrice());
        assertEquals(editedModel.getDelivery(),result.getDelivery().name());
        verify(advertisementRepository).save(any());
    }

    @Test(expected = AdvertisementNotValidException.class)
    public void edit_shouldThrow_whenInvalidArgument(){

        when(advertisementValidationService.isValid(any())).thenReturn(false);

        advertisementService.edit(new AdvertisementEditedModel());
    }

    @Test(expected = EntityNotFoundException.class)
    public void edit_shouldThrow_whenInvalidId(){

        when(advertisementRepository.findById(any())).thenReturn(Optional.empty());

        advertisementService.edit(new AdvertisementEditedModel());
    }

    @Test(expected = EntityNotFoundException.class)
    public void edit_shouldThrow_whenInvalidCategoryId(){

        when(advertisementRepository.findById(any())).thenReturn(Optional.of(new Advertisement()));

        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        advertisementService.edit(new AdvertisementEditedModel());
    }

    @Test(expected = NotAllowedException.class)
    public void edit_shouldThrow_whenEditorNotCreator(){

        Advertisement advertisementFromDB = new Advertisement();
        UserProfile creator = new UserProfile();
        User user = new User();
        user.setUsername("user");
        creator.setUser(user);
        advertisementFromDB.setCreator(creator);

        when(advertisementRepository.findById(any())).thenReturn(Optional.of(advertisementFromDB));

        when(categoryRepository.findById(any())).thenReturn(Optional.of(new Category()));

        AdvertisementEditedModel editedModel = new AdvertisementEditedModel();
        editedModel.setCreator("another");

        advertisementService.edit(new AdvertisementEditedModel());
    }
}