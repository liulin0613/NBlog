<template>
  <div>
    <div style="padding:5px;">
      <Breadcrumb :route="breadcrumb"/>
      <el-container>
        <el-main>
          <div class="scroll-div" id="scroll">
            <Article v-for="item in articles" :tags="item.atags" :description="item.adescription">
              <div slot="title"><h1><span @click="skip(item.aid)">
              {{item.atitle}}
              <span v-if="getYMD===item.atime">
                <span  class="new-article">
                  <el-button type="danger" size="mini" round>new</el-button>
                </span>
              </span>
            </span></h1></div>
              <span slot="description">{{item.adescription}}</span>
              <span slot="time">{{item.atime}}</span>
              <span slot="author">{{item.aauthor}}</span>
            </Article>
          </div>
        </el-main>
        <el-aside width="350px">
          <el-card class="box-card">
            <div slot="header" class="clearfix tag-cloud">
              <span class="el-icon-edit"></span> &nbsp;&nbsp;
              <span>标签: <span style="color: #dd6161;">{{this.$route.params.tagname}}</span></span>
            </div>
            <ul style="padding-left: 10px;">
              <li >共有 ： {{articles.length}} 篇</li>
            </ul>
          </el-card>
        </el-aside>
      </el-container>
    </div>
  </div>
</template>

<script>
  import Breadcrumb from "components/content/breadcrumb/Breadcrumb";
  import {getTagArticleData} from "network/tag";
  import Article from "components/content/article/Article";

  export default {
    name: "TagDetailsMain",
    components:{
      Breadcrumb,
      Article
    },
    data(){
      return {
        breadcrumb:[
          {title:'首页',path:'/nblog/new'},
          {title:'标签',path: '/nblog/tags'},
          {title: ''}
        ],
        articles:[],
      }
    },
    methods:{
      skip(url){
        localStorage.setItem("currentArticle",url)
        localStorage.setItem("currentTagName",this.$route.params.tagname)
        this.$router.push("/nblog/article/tag")
      },
      getData(){
        getTagArticleData(this.$route.params.tagname,this.$store.state.user)
          .then(res => {
            const data=res.data.extend.articles
            data.forEach(function(item,index){
              item.atags=item.atags.split(",")
              item.adescription=item.acontent
              item.adescription=item.adescription.replace(new RegExp(("\$\$"), 'g'), ()=>"");
              item.adescription=item.adescription.replace(new RegExp(("!\\[(.*?)\\]\\((.*?)\\)"), 'g'), ()=>"{ image }");
              item.adescription=item.adescription.replace(new RegExp(("#"), 'g'), ()=>"");
              item.adescription=item.adescription.replace(new RegExp(("<hr>|<br>|\\\\|- |\s|[\r\n]"), 'g'), ()=>"");

              item.adescription="<span style='background-color:green;padding:3px;color: white;border-radius: 8px;'>简介</span>  "+item.adescription
              item.adescription=item.adescription+"<span style='color: darkgreen; font-size: 25px;'> &nbsp; ··· &nbsp; </span>"
            })
            this.articles=data
          })
          .catch(error => console.log(error))
      },
    },
    created() {
      this.getData()
    },
    mounted() {
      this.breadcrumb[2].title=this.$route.params.tagname
      if(localStorage.getItem('tagscroll')!==null){
        setTimeout(()=>{
          document.getElementById('scroll').scrollTop=parseFloat(localStorage.getItem('tagscroll'))
        },100)

      }

      document.getElementById('scroll').addEventListener('scroll', () => {
        localStorage.setItem("tagscroll",document.getElementById('scroll').scrollTop+"")
      })
    },
  }
</script>

<style scoped>
  .el-main{
    padding-bottom: 0;
  }
  .scroll-div{
    overflow: scroll;
    max-height: 77vh;
    scrollbar-width: none;
    -ms-overflow-style: none;
  }

  .scroll-div::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }

  h1>span{
    font-size: 1.7rem;
    color: cadetblue ;
    cursor: pointer;
  }

  h1>span:hover{
    color: green;
  }
  .new-article >button{
    position: absolute;
    top:20px;
    padding: 6px 6px;
    margin-left: 10px;
  }

</style>
