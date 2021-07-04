<template>
  <div>
    <Layout>
      <div slot="header">
        <Header/>
      </div>
      <div slot="aside"><Nav/></div>
      <div slot="main">
        <div>
          <div style="padding:5px;">
            <Breadcrumb :route="breadcrumb"/>
            <div v-if="this.articles.length===0">
              <p>该用户暂无发表</p>
            </div>

            <div v-else class="block">
              <div class="radio">
                按发表时间排序：
                <el-radio-group v-model="reverse">
                  <el-radio :label="true">正序</el-radio>
                  <el-radio :label="false">倒序</el-radio>
                </el-radio-group>
              </div>

              <el-timeline :reverse="reverse">
                <el-timeline-item
                  v-for="(article, index) in articles"
                  :key="index"
                  :timestamp="article.atime">
                  <h3 class="atitle" @click="skip(article.aid)">{{article.atitle}}</h3>
                </el-timeline-item>
              </el-timeline>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  </div>
</template>

<script>
  import Layout from "components/content/layout/Layout";
  import Header from "components/content/header/Header";
  import Nav from "components/content/nav/Nav";
  import Breadcrumb from "components/content/breadcrumb/Breadcrumb";
  import {getPublishedByName} from "network/follow";

  export default {
    name: "FollowList",
    components:{
      Layout,
      Header,
      Nav,
      Breadcrumb
    },
    data(){
      return {
        breadcrumb:[
          {title:'首页',path:'/nblog/new'},
          {title:'我的关注',path: '/nblog/follow'},
          {title: '用户'}
        ],
        articles:[],
        reverse: true,
      }
    },
    methods:{
      skip(aid){
        localStorage.setItem("currentArticle",aid)
        this.$router.push("/nblog/article/follow")
      }
    },
    created() {
      this.breadcrumb[2].title=localStorage.getItem('currentList') +'的发表记录'
      getPublishedByName(localStorage.getItem('currentList'))
        .then(res => {
          console.log(res)
          this.articles=res.data.extend.article
        })
        .catch(err => console.log(err))
    }

  }
</script>

<style scoped>
  .block{
    padding: 20px;
  }

  .radio{
    padding-bottom: 20px;
  }

  .atitle{
    cursor: pointer;
  }

  .atitle:hover{
    color: green;
  }
</style>
