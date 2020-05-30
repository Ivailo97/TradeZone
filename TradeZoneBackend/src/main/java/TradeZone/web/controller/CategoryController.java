package TradeZone.web.controller;

import TradeZone.service.MappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import TradeZone.data.model.rest.CategoryCreateModel;
import TradeZone.data.model.view.CategoryListViewModel;
import TradeZone.data.model.view.CategorySelectViewModel;
import TradeZone.data.model.view.TopCategoryViewModel;
import TradeZone.service.CategoryService;

import static org.springframework.http.ResponseEntity.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    private final MappingService mappings;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryListViewModel>> categories() {
        return ok(categoryService.getAll().stream()
                .map(x -> mappings.getMapper().map(x, CategoryListViewModel.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/top")
    public ResponseEntity<List<TopCategoryViewModel>> topCategories(@RequestParam(name = "count") Integer count) {
        return ok(categoryService.getTop(count)
                .stream()
                .map(mappings.serviceToTopCategoryViewModel())
                .collect(Collectors.toList()));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Void> create(@RequestBody CategoryCreateModel restModel) {
        categoryService.create(restModel);
        return ok().build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<CategorySelectViewModel>> categorySelect() {
        return ok(categoryService.getAll().stream()
                .map(x -> mappings.getMapper().map(x, CategorySelectViewModel.class))
                .collect(Collectors.toList()));
    }
}
