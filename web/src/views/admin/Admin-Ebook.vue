<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
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
<!--          <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>-->
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
<!--            <a-popconfirm-->
<!--                title="删除后不可恢复，确认删除?"-->
<!--                ok-text="是"-->
<!--                cancel-text="否"-->
<!--                @confirm="handleDelete(record.id)"-->
<!--            >-->
              <a-button type="danger">
                Delete
              </a-button>
<!--            </a-popconfirm>-->
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, reactive, toRef } from 'vue';
import axios from 'axios';

export default defineComponent({
  name: 'AdminEbook',
  setup() { //setup 就放一些参数定义，方法定义
    const ebooks = ref(); //定义响应式数据变量，变化实时响应到界面
    const pagination = ref({
          current: 1,
          pageSize: 2,
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
      axios.get("ebook/list", params).then((response) => {
        loading.value = false;
        const data = response.data;
        ebooks.value = data.content;

        //重置分页按钮
        pagination.value.current = params.page;
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

    onMounted(() => {
      handleQuery({});
    });

    return {
      ebooks,  //html代码要拿到响应式变量，需要在setup最后return
      pagination,
      columns,
      loading,
      handleTableChange
    }
  }
});
</script>