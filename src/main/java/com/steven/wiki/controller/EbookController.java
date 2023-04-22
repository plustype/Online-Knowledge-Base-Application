package com.steven.wiki.controller;

import com.steven.wiki.request.EbookQueryReq;
import com.steven.wiki.request.EbookSaveReq;
import com.steven.wiki.response.CommonResp;
import com.steven.wiki.response.EbookQueryResp;
import com.steven.wiki.response.PageResp;
import com.steven.wiki.service.EbookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ebook")  //put common path before class to apply to all requests
public class EbookController {

    @Resource
    private EbookService ebookService;

    //Ebook Management API
    @GetMapping("/list")
    public CommonResp list(EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    //Home page API
    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<List<EbookQueryResp>> resp = new CommonResp<>();
        List<EbookQueryResp> all = ebookService.all();
        resp.setContent(all);
        return resp;
    }

    //Save ebook API
    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }
}
