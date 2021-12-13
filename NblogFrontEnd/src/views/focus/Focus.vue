<template>
  <div>
    <el-container>
      <el-header>
        <Header/>
      </el-header>
      <el-main style="z-index: 999;display: flex;justify-content: center">
        <div class="adjust_width favorite_main">
          <el-container>
            <el-aside style="width:150px">
              <Nav :activate_nav="'我的关注'"/>
            </el-aside>
            <el-main>
              <div class="fav_card">
                <div class="favEmpty" v-if="atts.length === 0"></div>
                <div v-else>
                  <el-card class="box-card" v-for="(item,i) in atts" :key="i">
                    <div slot="header" class="clearfix">
                      <span>NBlog</span>
                      <el-button @click="cancelFollow(item.id,i)" style="float: right; padding: 3px 0" type="text">取消关注</el-button>
                    </div>
                    <div class="user">
                      <el-avatar :src="'http://nblog.org.cn/avatar/'+item.avatar+'.png'"></el-avatar>
                    </div>
                    <div>
                      <ul class="details">
                        <li>
                          用户名：{{item.name}}
                        </li>
                        <li style="white-space: nowrap;text-overflow: ellipsis">
                          简介：
                          <el-tooltip class="item" effect="light" placement="right">
                            <span slot="content">{{item.intro}}</span>
                            <a style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;padding-bottom: 10px"><span> {{item.intro}} </span></a>
                          </el-tooltip>
                        </li>
                        <li>
                          注册时间：{{item.time}}
                        </li>
                        <li>
                          拥有粉丝：{{item.fansCount}} 人  &nbsp;&nbsp;
                          <el-tag v-if="item.fansCount<5" size="mini" class="tag" type="success">初涉江湖</el-tag>
                          <el-tag v-else-if="item.fansCount<10" size="mini" class="tag" type="warning">人海孤鸿</el-tag>
                          <el-tag v-else-if="item.fansCount<15" size="mini" class="tag" type="danger">名震江湖</el-tag>
                          <el-tag v-else size="mini" class="tag" type="danger">一代宗师</el-tag>
                        </li>
                        <li>
                          关注：{{item.attCount}} 人
                        </li>
                        <li>
                          共发表文章：{{item.articleCount}} 篇  &nbsp;
                          <el-button v-if="item.articlecount!==0" @click="see(item.id)" type="text">查看</el-button>
                        </li>
                      </ul>
                    </div>

                  </el-card>
                </div>
              </div>
            </el-main>
          </el-container>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import Nav from "../nav/Nav";
  import Header from "../../components/header/Header";
  import {getAllFollow} from "../../network/info";
  import {deleteFollow} from "../../network/articleDetails";

  let  loginCheck;
  export default {
    name: "Focus",
    components: {Nav, Header},
    data(){
      return{
        atts:[]
      }
    },
    methods:{
      cancelFollow(fid,i){
        deleteFollow(fid)
          .then(res => {
            if(res.data.code === 20000){
              this.atts.splice(i,1)
              this.$message('已取消关注')
            }else {
              this.$message(res.data.message)
            }
          })
          .catch(() => {})
      },
      see(id){
        localStorage.setItem("currentList",id)
        this.$router.push("/nblog/list")
      },
    },
    created(){
      getAllFollow().then(res=>{
        if(res.data.code === 20000){
          this.atts = res.data.data
        }
      }).catch(()=>{})
    },
    mounted(){
      let _this = this
      loginCheck = setInterval(()=>{
        let token = localStorage.getItem("token") || ""
        if(token.length<5){
          _this.$message.info("登录已退出")
          _this.$router.push('/')
        }
      },100)
    },
    destroyed(){
      clearInterval(loginCheck)
    }
  }
</script>

<style scoped>
  .favorite_main{
    width: 1500px;
    padding: 0 120px 0 120px;
    position: relative;
    z-index: 100;
  }

  .fav_card{
    height: 80vh;
    width: 100%;
    background-color: #fff;
    overflow: scroll;
    -ms-overflow-style: none;
    scrollbar-width: none; /* Firefox */
  }
  .fav_card::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }

  .fav_card::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }

  .favEmpty{
    width: 100%;
    height: 79vh;
    background-size: cover;
    -webkit-background-size: cover;
    -o-background-size: cover;
    background: url("../../assets/img/nodata.png") no-repeat center 0;
  }
  .box-card{
    width: 300px;
    height: 380px;
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
    line-height: 30px;
    padding-left: 10px;
  }
</style>
