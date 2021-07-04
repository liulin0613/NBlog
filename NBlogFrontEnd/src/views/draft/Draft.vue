<template>
  <div>
    <Layout>
      <div slot="header">
        <Header/>
      </div>
      <div slot="aside"><Nav/></div>
      <div slot="main">
        <draft-main/>
      </div>
    </Layout>
  </div>
</template>

<script>
  import Layout from "components/content/layout/Layout";
  import Header from "components/content/header/Header";
  import Nav from "components/content/nav/Nav";
  import DraftMain from "./DraftMain";
  export default {
    name: "Draft",
    components:{
      Layout,
      Header,
      Nav,
      DraftMain
    }
  }
</script>

<style scoped>

</style>
