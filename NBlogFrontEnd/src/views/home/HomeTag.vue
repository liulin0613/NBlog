<template>
  <div>
    <el-card class="box-card">
      <div slot="header" >
        <span class="el-icon-search"></span> &nbsp;&nbsp;
        <span>全局搜索</span>
      </div>
      <div class="demo-input-suffix">
        <el-autocomplete
          v-model="state"
          :fetch-suggestions="querySearchAsync"
          placeholder="请输入标题"
          @select="handleSelect"
        ></el-autocomplete>

      </div>
    </el-card>
    <br>

    <el-card class="box-card">
      <div slot="header" class="clearfix tag-cloud">
        <span class="el-icon-edit"></span> &nbsp;&nbsp;
        <span>热门标签</span>
      </div>
      <div class="tag-group">
        <el-tag
          v-for="(item,i) in formatTags"
          :key="i"
          :type="type[i%5]"
          effect="dark">
          <a href="javascript:;" @click="tagurl(item.name)">{{item.name}} ({{item.count}})</a>
        </el-tag>
      </div>
    </el-card>
    <br>

    <el-card class="box-card adv">
      <div title="由 Spring Boot 强力驱动"><el-button size="mini" type="info" round>Powered : SpringBoot</el-button></div><br>
      <div title="vue.js 客服端渲染"><el-button size="mini" type="info" round>SPA : Vue.js</el-button></div><br>
      <div title="ui 框架 ：element ui"><el-button size="mini" type="info" round>UI : Element-UI</el-button></div><br>
      <div title="阿里云提供服务器和域名相关服务"><el-button size="mini" type="info" round>VPS & DNS : Aliyun</el-button></div><br>
    </el-card>
  </div>
</template>

<script>
  import {getHotTags,getAllTitles} from "network/home";

  export default {
    name: "HomeTag",
    data(){
        return {
          type:['success','','warning','danger','info'],
          tagName:[],
          tagCount:[],
          tags:[],
          state:'',
          searchBlogs:[],
          titles:[],
          ids:[]
        }
    },
    created() {
      getHotTags()
        .then(res => {
          this.tagName=res.data.extend.tagname
          this.tagCount=res.data.extend.tagcount
        })
        .catch(err => console.log(err))
    },
    computed:{
      formatTags(){
        for(let i=0;i<this.tagName.length;i++){
          this.tags[i]={"name":this.tagName[i],"count":this.tagCount[i]}
        }
        return this.tags
      },
      formatTitles(){
        for(let i=0;i<this.titles.length;i++){
          this.titles[i]={"value":this.titles[i],"path":this.ids[i]}
        }
        return this.titles
      }
    },
    methods:{
      querySearchAsync(queryString, cb) {
        var searchBlogs = this.searchBlogs;
        var results = queryString ? searchBlogs.filter(this.createStateFilter(queryString)) : searchBlogs;
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          cb(results);
        }, 1000 * Math.random());
      },
      createStateFilter(queryString) {
        return (state) => {
          return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1);
        };
      },
      handleSelect(item) {
        localStorage.setItem("currentArticle",item["path"])
        this.$router.push("/nblog/article/new")
      },
      tagurl(tagname){
        this.$router.push("/nblog/tag/"+tagname)
      }
    },
    mounted() {
      getAllTitles().then(res => {
        this.titles=res.data.extend.titles
        this.ids=res.data.extend.ids
        this.searchBlogs=this.formatTitles
      }).catch(err => console.log(err))
    }
  }
</script>

<style scoped>
  .adv{
    background: url("~assets/img/bg.jpg");
  }
  .tag-group{
    height: 220px;
    overflow: scroll;
    scrollbar-width: none; /* Firefox */
    -ms-overflow-style: none; /* IE 10+ */
  }
  .tag-group > .el-tag {
    margin: 2px;
  }

  .tag-group::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }
</style>
