<template>
  <div>
    <Layout>
      <div slot="header">
        <Header/>
      </div>
      <div slot="aside"><Nav/></div>
      <div slot="main">
        <EditorMain/>
      </div>
    </Layout>
  </div>
</template>

<script>
  import EditorMain from "./EditorMain";
  import Layout from "components/content/layout/Layout";
  import Header from "components/content/header/Header";
  import Nav from "components/content/nav/Nav";

  export default {
    name: "Editor",
    components:{
      EditorMain,
      Layout,
      Header,
      Nav
    }
  }
</script>

<style scoped>

</style>
