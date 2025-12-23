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
     * 
     * @param form
     * @return
     */
    @PostMapping("/save")
    public Long save(@RequestBody ProductSaveForm form) {
        return productService.saveProduct(form);
    }

    /**
     * 用于根据不同的条件获取不同的列表
     * 
     * @param form
     * @param pageable
     * @return
     */
    @GetMapping("/list")
    public Page<ProductListDTO> list(
            ProductQueryForm form,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        return productService.list(form, pageable);
    }

    /**
     * 根据product获取详情
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ProductDetailDTO detail(@PathVariable Long id) {
        return productService.detail(id);
    }

    /**
     * 用于更新商品信息，暂时不支持修改，因为修改后价格发生变化
     * 
     * @param form
     */
    public void update(@RequestBody ProductUpdateForm form) {
        productService.updateProduct(form);
    }

    /**
     * 删除商品
     * 
     * @param id
     */
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    private final com.example.demo.service.MinioService minioService;

    @GetMapping("/proxy/image")
    public void proxyImage(@RequestParam String filename, jakarta.servlet.http.HttpServletResponse response) {
        try (java.io.InputStream is = minioService.downloadFile(filename)) {
            response.setContentType("image/jpeg"); // Default to jpeg, or detect
            org.springframework.util.StreamUtils.copy(is, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(404);
        }
    }

    @GetMapping("/proxy/image/base64")
    public java.util.Map<String, String> proxyImageBase64(@RequestParam String filename) {
        try (java.io.InputStream is = minioService.downloadFile(filename)) {
            byte[] bytes = org.springframework.util.StreamUtils.copyToByteArray(is);
            String base64 = java.util.Base64.getEncoder().encodeToString(bytes);
            return java.util.Collections.singletonMap("data", "data:image/jpeg;base64," + base64);
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.singletonMap("error", e.getMessage());
        }
    }
}
