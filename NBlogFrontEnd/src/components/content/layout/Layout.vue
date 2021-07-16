<template>
  <el-container>
    <el-header style="height: 70px"><slot name="header"></slot></el-header>
    <el-container>
      <el-aside width="200px"><slot name="aside"></slot></el-aside>
      <el-main>
        <slot name="main"></slot>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
    export default {
        name: "Layout"
    }
</script>

<style scoped>
  .el-main{
    padding-top: 0;
    padding-left: 0;
  }
  .el-header{
    padding-left:0;
  }

  @media screen and (max-width: 900px) {
    .el-aside {
      display: none;
    }
  }

</style>
