<template>
    <el-row type="flex" class="header_row" justify="space-around">
        <el-col :span="4">&nbsp;</el-col>
        <el-col :span="6" class = "pad_top" >
          <el-row class="">
            <el-col :span="10" class="width_all">
              <div class="header_icon">
                <img class="header_img" src="@/assets/img/index.png">
                <span class="header_name">NBlog</span>
              </div>
            </el-col>
            <el-col :span="1">&nbsp;</el-col>
            <el-col :span="13" class="displayNav">
              <el-tooltip :manual="true" :tabindex='1' :value="searchResult" placement="bottom" effect="light">
                <div slot="content" @blur="closeSearchContent">
                  <el-row class="search_c_row">
                    <el-col :span="16">
                      <el-input
                        placeholder="请输入关键字"
                        prefix-icon="el-icon-search"
                        v-model="searchKeyWords"
                        @input="getSearchResults">
                      </el-input>
                    </el-col>
                    <el-col :span="6">
                      <el-select v-model="searchType" placeholder="请选择搜索类型" @change="getSearchResults">
                        <el-option
                          v-for="item in options"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                          @change="getSearchResults">
                        </el-option>
                      </el-select>
                    </el-col>
                    <el-col :span="1">
                      <span @click="closeSearchContent" style="font-size: 20px;padding-left: 20px"><i class="el-icon-close"></i></span>
                    </el-col>
                  </el-row>
                  <el-row id="myDivider"></el-row> <!--分割线-->

                  <div id="searchPart">
                    <div class="searchEmpty" v-if="searchResults.length === 0"></div>
                    <div v-else>
                      <div v-for="item in searchResults" class="eachPart">
                        <el-row type="flex" align="middle">
                          <el-col :span="18" class="searchTitle">
                            <a @click="skip(item.id)" v-html="item.title"></a>
                          </el-col>
                          <el-col :span="6" >
                            <div style="font-size: 15px">&nbsp;&nbsp;&nbsp;&nbsp;<span v-html="item.author"></span>&nbsp;&nbsp;&nbsp;&nbsp;{{item.time}}</div>
                          </el-col>
                        </el-row>
                        <br>
                        <div v-html="item.content" class="eachSearchContent"></div>
                      </div>
                      <div class="search_c_row center">已加载全部，共 &nbsp; {{searchResults.length}} &nbsp; 条</div>
                    </div>
                  </div>
                </div>
                <div @click="showSearchContent">
                  <el-input
                    placeholder="点击搜索"
                    prefix-icon="el-icon-search"
                    class="s_input"
                    disabled
                  >
                  </el-input>
                </div>
              </el-tooltip>
            </el-col>

          </el-row>
        </el-col>
        <el-col :span="8">&nbsp;</el-col>
        <el-col :span="1" style="min-width: 90px ;padding-top: 5px" class="displayNav">
            <div class="header_icon" v-if="!hasLogin">
                <el-avatar icon="el-icon-user-solid"></el-avatar>
                <a href="javascript:;" @click="requireLogin()" class="login">登录</a>
            </div>
          <el-tooltip  v-else class="item" effect="dark" placement="bottom">
            <div  class="header_icon" >
              <el-avatar :src="'http://nblog.org.cn/avatar/'+user.avatar+'.png'"></el-avatar>
              <a href="javascript:;" class="login">{{user.name}}</a>
            </div>
            <div slot="content">
              <el-button type="text" @click="exitLogin()">退出登录</el-button>
              <br>
              <el-button type="text" @click="info()">个人中心</el-button>
            </div>
          </el-tooltip>
        </el-col>
        <el-col :span="4">&nbsp;</el-col>
    </el-row>
</template>

<script>
    import {search} from "../../network/home";
    import {loginClick} from "../../assets/js/login";
    import {checkExpiration, exit} from "../../network/header";
    import axios from "axios";
    let interval;
    export default {
      name: "Header",
      data(){
            return{
              searchResult:false,
              searchKeyWords: '',
              searchResults: [],
              searchType: 4,
              options: [
                {value: 1, label: '按作者名搜索'},
                {value: 2, label: '按标题搜索'},
                {value: 3, label: '按内容搜索'},
                {value: 4, label: '混合搜索'},
              ],
              user:{'avatar':'default_avatar'},
              hasLogin:false
            }
        },
      methods:{
        showSearchContent(){
            this.searchResult = true;
          },
        closeSearchContent(){
          this.searchResult = false;
        },
        skip(id) {
          const url = (id * 99) ^ 0xBEEF;
          this.$router.push("/NBlog/article/"+ url +".html");
        },
        getSearchResults() {
          let _this = this
          search(_this.searchKeyWords, _this.searchType).then(resp => {
            let results = resp.data['data'];
            results.forEach(function(item) {
              item.time = item.createTime.slice(0, 10);
            });
            _this.searchResults = results;
          }).catch(() => this.$message.error('操作失败'));
        },
        requireLogin(){
          let hasLogin = loginClick();
        },
        checkLogin(){
          let token = localStorage.getItem("token") || "";
          return token.length>5
        },
        exitLogin(){
          let _this = this
          exit().then(res=>{
            if(res.data.code === 20000){
              axios.defaults.headers.token = "";
              localStorage.setItem("token","");
              localStorage.setItem("nbUser","");
              _this.hasLogin = false;
              _this.$message.success("已退出")
            }
          })

        },
        info(){
          this.$router.push('/nblog/info')
        }
      },
      created() {
        let _this = this
        if(this.checkLogin()){
          axios.defaults.headers = {
            "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
          }
          axios.defaults.headers.token = localStorage.getItem("token")

          checkExpiration().then(res => {
            if(res.data.code !== 20000 ){
              _this.$message.info("登录信息已过期")
              localStorage.setItem("nbUser","")
              localStorage.setItem("token","")
              _this.hasLogin = false;
              axios.defaults.headers.token=''
            }else{
              _this.hasLogin = true;
              _this.user =  JSON.parse(localStorage.getItem("nbUser"))
            }
          })
        }else {
          axios.defaults.headers.token=''
        }
      },
      mounted() {
        let _this = this

        interval=setInterval(()=>{
          let token =  localStorage.getItem("token") || ""
          if(token.length>5){
            axios.defaults.headers = {
              "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
            }
            axios.defaults.headers.token = localStorage.getItem("token")
            _this.hasLogin = true;
            _this.user =  JSON.parse(localStorage.getItem("nbUser"))
          }else {
            _this.hasLogin = false;
          }
        },10);
      },
      destroyed() {
        clearInterval(interval)
      }
    }
</script>

<style scoped>
    .header_icon{
        display: flex;
        align-items:center;
        justify-content:start;
        gap: 10px;
    }
    .header_name{
        font-size: 28px;
        color: #ff8c00;
        overflow: hidden;
        font-style: oblique;
    }
    .header_img{
        height: 50px;
    }
    .header_row{
      background-color: #FCF4EF;
        position: fixed;
        height: 60px;
        width: 100%;
        padding: 5px 0;
        z-index: 999;
    }
    .pad_top{
      /*min-width: 500px;*/
      padding-top: 5px;
    }

    .login{
        font-size: 16px;
    }

    .login:hover{
        color: firebrick;
    }

  .search_c_row{
    display: flex;
    align-items:center;
    justify-content:start;
    width: 700px;
    gap: 5px;
  }

  .center{
    justify-content:center;
    height: 50px;
    font-size: 18px;
    color: #c9c9c9;
  }

    #searchPart {
      height: 500px;;
      overflow: scroll;
      -ms-overflow-style: none;
      scrollbar-width: none; /* Firefox */
    }
    #searchPart::-webkit-scrollbar {
      display: none; /* Chrome Safari */
    }
    .eachPart {
      width: 700px;
      padding: 20px 5px 20px 3px;
      border-bottom: 1px dashed black;
    }
    .el-dialog div .el-row {
      margin: 10px 0;
    }
    .eachSearchContent {
      height: 70px;
      overflow: hidden;
      font-size: 95%;
      color: #000;
      line-height: 2;
      margin: 0.375rem 0;
    }

    .searchTitle {
      font-size: 20px;
    }

  .searchEmpty{
    width: 100%;
    height: 100%;
    background-size: cover;
    -webkit-background-size: cover;
    -o-background-size: cover;
    background: url("../../assets/img/nodata.png") no-repeat center 0;
  }

    /* ipad */
    @media screen and (max-width: 900px) {
      .pad_top{
        width: 100% !important;
      }

      .width_all{
        width: 100%!important;
      }

    }

    /* ipad */
    @media screen and (max-width: 901px) {
      .pad_top{
        width: 500px!important;
      }

      .width_all{
        width: 60%!important;
      }

    }

</style>
