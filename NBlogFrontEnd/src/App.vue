<template>
  <div id="app">
    <div>
      <router-view/>
    </div>
  </div>
</template>

<script>
  import Home from "views/home/Home";
  import Layout from "components/content/layout/Layout";
  import Header from "components/content/header/Header";
  import Nav from "components/content/nav/Nav";

  export default {
    name:'App',

    components:{
      Home,
      Layout,
      Header,
      Nav
    },
    created() {
      if(this.$store.state.user===''){
        if(localStorage.getItem('user')==='undefined'||localStorage.getItem('user')===null){
          this.$router.push('/')
        }else {
          this.$store.state.user=localStorage.getItem('user')
        }
      }
    }
  }
</script>

<style>
  @import "assets/css/base.css";
  #app{
    padding-left: 20px;
  }

  @media screen and (max-width: 900px) {
    #app{
      padding-left: 0;
    }
  }
</style>
