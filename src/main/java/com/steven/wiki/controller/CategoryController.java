package com.steven.wiki.controller;

import com.steven.wiki.request.CategoryQueryReq;
import com.steven.wiki.request.CategorySaveReq;
import com.steven.wiki.response.CommonResp;
import com.steven.wiki.response.CategoryQueryResp;
import com.steven.wiki.response.PageResp;
import com.steven.wiki.service.CategoryService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")  //put common path before class to apply to all requests
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    //Category Management API
    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq req) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    //Home page API
    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> all = categoryService.all();
        resp.setContent(all);
        return resp;
    }

    //Save category API
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req) {
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    //Delete category API
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }

}
