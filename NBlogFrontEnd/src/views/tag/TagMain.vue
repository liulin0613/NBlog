<template>
  <div>
    <div style="padding:5px;">
      <Breadcrumb :route="breadcrumb"/>
      <el-container>
        <el-main>
          <div class="tag-group">
            <el-tag
              v-for="(item,i) in formatTags"
              :key="i"
              :type="type[i%5]"
              effect="dark">
              <a href="javascript:;" @click="tagurl(item.name)">{{item.name}} ({{item.count}})</a>
            </el-tag>
          </div>
        </el-main>
        <el-aside width="350px">
        </el-aside>
      </el-container>
    </div>
  </div>
</template>

<script>
  import Breadcrumb from "@/components/content/breadcrumb/Breadcrumb";
  import {getAllTags} from "@/network/tag";

  export default {
    name: "TagMain",
    components:{
      Breadcrumb
    },
    data(){
      return {
        breadcrumb:[
          {title:'首页',path:'/nblog/new'},
          {title:'标签'}
        ],
        items: [],
        tagname:[],
        tagcount:[],
        type:['success','','warning','danger','info'],
      }
    },
    computed:{
      formatTags(){
        for(let i=0;i<this.tagname.length;i++){
          this.items[i]={"name":this.tagname[i],"count":this.tagcount[i]}
        }
        return this.items
      },
    },
    created() {
      getAllTags()
        .then(res => {
          this.tagname=res.data.extend.tagname
          this.tagcount=res.data.extend.tagcount
        })
        .catch(err => console.log(err))
    },
    methods:{
      tagurl(tagname){
        this.$router.push("/nblog/tag/"+tagname)
      }
    }
  }
</script>

<style scoped>
  .tag-group{
    height: 77vh;
    overflow: scroll;
    scrollbar-width: none; /* Firefox */
    -ms-overflow-style: none; /* IE 10+ */
  }
  .tag-group > .el-tag {
    margin: 2px;
  }

  .tag-group::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }
</style>
