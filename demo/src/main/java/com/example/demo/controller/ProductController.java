package com.example.demo.controller;


import com.example.demo.bean.dto.ProductDetailDTO;
import com.example.demo.bean.dto.ProductListDTO;
import com.example.demo.bean.form.ProductQueryForm;
import com.example.demo.bean.form.ProductSaveForm;
import com.example.demo.bean.form.ProductUpdateForm;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 用于
     * @param form
     * @return
     */
    @PostMapping("/save")
    public Long save(@RequestBody ProductSaveForm form) {
        return productService.saveProduct(form);
    }

    /**
     * 用于根据不同的条件获取不同的列表
     * @param form
     * @param pageable
     * @return
     */
    @GetMapping("/list")
    public Page<ProductListDTO> list(
            ProductQueryForm form,
            @PageableDefault(size = 20, sort = "createTime", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return productService.list(form, pageable);
    }

    /**
     * 根据product获取详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ProductDetailDTO detail(@PathVariable Long id) {
        return productService.detail(id);
    }

    /**
     * 用于更新商品信息，暂时不支持修改，因为修改后价格发生变化
     * @param form
     */
    @PutMapping("/update")
    public void update(@RequestBody ProductUpdateForm form) {
        productService.updateProduct(form);
    }
}
