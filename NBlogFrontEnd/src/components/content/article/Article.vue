<template>
  <div>
      <div class="article-con">
        <el-row class="article-row">
          <el-col :span="24"><h1><slot name="title"></slot></h1></el-col>
        </el-row>

        <el-row class="editor-show-row">
            <mavon-editor
              class="md description"
              :value="description"
              :defaultOpen="'preview'"
              :toolbarsFlag="false"
              :editable="false"
              :scrollStyle="false"
              :subfield = "subfield"
              :ishljs="true"
              :codeStyle="code_style"
              :externalLink="externalLink"
            />
        </el-row>

        <el-row class="font">
          <el-col :span="4"><span class="el-icon-time">&nbsp;<slot name="time"></slot></span></el-col>
          <el-col :span="6"><span class="el-icon-user">&nbsp;author : <slot name="author"></slot></span></el-col>
          <el-col :span="10" v-if="tags[0]!==''">
            <div class="tags">
              <el-tag
                v-for="(item,i) in tags"
                :key="item"
                :type="type[i%5]"
                effect="plain">
                <a href="javascript:;" @click="tagurl(item)">{{item}}</a>
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <el-divider></el-divider>
      </div>
  </div>

</template>

<script>
  export default {
    name: "Article",
    data(){
      return {
        type:['','success','info','danger','warning'],
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
      tags:{
        type:Array,
        default:[]
      },
      description:''
    },
    methods:{
      tagurl(tagname){
        this.$router.push("/nblog/tag/"+tagname)
      }
    }
  }
</script>

<style scoped>
  .article-con{
    line-height: 60px;
  }

  .tags{
    display: flex;
    align-items: center;
    justify-content:start;
    gap: 5px;
  }

  .font{
    display: flex;
    align-items: center;
  }

  .description {
    line-height: 30px;
    font-size: 1.03rem;
  }

  .editor-show-row{
    height: 100px;
    overflow: hidden;
    line-height: 30px;
    font-size: 1.03rem;
  }

  @media screen and (max-width: 900px) {
    h1{
      line-height: 30px;
      padding-bottom: 10px;
    }

    .tags{
      display: none;
    }
    .el-col-4{
      width: 50%;
    }
    .el-col-6{
      width: 50%;
    }

  }
</style>
