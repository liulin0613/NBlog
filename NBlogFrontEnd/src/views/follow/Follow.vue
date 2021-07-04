<template>
  <div>
    <Layout>
      <div slot="header">
        <Header/>
      </div>
      <div slot="aside"><Nav/></div>
      <div slot="main">
        <follow-main/>
      </div>
    </Layout>
  </div>
</template>

<script>
  import Layout from "components/content/layout/Layout";
  import Header from "components/content/header/Header";
  import Nav from "components/content/nav/Nav";
  import FollowMain from "./FollowMain";
  export default {
    name: "Follow",
    components:{
      Layout,
      Header,
      Nav,
      FollowMain

    }
  }
</script>

<style scoped>

</style>
