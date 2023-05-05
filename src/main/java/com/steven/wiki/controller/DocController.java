package com.steven.wiki.controller;

import com.steven.wiki.request.DocQueryReq;
import com.steven.wiki.request.DocSaveReq;
import com.steven.wiki.response.DocQueryResp;
import com.steven.wiki.response.CommonResp;
import com.steven.wiki.response.PageResp;
import com.steven.wiki.service.DocService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doc")  //put common path before class to apply to all requests
public class DocController {

    @Resource
    private DocService docService;

    //Doc Management API
    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    //Home page API
    @GetMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> all = docService.all(ebookId);
        resp.setContent(all);
        return resp;
    }

    //Save doc API
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    //Delete doc API
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        docService.delete(id);
        return resp;
    }

}
