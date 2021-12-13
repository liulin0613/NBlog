<template>
  <div class="home">
    <el-container>
      <el-header>
        <Header/>
      </el-header>
      <el-main class="no-padding" style="display: flex;justify-content: center">
        <home-main/>
      </el-main>
    </el-container>
  </div>
</template>

<script>
// @ is an alias to /src
import Header from "../../components/header/Header";
import HomeMain from "./HomeMain";

export default {
  name: 'Home',
  components: {
    HomeMain,
    Header,
  },
}
</script>

<style>
  .el-header{
    padding: 0;
  }
</style>
