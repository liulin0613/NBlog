<template>
  <div>
    <Layout>
      <div slot="header">
        <Header/>
      </div>
      <div slot="aside"><Nav/></div>
      <div slot="main">
        <TagMain/>
      </div>
    </Layout>
  </div>
</template>

<script>
  import Layout from "components/content/layout/Layout";
  import Header from "components/content/header/Header";
  import Nav from "components/content/nav/Nav";
  import TagMain from "./TagMain";

  export default {
    name: "Tag",
    components:{
      Layout,
      Header,
      Nav,
      TagMain
    }
  }
</script>

<style scoped>

</style>
