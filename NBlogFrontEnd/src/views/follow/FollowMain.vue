<template>
  <div>
    <div style="padding:5px;">
      <Breadcrumb :route="breadcrumb"/>
      <div v-if="this.persons.length===0">
        <p>暂无关注的用户</p>
      </div>

      <div v-else class="block">
        <el-card class="box-card" v-for="(item,i) in persons" :key="i">
          <div slot="header" class="clearfix">
            <span>NBlog</span>
            <el-button @click="cancelFollow(item.name,i)" style="float: right; padding: 3px 0" type="text">取消关注</el-button>
          </div>
          <div class="user">
            <el-avatar> {{item.name}} </el-avatar>
          </div>
          <div>
            <ul class="details">
              <li>
                用户名：{{item.name}}
              </li>
              <li>
                注册时间：{{item.time}}
              </li>
              <li>
                拥有粉丝：{{item.fanscount}} <el-tag class="tag" type="danger">初具人气</el-tag>
              </li>
              <li>
                关注：{{item.followcount}} 人
              </li>
              <li>
                共发表文章：{{item.articlecount}} 篇  &nbsp;
                <el-button v-if="item.articlecount!==0" @click="see(item.name)" type="text">查看</el-button>
              </li>
            </ul>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
  import Breadcrumb from "components/content/breadcrumb/Breadcrumb";
  import {getAllfollow} from "network/follow";
  import {deletefollow} from "network/follow";

  export default {
    name: "FollowMain",
    components:{
      Breadcrumb
    },
    data(){
      return {
        breadcrumb:[
          {title:'首页',path:'/nblog/new'},
          {title:'我的关注'},
        ],
        persons:[],
        reverse: true,
      }
    },
    created() {
      getAllfollow(this.$store.state.user)
        .then(res => {
          this.persons=res.data.extend.user
        })
        .catch(err => console.log(err))
    },
    methods:{
      see(name){
        localStorage.setItem("currentList",name)
        this.$router.push("/nblog/list")
      },
      cancelFollow(fname,i){
        deletefollow(this.$store.state.user,fname)
          .then(res => {
            this.persons.splice(i,1)
            this.$message('已取消关注'+ fname)
          })
          .catch(err => console.log(err))
      }
    }
  }
</script>

<style scoped>
  .box-card{
    width: 300px;
    height: 320px;
    margin: 20px;
    float: left;
  }
  .user{
    padding-left: 100px;
  }
  .user > .el-avatar{
    width: 60px;
    height: 60px;
    line-height: 55px;
    font-size: 18px;
  }

  .details{
    padding-top: 20px;
    list-style: none;
    line-height: 25px;
    padding-left: 15px;
  }
  .tag{
    height: 30px;
  }
</style>
