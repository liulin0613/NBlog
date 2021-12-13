<template>
  <div class="adjust_width home_main">
      <el-container class="main-container">
          <el-aside style="width:150px" class="displayNav">
            <Nav/>
          </el-aside>
          <el-main>
              <el-row style="max-width: 100%">
                  <el-col v-for="(item,index) in articles" :key="index" :span="24">
                      <el-card  class="box-card article">
                          <el-row style="height: 235px">
                            <el-col class="phone-show" :style="{width: item.img.length>0?'70%':'100%'}">
                              <div class="article-wrapper no-padding">
                                <div class="title" @click="look(item)">
                                  <a href="javascript:;">{{item.title}}</a>
                                </div>

                                <div @click="look(item)" class="article-info flex_center">
                                  <span v-if="item.isTop===1" style="color: #ff8200"><i class="el-icon-top"></i> 置顶</span>
                                  <span v-if="item.isTop===1">|</span>
                                  <span v-if="item.updateTime.substring(0,11).trim()===today()" style="color: crimson"> 今日发表</span>
                                  <span><i class="el-icon-date"></i> {{item.updateTime.substring(0,11)}}</span> |
                                  <span><i class="el-icon-user-solid"></i> {{item.author}}</span>
                                </div>

                                <div  v-if="item.tags[0]!==''" class="displayNav tagsGroup flex_center">
                                  <span v-for="tag in item.tags">
                                      <div class="tagDetails">
                                          <i class="el-icon-price-tag"></i> &nbsp;
                                          <span @click="toTag(tag)">{{tag}}</span>
                                      </div>
                                  </span>
                                </div>
                                <br>

                                <div class="article-content" @click="look(item)">
                                  {{item.desc}}
                                </div>
                              </div>
                            </el-col>

                            <el-col  class="phone-no-show" @click="look(item)" v-if="item.img.length>0" style="width: 30%;height: 100%; display: flex;align-items:center;justify-content: center">
                              <img @click="look(item)" style="max-width: 100%;max-height: 100%" :src="'http://nblog.org.cn/images/'+item.userId+'/'+item.img+'.png'" />
                            </el-col>
                          </el-row>
                      </el-card>
                  </el-col>
              </el-row>
          </el-main>
        <el-aside style="width:300px" class="displayNav">
          <HomeAside />
        </el-aside>
      </el-container>
  </div>
</template>

<script>
    import Nav from "../nav/Nav";
    import HomeAside from "./HomeAside";
    import {getArticles} from "../../network/home";
    let homeScrollTop_inter
    export default {
        name: "HomeMain",
        components: {Nav,HomeAside},
        data(){
            return{
                curPage:0,
                allArticleCount:1,
                articles:[],
                dialogVisible:true,
                currentShowUrl:''
            }
        },
        created() {

          let _this=this;

          if(localStorage.getItem("homeScrollTop")===undefined || localStorage.getItem("homeScrollTop")===null){
            localStorage.setItem("homeScrollTop",201)
          }
          // getArticles(this.curPage,10).then(res=>{
          //   if(res.data.code === 20000){
          //     _this.allArticleCount = res.data.data.total
          //     res.data.data.articles.forEach((item) =>{
          //       item.tags = item.tags.split(",");
          //       _this.articles.push(item)
          //     })
          //   }
          // }).catch(err => {
          //   this.$message({
          //     type: 'error',
          //     message: '网络连接失败'
          //   });
          // })


          if(localStorage.getItem("homeScrollTop")!==undefined && localStorage.getItem("homeScrollTop")!==null){
            let page = parseInt(parseInt(localStorage.getItem("homeScrollTop")) / 280 / 10 + 1 + "")
            _this.articles = []
            this.getArticleData((page)*10)

             homeScrollTop_inter=self.setInterval(()=>{
              var scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
              let homeScrollTop = parseInt(localStorage.getItem("homeScrollTop"))
              if(scrollHeight > homeScrollTop){
                document.documentElement.scrollTop = parseInt(localStorage.getItem("homeScrollTop"))
                window.clearInterval(homeScrollTop_inter)
                if(document.documentElement.scrollTop<200){
                  document.documentElement.scrollTop = 0
                }
              }
            },50);
          }
        },
        destroyed(){
            clearInterval(homeScrollTop_inter)
        },
        methods:{
          showImg(item){
            this.currentShowUrl = 'http://nblog.org.cn/images/'+item.userId+'/'+item.img+'.png';
            this.dialogVisible=true
          },
            today(){
              let myDate = new Date();
              let year = myDate.getFullYear();
              let month = myDate.getMonth() + 1;
              let day = myDate.getDate();

              if(month<10){
                month = "0"+month;
              }

              if(day<10){
                day = "0" + day;
              }
              return year + "-" + month + "-" + day;
            },
            getArticleData(limit){
                let _this=this
                if(_this.articles.length<_this.allArticleCount){
                  _this.curPage+=1;
                    getArticles(this.curPage-1,limit).then(res=>{
                        if(res.data.code === 20000){
                            _this.allArticleCount = res.data.data.total
                            res.data.data.articles.forEach((item) =>{
                                item.tags = item.tags.split(",");
                                _this.articles.push(item)
                            })
                          }
                    }).catch(err => {
                        this.$message({
                            type: 'error',
                            message: '网络连接失败'
                        });
                    })
                }
            },
            look(item){
                const encode=(item.id*99)^0xBEEF;
                this.$router.push("/nblog/article/"+encode+".html")
            },
            toTag(tag){
                this.$router.push("/nblog/tag/"+tag)
            }
        },
      mounted() {
        let _this = this
        window.onscroll = function() {
          //变量scrollTop是滚动条滚动时，距离顶部的距离
          let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
          if(scrollTop>200){
            localStorage.setItem("homeScrollTop",scrollTop)
          }
          //变量windowHeight是可视区的高度
          var windowHeight = document.documentElement.clientHeight || document.body.clientHeight;
          //变量scrollHeight是滚动条的总高度
          var scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
          //滚动条到底部的条件
          if (scrollHeight - (scrollTop + windowHeight) < 1) {
            //写后台加载数据的函数
            if(_this.articles.length>0){
              _this.getArticleData(10)
            }
          }
        }
        }
    }
</script>

<style scoped>
    .flex_center{
        display: flex;
        align-items:center;
        justify-content:start;
        gap: 10px;
    }
    .home_main{
        width: 1500px;
        padding: 0 120px 0 120px;
        position: relative;
        z-index: 100;
        min-height: 100vh;
    }

    .article{
        height: 280px;
        border-radius: 20px;
        margin: 20px 10px 0 10px;
    }

    .article-wrapper{
        padding: 20px 40px;
        line-height: 1.4;
    }

    .article-info{
        font-size: 95%;
        color: #858585;
        line-height: 2;
        margin: 0.375rem 0;
    }

    .tagsGroup{
        font-size: 95%;
        color: #858585;
    }
    .title{
        font-size: 24px;
    }

    .article-content{
        line-height: 2;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
    }

    .article:hover{
        transform: translateY(-6px);
        -webkit-transform: translateY(-6px);
        -moz-transform: translateY(-6px);
        box-shadow: 0 26px 40px -24px rgba(0, 36, 100, .5);
        -webkit-box-shadow: 0 26px 40px -24px rgba(0, 36, 100, .5);
        -moz-box-shadow: 0 26px 40px -24px rgba(0, 36, 100, .5);
        /*background-image: url("../../assets/img/article_bg_02.png");*/
        /*background-size: cover;*/
        opacity: 0.9;
    }

    .catalog div{
        padding: 0;
    }
    .el-main{
        padding: 0;
    }

    .v-show-content{
        padding: 0;
    }

    .tagDetails :hover{
        color: red;
    }

    /* ipad */
    @media screen and (max-width: 600px) {
      .phone-show{
        width: 100% !important;
      }

      .phone-no-show{
        display: none;
      }

      .main-container{
        width: 100% !important;
      }
    }

</style>
