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
                placeholder="doc name"
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
          :data-source="level1"
          :loading="loading"
          :pagination="false"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" width="50" height="50"/>
        </template>
<!--        <template v-slot:doc="{ text, record }">-->
<!--          <span>{{ getDocName(record.doc1Id) }} / {{ getDocName(record.doc2Id) }}</span>-->
<!--        </template>-->
        <template v-slot:action="{ text, record }">
          <a-space size="small">
<!--            <router-link :to="'/admin/doc?docId=' + record.id">-->
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
      title="Doc Form"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="Name">
        <a-input v-model:value="doc.name" />
      </a-form-item>
      <a-form-item label="Parent Doc">
        <a-tree-select
            v-model:value="doc.parent"
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="treeSelectData"
            placeholder="Please select"
            tree-default-expand-all
            :fieldNames="{label: 'name', key: 'id', value: 'id'}"
        >
        </a-tree-select>
      </a-form-item>
      <a-form-item label="Parent Doc">
<!--        <a-input v-model:value="doc.parent" />-->
        <a-select
            ref="select"
            v-model:value="doc.parent"
        >
          <a-select-option value="0">
            None
          </a-select-option>
          <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="doc.id === c.id">
            {{c.name}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="Sort">
        <a-input v-model:value="doc.sort" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, reactive, toRef } from 'vue';
import axios from 'axios';
import { message} from "ant-design-vue";
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminDoc',
  setup() { //setup 就放一些参数定义，方法定义
    const param = ref();
    param.value ={};
    const docs = ref(); //定义响应式数据变量，变化实时响应到界面
    const loading = ref(false);

    const columns = [
      {
        title: 'Name',
        dataIndex : 'name',
      },
      {
        title: 'Parent Doc',
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
     * 一级分类树，children属性就是二级分类
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级分类树，children属性就是二级分类
    // level1.value = [];

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      loading.value = true;
      axios.get("doc/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          console.log("Origin Array: ", docs.value);

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value,0);
          console.log("Tree Structure: ", level1);
        }else {
          message.error(data.message)
        }
      });
    }


    //---Doc Form-----
    // 因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
    const treeSelectData = ref();
    treeSelectData.value = [];

    const doc = ref({});
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);
    const handleModalOk = () => {
      modalLoading.value = true;

      axios.post("doc/save", doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;  // data = commonResp at backend
        if (data.success) {
          modalVisible.value = false;

          //reloading doc list
          handleQuery();
        }else {
          message.error(data.message);
        }
      });
    };

    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    //edit button
    const edit = (record: any) => {
      modalVisible.value = true;
      doc.value = Tool.copy(record);  //press 'edit' button, copy current record to edit

      // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: 'None'});
    };

    //add button
    const add = () => {
      modalVisible.value = true;
      doc.value = {};

      treeSelectData.value = Tool.copy(level1.value) || [];

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: 'None'});
    };

    //Delete button
    const handleDelete = (id : number) => {
      axios.delete("doc/delete/" + id).then((response) => {
        const data = response.data;  // data = commonResp at backend
        if (data.success) {  // if action is successes
          //reloading doc list
          handleQuery();
        }
      });
    }


    onMounted(() => {
      handleQuery();
    });

    return {
      param,
      //docs,  //html代码要拿到响应式变量，需要在setup最后return
      level1,
      columns,
      loading,
      handleQuery,

      edit,
      add,

      doc,
      modalVisible,
      modalLoading,
      handleModalOk,
      handleDelete,

      treeSelectData,
    }
  }
});
</script>
