package com.steven.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.steven.wiki.domain.Doc;
import com.steven.wiki.domain.DocExample;
import com.steven.wiki.mapper.DocMapper;
import com.steven.wiki.request.DocQueryReq;
import com.steven.wiki.request.DocSaveReq;
import com.steven.wiki.response.DocQueryResp;
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
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private DocMapper docMapper;
    public PageResp<DocQueryResp> list(DocQueryReq req) {

        //fix, create criteria(where in sql); 固定写法，创建criteria变量（相当于sql中的where条件）
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName()))    //if req.name is not empty, add below constrain into sql
        {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        var docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
        
        /*
        transfer properties to DocResp; 将值传给封装的返回值类
         */
//        List<DocResp> respList = new ArrayList<>();
//        for (Doc doc : docList) {
//            DocResp docResp = new DocResp();
//            BeanUtils.copyProperties(doc, docResp);
//            respList.add(docResp);
//        }

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class); //copy list value

        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public List<DocQueryResp> all() {

        //fix, create criteria(where in sql); 固定写法，创建criteria变量（相当于sql中的where条件）
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        var docList = docMapper.selectByExample(docExample);

        return CopyUtil.copyList(docList, DocQueryResp.class); //copy list value
    }

    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //Insert a new doc
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        }else
            docMapper.updateByPrimaryKey(doc); //Update an existing doc
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

}
