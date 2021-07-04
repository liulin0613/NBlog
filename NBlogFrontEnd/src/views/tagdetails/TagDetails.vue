<template>
  <div>
    <Layout>
      <div slot="header">
        <Header/>
      </div>
      <div slot="aside"><Nav/></div>
      <div slot="main">
        <TagDetailsMain/>
      </div>
    </Layout>
  </div>
</template>

<script>
  import Layout from "components/content/layout/Layout";
  import Header from "components/content/header/Header";
  import Nav from "components/content/nav/Nav";
  import TagDetailsMain from "./TagDetailsMain";
  export default {
    name: "TagDetails",
    components:{
      Layout,
      Header,
      Nav,
      TagDetailsMain
    }
  }
</script>

<style scoped>

</style>
