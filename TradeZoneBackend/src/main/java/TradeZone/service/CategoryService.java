package TradeZone.service;

import TradeZone.data.model.rest.CategoryCreateModel;
import TradeZone.data.model.rest.message.response.ResponseMessage;
import TradeZone.data.model.service.CategoryServiceModel;

import java.util.List;

public interface CategoryService {

    CategoryServiceModel create(CategoryCreateModel createModel);

    List<CategoryServiceModel> getTop(Integer count);

    List<CategoryServiceModel> getAll();
}
