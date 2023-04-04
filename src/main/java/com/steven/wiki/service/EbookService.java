package com.steven.wiki.service;

import com.steven.wiki.domain.Ebook;
import com.steven.wiki.mapper.EbookMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }

}
