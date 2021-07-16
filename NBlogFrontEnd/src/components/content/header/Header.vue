<template>
  <div>
    <el-row type="flex" class="row-bg" justify="space-between">
      <el-col :span="4"><div class="grid-content">
        <img src="~assets/img/index.png" alt="自信且爱笑">
        <div>NBlog</div>
      </div></el-col>

      <el-col :span="5"><div class="center">
        <div>Simplicity &nbsp; is &nbsp; beauty</div>
      </div></el-col>

      <el-col :span="2"><div class="grid-content show">
        <el-menu class="el-menu-demo" mode="horizontal">
          <el-submenu index="1">

            <template slot="title">
            <span>
              <el-avatar> {{this.$store.state.user}} </el-avatar>
            </span>
            </template>
            <el-menu-item index="1-1" @click="exitLogin">退出登录</el-menu-item>
          </el-submenu>
        </el-menu>
      </div></el-col>
    </el-row>
  </div>

</template>

<script>
    export default {
        name: "Header",
      methods:{
        exitLogin(){
          this.$store.user=''
          localStorage.removeItem('user')
          this.$router.push("/")
        }
      }
    }
</script>

<style scoped>
  .center{
    font-size: 20px;
  }
  .el-menu-demo{
    background-color: inherit;
  }
  .row-bg{
    background-color: #F0F0F0;
    align-items:center;
    height: 70px;
    position: fixed;
    width: 97%;
    opacity: 1;
    z-index: 1501;
  }
  .grid-content{
    display: flex;
    align-items:center;
    justify-content:start;
    gap: 20px;
    min-width: 500px;
  }
  .grid-content >img{
    height: 60px;
  }
  .grid-content > div{
    font-size: 28px;
    color: darkorange;
    overflow: hidden;
    font-style:oblique
  }

  @media screen and (max-width: 900px) {
    .center {
      display: none;
    }

    .show{
      display: none;
    }
    .row-bg{
      width: 100%;
    }
  }

</style>
