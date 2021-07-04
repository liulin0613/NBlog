<template>
  <div>
    <Layout>
      <div slot="header">
        <Header/>
      </div>
      <div slot="aside"><Nav/></div>
      <div slot="main">
        <PublishedMain/>
      </div>
    </Layout>
  </div>
</template>

<script>
  import Layout from "components/content/layout/Layout";
  import Header from "components/content/header/Header";
  import Nav from "components/content/nav/Nav";
  import PublishedMain from "./PublishedMain";

  export default {
    name: "Published",
    components:{
      Layout,
      Header,
      Nav,
      PublishedMain
    }
  }
</script>

<style scoped>

</style>
