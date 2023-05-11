<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <p>
            <a-form layout="inline">
              <a-form-item>
                <a-input-search
                    :model="param"
                    v-model:value="param.name"
                    placeholder="doc name"
                    enter-button="Search"
                    style="width: 230px"
                    @search="handleSearchQuery({page: 1, size: pagination.pageSize})"
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
              v-if="level1.length > 0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :loading="loading"
              :pagination="false"
              :default-expand-all-rows="true"
          >
            <template #name="{ text, record }">
<!--              {{record.sort}} -->
              {{text}}
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
                <a-button type="primary" @click="edit(record)" size="small">
                  Edit
                </a-button>
                <a-popconfirm
                    title="Are you sure delete this record?"
                    ok-text="Yes"
                    cancel-text="No"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger" size="small">
                    Delete
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()" >
                  Save
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">
            <a-form-item >
              <a-input v-model:value="doc.name" placeholder="Name"/>
            </a-form-item>
            <a-form-item >
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="Select Parent Chapter"
                  tree-default-expand-all
                  :fieldNames="{label: 'name', key: 'id', value: 'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item >
              <a-input v-model:value="doc.sort" placeholder="Sort"/>
            </a-form-item>
            <a-form-item label="Content">
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

    </a-layout-content>
  </a-layout>
<!--  <a-modal-->
<!--      title="Doc Form"-->
<!--      v-model:visible="modalVisible"-->
<!--      :confirm-loading="modalLoading"-->
<!--      @ok="handleModalOk"-->
<!--  >-->
<!--    -->
<!--  </a-modal>-->
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, reactive, toRef, createVNode } from 'vue';
import axios from 'axios';
import { message, Modal} from "ant-design-vue";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
import E from "wangeditor";

declare var window: any



export default defineComponent({
  name: 'AdminDoc',
  setup() { //setup 就放一些参数定义，方法定义
    const route = useRoute();
    console.log("路由：", route);
    console.log("route.path：", route.path);
    console.log("route.query：", route.query);
    console.log("route.param：", route.params);
    console.log("route.fullPath：", route.fullPath);
    console.log("route.name：", route.name);
    console.log("route.meta：", route.meta);
    const param = ref();
    param.value ={};
    const docs = ref(); //定义响应式数据变量，变化实时响应到界面
    const loading = ref(false);
    // 因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
    const treeSelectData = ref();
    treeSelectData.value = [];

    const pagination = ref({
      current: 1,
      pageSize: 1000,
      total: 0
    });


    const columns = [
      {
        title: 'Name',
        dataIndex : 'name',
        slots: {customRender: `name`}
      },
      // {
      //   title: 'Parent Doc',
      //   key: 'parent',
      //   dataIndex : 'parent'
      // },
      // {
      //   title: 'Sort',
      //   dataIndex : 'sort'
      // },
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
    level1.value = [];

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      loading.value = true;
      axios.get("doc/all/" + route.query.ebookId).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          console.log("Origin Array: ", docs.value);

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value,0);
          console.log("Tree Structure: ", level1);

          //初始化parent chapter下拉栏
          treeSelectData.value = Tool.copy(level1.value);
          // 为选择树添加一个"无"
          treeSelectData.value.unshift({id: 0, name: 'None'});
        }else {
          message.error(data.message)
        }
      });
    }

    /**
     * content查询
     **/
    const handleQueryContent = () => {
      axios.get("doc/find-content/" + doc.value.id).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          editor.txt.html(data.content)
        }else {
          message.error(data.message)
        }
      });
    }

    const handleSearchQuery = (params: any) => {
      loading.value = true;
      axios.get("doc/list",
          {params : {
              page: params.page,
              size: params.size,
              name: param.value.name,
              ebookId: route.query.ebookId,
            }
        }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content.list;
          console.log("Origin Array: ", docs.value);

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value,0);
          console.log("Tree Structure: ", level1);

          //重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;

        }else {
          message.error(data.message)
        }
      });
    }


    //---Doc Form-----


    const doc = ref();
    doc.value = {};
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);
    const editor = new E("#content");
    editor.config.zIndex = 0;
    editor.config.lang = 'en'
    editor.i18next = window.i18next;


    const handleSave = () => {
      modalLoading.value = true;
      doc.value.content = editor.txt.html(); //get content html
      axios.post("doc/save", doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;  // data = commonResp at backend
        if (data.success) {
          //modalVisible.value = false;
          message.success("Saved!")

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

    const deleteIds: Array<string> = [];
    const deleteNames: Array<string> = [];

    //查找整根树枝，将目标id放入数组
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("delete", node);
          //将目标id放入数组
          deleteIds.push(id);
          deleteNames.push(node.name);

          // 遍历所有子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    //edit button
    const edit = (record: any) => {
      editor.txt.html("");  //clear text before edit
      modalVisible.value = true;
      doc.value = Tool.copy(record);  //press 'edit' button, copy current record to edit
      handleQueryContent();

      // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: 'None'});
    };

    //add button
    const add = () => {
      editor.txt.html("");  //clear text before edit
      modalVisible.value = true;
      doc.value ={ebookId: route.query.ebookId};

      treeSelectData.value = Tool.copy(level1.value) || [];

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: 'None'});
    };

    //Delete button
    const handleDelete = (id : number) => {
      // 清空数组，否则多次删除时，数组会一直增加
      deleteIds.length = 0;
      deleteNames.length = 0;
      getDeleteIds(level1.value,id);
      Modal.confirm({
        title: 'Important Notice',
        icon: createVNode(ExclamationCircleOutlined),
        content: 'Document [' + deleteNames.join("，") + "] will be deleted permanently, still proceed?",
        onOk() {
          axios.delete("doc/delete/" + deleteIds.join(",")).then((response) => {
            const data = response.data;  // data = commonResp at backend
            if (data.success) {  // if action is successes
              //reloading doc list
              handleQuery();
            }
          });
        },
      });
    }


    onMounted(() => {
      handleQuery();
      editor.config.height = 450
      editor.create();
    });

    return {
      param,
      pagination,
      //docs,  //html代码要拿到响应式变量，需要在setup最后return
      level1,
      columns,
      loading,
      handleQuery,
      handleSearchQuery,

      edit,
      add,

      doc,
      modalVisible,
      modalLoading,
      handleSave,
      handleDelete,

      treeSelectData,
    }
  }
});
</script>
