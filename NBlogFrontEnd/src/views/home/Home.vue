<template>
  <div>
    <Layout>
      <div slot="header">
        <Header/>
      </div>
      <div slot="aside"><Nav/></div>
      <div slot="main">
        <HomeMain/>
      </div>
    </Layout>
  </div>
</template>

<script>
  import HomeMain from "./HomeMain";
  import Layout from "components/content/layout/Layout";
  import Header from "components/content/header/Header";
  import Nav from "components/content/nav/Nav";

  export default {
    name: "Home",
    components:{
      HomeMain,
      Layout,
      Header,
      Nav
    },
  }
</script>

<style scoped>

</style>
