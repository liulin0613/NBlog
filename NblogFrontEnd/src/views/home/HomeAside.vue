<template>
  <div class="home-aside-nav">
    <el-card>
      <el-row>
        <el-col><span class="el-icon-edit">&nbsp;&nbsp;&nbsp;&nbsp;<span>热门标签</span></span></el-col>
      </el-row>
      <br>
      <el-row class="tagsGroup">
        <el-tag
          v-for="(item, index) in tags"
          :key="index"
          @click="tagUrl(item.tagName)"
          :type="type[index % 5]"
          effect="dark">
          <a >{{item.tagName}} ({{item.tagCount}})</a>
        </el-tag>
      </el-row>
    </el-card>

    <br>

    <el-card>
      <el-row>
        <el-row>
          <el-col><span class="el-icon-medal">&nbsp;&nbsp;&nbsp;&nbsp;<span>热门文章</span></span></el-col>
        </el-row><br>
        <el-row>
          <ul>
            <li v-for="(item, index) in recommenders">
              <span>{{index + 1}}</span>&nbsp;
              <el-tooltip class="item" effect="light" placement="right">
                <span slot="content">{{item.name}} &nbsp;<span style="color: #c9c9c9">( {{item.count}} 次阅读) </span></span>
                <a style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;padding-bottom: 10px" @click="skip(item.id)">{{item.name}} &nbsp;<span style="color: #c9c9c9">( {{item.count}} 次阅读) </span></a>
              </el-tooltip>
            </li>
          </ul>
        </el-row>
      </el-row>
    </el-card>

    <br>

    <el-card style="padding-right: 2px">
      <span><i class="el-icon-s-data"></i> &nbsp;网站资讯</span>
      <br>
      <br>
      <el-row>
        <el-col :span="24">
          <el-row>
            <el-col :span="9">
              <span>运行时间</span>
            </el-col>
            <el-col :span="15">
              <span style="font-size: 12px">{{timeline}}</span>
            </el-col>
          </el-row>
          <br>
          <el-row>
            <el-col :span="9">
              <span>总访问量</span>
            </el-col>
            <el-col :span="15">
              <span>{{views}}</span>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
  import {getHotTags, getRecommendArticle, getViews} from "../../network/home";
  let inter;
  export default {
      name: "HomeAside",
      data(){
          return{
            type:['success','','warning','danger','info'],
            tags:[],
            recommenders:[],
            timeline:'',
            views:0
          }
      },
      created() {
        this.getAllHotTagsAndCounts();

        getRecommendArticle().then(res=>{
          this.recommenders = res.data.data
        }).catch(()=>{})

        getViews().then(res=>{
          if(res.data.code === 20000){
            this.views=res.data.data
          }
        }).catch(()=>{})
      },
      methods:{
        skip(id){
          const encode=(id*99)^0xBEEF;
          this.$router.push("/nblog/article/"+encode+".html")
        },
        getAllHotTagsAndCounts() {
          let _this = this
          getHotTags().then(resp => {
            _this.tags = resp.data['data']
          }).catch(() => {});
        },
        tagUrl(tagName) {
          this.$router.push("/nblog/tag/" + tagName);
        },
        timeLine(){
          let t1="2021/06/14 00:00:00";
          let dateEnd = new Date();
          let dateDiff = dateEnd.getTime() - new Date(t1).getTime();//时间差的毫秒数
          let dayDiff = Math.floor(dateDiff / (24 * 3600 * 1000));//计算出相差天数
          let leave1=dateDiff%(24*3600*1000) //计算天数后剩余的毫秒数
          let hours=Math.floor(leave1/(3600*1000))//计算出小时数
          //计算相差分钟数
          let leave2=leave1%(3600*1000) //计算小时数后剩余的毫秒数
          let minutes=Math.floor(leave2/(60*1000))//计算相差分钟数
          //计算相差秒数
          let leave3=leave2%(60*1000) //计算分钟数后剩余的毫秒数
          let seconds=Math.round(leave3/1000)
          this.timeline= dayDiff+" 天 "+hours+" 小时 "+minutes+" 分钟 "+seconds+" 秒 "
        }
      },
      mounted(){
        let _this= this
        inter = setInterval(()=>{
            _this.timeLine()
        },1000)
      },
      destroyed(){
        clearInterval(inter)
      }
    }
</script>

<style scoped>

  .home-aside-nav{
    padding-top: 20px;
    position: fixed;
    width: 300px;
  }

  .tagsGroup span{
    position: relative;
    margin: 2px;
  }

  li {
    margin-bottom: 12px;
    margin-left: 4px;
    font-size: 13px;
    display: flex;
    color: black;
  }

  li span {
    margin-top: 2px;
    margin-right: 5px;
  }
  li a:hover {
    color: red;
  }
</style>
