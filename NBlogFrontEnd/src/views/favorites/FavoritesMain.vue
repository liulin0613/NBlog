<template>
  <div>
    <div style="padding:5px;">
      <Breadcrumb :route="breadcrumb"/>
      <div v-if="this.articles.length===0">
        <p>暂无收藏</p>
      </div>

      <div v-else class="block">
        <div class="radio">
          按收藏时间排序：
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
</template>

<script>
  import Breadcrumb from "components/content/breadcrumb/Breadcrumb";
  import {getAllfavorites} from "network/favorites";

  export default {
    name: "FavoritesMain",
    components:{
      Breadcrumb
    },
    data(){
      return {
        breadcrumb:[
          {title:'首页',path:'/nblog/new'},
          {title:'我的收藏'},
        ],
        articles:[],
        reverse: true,
      }
    },
    created() {
      getAllfavorites(this.$store.state.user)
        .then(res => {
          this.articles=res.data.extend.article
        })
        .catch(err => console.log(err))
    },
    methods:{
      skip(url){
        localStorage.setItem("currentArticle",url)
        this.$router.push("/nblog/article/favorites")
      },
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
