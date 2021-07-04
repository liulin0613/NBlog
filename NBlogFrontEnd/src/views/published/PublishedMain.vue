<template>
  <div>
    <div style="padding:5px;">
      <Breadcrumb :route="breadcrumb"/>
      <el-container>
        <el-main>
          <div id="block">
            <el-timeline>
              <el-timeline-item timestamp="今天" placement="top">
                <el-card>
                  <div v-if="articles.length===0" style="color: cadetblue">
                    <h1>您还未发表文章，赶紧去分享吧~</h1>
                  </div>
                  <div v-else style="color: cadetblue">
                    <h1>共发表文章 <span style="color: red"> {{articles.length}} </span>篇</h1>
                    <br>
                    <el-row style="display: flex;align-items:center;">
                      <el-col :span="3">&nbsp;
                        <h2>过滤器：</h2>
                      </el-col>
                      <el-col :span="8">&nbsp;
                        <el-input id="searchInput" v-model="state" @input.enter.native="handleInput" placeholder="请输入内容"></el-input>
                      </el-col>
                    </el-row>
                  </div>
                </el-card>
              </el-timeline-item>
              <el-timeline-item v-for="(item,i) in articles" :timestamp="item.atime" placement="top">
                <el-card>
                  <h1>
                    <span @click="skip(item.aid)">
                      {{item.atitle}}
                    </span>
                  </h1>
                  <br>
                  <p>
                    {{item.acontent}}
                    <span v-if="item.acontent.length===100"><span style='color: darkgreen; font-size: 16px;'> &nbsp; ··· &nbsp; </span></span>
                  </p>
                  <br>
                  <div>
                    <el-button type="text">操作：</el-button>
                    &nbsp;
                    <el-popconfirm title="重新编辑会删除原博文，是否重新编辑？" @confirm="editor(item.aid)">
                      <el-button slot="reference"  type="primary" size="mini" icon="el-icon-edit" round>编辑</el-button>
                    </el-popconfirm>
                    &nbsp;&nbsp;
                    <el-popconfirm :title="'确定删除博文 -- '+item.atitle" @confirm="deleteArticle(item.aid,i)">
                      <el-button slot="reference" type="danger" size="mini" icon="el-icon-delete" round> 删除</el-button>
                    </el-popconfirm>

                  </div>
                </el-card>
              </el-timeline-item>

            </el-timeline>
          </div>
        </el-main>
        <el-aside width="350px">
          <published-e-charts/>
        </el-aside>
      </el-container>
    </div>
  </div>
</template>

<script>
  import {getPublished,deleteArticle} from "network/published";
  import Breadcrumb from "components/content/breadcrumb/Breadcrumb";
  import PublishedECharts from "./PublishedECharts";
  export default {
    name: "PublishedMain",
    components:{
      Breadcrumb,
      PublishedECharts
    },
    data(){
      return {
        breadcrumb:[
          {title:'首页',path:'/nblog/new'},
          {title:'已发表'}
        ],
        articles:[],
        state:'',
        articleCopy:[]
      }
    },
    created() {
      getPublished(this.$store.state.user)
        .then(res =>{
          this.articles=res.data.extend.articles
          this.articleCopy=res.data.extend.articles
        })
        .catch(err => this.$message.error("列表加载失败"))
    },
    methods:{
      skip(url){
        localStorage.setItem("currentArticle",url)
        this.$router.push("/nblog/article/published")
      },
      editor(aid){
        deleteArticle(aid)
          .then(res => {
            this.$store.state.reEditor=aid
            this.$router.push('/nblog/publish/reEditor')
          })
          .catch(err => this.$message.error("删除失败"))
      },
      deleteArticle(aid,i){
        deleteArticle(aid)
          .then(res => {
            this.$message({
              type:"success",
              message:"删除--"+this.articles[i].atitle+"--成功"
            })
            this.articles.splice(i,1)
          })
          .catch(err => this.$message.error("删除失败"))
      },
      handleInput(){
        this.articles=this.articleCopy
        let con=document.getElementById('searchInput').value
        let res = this.articles.filter(function(item,index,array){
          return (item.atitle.toLowerCase().indexOf(con.toLowerCase())!==-1);
        });
        if(res.length!==0){
          this.articles=res
        }
      }
    },
    mounted() {
      if(localStorage.getItem('publishScroll')!==null){
        setTimeout(()=>{
          document.getElementById('block').scrollTop=parseFloat(localStorage.getItem('publishScroll'))
        },100)

      }

      document.getElementById('block').addEventListener('scroll', () => {
        localStorage.setItem("publishScroll",document.getElementById('block').scrollTop+"")
      })
    },
  }
</script>

<style scoped>
  h1>span{
    font-size: 1.7rem;
    color: cadetblue ;
    cursor: pointer;
  }

  h1>span:hover{
    color: green;
  }

  #block{
    overflow: scroll;
    max-height: 77vh;
    scrollbar-width: none;
    -ms-overflow-style: none;
  }

  #block::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }
</style>
