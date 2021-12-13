<template>
    <div class="nav">
        <div @click="skip(item)" class="nav_item" :class="item.title === activate? 'highlight' : ''" v-for="item in tabs">
            <i class="nav_icon" :class="item.icon"></i>
            <span class="nav_name" >{{item.title}}</span>
        </div>
        <br><br>
    </div>
</template>

<script>
    export default {
        name: "Nav",
        data(){
            return {
                tabs:[{title:"主页",icon:"el-icon-s-home",path:"/nblog/home"},
                    // {title:"交流社区",icon:"el-icon-chat-dot-square",path:""},
                    {title:"标签",icon:"el-icon-price-tag",path:"/nblog/tags"},
                    {title:"收藏",icon:"el-icon-folder-opened",path:"/nblog/favorite"},
                    {title:"发表文章",icon:"el-icon-edit",path:"/nblog/publish"},
                    {title:"已发表",icon:"el-icon-document-checked",path:"/nblog/published"},
                    {title:"草稿箱",icon:"el-icon-document-copy",path:"/nblog/draft"},
                    {title:"个人中心",icon:"el-icon-s-custom",path:"/nblog/info"},
                    {title:"我的关注",icon:"el-icon-star-on",path:"/nblog/focus"}],
                activate:""
            }
        },
      props:{
          activate_nav:{
            default:"主页",
            type:String
          }
      },
      mounted() {
          this.activate = this.activate_nav
      },
      methods:{
            skip(item){
              let notLogin = ["主页","交流社区","标签","发表文章"]
              if(notLogin.indexOf(item.title) !== -1){
                this.$router.push(item.path)
              }else {
                if(this.hasLogin()){
                  this.$router.push(item.path)
                }else{
                  document.getElementById("login").style.display = "block"
                  document.getElementsByClassName("v-modal")[0].style.display = "block"
                  this.$message.info("请先登录")
                }
              }
            },
            hasLogin(){
              let token = localStorage.getItem("token") || ""
              return token.length>5
            }
        }
    }
</script>

<style scoped>

    .highlight{
        color: #ff8200;
    }

    .nav{
        width: 150px;
        height: 50vh;
        position: fixed;
        margin-top: 20px;
        font-family: system-ui,-apple-system,Segoe UI,Roboto,Ubuntu,Cantarell,Noto Sans,sans-serif;
        font-size: 16px;
        -webkit-box-direction: normal;
        background-color: #fff;
        overflow-y: auto;
        border-radius: 4px 4px 0 0;
        box-sizing: border-box;
        padding-top: 10px;
        border-color: #f2f2f2;

    }

    .nav_item{
        font-family: system-ui,-apple-system,Segoe UI,Roboto,Ubuntu,Cantarell,Noto Sans,sans-serif;
        font-size: 16px;
        -webkit-box-direction: normal;
        display: flex;
        align-items: center;
        -webkit-box-align: center;
        position: relative;
        padding: 8px 16px;
        border-radius: 2px;
    }

    .nav_icon{
        width: 24px;
        height: 24px;
        margin-right: 18px;
        overflow: hidden;
        font-size: 24px;
        flex-shrink: 0;
        -webkit-font-smoothing: antialiased;
        font-style: normal;
        font-feature-settings: normal;
        font-variant: normal;
        font-weight: 400;
        text-decoration: none;
        text-transform: none;
        display: inline-block;
        line-height: 1;
    }

    .nav_name{
        font-family: system-ui,-apple-system,Segoe UI,Roboto,Ubuntu,Cantarell,Noto Sans,sans-serif;
        line-height: 20px;
        font-size: 14px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        -webkit-box-flex: 1;
        flex-grow: 1;
    }

    .nav_item:hover{
        background-color: #99999980;
    }
</style>
