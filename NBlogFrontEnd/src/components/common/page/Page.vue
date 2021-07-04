<template>
  <div>

  </div>
</template>

<script>
    export default {
      name: "Page",
      data(){
        return{
          currentPage:0,
          value:true
        }
      },
      props:{
        total:{
          default:0,
        },
      },
      methods: {

      },
    }
</script>

<style scoped>
  .block{
  }
</style>
