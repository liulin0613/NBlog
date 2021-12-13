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
              <Nav :activate_nav="'收藏'"/>
            </el-aside>
            <el-main>
              <div class="fav_card">
                <div class="favEmpty" v-if="favs.length === 0"></div>
                <div v-else class="favdiv">
                  <span class="title">共收藏 {{favs.length}} 篇文章</span>
                  <br><br><br>
                  <el-timeline>
                    <el-timeline-item
                      v-for="(fav, index) in formatFavs()"
                      :key="index"
                      :size="fav.size"
                      class="hover-timeline"
                      :timestamp="fav.timestamp">
                      <span @click="skip(fav.id)" class="content">{{fav.content}}</span>
                    </el-timeline-item>
                  </el-timeline>
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
    import {getAllFavorites} from "../../network/favorite";
    let  loginCheck;
    export default {
      name: "Favorite",
      components:{Nav,Header},
      data(){
        return{
          favs:[],
        }
      },
      methods:{
        formatFavs(){
          let len = this.favs.length;
          if(len === 0 ){
            return []
          }else{
            let fm = [];
            for(let i=0;i<len;i++){
              fm.push({
                content: this.favs[i].title,
                timestamp: "收藏时间：" + this.favs[i].updateTime.substring(0,11),
                id:this.favs[i].id
              })
            }
            return fm
          }
        },
        skip(id){
          const encode=(id*99)^0xBEEF;
          this.$router.push("/nblog/article/"+encode+".html")
        }
      },
      created() {
        getAllFavorites().then((res)=>{
          this.favs = res.data.data
        })
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

  .favdiv{
    padding: 20px 20px 20px 40px;
  }

  .content{
    font-size: 18px;
  }

  .hover-timeline :hover{
    color: #990000;
  }

  .title{
    font-size: 20px;
  }
</style>
