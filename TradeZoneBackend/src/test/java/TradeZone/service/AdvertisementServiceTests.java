package TradeZone.service;

import TradeZone.data.error.exception.*;
import TradeZone.data.model.entity.*;
import TradeZone.data.model.rest.*;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.rest.search.FullSearchRequest;
import TradeZone.data.model.rest.search.SearchRequest;
import TradeZone.data.model.service.AdvertisementServiceModel;
import TradeZone.data.model.service.PhotoServiceModel;
import TradeZone.service.validation.*;
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

import static org.junit.Assert.*;
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
    DeleteAdvImageRequestValidationService deleteAdvImageRequestValidationService;

    @Mock
    DeleteAdvRequestValidationService deleteAdvRequestValidationService;

    @Mock
    ViewsUpdateValidationService viewsUpdateValidationService;

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
        assertEquals(editedModel.getId(), result.getId());
        assertEquals(editedModel.getCategory(), result.getCategory().getId());
        assertEquals(editedModel.getCondition(), result.getCondition().name());
        assertEquals(editedModel.getCreator(), result.getCreator().getUser().getUsername());
        assertEquals(editedModel.getDescription(), result.getDescription());
        assertEquals(editedModel.getPrice(), result.getPrice());
        assertEquals(editedModel.getDelivery(), result.getDelivery().name());
        verify(advertisementRepository).save(any());
    }

    @Test(expected = AdvertisementNotValidException.class)
    public void edit_shouldThrow_whenInvalidArgument() {

        when(advertisementValidationService.isValid(any())).thenReturn(false);

        advertisementService.edit(new AdvertisementEditedModel());
    }

    @Test(expected = EntityNotFoundException.class)
    public void edit_shouldThrow_whenInvalidId() {

        when(advertisementRepository.findById(any())).thenReturn(Optional.empty());

        advertisementService.edit(new AdvertisementEditedModel());
    }

    @Test(expected = EntityNotFoundException.class)
    public void edit_shouldThrow_whenInvalidCategoryId() {

        when(advertisementRepository.findById(any())).thenReturn(Optional.of(new Advertisement()));

        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        advertisementService.edit(new AdvertisementEditedModel());
    }

    @Test(expected = NotAllowedException.class)
    public void edit_shouldThrow_whenEditorNotCreator() {

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

    @Test
    public void delete_shouldWorkCorrect_whenValidRequest() {

        //creating user profile with one created advertisement
        String loggedUser = "user";
        Advertisement advertisementFromDB = new Advertisement();
        User user = new User();
        user.setUsername(loggedUser);
        UserProfile creatorProfile = new UserProfile(user);
        creatorProfile.getCreatedAdvertisements().add(advertisementFromDB);
        advertisementFromDB.setCreator(creatorProfile);

        //creating user profile which liked the advertisement the first user created
        User user1 = new User();
        user1.setUsername("liker");
        UserProfile userProfile1 = new UserProfile(user1);

        //adding the advertisement to favorite and viewed collection of the profile
        userProfile1.getViewed().add(advertisementFromDB);
        userProfile1.getFavorites().add(advertisementFromDB);

        //creating the delete request
        DeleteAdvRequest deleteRequest = new DeleteAdvRequest();
        deleteRequest.setAdvertisementId(1L);
        deleteRequest.setUsername(loggedUser);

        when(deleteAdvRequestValidationService.isValid(any())).thenReturn(true);

        when(advertisementRepository.findById(any())).thenReturn(Optional.of(advertisementFromDB));

        doNothing().when(photoService).deleteAll(any());

        //Act
        AdvertisementServiceModel actual = advertisementService.delete(loggedUser, deleteRequest);

        //Assert
        verify(photoService).deleteAll(any());
        verify(advertisementRepository).save(any());
        verify(advertisementRepository).delete(any());
        assertNull(actual.getProfilesWhichLikedIt());
        assertNull(actual.getProfilesWhichViewedIt());
        assertEquals(0, creatorProfile.getFavorites().size());
        assertEquals(0, creatorProfile.getViewed().size());
        assertEquals(0, creatorProfile.getCreatedAdvertisements().size());
    }

    @Test(expected = DeleteRequestNotValidException.class)
    public void delete_shouldThrow_whenInvalidRequest() {

        when(deleteAdvRequestValidationService.isValid(any())).thenReturn(false);

        advertisementService.delete("user", new DeleteAdvRequest());
    }

    @Test(expected = EntityNotFoundException.class)
    public void delete_shouldThrow_whenInvalidAdvertisementIdInRequest() {

        when(deleteAdvRequestValidationService.isValid(any())).thenReturn(true);

        DeleteAdvRequest deleteRequest = new DeleteAdvRequest();
        deleteRequest.setUsername("user");
        deleteRequest.setAdvertisementId(1L);

        when(advertisementRepository.findById(any())).thenReturn(Optional.empty());

        advertisementService.delete("user", deleteRequest);
    }

    @Test(expected = NotAllowedException.class)
    public void delete_shouldThrow_whenUserNotCreator() {

        String loggedUser = "user";
        Advertisement advertisementFromDB = new Advertisement();
        User user = new User();
        user.setUsername(loggedUser);
        UserProfile creatorProfile = new UserProfile(user);
        creatorProfile.getCreatedAdvertisements().add(advertisementFromDB);
        advertisementFromDB.setCreator(creatorProfile);

        when(deleteAdvRequestValidationService.isValid(any())).thenReturn(true);

        DeleteAdvRequest deleteRequest = new DeleteAdvRequest();
        deleteRequest.setUsername("notCreator");
        deleteRequest.setAdvertisementId(1L);

        when(advertisementRepository.findById(any())).thenReturn(Optional.of(advertisementFromDB));

        advertisementService.delete(loggedUser, deleteRequest);
    }

    @Test(expected = NotAllowedException.class)
    public void delete_shouldThrow_whenUserNotLoggedUser() {

        String loggedUser = "user";
        Advertisement advertisementFromDB = new Advertisement();
        User user = new User();
        user.setUsername(loggedUser);
        UserProfile creatorProfile = new UserProfile(user);
        creatorProfile.getCreatedAdvertisements().add(advertisementFromDB);
        advertisementFromDB.setCreator(creatorProfile);

        when(deleteAdvRequestValidationService.isValid(any())).thenReturn(true);

        DeleteAdvRequest deleteRequest = new DeleteAdvRequest();
        deleteRequest.setUsername(loggedUser);
        deleteRequest.setAdvertisementId(1L);

        when(advertisementRepository.findById(any())).thenReturn(Optional.of(advertisementFromDB));

        advertisementService.delete("another", deleteRequest);
    }

    @Test
    public void updateViews_shouldWorkCorrectly_whenFirstView() {

        when(viewsUpdateValidationService.isValid(any())).thenReturn(true);

        Advertisement advertisement = new Advertisement();
        advertisement.setId(1L);
        advertisement.setViews(0L);

        User user = new User();
        user.setUsername("ivo");
        UserProfile userProfile = new UserProfile(user);

        when(advertisementRepository.findById(any())).thenReturn(Optional.of(advertisement));

        when(userProfileRepository.findByUserUsername(any())).thenReturn(Optional.of(userProfile));

        when(userProfileRepository.save(any())).thenReturn(null);
        when(advertisementRepository.save(any())).thenReturn(null);

        ViewsUpdate viewsUpdate = new ViewsUpdate(user.getUsername(), 1L);

        AdvertisementServiceModel actual = advertisementService.updateViews(1L, viewsUpdate);

        long actualViews = actual.getViews();
        assertEquals(1L, actualViews);
        assertEquals(1, userProfile.getViewed().size());
        long expectedId = userProfile.getViewed().get(0).getId();
        assertEquals(1L, expectedId);
        verify(userProfileRepository).save(any());
        verify(advertisementRepository).save(any());
    }

    @Test(expected = ViewsUpdateNotValidException.class)
    public void updateViews_shouldThrow_whenInvalidUpdate() {

        when(viewsUpdateValidationService.isValid(any())).thenReturn(false);

        advertisementService.updateViews(1L, new ViewsUpdate());
    }

    @Test
    public void updateViews_shouldNotUpdate_whenAlreadyViewedByTheProfile() {

        when(viewsUpdateValidationService.isValid(any())).thenReturn(true);

        Advertisement advertisement = new Advertisement();
        advertisement.setId(1L);
        advertisement.setViews(0L);

        User user = new User();
        user.setUsername("ivo");
        UserProfile userProfile = new UserProfile(user);
        userProfile.getViewed().add(advertisement);

        when(advertisementRepository.findById(any())).thenReturn(Optional.of(advertisement));

        when(userProfileRepository.findByUserUsername(any())).thenReturn(Optional.of(userProfile));

        ViewsUpdate viewsUpdate = new ViewsUpdate(user.getUsername(), 1L);

        AdvertisementServiceModel actual = advertisementService.updateViews(1L, viewsUpdate);

        long actualViews = actual.getViews();
        assertEquals(0L, actualViews);
        assertEquals(1, userProfile.getViewed().size());
        long expectedId = userProfile.getViewed().get(0).getId();
        assertEquals(1L, expectedId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateViews_shouldThrow_whenInvalidAdvertisementId() {

        when(viewsUpdateValidationService.isValid(any())).thenReturn(true);

        when(advertisementRepository.findById(any())).thenReturn(Optional.empty());

        advertisementService.updateViews(1L, new ViewsUpdate());
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateViews_shouldThrow_whenInvalidProfileUsername() {

        when(viewsUpdateValidationService.isValid(any())).thenReturn(true);

        when(advertisementRepository.findById(any())).thenReturn(Optional.of(advertisement));

        when(userProfileRepository.findByUserUsername(any())).thenReturn(Optional.empty());

        advertisementService.updateViews(1L, new ViewsUpdate());
    }

    @Test
    public void deletePhoto_shouldDetachAndThenDeletePhoto_whenValidRequest() {

        String creator = "user";

        when(deleteAdvImageRequestValidationService.isValid(any())).thenReturn(true);

        when(advertisementRepository.findById(any())).thenReturn(Optional.of(advertisement));

        when(photoService.delete(any())).thenReturn(new ResponseMessage(""));

        when(advertisementRepository.save(any())).thenReturn(null);

        DeleteAdvImageRequest request = new DeleteAdvImageRequest();
        request.setPhotoId(1L);
        request.setAdvertisementId(1L);
        request.setUsername(creator);

        Photo photo = new Photo();
        photo.setId(1L);
        advertisement.getPhotos().add(photo);
        User user = new User();
        user.setUsername(creator);
        advertisement.setCreator(new UserProfile(user));

        AdvertisementServiceModel actual = advertisementService.deletePhoto(request);

        verify(photoService).delete(any());
        verify(advertisementRepository).save(any());
        assertEquals(0, actual.getPhotos().size());
    }

    @Test(expected = DeleteRequestNotValidException.class)
    public void deletePhoto_shouldThrow_whenInvalidRequest() {

        when(deleteAdvImageRequestValidationService.isValid(any())).thenReturn(false);

        advertisementService.deletePhoto(new DeleteAdvImageRequest());
    }

    @Test(expected = EntityNotFoundException.class)
    public void deletePhoto_shouldThrow_whenInvalidAdvertisementId() {

        when(deleteAdvImageRequestValidationService.isValid(any())).thenReturn(true);

        when(advertisementRepository.findById(any())).thenReturn(Optional.empty());

        advertisementService.deletePhoto(new DeleteAdvImageRequest());
    }

    @Test(expected = EntityNotFoundException.class)
    public void deletePhoto_shouldThrow_whenInvalidImageId() {

        when(deleteAdvImageRequestValidationService.isValid(any())).thenReturn(true);

        when(advertisementRepository.findById(any())).thenReturn(Optional.of(advertisement));

        String creator = "user";
        DeleteAdvImageRequest request = new DeleteAdvImageRequest();
        request.setPhotoId(1L);
        request.setAdvertisementId(1L);
        request.setUsername(creator);

        Photo photo = new Photo();
        photo.setId(2L);
        advertisement.getPhotos().add(photo);
        User user = new User();
        user.setUsername(creator);
        advertisement.setCreator(new UserProfile(user));

        advertisementService.deletePhoto(request);
    }

    @Test(expected = NotAllowedException.class)
    public void deletePhoto_shouldThrow_whenInvalid() {

        when(deleteAdvImageRequestValidationService.isValid(any())).thenReturn(true);

        when(advertisementRepository.findById(any())).thenReturn(Optional.of(advertisement));

        String creator = "user";
        DeleteAdvImageRequest request = new DeleteAdvImageRequest();
        request.setPhotoId(1L);
        request.setAdvertisementId(1L);
        request.setUsername("notCreator");

        Photo photo = new Photo();
        photo.setId(1L);
        advertisement.getPhotos().add(photo);
        User user = new User();
        user.setUsername(creator);
        advertisement.setCreator(new UserProfile(user));

        advertisementService.deletePhoto(request);
    }
}