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
                placeholder="category name"
                enter-button="Search"
                style="width: 230px"
                @search="handleQuery()"
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
          :data-source="categorys"
          :loading="loading"
          :pagination="false"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" width="50" height="50"/>
        </template>
<!--        <template v-slot:category="{ text, record }">-->
<!--          <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>-->
<!--        </template>-->
        <template v-slot:action="{ text, record }">
          <a-space size="small">
<!--            <router-link :to="'/admin/doc?categoryId=' + record.id">-->
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
      title="Category Form"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="Name">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="Parent Category">
        <a-input v-model:value="category.parent" />
      </a-form-item>
      <a-form-item label="Sort">
        <a-input v-model:value="category.sort" />
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
  name: 'AdminCategory',
  setup() { //setup 就放一些参数定义，方法定义
    const param = ref();
    param.value ={};
    const categorys = ref(); //定义响应式数据变量，变化实时响应到界面
    const loading = ref(false);

    const columns = [
      {
        title: 'Name',
        dataIndex : 'name',
      },
      {
        title: 'Parent Category',
        key: 'parent',
        dataIndex : 'parent'
      },
      {
        title: 'Sort',
        dataIndex : 'sort'
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
    const handleQuery = () => {
      loading.value = true;
      axios.get("category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys.value = data.content;
        }else {
          message.error(data.message)
        }
      });
    }


    //Category Form
    const category = ref({});
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);
    const handleModalOk = () => {
      modalLoading.value = true;

      axios.post("category/save", category.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;  // data = commonResp at backend
        if (data.success) {
          modalVisible.value = false;

          //reloading category list
          handleQuery();
        }else {
          message.error(data.message);
        }
      });
    };

    //edit button
    const edit = (record: any) => {
      modalVisible.value = true;
      category.value = Tool.copy(record);  //press 'edit' button, copy current record to edit
    };

    //add button
    const add = () => {
      modalVisible.value = true;
      category.value = {};
    };

    //Delete button
    const handleDelete = (id : number) => {
      axios.delete("category/delete/" + id).then((response) => {
        const data = response.data;  // data = commonResp at backend
        if (data.success) {  // if action is successes
          //reloading category list
          handleQuery();
        }
      });
    }


    onMounted(() => {
      handleQuery();
    });

    return {
      param,
      categorys,  //html代码要拿到响应式变量，需要在setup最后return
      columns,
      loading,
      handleQuery,

      edit,
      add,

      category,
      modalVisible,
      modalLoading,
      handleModalOk,
      handleDelete,

    }
  }
});
</script>
