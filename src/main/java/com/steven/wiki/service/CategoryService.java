package com.steven.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.steven.wiki.domain.Category;
import com.steven.wiki.domain.CategoryExample;
import com.steven.wiki.mapper.CategoryMapper;
import com.steven.wiki.request.CategoryQueryReq;
import com.steven.wiki.request.CategorySaveReq;
import com.steven.wiki.response.CategoryQueryResp;
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
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private CategoryMapper categoryMapper;
    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {

        //fix, create criteria(where in sql); 固定写法，创建criteria变量（相当于sql中的where条件）
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName()))    //if req.name is not empty, add below constrain into sql
        {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        var categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
        
        /*
        transfer properties to CategoryResp; 将值传给封装的返回值类
         */
//        List<CategoryResp> respList = new ArrayList<>();
//        for (Category category : categoryList) {
//            CategoryResp categoryResp = new CategoryResp();
//            BeanUtils.copyProperties(category, categoryResp);
//            respList.add(categoryResp);
//        }

        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class); //copy list value

        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public List<CategoryQueryResp> all() {

        //fix, create criteria(where in sql); 固定写法，创建criteria变量（相当于sql中的where条件）
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
//        if (!ObjectUtils.isEmpty(req.getName()))    //if req.name is not empty, add below constrain into sql
//        {
//            criteria.andNameLike("%" + req.getName() + "%");
//        }

        var categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        /*
        transfer properties to CategoryResp; 将值传给封装的返回值类
         */
//        List<CategoryResp> respList = new ArrayList<>();
//        for (Category category : categoryList) {
//            CategoryResp categoryResp = new CategoryResp();
//            BeanUtils.copyProperties(category, categoryResp);
//            respList.add(categoryResp);
//        }

         return CopyUtil.copyList(categoryList, CategoryQueryResp.class); //copy list value
    }

    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //Insert a new category
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else
            categoryMapper.updateByPrimaryKey(category); //Update an existing category
    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

}
