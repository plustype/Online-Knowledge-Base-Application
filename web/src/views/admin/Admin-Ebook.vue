<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline">
          <a-form-item>
            <a-input-search
                :model="param"
                v-model:value="param.name"
                placeholder="input ebook name"
                enter-button="Search"
                style="width: 230px"
                @search="handleQuery({page: 1, size: pagination.pageSize})"
            />
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()" >
              Add
            </a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" width="50" height="50"/>
        </template>
<!--        <template v-slot:category="{ text, record }">-->
<!--          <span>{{ getCategoryName(record.category1) }} / {{ getCategoryName(record.category2) }}</span>-->
<!--        </template>-->
        <template v-slot:action="{ text, record }">
          <a-space size="small">
<!--            <router-link :to="'/admin/doc?ebookId=' + record.id">-->
<!--              <a-button type="primary">-->
<!--                文档管理-->
<!--              </a-button>-->
<!--            </router-link>-->
            <a-button type="primary" @click="edit(record)">
              Edit
            </a-button>
            <a-popconfirm
                title="Are you sure delete this record?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                Delete
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
      title="Ebook Form"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="Cover">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="Name">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="Category">
        <a-cascader
            v-model:value="categoryIds"
          :field-names="{ label: 'name', value: 'id', children: 'children' }"
          :options="level1"
          />
      </a-form-item>
      <a-form-item label="Description">
        <a-input v-model:value="ebook.description" type="textarea"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, reactive, toRef } from 'vue';
import axios from 'axios';
import { message} from "ant-design-vue";
import {Tool} from "../../../util/tool";

export default defineComponent({
  name: 'AdminEbook',
  setup() { //setup 就放一些参数定义，方法定义
    const param = ref();
    param.value ={};
    const ebooks = ref(); //定义响应式数据变量，变化实时响应到界面
    const pagination = ref({
          current: 1,
          pageSize: 4,
          total: 0
        });

    const loading = ref(false);

    const columns = [
      {
        title: 'Cover',
        dataIndex : 'cover',
        slots: {customRender: 'cover'}
      },
      {
        title: 'Name',
        dataIndex : 'name',
      },
      {
        title: 'Category 1',
        key: 'category1',
        dataIndex : 'category1'
      },
      {
        title: 'Category 2',
        dataIndex : 'category2'
      },
      {
        title: 'Chapter Count',
        dataIndex : 'chapterCount'
      },
      {
        title: 'View Count',
        dataIndex : 'viewCount'
      },
      {
        title: 'Like Count',
        dataIndex : 'likeCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      },
    ]

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("ebook/list",
          {params : {
            page: params.page,
            size: params.size,
            name: param.value.name
          }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          ebooks.value = data.content.list;

          //重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        }else {
          message.error(data.message)
        }
      });
    }

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    //Ebook Form
    const categoryIds = ref();   //this is an array, eg. [1000, 1001] --> Front-End Dev/Vue
    const ebook = ref();
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      ebook.value.category1 = categoryIds.value[0];  //pass category value
      ebook.value.category2 = categoryIds.value[1];

      axios.post("ebook/save", ebook.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;  // data = commonResp at backend
        if (data.success) {
          modalVisible.value = false;

          //reloading ebook list
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }else {
          message.error(data.message);
        }
      });
    };

    //edit button
    const edit = (record: any) => {
      modalVisible.value = true;
      ebook.value = Tool.copy(record);  //press 'edit' button, copy current record to edit
      categoryIds.value = [ebook.value.category1, ebook.value.category2]
    };

    //add button
    const add = () => {
      modalVisible.value = true;
      ebook.value = {};
    };

    //Delete button
    const handleDelete = (id : number) => {
      axios.delete("ebook/delete/" + id).then((response) => {
        const data = response.data;  // data = commonResp at backend
        if (data.success) {  // if action is successes
          //reloading ebook list
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }
      });
    }

    const level1 = ref(); // 一级分类树，children属性就是二级分类

    /**
     * 分类数据查询
     **/
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          const categorys = data.content;
          console.log("Origin Array: ", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys,0);
          console.log("Tree Structure: ", level1.value);
        }else {
          message.error(data.message)
        }
      });
    }


    onMounted(() => {
      handleQueryCategory();
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      param,
      ebooks,  //html代码要拿到响应式变量，需要在setup最后return
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,

      edit,
      add,

      ebook,
      modalVisible,
      modalLoading,
      handleModalOk,
      categoryIds,
      level1,

      handleDelete,

    }
  }
});
</script>
