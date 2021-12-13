<template>
  <div>
    <el-row style="min-height: 400px">
      <div class="shade">&nbsp;
      </div>

      <div class="removesha">
        <div class="article-info-container">
          <div class="article-title">发表详情 </div>
          <div class="article-info">
            <div class="first-line">
              <span>&nbsp;</span>

            </div>
            <div class="second-line">
              &nbsp;
            </div>
          </div>
        </div>
      </div>

      <div class="banner">
        &nbsp;
      </div>
    </el-row>

    <el-row class="tag_info">
      <el-row type="flex" class="row-bg" justify="center">
        <el-col :span="10" class = "adjust_content">
          <el-card style="padding: 20px; width: 100%">
            <h1>共发表文章 {{articles.length}} 篇</h1>
            <br><br>
            <el-timeline>
              <el-timeline-item
                v-for="(article, index) in articles"
                :key="index"
                :timestamp="article.createTime.substring(0,11)">
                <h2 class="title" @click="skip(article.id)">{{article.title}}</h2>
              </el-timeline-item>
            </el-timeline>
          </el-card>
        </el-col>
      </el-row>
    </el-row>
  </div>
</template>

<script>
  import {getPublishedByID} from "../../network/info";

  export default {
    name: "List",
    data(){
      return{
        articles:[]
      }
    },
    created(){
      getPublishedByID(parseInt(localStorage.getItem('currentList'))).then(res=>{
        if(res.data.code === 20000){
          this.articles = res.data.data
        }
      })
    },
    methods:{
      skip(id){
        const encode=(id*99)^0xBEEF;
        this.$router.push("/nblog/article/"+encode+".html")
      }
    }
  }
</script>

<style scoped>
  .title:hover{
    color: #880000;
  }
  .content img{
    width: 100%;
    height: 90%;
  }

  .article-info-container{
    -webkit-text-size-adjust: 100%;
    font-size: 16px;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    line-height: 1.5;
    color: #eee!important;
    background-repeat: no-repeat;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    position: absolute;
    bottom: 6.25rem;
    padding: 0 8%;
    width: 100%;
    text-align: center;
    z-index: 202;
  }

  .article-title{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    line-height: 1.5;
    color: #eee!important;
    text-align: center;
    background-repeat: no-repeat;
    padding: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    font-size: 35px;
    margin: 20px 0 8px;
  }

  .article-info{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    color: #eee!important;
    text-align: center;
    background-repeat: no-repeat;
    padding: 0;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    font-size: 14px;
    line-height: 1.9;
    display: inline-block;
  }

  .first-line{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    color: #eee!important;
    text-align: center;
    font-size: 14px;
    line-height: 1.9;
    background-repeat: no-repeat;
    padding: 0;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
  }

  .tag_info{
    padding: 15px;
    position: relative;
    z-index: 999;
  }

  .banner{
    -webkit-text-size-adjust: 100%;
    font-size: 16px;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    line-height: 1.5;
    padding: 0;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    position: absolute;
    background-color: #49b1f5!important;
    left: 0;
    right: 0;
    height: 400px;
    animation: header-effect 1s;
    color: #eee!important;
    background: url("../../assets/img/article_bg_03.png") center center / cover no-repeat;
    z-index: 200;
  }

  .removesha{
    background-color:inherit;
    z-index: 203;
  }

  .shade{
    position: absolute;
    left: 0;
    right: 0;
    height: 400px;
    width: 100%;
    background-color: black;
    opacity: .3;
    z-index: 201;
  }
</style>
