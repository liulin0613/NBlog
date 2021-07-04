<template>
  <div>
    <Layout>
      <div slot="header">
        <Header/>
      </div>
      <div slot="aside"><Nav/></div>
      <div slot="main">
        <DetailsMain/>
      </div>
    </Layout>
  </div>
</template>

<script>
  import DetailsMain from "./DetailsMain";
  import Layout from "components/content/layout/Layout";
  import Header from "components/content/header/Header";
  import Nav from "components/content/nav/Nav";

  export default {
    name: "Details",
    components:{
      DetailsMain,
      Layout,
      Header,
      Nav
    }
  }
</script>

<style scoped>

</style>
