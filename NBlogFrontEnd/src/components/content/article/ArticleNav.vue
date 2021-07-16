<template>
  <div>
    <div class="nav-div">
      <el-card class="box-card" id="ml">
        <div slot="header" class="fix-head">
          <span class="el-icon-share"></span> &nbsp;&nbsp;
          <span>目录结构</span>
        </div>

        <span id="con-load"><i class="el-icon-loading"></i> 目录加载中...</span>
        <ul id="contents">
          <li v-for="item in aData">
            <p @click="url(item.id)">
              <a href="javascript:void(0);">{{item.name}}</a>
            </p>
          </li>
        </ul>
      </el-card>

      <br><br>
      <el-card class="box-card" id="details-pane">
        <div slot="header">
          <span class="el-icon-info"></span> &nbsp;&nbsp;
          <span>博文详情</span>
        </div>
        <div class="info">
          <ul>
            <li>标题：{{title}}</li>
            <li>
              作者：{{author}}
              <el-tag v-if="this.$store.state.user===author" type="danger" effect="dark">我</el-tag>
            </li>
            <li>发表时间：{{time}}</li>
            <li v-if="transformTags[0]!==''">标签：
              <div class="tags">
                <el-tag
                  v-for="(item,i) in transformTags"
                  :key="item"
                  :type="type[i%5]"
                  effect="plain">
                  <a href="javascript:;" @click="tagurl(item)">{{item}}</a>
                </el-tag>
              </div>
            </li>
            <li>
              <div v-if="this.$store.state.user!==author" class="buttonGroup">
                <div>操作：<br></div>

                <el-button @click="fav" v-if="!hasfav" plain icon="el-icon-star-off">收藏</el-button>
                <el-button @click="notfav" type="success" v-else plain icon="el-icon-check">已收藏</el-button>

                <el-button @click="att" v-if="!hasatten" type="primary" plain icon="el-icon-circle-plus-outline">关注作者</el-button>
                <el-button @click="notatt" type="success" v-else plain icon="el-icon-check">已关注</el-button>

              </div>
            </li>
          </ul>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
  import {hasfavorites,addfavorites,deletefavorites} from "network/favorites";
  import {hasfollow,addfollow,deletefollow} from "network/follow";

  export default {
    name: "ArticleNav",
    data(){
      return {
        type:['info','','success','warning','danger'],
        hasfav:false,
        hasatten:false,
        fname:'',
      }
    },
    props:{
      aData: {
        type:Array,
        default:''
      },
      author:'',
      time:'',
      tags:'',
      title:'',
      aid:''
    },
    computed:{
      transformTags(){
        return (this.tags || "").split(',')
      }
    },
    methods:{
      url(url) {
        document.querySelector('#' + url).scrollIntoView(true);
      },
      tagurl(tagname){
        this.$router.push("/nblog/tag/"+tagname)
      },
      fav(){
        addfavorites(this.aid,this.$store.state.user)
          .then(res => {
            this.hasfav=true
            this.$message({
              type:"success",
              message:'已收藏'
            })
          })
          .catch(err => console.log(err))
      },
      notfav(){
        deletefavorites(this.aid,this.$store.state.user)
          .then(res => {
            this.hasfav=false
            this.$message('已取消收藏')
          })
          .catch(err => console.log(err))

      },
      att(){
        addfollow(this.$store.state.user,this.author)
          .then(res => {
            this.hasatten=true
            this.$message({
              type:"success",
              message:'已关注'
            })
          })
          .catch(err => console.log(err))
      },
      notatt(){
        deletefollow(this.$store.state.user,this.author)
          .then(res => {
            this.hasatten=false
            this.$message('已取消关注')
          })
          .catch(err => console.log(err))
      }
    },
    destroyed() {
      window.removeEventListener("scroll", this.directory);
    },
    created() {
      hasfavorites(this.aid,this.$store.state.user)
        .then(res => {
          this.hasfav = res.data.extend.count !== 0;
        })
        .catch(err => console.log(err))

      setTimeout(()=>{
        hasfollow(this.$store.state.user,this.author)
          .then(res => {
            this.hasatten = res.data.extend.count !== 0;
          })
          .catch(err => console.log(err))

      },300)

      setTimeout(()=>{
        document.getElementById('contents').style.display='block'
        document.getElementById('con-load').style.display='none'
      },2000)
    },
  }
</script>

<style scoped>
  .nav-div{
    position: fixed;
    width: 300px;
  }

  ul{
    line-height: 1.7;
    color: rgba(0,0,0,.85);
    font: 14px Helvetica Neue,Helvetica,PingFang SC,Tahoma,Arial,sans-serif;
    pointer-events: auto;
    margin: 0;
    padding: 0;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    display: block;
    list-style: none;
  }

  li{
    color: rgba(0,0,0,.85);
    font: 14px Helvetica Neue,Helvetica,PingFang SC,Tahoma,Arial,sans-serif;
    pointer-events: auto;
    margin: 0;
    padding: 0;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    list-style: none;
    line-height: 26px;
    margin-left: 20px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .tags{
    overflow: auto;
    min-height: 60px;
    display: flex;
    align-items: center;
    justify-content:start;
    gap: 5px;
    scrollbar-width: none; /* Firefox */
    -ms-overflow-style: none; /* IE 10+ */
  }

  .tags::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }
  .buttonGroup{
    padding-top: 8px;
  }

  #contents{
    max-height: 250px;
    overflow: auto;
    display: none;
    scrollbar-width: none; /* Firefox */
    -ms-overflow-style: none; /* IE 10+ */
  }

  #contents::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }

</style>
