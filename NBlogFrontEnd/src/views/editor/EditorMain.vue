<template>
  <div>
    <div class="editor-main">
      <Breadcrumb :route="breadcrumb"/>
      <EditorTools/>
    </div>
  </div>
</template>

<script>
  import Breadcrumb from "components/content/breadcrumb/Breadcrumb";
  import EditorTools from "./EditorTools";
  export default {
    name: "EditorMain",
    components:{
      Breadcrumb,
      EditorTools,
    },
    data(){
      return {
        breadcrumb:[
          {title:'首页',path:'/nblog/new'},
          {title:'发表文章',path:''}
        ]
      }
    }
  }
</script>

<style scoped>
  .editor-main{
    padding: 5px;
  }
</style>
