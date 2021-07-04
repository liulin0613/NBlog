<template>
  <div>
    <Layout>
      <div slot="header">
        <Header/>
      </div>
      <div slot="aside"><Nav/></div>
      <div slot="main">
        <favorites-main/>
      </div>
    </Layout>
  </div>
</template>

<script>
  import Layout from "components/content/layout/Layout";
  import Header from "components/content/header/Header";
  import Nav from "components/content/nav/Nav";
  import FavoritesMain from "./FavoritesMain";

  export default {
    name: "Favorites",
    components:{
      Layout,
      Header,
      Nav,
      FavoritesMain
    },
  }
</script>

<style scoped>

</style>
