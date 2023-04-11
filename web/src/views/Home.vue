<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
    <a-menu
        mode="inline"
        v-model:selectedKeys="selectedKeys2"
        v-model:openKeys="openKeys"
        :style="{ height: '100%', borderRight: 0 }"
    >
      <a-sub-menu key="sub1">
        <template #title>
              <span>
                <user-outlined />
                subnav 1
              </span>
        </template>
        <a-menu-item key="1">option1</a-menu-item>
        <a-menu-item key="2">option2</a-menu-item>
        <a-menu-item key="3">option3</a-menu-item>
        <a-menu-item key="4">option4</a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="sub2">
        <template #title>
              <span>
                <laptop-outlined />
                subnav 2
              </span>
        </template>
        <a-menu-item key="5">option5</a-menu-item>
        <a-menu-item key="6">option6</a-menu-item>
        <a-menu-item key="7">option7</a-menu-item>
        <a-menu-item key="8">option8</a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="sub3">
        <template #title>
              <span>
                <notification-outlined />
                subnav 3
              </span>
        </template>
        <a-menu-item key="9">option9</a-menu-item>
        <a-menu-item key="10">option10</a-menu-item>
        <a-menu-item key="11">option11</a-menu-item>
        <a-menu-item key="12">option12</a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
  >
      <pre>
        {{ebooks}}  <!--使用{{xxx}}来获取变量 -->
        {{ebooks2}}
      </pre>
  </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, reactive, toRef } from 'vue';
import HelloWorld from '@/components/HelloWorld.vue'; // @ is an alias to /src
import axios from 'axios';

export default defineComponent({
  name: 'Home',
  components: {
    HelloWorld,
  },
  setup() { //setup 就放一些参数定义，方法定义
    console.log("setup");
    const ebooks = ref(); //定义响应式数据变量，变化实时响应到界面
    const ebooks1 = reactive({books: []}); //books是自己定义的属性，属性值用来放电子书列表

    onMounted(() => {   //生命周期函数
      console.log("onMounted");
      axios.get("http://localhost:8081/ebook/list?name=java").then((response) => {  //初始化逻辑都写到onMounted方法里
        const data = response.data;
        ebooks.value = data.content;
        ebooks1.books = data.content;
        console.log(response);
      })
    });

    return {
      ebooks,  //html代码要拿到响应式变量，需要在setup最后return
      ebooks2: toRef(ebooks1, "books") //将ebooks1里面的books属性编程响应式变量
    }
  }
});
</script>
