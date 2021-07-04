<template>
  <div>
    <div class="show-article">
      <el-row class="editor-show-row">
        <mavon-editor
          ref="md"
          class="md description"
          :value="article"
          :subfield="false"
          :defaultOpen="'preview'"
          :toolbarsFlag="false"
          :editable="false"
          :scrollStyle="false"
          :ishljs="true"
        />
      </el-row>
    </div>
  </div>
</template>

<script>
  import {getBase64ByUrl} from "network/editor";

  export default {
      name: "DetailsArticle",
      data(){
        return {
          description:'description',
          aData: [],
          imagesUrl:[]
        }
      },
      props:{
        article:'',
        time:''
      },
      mounted() {
        setTimeout(()=>{
          this.alertUrl()
        },600)
      },
      methods:{
        alertUrl(){
          const imgs=document.getElementsByTagName("img")
          let urls=[]
          let count=0
          for(let i=1;i<imgs.length;i++){
            if(urls.indexOf(document.getElementsByTagName("img")[i].src.slice(document.getElementsByTagName("img")[i].src.lastIndexOf("/")))!==0){
              urls[count]=document.getElementsByTagName("img")[i].src.slice(document.getElementsByTagName("img")[i].src.lastIndexOf("/")).slice(1)
              count+=1
            }
          }


          getBase64ByUrl(urls,this.time)
            .then(res=>{
              for(let i=0;i<res.data.extend.names.length;i++){
                this.$refs.md.$imgUpdateByUrl(res.data.extend.names[i], res.data.extend.url[i]);
              }
            })
            .catch(err => console.log(err))
        }
      }
    }
</script>

<style scoped>
  .show-article{
    overflow: hidden;
  }
</style>
