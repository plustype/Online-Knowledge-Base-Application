package com.steven.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.steven.wiki.domain.Ebook;
import com.steven.wiki.domain.EbookExample;
import com.steven.wiki.mapper.EbookMapper;
import com.steven.wiki.request.EbookQueryReq;
import com.steven.wiki.request.EbookSaveReq;
import com.steven.wiki.response.EbookQueryResp;
import com.steven.wiki.response.PageResp;
import com.steven.wiki.utils.CopyUtil;
import com.steven.wiki.utils.SnowFlake;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private EbookMapper ebookMapper;
    public PageResp<EbookQueryResp> list(EbookQueryReq req) {

        //fix, create criteria(where in sql); 固定写法，创建criteria变量（相当于sql中的where条件）
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName()))    //if req.name is not empty, add below constrain into sql
        {
            criteria.andNameLike("%" + req.getName() + "%");
        }

//        if (!ObjectUtils.isEmpty(req.getCategory2()))    //if req.name is not empty, add below constrain into sql
//        {
//            criteria.andCategory2EqualTo(req.getCategory2());
//        }

        PageHelper.startPage(req.getPage(), req.getSize());
        var ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
        
        /*
        transfer properties to EbookResp; 将值传给封装的返回值类
         */
//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
//            EbookResp ebookResp = new EbookResp();
//            BeanUtils.copyProperties(ebook, ebookResp);
//            respList.add(ebookResp);
//        }

        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class); //copy list value

        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public List<EbookQueryResp> all(EbookQueryReq req) {

        //fix, create criteria(where in sql); 固定写法，创建criteria变量（相当于sql中的where条件）
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
//        if (!ObjectUtils.isEmpty(req.getName()))    //if req.name is not empty, add below constrain into sql
//        {
//            criteria.andNameLike("%" + req.getName() + "%");
//        }

        if (!ObjectUtils.isEmpty(req.getCategory2()))    //if req.name is not empty, add below constrain into sql
        {
            criteria.andCategory2EqualTo(req.getCategory2());
        }

        var ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        /*
        transfer properties to EbookResp; 将值传给封装的返回值类
         */
//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
//            EbookResp ebookResp = new EbookResp();
//            BeanUtils.copyProperties(ebook, ebookResp);
//            respList.add(ebookResp);
//        }

         return CopyUtil.copyList(ebookList, EbookQueryResp.class); //copy list value
    }

    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //Insert a new ebook
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        }else
            ebookMapper.updateByPrimaryKey(ebook); //Update an existing ebook
    }

    public void delete(Long id) {
        ebookMapper.deleteByPrimaryKey(id);
    }

}
