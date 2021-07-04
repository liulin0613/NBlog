<template>
  <el-breadcrumb separator-class="el-icon-arrow-right" class="header">
    <el-breadcrumb-item v-for="item in route" :to="{ path: item.path }">
      {{item.title}}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script>
  export default {
    name: "EditorHeader",
    props:{
      route:[],
    }
  }
</script>

<style scoped>
  .el-breadcrumb{
    height: 40px;
    display: flex;
    align-items:center;
  }

</style>
