<template>
  <div>
    <div class="iconDiv noPrint">
      <img @click="toindex()" src="@/assets/img/index.png">
    </div>
    <el-row class="noPrint"  style="min-height: 400px">
      <div class="shade">&nbsp;
      </div>

      <div class="removesha">
        <div class="article-info-container">
          <div class="article-title">{{article.title}}</div>
          <div class="article-info">
            <div class="first-line">
              <span> <i class="el-icon-date"> &nbsp;</i>发表于 ：{{article.createTime.substring(0,11)}} </span>
              <span> &nbsp;|&nbsp; </span>
              <span>  <i class="el-icon-refresh"> &nbsp;</i>更新于 ：{{article.updateTime.substring(0,11)}} </span>
            </div>
            <div class="second-line">
              <span>  <i class="el-icon-view"> &nbsp;</i>阅读 ：{{article.allView}} </span>
              <span> &nbsp;|&nbsp; </span>
              <span>  <i class="el-icon-thumb"> &nbsp;</i>点赞 ：{{article.allLike}} </span>
              <span> &nbsp;|&nbsp; </span>
              <span>  <i class="el-icon-star-on"> &nbsp;</i>收藏 ：{{article.allFavorites}} </span>
              <span> &nbsp;|&nbsp; </span>
              <span>  <i class="el-icon-chat-dot-round"> &nbsp;</i>评论 ：0 </span>
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
    <el-row class="article_info">
      <el-col class="noPrint"  :span="2">&nbsp;</el-col>
      <el-col class="needPrint" :span="16" >
          <mavon-editor
            class="md"
            :value="fullContents"
            :defaultOpen="'preview'"
            :toolbarsFlag="false"
            :editable="false"
            :boxShadow="false"
            :scrollStyle="false"
            :previewBackground="'inherit'"
            :subfield = "false"
            :ishljs="true"
            :codeStyle="'idea'"
            :externalLink="externalLink"
          />

        <el-row type="flex" class="displayNav dzAndsc noPrint" justify="center" style="position: relative;z-index: 999">
          <el-col :span="6" style="min-width: 250px">
            <div class="dzAndsc_btn">
              <el-button @click="addLike()" v-if="article.is_like !== 1" type="info"><i class="el-icon-thumb"></i>&nbsp;点赞</el-button>
              <el-button @click="deleteLike()" v-else type="success"><i class="el-icon-check"></i>&nbsp;已点赞</el-button>
              &nbsp;&nbsp;
              <el-button @click="addFavorites()" type="primary" v-if="article.is_Favorites !== 1"><i class="el-icon-star-on"></i>&nbsp;收藏</el-button>
              <el-button @click="deleteFavorites()" type="success" v-else><i class="el-icon-check"></i>&nbsp;已收藏</el-button>
            </div>
          </el-col>
        </el-row>

        <el-row class="noPrint"  style="background-color: #fff; position: relative;z-index: 999">
          <hr class="myhr">
        </el-row>
        <el-row class="noPrint"  style="background-color: #fff;padding-left: 20px;position: relative;z-index: 999">
          <div class="commen-title">
            <i class="el-icon-chat-dot-round"></i> &nbsp;&nbsp;评论
          </div>
        </el-row>

        <el-row class="noPrint"  style="background-color: #fff;padding: 20px;position: relative;z-index: 999">
          <div class="comment-input-wrapper">
              <div style="display: flex;">
                <div class="v-avatar" >
                  <img src="https://www.static.talkxj.com/photos/0bca52afdb2b9998132355d716390c9f.png">
                </div>

                <div class="ml-3" style="width: 100%;">
                  <div class="comment-input">
                    <textarea data-v-1aa9cea5="" placeholder="评论功能暂未开启..." auto-grow="" dense="" class="comment-textarea"></textarea>
                  </div>
                  <div class="emoji-container">
                    <span  class="emoji-btn">
                      <i class="iconfont iconbiaoqing"></i>
                    </span>
                    <button  class="upload-btn v-comment-btn" style="margin-left: auto;"> 提交 </button>
                  </div>
                </div>
              </div>
          </div>
        </el-row>
      </el-col>
      <el-col class="noPrint"  :span="5" style="padding-left: 20px;position: relative;z-index: 999">
        <el-card id="catalogue" style="position:relative;z-index: 999">
          <el-row>
            <el-row>
              <el-col :span="18">
                <span><i class="el-icon-s-operation"></i> &nbsp; 目录</span>
              </el-col>
              <el-col :span="6" >
                <span id="arrow_up" @click="arrow_up"><i class="el-icon-arrow-up"> &nbsp;</i> <span class="arrow-up">收起</span></span>
                <span id="arrow_down" @click="arrow_dowm"><i class="el-icon-arrow-down"> &nbsp;</i> <span class="arrow-up">展开</span></span>
              </el-col>
            </el-row>

            <div id="catalogue-div">
              <br><br>
              <ul class="catalogue">
                <li v-if="catalogue.length===0">无目录</li>
                <li @click="url(item.id)" :class="activteId === item.id ? 'activate_cata':''" class="triDian" v-for="(item, index) in catalogue"
                    :key="index">
                  <p class="title_elli">
                    <span v-html="item.order"></span>
                    <span >{{item.title}}</span>
                  </p>
                </li>
              </ul>
            </div>
          </el-row>
        </el-card>

        <br>

        <el-card class="op" id="op" style="position:relative;z-index: 998">
          <div slot="header">
            <span class="el-icon-info"></span> &nbsp;&nbsp;
            <span>博文详情</span>
            <br>
          </div>

          <div class="info">
            <ul>
              <li><span style="color: #dd6161; line-height: 25px"> 标题： </span>{{article.title}}</li>
              <li>
                <span style="color: #dd6161;"> 作者：</span> {{article.name}}
              </li>
              <li v-if="article.tags.split(',')[0]!==''">
                <span style="color: #dd6161;">  标签：</span>
                <br><br>
                <div class="tags">
                  <el-tag
                    v-for="(item,i) in article.tags.split(',')"
                    :key="item"
                    :type="type[i%5]"
                    effect="plain">
                    <a href="javascript:;" @click="skip(item)">{{item}}</a>
                  </el-tag>
                </div>
              </li>
              <li>
                <div  class="buttonGroup">
                  <div>
                    <span style="color: #dd6161;">  操作：</span>
                    <br><br>
                  </div>
                  <el-button @click="addFollow()" v-if="article.is_Attention !== 1" type="primary" plain icon="el-icon-circle-plus-outline">关注作者</el-button>
                  <el-button @click="deleteFollow()" v-else type="success" plain icon="el-icon-check">已关注</el-button>
                  <el-button @click="downloadPDF()">保存为 pdf</el-button>
                </div>

              </li>

            </ul>

          </div>
        </el-card>

      </el-col>
    </el-row>
  </div>
</template>

<script>

  import {
    addFavorites,
    addFollow, addLike,
    deleteFavorites,
    deleteFollow, deleteLike,
    getArticlesDetailsByID,
    getMDs
  } from "../../network/articleDetails";
  import axios from "axios";

    export default {
        name: "Article",
      data(){
          return {
            article:{
              createTime:"",
              updateTime:"",
              content:"",
              tags:"",
              authorID:-361,
            },
            type:['success','','warning','danger','info'],
            activteId:"",
            fullContents:"",
            catalogue:[],
            externalLink: {
              markdown_css: function() {
                // 这是你的 markdown css文件路径
                return './markdown/github-markdown.min.css';
              },
              hljs_js: function() {
                // 这是你的hljs文件路径
                return './highlightjs/highlight.min.js';
              },
              hljs_css: function(css) {
                // 这是你的代码高亮配色文件路径
                return './highlightjs/styles/' + css + '.min.css';
              },
              hljs_lang: function(lang) {
                // 这是你的代码高亮语言解析路径
                return './highlightjs/languages/' + lang + '.min.js';
              },
              katex_css: function() {
                // 这是你的katex配色方案路径路径
                return './katex/katex.min.css';
              },
              katex_js: function() {
                // 这是你的katex.js路径
                return './katex/katex.min.js';
              },
            }
          }
      },
      computed:{
          authorIsMy(){
            return localStorage.getItem("user")!==undefined && localStorage.getItem("user").id === this.article.authorID
          }
      },
      created() {
        document.documentElement.scrollTop = 0

        const path=this.$route.params.id;
        let aid=(parseInt(path.substring(0,path.lastIndexOf(".")))^0xBEEF)/99;
        let _this = this;
        let serverPath = 'http://nblog.org.cn/images/'

        getArticlesDetailsByID(aid).then(res => {
          _this.article=res.data.data;
          axios.defaults.headers = {}
          getMDs(_this.article.authorId,_this.article.content).then(res=>{
            axios.defaults.headers = {
              "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
            }
            axios.defaults.headers.token = localStorage.getItem("token")

            // 获取整篇博文的.md文件
            this.fullContents = res.data;
            // 匹配博文中所有图片，[]兜底防止匹配不到
            let fakeUrls = this.fullContents.match(/(?<=\!\[.*\]\()(.+)(?=\))/g) || [];
            // backUrl替换为realUrl，以正确显示博文图片
            fakeUrls.forEach(function(item) {
              _this.fullContents = _this.fullContents.replace(item, serverPath + _this.article.authorId + '/' + item + '.png');
            });

            // 获取博文所以类似‘# ’的字符串
            let allTitles = _this.fullContents.match(/(#+)(\s+)(.*)/g) || [];
            // 获取博文代码块字符串
            let codeBlock = _this.fullContents.match(/(```([\s\S]+?)```)/g) || [];
            let codeTitles = [];
            // 对每个代码块抽取类似‘# ’的字符串
            for(let i= 0; i < codeBlock.length; i++){
              let k = codeBlock[i].match(/(#+)(\s+)(.*)/g) || [];
              k.forEach(function(item) {
                codeTitles.push(item);
              });
            }

            let real_titles = [];
            // 从所有抽取到的以‘# ’开头的一行字符串集合中过滤到从各个代码块中抽取的以‘# ’开头的一行字符串
            for (let i = 0, j = 0; i < allTitles.length;) {
              if (j < codeTitles.length && codeTitles[j] === allTitles[i]) {
                i++;
                j++;
              } else {
                real_titles.push(allTitles[i]);
                i++;
              }
            }

            let min_title = 7;
            real_titles.forEach(function(item) {
              let pos = item.indexOf(' '); // 第一个空格出现的位置
              let front = item.substring(0, pos);
              let len = front.length;


              if(len < min_title) min_title=len;

              let kong = '';
              for (let i = 1; i < len; i++) { // 根据len加空格方便渲染目录结构
                kong += '&nbsp;&nbsp;&nbsp;&nbsp;';
              }

              let title = item.substring(pos + 1, item.length).trim();
              let id = _this.generateMixed();
              _this.catalogue.push({'id': id, 'title': title, 'order': kong});
              _this.fullContents = _this.fullContents.replace(item, '\n' + '<h' + len + '><a class="title" id="' + id + '"></a>' + title + '</h' + len + '>' + '\n');
            })

            if(min_title !== 1){
              _this.catalogue.forEach((item) => {
                item.order = item.order.substring((min_title - 1)*24)
              })
            }

            _this.activteId=_this.catalogue[0].id
          }).catch(()=>{})
        }).catch(()=>{
          axios.defaults.headers = {
            "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
          }
          axios.defaults.headers.token = localStorage.getItem("token")

        })


      },
      methods:{
        downloadPDF(){
          window.print()
        },
        deleteLike(){
          if(this.hasLogin()){
            deleteLike(this.article.id).then(res =>{
              if(res.data.code === 20000){
                this.article.is_like = 0
                this.$message.success("已取消点赞")
                this.article.allLike -= 1
              }
            })
          }else{
            document.getElementById("login").style.display = "block"
            document.getElementsByClassName("v-modal")[0].style.display = "block"
            this.$message.info("请先登录")
          }
        },
        addLike(){
          if(this.hasLogin()){
            addLike(this.article.id).then(res =>{
              if(res.data.code === 20000){
                this.article.is_like = 1
                this.$message.success("已点赞")
                this.article.allLike += 1
              }
            })
          }else{
            document.getElementById("login").style.display = "block"
            document.getElementsByClassName("v-modal")[0].style.display = "block"
            this.$message.info("请先登录")
          }
        },
        deleteFollow(){
          if(this.hasLogin()){
            deleteFollow(this.article.authorId).then(res =>{
              if(res.data.code === 20000){
                this.article.is_Attention = 0
                this.$message.success("已取消关注")
              }
            })
          }else{
            document.getElementById("login").style.display = "block"
            document.getElementsByClassName("v-modal")[0].style.display = "block"
            this.$message.info("请先登录")
          }
        },
        addFollow(){
          if(this.hasLogin()){
            addFollow(this.article.authorId).then(res =>{
              if(res.data.code === 20000){
                this.article.is_Attention = 1
                this.$message.success("已关注")
              }
            })
          }else{
            document.getElementById("login").style.display = "block"
            document.getElementsByClassName("v-modal")[0].style.display = "block"
            this.$message.info("请先登录")
          }
        },
        addFavorites(){
          if(this.hasLogin()){
            addFavorites(this.article.id).then(res =>{
              if(res.data.code === 20000){
                this.article.is_Favorites = 1
                this.$message.success("已收藏")
                this.article.allFavorites += 1
              }
            })
          }else{
            document.getElementById("login").style.display = "block"
            document.getElementsByClassName("v-modal")[0].style.display = "block"
            this.$message.info("请先登录")
          }
        },
        deleteFavorites(){
          if(this.hasLogin()){
            deleteFavorites(this.article.id).then(res =>{
              if(res.data.code === 20000){
                this.article.is_Favorites = 0
                this.$message.success("已取消收藏")
                this.article.allFavorites -= 1
              }
            })
          }else{
            document.getElementById("login").style.display = "block"
            document.getElementsByClassName("v-modal")[0].style.display = "block"
            this.$message.info("请先登录")
          }
        },

        hasLogin(){
          let token = localStorage.getItem("token") || ""
          return token.length>5
        },
        toindex(){
          this.$router.push('/')
        },
        skip(name){
          window.onscroll= null
          this.$router.push('/nblog/tag/'+name)
        },
        generateMixed(){
          return 'a'+'xxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0,
              v = c === 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
          });
        },
        url(url) {
          this.activteId = url
          document.querySelector('#' + url).scrollIntoView(true);
        },
        arrow_up(){
          document.getElementById("arrow_up").style.display='none'
          document.getElementById("arrow_down").style.display='block'
          document.getElementById("catalogue-div").style.display='none'
          document.getElementById("op").style.display="block"

          this.divPosition()
        },
        arrow_dowm(){
          document.getElementById("arrow_up").style.display='block'
          document.getElementById("arrow_down").style.display='none'
          document.getElementById("catalogue-div").style.display='block'

          this.divPosition()
        },
        divPosition(){
          //变量scrollTop是滚动条滚动时，距离顶部的距离
          var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;

          //变量windowHeight是可视区的高度
          var windowHeight = document.documentElement.clientHeight || document.body.clientHeight;

          if(scrollTop > 420){
            document.getElementById("catalogue").style.position="fixed"
            document.getElementById("catalogue").style.top="20px"


            if(document.getElementById("catalogue").offsetHeight +
              document.getElementById("op").offsetHeight < windowHeight){
              document.getElementById("op").style.position="fixed"
              document.getElementById("op").style.top= document.getElementById("catalogue").offsetHeight +40 + "px"
            }else {
              document.getElementById("op").style.display="none"
            }


          }else{
            document.getElementById("catalogue").style.position="relative"
            document.getElementById("catalogue").style.top="0"
            document.getElementById("op").style.position="relative"
            document.getElementById("op").style.top="0"
            document.getElementById("op").style.display="block"
          }
        }
      },
      mounted() {
        let _this = this
        window.onscroll = function() {
          _this.divPosition()

          //变量scrollTop是滚动条滚动时，距离顶部的距离
          var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;

          _this.catalogue.forEach((item)=>{
            if(scrollTop > 420 + document.getElementById(item.id).offsetTop){
              _this.activteId = item.id
            }
          })

        }
      }
    }
</script>

<style scoped>
  .upload-btn{
    -webkit-text-size-adjust: 100%;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    overflow-wrap: break-word;
    white-space: normal;
    background-repeat: no-repeat;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    font: inherit;
    overflow: visible;
    -webkit-appearance: button;
    text-transform: none;
    color: #fff;
    background-color: #49b1f5;
    border-color: #fff;
    border-radius: 4px;
    text-align: center;
    padding: 6px 16px;
    font-size: 14px;
    transition: all .3s;
    outline: none;
    margin-left: auto;
  }
  .emoji-btn{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    line-height: 1.5;
    overflow-wrap: break-word;
    white-space: normal;
    color: #4c4948;
    background-repeat: no-repeat;
    padding: 0;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    font-size: 1.3rem;
  }
  .emoji-container{
    -webkit-text-size-adjust: 100%;
    font-size: 16px;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    line-height: 1.5;
    overflow-wrap: break-word;
    white-space: normal;
    color: #4c4948;
    background-repeat: no-repeat;
    padding: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    margin: 10px 0;
    display: flex;
    align-items: center;
  }
  .comment-textarea{
    -webkit-text-size-adjust: 100%;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    overflow: auto;
    font: inherit;
    border-style: none;
    font-size: .875rem;
    color: #555!important;
    outline: none;
    padding: 10px 5px;
    min-height: 122px;
    resize: none;
    width: 100%;
    border-radius: 4px;
  }
  .comment-input{
    -webkit-text-size-adjust: 100%;
    font-size: 16px;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    line-height: 1.5;
    overflow-wrap: break-word;
    white-space: normal;
    color: #4c4948;
    background-repeat: no-repeat;
    padding: 0;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    position: relative;
  }
  .ml-3{
    -webkit-text-size-adjust: 100%;
    font-size: 16px;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    line-height: 1.5;
    overflow-wrap: break-word;
    white-space: normal;
    color: #4c4948;
    background-repeat: no-repeat;
    padding: 0;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    margin-left: 12px!important;
    width: 100%;
  }
  .v-avatar img{
    -webkit-text-size-adjust: 100%;
    font-size: 16px;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    overflow-wrap: break-word;
    white-space: normal;
    color: #4c4948;
    line-height: normal;
    text-align: center;
    background-repeat: no-repeat;
    padding: 0;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    border-style: none;
    border-radius: inherit;
    display: inline-flex;
    height: inherit;
    width: inherit;
  }

  .v-avatar{
    -webkit-text-size-adjust: 100%;
    font-size: 16px;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    overflow-wrap: break-word;
    white-space: normal;
    color: #4c4948;
    background-repeat: no-repeat;
    padding: 0;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    align-items: center;
    border-radius: 50%;
    display: inline-flex;
    justify-content: center;
    line-height: normal;
    position: relative;
    text-align: center;
    vertical-align: middle;
    overflow: hidden;
    height: 40px;
    min-width: 40px;
    width: 40px;
  }
  .comment-input-wrapper{
    -webkit-text-size-adjust: 100%;
    font-size: 16px;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    line-height: 1.5;
    overflow-wrap: break-word;
    white-space: normal;
    color: #4c4948;
    background-repeat: no-repeat;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    border: 1px solid rgba(144,147,153,.31);
    border-radius: 4px;
    padding: 10px;
    margin: 0 0 10px;
  }

  .commen-title{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    overflow-wrap: break-word;
    white-space: normal;
    color: #4c4948;
    background-repeat: no-repeat;
    box-sizing: border-box;
    padding: 0px;
    margin: 0px;
    text-decoration: none;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    display: flex;
    align-items: center;
    font-size: 1.25rem;
    font-weight: 700;
    line-height: 40px;
    margin-bottom: 10px;
  }

  .comment-title i {
    font-size: 1.5rem;
    margin-right: 5px;
  }

  .myhr{
    -webkit-text-size-adjust: 100%;
    font-size: 16px;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    line-height: 1.5;
    overflow-wrap: break-word;
    white-space: normal;
    color: #4c4948;
    background-repeat: no-repeat;
    box-sizing: border-box;
    padding: 0px;
    text-decoration: none;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    overflow: visible;
    position: relative;
    margin: 40px auto;
    border: 2px dashed #d2ebfd;
    width: calc(100% - 4px);
  }

  .dzAndsc_btn button{
    padding-left: 20px;
  }
  .dzAndsc{
    background-color: #fff;
    width: 100%;
    height: 50px;
    display: flex;
    align-items: center;
    align-content: center;
    padding-top: 10px;
  }
  .iconDiv img{
    width: 80px;
    position: fixed;
    top: 0;
    left: 5px;
    z-index: 1000;
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
    background: url("../../assets/img/details_header.png") center center / cover no-repeat;
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

  .article-info span{
    font-size: 95%;
  }

  .article_info{
    padding: 40px;
  }

  .catalogue li {
    margin: 1px 0;
    padding: 5px 5px;
    /*color: slategrey;*/
  }

  .catalogue{
    overflow: scroll;
    max-height: 85vh;
    -ms-overflow-style: none;
    scrollbar-width: none; /* Firefox */
  }

  .catalogue::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }

  li {
    list-style: none;
    margin-bottom: 15px;
  }

  #catalogue{
    width: 300px;
    top:20px;
  }

  .activate_cata{
    border-left:#009d92 3px solid;
    background-color: #00c4b6;
    color: #fff !important;
  }

  .title_elli{
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
    word-break: break-all;
  }

  #op{
    width: 300px;
    top:20px;
  }

  .tags span{
    margin-left: 5px;
  }

  .tags{
    overflow: hidden;
    line-height: 40px;
  }

  .arrow-up :hover{
    color: red;
  }

  .arrow-up{
  }

  #arrow_down{
    display: none;
  }

  @media screen and (max-width: 900px) {
    .el-col-5{
      display: none;
      width: 0;
    }
    .el-col-16{
      width: 98%;
    }

    .el-col-1{
      width: 0%;
    }

    .article-title{
      font-size: 18px;
    }
    .article_info{
      padding: 0 0 0 5px;
    }
  }
</style>
