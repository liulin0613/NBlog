<template>
  <div>
    <div class="show-article">
      <el-row class="editor-show-row">
        <mavon-editor
          ref="md"
          class="md description"
          :value="article"
          :defaultOpen="'preview'"
          :toolbarsFlag="false"
          :editable="false"
          :scrollStyle="false"
          :ishljs="true"
          :subfield = "subfield"
          :codeStyle="code_style"
          :externalLink="externalLink"

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
          imagesUrl:[],
          subfield: false,
          code_style: 'idea',
          externalLink: {
            markdown_css: function() {
              // 这是你的markdown css文件路径
              return './markdown/github-markdown.min.css';
            },
            hljs_js: function() {
              // 这是你的hljs文件路径
              return './highlightjs/highlight.min.js';
            },
            hljs_css: function(css) {
              // 这是你的代码高亮配色文件路径
              return './highlightjs/styles/' + css + '.min.css';
            },
            hljs_lang: function(lang) {
              // 这是你的代码高亮语言解析路径
              return './highlightjs/languages/' + lang + '.min.js';
            },
            katex_css: function() {
              // 这是你的katex配色方案路径路径
              return './katex/katex.min.css';
            },
            katex_js: function() {
              // 这是你的katex.js路径
              return './katex/katex.min.js';
            },
          }
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
