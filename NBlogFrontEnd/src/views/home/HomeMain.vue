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
        <div class="block">
          <el-pagination
            @current-change="handleCurrentChange"
            :hide-on-single-page="value"
            :current-page="parseInt(this.$store.state.page)+1"
            :page-size="10"
            layout="total, prev, pager, next, jumper"
            :total="lens">
          </el-pagination>
        </div>

        <div v-if="articles.length===0">暂无内容</div>
      </el-main>
      <el-aside width="300px">
        <HomeTag/>
      </el-aside>
    </el-container>
  </div>
</div>
</template>

<script>
  import Breadcrumb from "components/content/breadcrumb/Breadcrumb";
  import HomeTag from "./HomeTag";
  import Article from "components/content/article/Article";
  import Page from "components/common/page/Page";

  import {getArticleData} from "network/home";

  export default {
    name: "HomeMain",
    data(){
      return {
        articles:[],
        breadcrumb:[
          {title:'首页'},
          {title:'最新分享'}
        ],
        lens:0,
        value:true,
        saveY:0,
        tagname:''
      }
    },
    components:{
      Breadcrumb,
      HomeTag,
      Article,
      Page
    },
    computed:{
      getYMD(){
        const date = new Date();
        let year=date .getFullYear()
        let month=date .getMonth()+1
        let day=date .getDate()

        if(month<10){
          month='0'+month
        }

        if(day<10){
          day='0'+day
        }

        return year+"-"+month+"-"+day
      }
    },
    created() {
      if (localStorage.getItem("PageStore") === null) {
        localStorage.setItem("PageStore", "0")
      }

      this.$store.commit('alertPage', localStorage.getItem("PageStore"))
      localStorage.getItem("PageStore")
      this.getData()

    },
    methods:{
      skip(url){
        localStorage.setItem("currentArticle",url)
        this.$router.push("/nblog/article/new")
      },
      getData(){
        getArticleData(this.$store.state.page,10,this.$store.state.user)
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
            this.lens=res.data.extend.len
          })
          .catch(error => console.log(error))
      },
      handleCurrentChange(val) {
        document.getElementById('scroll').scrollTop=0
        this.$store.commit('alertPage',val-1)
        this.getData();
      },
    },
    mounted() {
      if(localStorage.getItem('scroll')!==null){
        setTimeout(()=>{
          document.getElementById('scroll').scrollTop=parseFloat(localStorage.getItem('scroll'))
        },100)

      }

      document.getElementById('scroll').addEventListener('scroll', () => {
        localStorage.setItem("scroll",document.getElementById('scroll').scrollTop+"")
      })
    }
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
