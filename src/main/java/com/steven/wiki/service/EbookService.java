package com.steven.wiki.service;

import com.steven.wiki.domain.EbookExample;
import com.steven.wiki.mapper.EbookMapper;
import com.steven.wiki.request.EbookReq;
import com.steven.wiki.response.EbookResp;
import com.steven.wiki.utils.CopyUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {

        //fix, create criteria(where in sql); 固定写法，创建criteria变量（相当于sql中的where条件）
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + req.getName() + "%");

        var ebookList = ebookMapper.selectByExample(ebookExample);
        
        /*
        transfer properties to EbookResp; 将值传给封装的返回值类
         */
//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
//            EbookResp ebookResp = new EbookResp();
//            BeanUtils.copyProperties(ebook, ebookResp);
//            respList.add(ebookResp);
//        }

        return CopyUtil.copyList(ebookList, EbookResp.class); //copy list value
    }

}
