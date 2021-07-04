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
              :subfield="false"
              :defaultOpen="'preview'"
              :toolbarsFlag="false"
              :editable="false"
              :scrollStyle="false"
              :ishljs="true"
            />
        </el-row>

        <el-row class="font">
          <el-col :span="4"><span class="el-icon-time">&nbsp;<slot name="time"></slot></span></el-col>
          <el-col :span="4"><span class="el-icon-user">&nbsp;author : <slot name="author"></slot></span></el-col>
          <el-col :span="12" v-if="tags[0]!==''">
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
        type:['','success','info','danger','warning']
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

</style>
