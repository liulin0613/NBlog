<template>
  <el-menu
    :default-openeds="open"
    :default-active="this.$route.path"
    router
    background-color="#F0F0F0"
    class="el-menu-vertical-demo"
  >

    <el-submenu index="1">
      <template slot="title">
        <i class="el-icon-platform-eleme"></i>
        <span>交流社区</span>
      </template>
      <el-menu-item-group>
        <template slot="title">智能小N</template>
        <el-menu-item index="/nblog/new">最新分享</el-menu-item>
        <el-menu-item index="/nblog/tags">标签</el-menu-item>
      </el-menu-item-group>
      <el-menu-item-group title="足迹">
        <el-menu-item index="/nblog/favorites">收藏</el-menu-item>
      </el-menu-item-group>
    </el-submenu>
    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-edit-outline"></i>
        <span>记录时刻</span>
      </template>
      <el-menu-item-group>
        <template slot="title">文章助手</template>
        <el-menu-item index="/nblog/publish/Editor">发表文章</el-menu-item>
        <el-menu-item index="/nblog/Published">已发表</el-menu-item>
        <el-menu-item index="/nblog/draft">草稿箱</el-menu-item>
      </el-menu-item-group>
    </el-submenu>

    <el-submenu index="3">
      <template slot="title">
        <i class="el-icon-star-off"></i>
        <span>用户管理</span>
      </template>
      <el-menu-item-group>
        <template slot="title">与我有瓜</template>
        <el-menu-item index="/nblog/info">个人中心</el-menu-item>
        <el-menu-item index="/nblog/follow">我的关注</el-menu-item>
      </el-menu-item-group>
    </el-submenu>
  </el-menu>
</template>

<script>
    export default {
      name: "Nav",
      data(){
        return {
          open:["1","2","3"]
        }
      }


    }
</script>

<style scoped>
  .el-menu-vertical-demo {
    height: 87vh;
    position: fixed;
  }
</style>
