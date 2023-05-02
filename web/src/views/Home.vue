<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
    <a-menu
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
        @click="handleClick"
    >
      <a-menu-item key="welcome">
          <MailOutlined />
          <span>Welcome</span>
      </a-menu-item>
      <a-sub-menu v-for="item in level1" :key="item.id">
        <template v-slot:title>
          <span>{{item.name}}</span>
        </template>
        <a-menu-item v-for="child in item.children" :key="child.id">
          <MailOutlined   /><span>{{child.name}}</span>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >
      <div class="welcome" v-show="isShowWelcome">
        <h1>welcome to Wiki</h1>
      </div>
      <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }"
              :data-source="ebooks">

        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
            <span v-for="{ type, text } in actions" :key="type">
            <component v-bind:is="type" style="margin-right: 8px" />
            {{ text }}
          </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
  </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, reactive, toRef } from 'vue';
import HelloWorld from '@/components/HelloWorld.vue'; // @ is an alias to /src
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";

// const listData: Record<string, string>[] = [];
//
// for (let i = 0; i < 23; i++) {
//   listData.push({
//     href: 'https://www.antdv.com/',
//     title: `ant design vue part ${i}`,
//     avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
//     description:
//         'Ant Design, a design language for background applications, is refined by Ant UED Team.',
//     content:
//         'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
//   });
// }

export default defineComponent({
  name: 'Home',
  components: {
    HelloWorld,
  },
  setup() { //setup 就放一些参数定义，方法定义
    const ebooks = ref(); //定义响应式数据变量，变化实时响应到界面
    const ebooks1 = reactive({books: []}); //books是自己定义的属性，属性值用来放电子书列表
    // const pagination = {
    //   onChange: (page: number) => {
    //     console.log(page);
    //   },
    //   pageSize: 3,
    // };
    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ];

    const level1 = ref();
    let categorys: any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("Origin Array：", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("Tree Structure：", level1.value);
        } else {
          message.error(data.message);
        }
      });
    };

    const isShowWelcome = ref(true);
    let category2 = 0;

    const handleQueryEbook = () => {
      axios.get("/ebook/all", {
        params: {
          page: 1,
          size: 1000,
          category2: category2
        }
      }).
      then((response) => {  //初始化逻辑都写到onMounted方法里
        const data = response.data;
        ebooks.value = data.content;
        //ebooks1.books = data.content;
      })
    }

    const handleClick = (value: any) => {
      //console.log("menu click", value)
      if (value.key === 'welcome') {
        isShowWelcome.value = true;
      }else {
        category2 = value.key;
        isShowWelcome.value = false;
        handleQueryEbook();
      }
    };

    onMounted(() => {   //生命周期函数
      handleQueryCategory();
    });

    return {
      ebooks,  //html代码要拿到响应式变量，需要在setup最后return
      //ebooks2: toRef(ebooks1, "books"), //将ebooks1里面的books属性编程响应式变量
      //listData,
      //pagination,
      actions,
      handleClick,
      level1,
      isShowWelcome,
    }
  }
});
</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>
