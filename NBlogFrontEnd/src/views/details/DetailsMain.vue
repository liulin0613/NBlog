<template>
  <div>
    <div style="padding:5px;">
      <Breadcrumb :route="breadcrumb"/>
      <el-container>
        <el-main>
          <details-article :article="article.acontent" :time="article.atime"/>
        </el-main>
        <el-aside width="300px">
          <article-nav
            :aData="aData"
            :title="article.atitle"
            :author="article.aauthor"
            :time="article.atime"
            :tags="article.atags"
            :aid="aid"
          />
        </el-aside>
      </el-container>
    </div>
  </div>
</template>

<script>
  import Breadcrumb from "components/content/breadcrumb/Breadcrumb";
  import {getDetailsById} from "network/details";
  import ArticleNav from "components/content/article/ArticleNav";
  import DetailsArticle from "./DetailsArticle";

  export default {
    name: "DetailsMain",
    components:{
      Breadcrumb,
      ArticleNav,
      DetailsArticle
    },
    data(){
      return {
        article:'',
        breadcrumb:[
          {title:'首页',path:'/nblog/new'},
          {title:'最新分享',path: '/nblog/new'},
          {title:'博文详情'}
        ],
        aData:[],
        firstId:'',
        aid:-1
      }
    },
    created() {
      if(this.$route.params.class==='published'){
        this.breadcrumb[1].title="已发表"
        this.breadcrumb[1].path='/nblog/Published'
      }else if(this.$route.params.class==='tag'){
        this.breadcrumb=[
          {title:'首页',path:'/nblog/new'},
          {title:'标签',path: '/nblog/tags'},
          {title:localStorage.getItem("currentTagName"),path:'/nblog/tag/'+localStorage.getItem("currentTagName")},
          {title:'博文详情'}
        ]
      }else if(this.$route.params.class==='favorites'){
        this.breadcrumb=[
          {title:'首页',path:'/nblog/new'},
          {title:'收藏',path: '/nblog/favorites'},
          {title:'博文详情'}
        ]
      }else if(this.$route.params.class==='follow'){
        this.breadcrumb=[
          {title:'首页',path:'/nblog/new'},
          {title:'我的关注',path: '/nblog/follow'},
          {title:localStorage.getItem('currentList') +'的发表记录',path:"/nblog/list"},
          {title:'博文详情'}
        ]
      }

      if(localStorage.getItem("currentArticle")===null){
        this.$router.push("/nblog/new")
      }else{
        this.aid=localStorage.getItem("currentArticle")
        getDetailsById(localStorage.getItem("currentArticle"))
          .then(res => {
            this.article=res.data.extend.article
            if(this.$route.params.class==='tag'||this.$route.params.class==='follow'){
              this.breadcrumb[3].title=this.article.atitle
            }else{
              this.breadcrumb[2].title=this.article.atitle
            }
          })
          .catch(err => console.log(err))
      }
    },
    mounted() {
      setTimeout(()=>{
        this.listA();  // 获取页面所有a标签
      },2000)
    },
    methods:{
      listA() {
          for(let i=0; i<document.getElementsByTagName("a").length; i++) {

            if (document.getElementsByTagName("a")[i].id !== '') {
              if(this.firstId==="n"+document.getElementsByTagName("a")[i].id){
                break;
              }
              document.getElementsByTagName("a")[i].id="n"+document.getElementsByTagName("a")[i].id
              let aData = {
                'id' : document.getElementsByTagName("a")[i].id,  // 获取a标签的id
                'name' : document.getElementsByTagName("a")[i].parentNode.innerText  //
              };

              this.aData.splice(i, 1, aData);

              if(i===0){
                this.firstId=document.getElementsByTagName("a")[i].id
              }
            }

          }
      }
    }
  }
</script>

<style scoped>

</style>
