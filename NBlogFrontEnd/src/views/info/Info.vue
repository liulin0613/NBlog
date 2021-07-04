<template>
  <div>
    <Layout>
      <div slot="header">
        <Header/>
      </div>
      <div slot="aside"><Nav/></div>
      <div slot="main">
        <info-main/>
      </div>
    </Layout>
  </div>
</template>

<script>
  import Layout from "components/content/layout/Layout";
  import Header from "components/content/header/Header";
  import Nav from "components/content/nav/Nav";
  import InfoMain from "./InfoMain";

  export default {
    name: "Info",
    components:{
      Layout,
      Header,
      Nav,
      InfoMain
    },
  }
</script>

<style scoped>

</style>
